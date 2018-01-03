package com.incode_it.test2.koganov.personalaccountant;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Controller
{
    private MainActivity ma;
    private String keyUser;
    private User user;
    private String typeFirebaseTransaction;
    private int curAccount;

    public Controller(MainActivity ma) {
        this.ma = ma;
        user = new User();
        user.setAccounts(new ArrayList<Account>());
    }

    class MyAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unused)
        {
            if(typeFirebaseTransaction.equals("addNewUserInFirebase"))
            {
                addNewUserInFirebase();
            }
            else if(typeFirebaseTransaction.equals("getUserFromFirebase"))
            {
                getUserFromFirebase();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... items) {

        }
        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    public void newAsync(String typeTransaction)
    {
        typeFirebaseTransaction = typeTransaction;
//        user.setAccounts(new ArrayList<Account>());
//        user.setCategoriesTrans(new ArrayList<String>());
        new MyAsync().execute();
    }

    public void addNewUserInFirebase()
    {
        //create the unique key for next user in DB
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().push();

        databaseReference.setValue(user);
    }

    public void addNewAccInFirebase()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

//        myRef.child(keyUser).child("email").child("accounts").setValue(user.getAccounts());

        myRef.child(keyUser).child("accounts").setValue(user.getAccounts());

    }

    public void addNewCategotyTrans()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child(keyUser).child("categoriesTransaction").setValue(user.getCategoriesTransaction());
    }

    public void addNewTransactionInFirebase()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

//        myRef.child(keyUser).child("accounts").setValue(user.getAccounts());

        /*myRef.child(keyUser).child("accounts").child(Integer.toString(curAccount))
                .setValue(user.getAccounts().get(curAccount).getTransactions());*/

        myRef.child(keyUser).child("accounts").child(Integer.toString(curAccount))
                .setValue(user.getAccounts().get(curAccount));
    }

    public Boolean isCategory(String isCat)
    {
        for(int i=0; i<user.getCategoriesTransaction().size();i++)
        {
            if(user.getCategoriesTransaction().get(i).equals(isCat))
            return true;
        }
        return false;
    }

    public void getUserFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    if (user.getEmail().equals(childDataSnapshot.child("email").getValue())) {
                        keyUser = childDataSnapshot.getKey();
                        user=null;
                        user = childDataSnapshot.getValue(User.class);
                        Log.i("MyTag getUserFromFireDB", user.getEmail());
                        Log.i("MyTag getUserFromFireDB", user.getAccounts().toString());

                        Log.i("MyTag getCategoryTrans", user.getCategoriesTransaction().toString());

                        ma.installFragment(new AccountFragment(), false);//TODO
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public String getTypeFirebaseTransaction() {
        return typeFirebaseTransaction;
    }


    public void setTypeFirebaseTransaction(String typeFirebaseTransaction) {
        this.typeFirebaseTransaction = typeFirebaseTransaction;
    }

    public User getUser() {
        return user;
    }

   public void setUser(User user) {
        this.user = user;
    }

   public String getKeyUser() {
        return keyUser;
    }

    public int getCurAccount() {
        return curAccount;
    }

    public void setCurAccount(int curAccount) {
        this.curAccount = curAccount;
    }
}
