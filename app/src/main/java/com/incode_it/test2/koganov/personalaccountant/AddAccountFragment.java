package com.incode_it.test2.koganov.personalaccountant;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAccountFragment extends Fragment {

//    private DatabaseReference databaseReference;

    private Controller con;

    private Spinner spinnerCurrency;

    private EditText etName;
    private EditText etAmount;

    public AddAccountFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_account, container, false);

        con = ((MainActivity)getActivity()).getCon();

        spinnerCurrency = v.findViewById(R.id.spinnerCurrency);

        etAmount = v.findViewById(R.id.etAmount);
        etName = v.findViewById(R.id.etName);

        v.findViewById(R.id.btnAddNewAcc).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if(etAmount.getText().length()== 0)
                {
                    etAmount.setText("0");
                }

                if(etName.getText().length()== 0)
                {
                    Toast.makeText(getActivity(), "Input Name of Account!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(con.isAccount(etName.getText().toString(), spinnerCurrency.getSelectedItem().toString()))
                    {
                        Toast.makeText(getActivity(), "Sorry, this Account has already created!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Account acc = new Account(etName.getText().toString(),
                                Double.parseDouble(etAmount.getText().toString())
                                , spinnerCurrency.getSelectedItem().toString());

                        con.getUser().getAccounts().add(acc);

                        con.setCurAccount(con.getUser().getAccounts().size()-1);

                        con.newAsync("addNewAccInFirebase");

                        ((MainActivity) getActivity()).installFragment(new AccountFragment(), false);
                    }
                }

            }//end onClick

        });//end View.OnClickListener()

        return v;
    }
}