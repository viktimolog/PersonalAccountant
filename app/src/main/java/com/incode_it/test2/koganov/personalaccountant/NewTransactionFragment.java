package com.incode_it.test2.koganov.personalaccountant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTransactionFragment extends Fragment {

    private Controller con;

    private EditText etSum;
    private EditText etRecipient;

    private EditText description;

    private Spinner spinnerCategoryTrans;

    private Spinner spTypeTransaction;

    private ArrayAdapter<String> adapterCategoryTrans;


    public NewTransactionFragment() {
    }

    public void refreshSpinner()
    {
//          con.getUserFromFirebase();

//        con.newAsync("getUserFromFirebase");

        adapterCategoryTrans=null;

        adapterCategoryTrans = new ArrayAdapter<>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , con.getUser().getCategoriesTransaction());

        spinnerCategoryTrans.setAdapter(adapterCategoryTrans);

        spinnerCategoryTrans.setSelection(spinnerCategoryTrans.getCount()-1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_transaction, container, false);

        con = ((MainActivity)getActivity()).getCon();

        spinnerCategoryTrans = v.findViewById(R.id.spCategoryTransaction);
        spTypeTransaction = v.findViewById(R.id.spTypeTransaction);
        description = v.findViewById(R.id.etDescription);
        etSum = v.findViewById(R.id.etSum);
        etRecipient = v.findViewById(R.id.etRecipient);

        refreshSpinner();

        Log.i("MyTag",con.getCurAccount()+"");


        v.findViewById(R.id.btnNewCategoryTransaction).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).installFragment(new NewCategoryTransFragment(), true);

            }
        });

        v.findViewById(R.id.btnAddNewTransaction).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Transaction trans = new Transaction(
            new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss").format(Calendar.getInstance().getTime())
                ,spTypeTransaction.getSelectedItem().toString()
                        ,spinnerCategoryTrans.getSelectedItem().toString()
                            ,description.getText().toString()
                                ,Double.parseDouble(etSum.getText().toString())
                                    ,etRecipient.getText().toString());

                con.handleNewTransaction(trans);

                ((MainActivity)getActivity()).installFragment(new AccountFragment(), false);

            }
        });

        return v;
    }

}
