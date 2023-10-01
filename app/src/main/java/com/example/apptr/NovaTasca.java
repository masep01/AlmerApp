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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.text.DateFormat;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class NovaTasca extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView titlepage, addtitle, adddesc, adddate;
    EditText titolNovaTasca, descNovaTasca, dateNovaTasca;
    Button btncreateTask, btncancelTask;

    DatabaseReference myRef;
    DatabaseReference myRef2;
    Integer numTasques = new Random().nextInt();
    String keytasca = Integer.toString(numTasques);

    FirebaseUser user;
    String uid;
    String classe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tasca);

        //TextView(s)
        titlepage = findViewById(R.id.titlepage);
        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);

        //EditText(s)
        titolNovaTasca = findViewById(R.id.titol_nova_tasca);
        descNovaTasca = findViewById(R.id.desc_nova_tasca);
        dateNovaTasca = findViewById(R.id.data_nova_tasca);

        dateNovaTasca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        //Botons
        btncreateTask = findViewById(R.id.btnCrearTasca);
        btncancelTask = findViewById(R.id.btnCancelarTasca);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        btncreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Referència a Firebase
                myRef = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                classe = snapshot.child("classe").getValue(String.class);

                                myRef = FirebaseDatabase.getInstance().getReference().child("Tasques")
                                        .child(classe)
                                        .child("Tasca" + numTasques);
                                //Carregar la info a Firebase

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        myRef.child("titoltasca").setValue(titolNovaTasca.getText().toString());
                                        myRef.child("desctasca").setValue(descNovaTasca.getText().toString());
                                        myRef.child("datatasca").setValue(dateNovaTasca.getText().toString());

                                        myRef.child("keytasca").setValue(keytasca);

                                        Intent i = new Intent(NovaTasca.this, agenda.class);
                                        startActivity(i);

                                        Toasty.success(NovaTasca.this, "Tasca creada", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toasty.error(NovaTasca.this, "Error al crear la nova tasca", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            }
        });

        btncancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = dayOfMonth + "/" + ( month + 1) + "/" + year;

        TextView textView = (TextView) findViewById(R.id.data_nova_tasca);
        textView.setText(currentDateString);
    }
}