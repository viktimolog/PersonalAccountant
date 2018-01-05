package com.incode_it.test2.koganov.personalaccountant;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Controller
{
    private MainActivity ma;
    private String keyUser;
    private User user;
    private String typeFirebaseTransaction;
    private int curAccount;
    private int recip;

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
            else if(typeFirebaseTransaction.equals("addNewTransactionInFirebase"))
            {
                addNewTransactionInFirebase();
            }
            else if(typeFirebaseTransaction.equals("addNewAccInFirebase"))
            {
                addNewAccInFirebase();
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

    public void handleNewTransaction(Transaction trans)
    {
        Double sumAcc;

        sumAcc = getUser().getAccounts().get(getCurAccount()).getSum();

        if(!trans.getType().equals("Income")&&(sumAcc<trans.getSum()))//-
        {
           Toast.makeText(ma, "Sorry, it is impossible. Account has too little money!", Toast.LENGTH_LONG).show();
        }
            else//+ or possible -
            {
                if(!trans.getType().equals("Income"))
                {
                    trans.setSum(-1*trans.getSum());
                }

                sumAcc += trans.getSum();

//                getUser().getAccounts().get(getCurAccount()).setSum(sumAcc);
//
//                getUser().getAccounts().get(getCurAccount()).getTransactions().add(trans);

                if(trans.getType().equals("Transfer"))
                {
                    Transaction inTrans = new Transaction(
                            new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss").format(Calendar.getInstance().getTime())
                            ,"Income"
                            ,trans.getCategory()
                            ,trans.getDescription()
                            ,-1*trans.getSum()
                            ,getUser().getAccounts().get(getCurAccount()).toString());

                    user.getAccounts().get(recip).setSum(user.getAccounts().get(recip).getSum()+inTrans.getSum());

                    getUser().getAccounts().get(getRecip()).getTransactions().add(inTrans);
                }

                getUser().getAccounts().get(getCurAccount()).setSum(sumAcc);

                getUser().getAccounts().get(getCurAccount()).getTransactions().add(trans);

                newAsync("addNewTransactionInFirebase");
            }
    }

    public void newAsync(String typeTransaction)
    {
        typeFirebaseTransaction = typeTransaction;
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

        myRef.child(keyUser).child("accounts").setValue(user.getAccounts());

       /* DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child(keyUser).child("accounts").child(Integer.toString(curAccount))
                .setValue(user.getAccounts().get(curAccount));

        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child(keyUser).child("accounts").child(Integer.toString(curAccount)).child("sum")
                .setValue(user.getAccounts().get(curAccount).getSum());*/
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
                    /*    Log.i("MyTag getUserFromFireDB", user.getEmail());
                        Log.i("MyTag getUserFromFireDB", user.getAccounts().toString());

                        Log.i("MyTag getCategoryTrans", user.getCategoriesTransaction().toString());*/

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

    public int getRecip() {
        return recip;
    }

    public void setRecip(int recip) {
        this.recip = recip;
    }
}
