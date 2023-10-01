package com.example.apptr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TasquesAdapter extends RecyclerView.Adapter<TasquesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Tasques> myTask;

    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
    String dataavui = sdf.format(new Date());

    DatabaseReference reference;
    FirebaseUser user;
    String uid;


    public TasquesAdapter(Context context1, ArrayList<Tasques> p){
        context = context1;
        myTask = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.items_llista, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.titoltasca.setText(myTask.get(i).getTitoltasca());
        myViewHolder.desctasca.setText(myTask.get(i).getDesctasca());
        myViewHolder.datatasca.setText(myTask.get(i).getDatatasca());

        if (myViewHolder.datatasca.getText().toString().equals(dataavui)){
                myViewHolder.datatasca.setText("Avui");
        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String admin = snapshot.child("admin").getValue(String.class);
                if (admin.equals("true")) {
                    //Editar la tasca que volguem

                    final String getTitoltasca = myTask.get(i).getTitoltasca();
                    final String getDesctasca = myTask.get(i).getDesctasca();
                    final String getDatatasca = myTask.get(i).getDatatasca();
                    final String getKeytasca = myTask.get(i).getKeytasca();

                    myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent a2 = new Intent(context, EditarTasca.class);
                            a2.putExtra("titletasca", getTitoltasca);
                            a2.putExtra("desctasca", getDesctasca);
                            a2.putExtra("datatasca", getDatatasca);
                            a2.putExtra("keytasca", getKeytasca);

                            context.startActivity(a2);
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
    public int getItemCount() {
        return myTask.size();
    }

   

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titoltasca, desctasca, datatasca;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titoltasca = (TextView)itemView.findViewById(R.id.titletasca);
            desctasca = (TextView)itemView.findViewById(R.id.desctasca);
            datatasca = (TextView)itemView.findViewById(R.id.datatasca);

        }
    }


}
