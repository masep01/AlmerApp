package com.example.apptr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class itinerari_social extends Fragment {

    EditText edtxtCAT;
    EditText edtxtCAST;
    EditText edtxtANG;
    EditText edtxtHIST;
    EditText edtxtFILO;
    EditText edtxtMAT;
    EditText edtxtFIS;
    EditText edtxtBLOC1;
    EditText edtxtBLOC2;

    Spinner spinner1;

    Button btn_calc;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public itinerari_social() {
        // Required empty public constructor
    }


    public static itinerari_social newInstance(String param1, String param2) {
        itinerari_social fragment = new itinerari_social();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerari_social, container, false);

        //Declarar variables

        edtxtCAT=view.findViewById(R.id.editTextCatala);
        edtxtCAST=view.findViewById(R.id.editTextCastella);
        edtxtANG=view.findViewById(R.id.editTextAngles);
        edtxtFILO=view.findViewById(R.id.editTextFilo);
        edtxtHIST=view.findViewById(R.id.editTextHistoria);

        edtxtMAT=view.findViewById(R.id.editTextOpt_1);
        edtxtFIS=view.findViewById(R.id.editTextOpt_2);
        edtxtBLOC1=view.findViewById(R.id.editTextOpt_3);
        edtxtBLOC2=view.findViewById(R.id.editTextOpt_4);

        spinner1=view.findViewById(R.id.spinner1);


        btn_calc=view.findViewById(R.id.btn_calc_mitja);

        //Spinner --> BLOC 1

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloc1_social, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner1.setAdapter(adapter);



        //Calcular mitja
        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Guardem el numero de cada casella a una String
                String notaCat = edtxtCAT.getText().toString();
                String notaCast = edtxtCAST.getText().toString();
                String notaAng = edtxtANG.getText().toString();
                String notaFilo = edtxtFILO.getText().toString();
                String notaHist = edtxtHIST.getText().toString();
                String notaMat= edtxtMAT.getText().toString();
                String notaFis = edtxtFIS.getText().toString();
                String notabloc1 = edtxtBLOC1.getText().toString();
                String notabloc2 = edtxtBLOC2.getText().toString();

                //Si algun camp és buit

                if(TextUtils.isEmpty(notaCat)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaCast)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaAng)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaFilo)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaHist)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaMat)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notaFis)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notabloc1)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(notabloc2)){
                    Toasty.warning(getActivity(), "Siusplau, ompli tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }

                int nota1 = Integer.parseInt(notaCat);
                int nota2 = Integer.parseInt(notaCast);
                int nota3 = Integer.parseInt(notaAng);
                int nota4 = Integer.parseInt(notaFilo);
                int nota5 = Integer.parseInt(notaHist);
                int nota6 = Integer.parseInt(notaMat);
                int nota7 = Integer.parseInt(notaFis);
                int nota8 = Integer.parseInt(notabloc1);
                int nota9 = Integer.parseInt(notabloc2);

                int mitja = (nota1 + nota2 +
                        nota3 + nota4 + nota5 +
                        nota6 + nota7 + nota8 +
                        nota9) / 9;

                AlertDialog.Builder resultat = new AlertDialog.Builder(getActivity());
                resultat.setMessage("La teva mitja és de:  " + mitja)
                        .setCancelable(false)
                        .setPositiveButton("D'acord", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = resultat.create();
                alertDialog.setTitle("Resultat");
                alertDialog.show();
            }
        });

        return view;
    }

}