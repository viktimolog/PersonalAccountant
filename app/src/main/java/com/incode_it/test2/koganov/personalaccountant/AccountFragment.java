package com.incode_it.test2.koganov.personalaccountant;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    private Controller con;

    private ListView lvTransactions;

    private Spinner spinnerAccounts;

    private ArrayAdapter<Account> adapterAccounts;

    private ArrayAdapter<Transaction> adapterTransactions;

    public AccountFragment()
    {
        // Required empty public constructor
    }

    public void refreshLvTransactions(ArrayList<Transaction> transList)
    {
        adapterTransactions=null;

        adapterTransactions = new ArrayAdapter<>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , transList);

        lvTransactions.setAdapter(adapterTransactions);
    }

    public void refreshSpinner()
    {

        adapterAccounts=null;

        adapterAccounts = new ArrayAdapter<>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , con.getUser().getAccounts());

        spinnerAccounts.setAdapter(adapterAccounts);

        spinnerAccounts.setSelection(con.getCurAccount()); //view last selected account
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        con = ((MainActivity)getActivity()).getCon();

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        spinnerAccounts = v.findViewById(R.id.spinnerAccounts);
        lvTransactions = v.findViewById(R.id.lvTransactions);

        refreshSpinner();

        spinnerAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                con.setCurAccount(spinnerAccounts.getSelectedItemPosition());

                refreshLvTransactions(con.getUser().getAccounts().get(con.getCurAccount()).getTransactions());

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        v.findViewById(R.id.btnNewAcc).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).installFragment(new AddAccountFragment(), true);

            }
        });

        v.findViewById(R.id.btnNewTr).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                con.setCurAccount(spinnerAccounts.getSelectedItemPosition());

                Log.d("MyTag",Integer.toString(con.getCurAccount()));

                if(con.getCurAccount()==-1)
                {
                    Toast.makeText(getActivity(), "Sorry, you do not have any acoounts!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ((MainActivity)getActivity()).installFragment(new NewTransactionFragment(), true);
                }

            }//end onClick

        });//end View.OnClickListener()

        return v;
    }

}
