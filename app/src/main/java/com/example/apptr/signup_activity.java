package com.example.apptr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class signup_activity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    private static final String TAG = "MSG:";
    private FirebaseAuth mAuth;

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputLayout TextMail;
    private TextInputLayout TextPassword;
    private TextInputLayout TextUsername;
    private TextInputLayout TextCode;

    private Button btnreg;

    private ProgressDialog progressDialog;

    private ImageView uploadphoto;
    private Uri mImageUri;

    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;


    private FirebaseUser user;

    private String uid;
    private String admin;

    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*LinearLayout constraintLayout = findViewById(R.id.Linear_ly2);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/


        // Iniciar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Referencies
        TextMail = findViewById(R.id.emailEditText);
        TextPassword = findViewById(R.id.passwordEditText);
        TextUsername = findViewById(R.id.userEditText);
        TextCode = findViewById(R.id.textCode);

        uploadphoto = findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);

        btnreg = findViewById(R.id.regbtn);

        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        spinner1 = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(signup_activity.this,
                R.array.classes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner1.setAdapter(adapter);

    }

    private boolean validateEmail() {
        String emailInput = TextMail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            TextMail.setError("Indiqui un correu electrònic");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            TextMail.setError("Indiqui un correu electrònic vàlid");
            return false;
        } else {
            TextMail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = TextPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            TextPassword.setError("Indiqui una contrasenya");
            return false;

        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            TextPassword.setError("Contrasenya poc segura");
            return false;

        } else {
            TextPassword.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String userInput = TextUsername.getEditText().getText().toString().trim();

        if (userInput.isEmpty()) {
            TextUsername.setError("Indiqui un nom");
            return false;
        } else {
            TextUsername.setError(null);
            return true;
        }
    }

    public void onClick(View v) {

        if (!validateEmail() | !validatePassword() | !validateUsername()) {
            return;
        }

        final String email = TextMail.getEditText().getText().toString().trim();
        final String password = TextPassword.getEditText().getText().toString().trim();
        final String username = TextUsername.getEditText().getText().toString().trim();
        final String classe = spinner1.getSelectedItem().toString();
        final String code = TextCode.getEditText().getText().toString().trim();

        if (code.equals("2nADMIN")){
            admin = "true";
        }
        if (code.isEmpty() || !code.equals("2nADMIN")){
            admin = "false";
        }
        progressDialog.setMessage("Relitzant registre...");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(signup_activity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid();

                        myRef = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            Toasty.success(signup_activity.this, "S'ha enregistrat correctament", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(signup_activity.this, agenda.class);
                            startActivity(i);
                        }
                        progressDialog.dismiss();
                    }

                })

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Veure si l'usuari ja existeix, si és així, que notifiqui a l'usuari
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(signup_activity.this);
                            alertdialog.setMessage("Ja existeix un compte associat a aquesta adreça electrònica")
                                    .setCancelable(false)
                                    .setPositiveButton("D'acord", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            AlertDialog alertDialog = alertdialog.create();
                            alertDialog.setTitle("Usuari existent");
                            alertDialog.show();

                        } else {

                            //Carregar la foto
                            if (mImageUri != null) {

                                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

                                mUploadTask = fileReference.putFile(mImageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String url = uri.toString();

                                                        myRef.child("imageurl").setValue(url);
                                                    }
                                                });


                                            }
                                        })

                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(signup_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    myRef.child("nom").setValue(username);
                                    myRef.child("correu").setValue(email);
                                    myRef.child("contrasenya").setValue(password);
                                    myRef.child("classe").setValue(classe);
                                    myRef.child("admin").setValue(admin);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(uploadphoto);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}





