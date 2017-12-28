package com.incode_it.test2.koganov.personalaccountant;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountFragment extends Fragment {

//    private DatabaseReference mDatabase;

    private Controller con;

    private Spinner spinnerAccounts;

    private ArrayAdapter<Account> adapterAccounts;

    private Button btnNewAcc;

    public AccountFragment()
    {
        // Required empty public constructor
    }

    class MyAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unused)
        {
            con.getUserFromFirebase();
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... items) {

        }
        @Override
        protected void onPostExecute(Void unused) {
            Log.i("MyTag onPostExecute", con.getUser().getEmail());
            Log.i("MyTag onPostExecute", con.getUser().getAccounts().toString());
            refreshSpinner();
        }
    }

    public void refreshSpinner()
    {
//          con.getUserFromFirebase();

//        con.newAsync("getUserFromFirebase");

        adapterAccounts=null;

        adapterAccounts = new ArrayAdapter<>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , con.getUser().getAccounts());

        spinnerAccounts.setAdapter(adapterAccounts);

        spinnerAccounts.setSelection(spinnerAccounts.getCount()-1);
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

        refreshSpinner();

        v.findViewById(R.id.btnNewAcc).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).installFragment(new AddAccountFragment(), true);

            }
        });

        return v;
    }

}
