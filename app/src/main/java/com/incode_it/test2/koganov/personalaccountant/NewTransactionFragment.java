package com.incode_it.test2.koganov.personalaccountant;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTransactionFragment extends Fragment {

    private Controller con;

    private TextView tvCurAccount;

    private EditText etSum;
    private EditText etRecipient;

    private EditText description;

    private Spinner spinnerCategoryTrans;

    private Spinner spTypeTransaction;

    private Spinner spToAcc;

    private ArrayAdapter<String> adapterCategoryTrans;

    private ArrayAdapter<Account> adapterAccounts;

    private ArrayAdapter<String> adapterSpTypeTransaction;

    private String[] masTypeTrans;


    public NewTransactionFragment() {
    }

    public void refreshSpTypeTransaction()
    {

        adapterSpTypeTransaction=null;

        adapterSpTypeTransaction = new ArrayAdapter<String>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , masTypeTrans)
        {

            @Override
            public boolean isEnabled(int position) {

                if (con.getUser().getAccounts().size() < 2 && position==2)
                {
                    Toast.makeText(getActivity(), "Sorry, transfer is impossible, you have only one account!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
            // Change color item
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (con.getUser().getAccounts().size() < 2 && position==2)
                {
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setTextColor(Color.BLACK);
                }
                return mView;
            }
        };

        spTypeTransaction.setAdapter(adapterSpTypeTransaction);

        spTypeTransaction.setSelection(con.getCurTypeTrans());


    }

    public void refreshSpinnerAccounts()
    {
        adapterAccounts=null;

        adapterAccounts = new ArrayAdapter<Account>(getActivity().getApplicationContext()
                ,R.layout.spinner_row
                , con.getUser().getAccounts())
        {

            @Override
            public boolean isEnabled(int position) {
                if (con.getCurAccount()== position)
                {
                    Toast.makeText(getActivity(), "Sorry, this account is the sender!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (con.getCurAccount()== position)
                {
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setTextColor(Color.BLACK);
                }
                return mView;
            }
        };

        spToAcc.setAdapter(adapterAccounts);

        if(con.getCurAccount()==con.getUser().getAccounts().size()-1)//last account
        {
            spToAcc.setSelection(con.getUser().getAccounts().size()-2);
        }
        else
        {
            spToAcc.setSelection(con.getCurAccount()+1);
        }
    }

    public void refreshSpinnerCategories()
    {
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

//        getActivity().setTitle("Current Account");

        masTypeTrans = getResources().getStringArray(R.array.typeTransaction);

        View v = inflater.inflate(R.layout.fragment_new_transaction, container, false);

        con = ((MainActivity)getActivity()).getCon();

        spinnerCategoryTrans = v.findViewById(R.id.spCategoryTransaction);
        spTypeTransaction = v.findViewById(R.id.spTypeTransaction);
        description = v.findViewById(R.id.etDescription);
        etSum = v.findViewById(R.id.etSum);
        etRecipient = v.findViewById(R.id.etRecipient);
        tvCurAccount = v.findViewById(R.id.tvCurAcc);
        spToAcc = v.findViewById(R.id.spToAcc);

        tvCurAccount.setText(con.getUser().getAccounts().get(con.getCurAccount()).toString());

        refreshSpinnerCategories();//categories

        refreshSpTypeTransaction(); //type Transactions


        spTypeTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId)
            {
                if(spTypeTransaction.getSelectedItem().toString().equals("Transfer"))
                {
                    etRecipient.setText("Select partner from list "+(char)11014);
                    etRecipient.setEnabled(false);
                    spToAcc.setVisibility(View.VISIBLE);
                    refreshSpinnerAccounts();
                }
                else
                {
                    etRecipient.setText("");
                    etRecipient.setHint(getString(R.string.hintPartner));
                    etRecipient.setEnabled(true);
                    spToAcc.setVisibility(View.INVISIBLE);
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        v.findViewById(R.id.btnNewCategoryTransaction).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                con.setCurTypeTrans(spTypeTransaction.getSelectedItemPosition());

                ((MainActivity)getActivity()).installFragment(new NewCategoryTransFragment(), true);

            }
        });

        v.findViewById(R.id.btnAddNewTransaction).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(spinnerCategoryTrans.getSelectedItem()==null)
                {
                    Toast.makeText(getActivity(), "Sorry, you do not have any categories!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(etSum.getText().length()== 0 || Double.parseDouble(etSum.getText().toString())==0)
                    {
                        Toast.makeText(getActivity(), "Sum = 0. It does not make sense, input another sum!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String recipient;
                        if(spTypeTransaction.getSelectedItem().toString().equals("Transfer"))
                        {
                            recipient = spToAcc.getSelectedItem().toString();
                            con.setRecip(spToAcc.getSelectedItemPosition());
                        }
                        else
                        {
                            recipient = etRecipient.getText().toString();
                        }
                        Transaction trans = new Transaction(
                                new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss").format(Calendar.getInstance().getTime())
                                ,spTypeTransaction.getSelectedItem().toString()
                                ,spinnerCategoryTrans.getSelectedItem().toString()
                                ,description.getText().toString()
                                ,Double.parseDouble(etSum.getText().toString())
                                ,recipient);

                        con.handleNewTransaction(trans);

                        getActivity().onBackPressed();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshSpinnerCategories();

        refreshSpTypeTransaction();

    }
}
