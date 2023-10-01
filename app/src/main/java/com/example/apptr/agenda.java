package com.example.apptr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class agenda extends AppCompatActivity {


    TextView titlepage, subtitolpage, endpage;
    FloatingActionButton btn_add_task;

    DatabaseReference reference;

    RecyclerView recycler_tasques;
    ArrayList<Tasques> myTaskList;
    TasquesAdapter task_adapter;

    FirebaseUser user;
    String uid;
    String classe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setSelectedItemId(R.id.nav_agenda);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_agenda:
                        return true;

                    case R.id.nav_calc_mitjanes:
                        startActivity(new Intent(getApplicationContext()
                                , calculadora_mitjana.class));
                        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_enter_anim);
                        return true;

                    case R.id.nav_perfil:
                        startActivity(new Intent(getApplicationContext()
                                , profile.class));
                        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_enter_anim);
                        return true;
                }
                return false;
            }
        });

        //TextViews
        titlepage = findViewById(R.id.titlepage);
        subtitolpage = findViewById(R.id.subtitlepage);
        endpage = findViewById(R.id.finalpg);

        SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
        String dataavui = sdf.format(new Date());

        subtitolpage.setText(dataavui);

        //Botó afegir tasca
        btn_add_task=findViewById(R.id.btnAddTask);
        btn_add_task.setVisibility(View.INVISIBLE);

        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(agenda.this, NovaTasca.class);
                startActivity(i);
            }
        });

        //Data del RecyclerView
        recycler_tasques = findViewById(R.id.recycler_tasques);
        recycler_tasques.setLayoutManager(new LinearLayoutManager(this));
        myTaskList= new ArrayList<Tasques>();

        //RealtimeDatabase

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                classe = snapshot.child("classe").getValue(String.class);
                String admin = snapshot.child("admin").getValue(String.class);
                    if (admin.equals("true")){
                        btn_add_task.setVisibility(View.VISIBLE);
                    }else{
                        btn_add_task.setVisibility(View.INVISIBLE);
                    }

                    reference = FirebaseDatabase.getInstance().getReference().child("Tasques").child(classe);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Substitueix la info i la mostra al layout

                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                            {
                                Tasques p = dataSnapshot1.getValue(Tasques.class);
                                myTaskList.add(p);
                            }
                            task_adapter = new TasquesAdapter(agenda.this, myTaskList);
                            recycler_tasques.setAdapter(task_adapter);
                            task_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "No hi ha data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    //Desabilita botó de anar enrere
    @Override
    public void onBackPressed() {
    }

}