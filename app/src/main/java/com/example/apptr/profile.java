package com.example.apptr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends AppCompatActivity {


    private static final String TAG = "Console: ";

    private TextView Username, Email, Password, signOut, Info, EditProfile;
    private CircleImageView imgview;


    private DatabaseReference myRef;

    private FirebaseUser user;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setSelectedItemId(R.id.nav_perfil);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_agenda:
                        startActivity(new Intent(getApplicationContext()
                                , agenda.class));
                        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_enter_anim);
                        return true;

                    case R.id.nav_calc_mitjanes:
                        startActivity(new Intent(getApplicationContext()
                                , calculadora_mitjana.class));
                        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_enter_anim);
                        return true;

                    case R.id.nav_perfil:
                        return true;
                }
                return false;
            }
        });

        Username = (TextView)findViewById(R.id.username);
        Email = (TextView) findViewById(R.id.email);
        //Password = findViewById(R.id.password);
        imgview = (CircleImageView) findViewById(R.id.imageView);

        signOut = (TextView) findViewById(R.id.logout_textview);
        EditProfile = (TextView)findViewById(R.id.editprofile_textview);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        myRef = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //Obtenir dades de Firebase DATABASE

                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    // String correu = dataSnapshot.child("correu").getValue(String.class);
                    String contrasenya = dataSnapshot.child("contrasenya").getValue(String.class);
                    String imgurl = dataSnapshot.child("imageurl").getValue(String.class);

                    //Dades de Firebase AUTH
                    String email = user.getEmail();

                    Username.setText(nom);
                    Email.setText(email);


                    // Password.setText(contrasenya);

                    Picasso.get().load(imgurl).into(imgview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void onClick(View view){

        AlertDialog.Builder dialog_signout = new AlertDialog.Builder(profile.this);
        dialog_signout.setIcon(R.drawable.ic_logout)
                .setMessage("Segur que desitja tancar sessió?")

                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())

                .setPositiveButton("Sí", (dialog, which) -> {

                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(profile.this, login_activity.class));
                    dialog.dismiss();
                });

        AlertDialog alertDialog = dialog_signout.create();
        alertDialog.setTitle("Tancar sessió");
        alertDialog.show();
    }

    public void onClick2 (View view){

        finish();
        Intent a = new Intent(profile.this, profile_info.class);
        startActivity(a);



    }
}