package com.example.apptr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import es.dmoral.toasty.Toasty;

public class EditarTasca extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText titolEditTasca, descEditTasca, dateEditTasca;
    Button btnEdit, btnDelete;

    DatabaseReference myRef;

    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tasca);

        //EditText(s)
        titolEditTasca = findViewById(R.id.titol_edit_tasca);
        descEditTasca = findViewById(R.id.desc_edit_tasca);
        dateEditTasca = findViewById(R.id.data_edit_tasca);

        //Botons
        btnEdit = findViewById(R.id.btnEditarTasca);
        btnDelete = findViewById(R.id.btnEliminar);


        //Donarlos el valor que tenen a NovaTasca.java
        titolEditTasca.setText(getIntent().getStringExtra("titletasca"));
        descEditTasca.setText(getIntent().getStringExtra("desctasca"));
        dateEditTasca.setText(getIntent().getStringExtra("datatasca"));

        String keyTasca = getIntent().getStringExtra("keytasca");

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String classe = snapshot.child("classe").getValue(String.class);
                    myRef = FirebaseDatabase.getInstance().getReference().child("Tasques")
                            .child(classe)
                            .child("Tasca" + keyTasca);


                    dateEditTasca.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment datePicker = new DatePickerFragment();
                            datePicker.show(getSupportFragmentManager(), "date picker");
                        }
                    });

                    //Eliminar tasca
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Intent i = new Intent(EditarTasca.this, agenda.class);
                                        startActivity(i);

                                        Toasty.info(EditarTasca.this, "Tasca esborrada", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(EditarTasca.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    //Editar tasca
                    btnEdit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            myRef.child("titoltasca").setValue(titolEditTasca.getText().toString());
                            myRef.child("desctasca").setValue(descEditTasca.getText().toString());
                            myRef.child("datatasca").setValue(dateEditTasca.getText().toString());


                            Intent i = new Intent(EditarTasca.this, agenda.class);
                            startActivity(i);

                            Toasty.info(EditarTasca.this, "Tasca editada", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = dayOfMonth + "/" + ( month + 1) + "/" + year;

        TextView textView = (TextView) findViewById(R.id.data_edit_tasca);
        textView.setText(currentDateString);
    }
}