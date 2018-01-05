package com.incode_it.test2.koganov.personalaccountant;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseLoginFragment extends Fragment implements View.OnClickListener
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;

    private Controller con;

    public FirebaseLoginFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        con = ((MainActivity)getActivity()).getCon();

        View v = inflater.inflate(R.layout.fragment_firebase_login, container, false);

        ETemail = v.findViewById(R.id.et_email);
        ETpassword = v.findViewById(R.id.et_password);

        v.findViewById(R.id.btn_sign_in).setOnClickListener(this);
        v.findViewById(R.id.btn_registration).setOnClickListener(this);

        return v;
    }

    public void signin(final String email , String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Authorization successful ", Toast.LENGTH_SHORT).show();
                            con.getUser().setEmail(email);
                            con.newAsync("getUserFromFirebase");
                        }
                        else
                            Toast.makeText(getActivity(), "Authorization failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void registration (final String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    con.getUser().setEmail(email);
                    con.newAsync("addNewUserInFirebase");
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean emptyET()
    {
        if(ETemail.getText().toString().matches(""))
            return true;
        if(ETpassword.getText().toString().matches(""))
            return true;
        return false;
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btn_sign_in)
        {
            if (emptyET())
            {
                Toast.makeText(getActivity(), "Email and Password can't be empty!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                signin(ETemail.getText().toString(), ETpassword.getText().toString());
            }
        }
        else if (view.getId() == R.id.btn_registration)
        {
            if (emptyET()) {
                Toast.makeText(getActivity(), "Email and Password can't be empty!", Toast.LENGTH_SHORT).show();
            }
            else {

                registration(ETemail.getText().toString(), ETpassword.getText().toString());
            }
        }
    }
}