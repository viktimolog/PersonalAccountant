package com.incode_it.test2.koganov.personalaccountant;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Controller
{
    private String url;
    private StringBuilder sb;
    private Double priceUAHUSD;
    private ArrayList<Bank>banks;

    private MainActivity ma;
    private String keyUser;
    private User user;
    private String typeFirebaseTransaction;
    private int curAccount;
    private int curTypeTrans;
    private int recip;

    public Controller(MainActivity ma) {
        url = "http://bank-ua.com/export/exchange_rate_cash.json";
        sb=new StringBuilder("{\"banks\":");
        this.ma = ma;
        user = new User();
        user.setAccounts(new ArrayList<Account>());
        curTypeTrans=0;
    }

    class Bank
    {
        private String date;
        private String bankName;
        private String sourceUrl;
        private String codeNumeric;
        private String codeAlpha;
        private String rateBuy;
        private String rateBuyDelta;
        private String rateSale;
        private String rateSaleDelta;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getCodeNumeric() {
            return codeNumeric;
        }

        public void setCodeNumeric(String codeNumeric) {
            this.codeNumeric = codeNumeric;
        }

        public String getCodeAlpha() {
            return codeAlpha;
        }

        public void setCodeAlpha(String codeAlpha) {
            this.codeAlpha = codeAlpha;
        }

        public String getRateBuy() {
            return rateBuy;
        }

        public void setRateBuy(String rateBuy) {
            this.rateBuy = rateBuy;
        }

        public String getRateBuyDelta() {
            return rateBuyDelta;
        }

        public void setRateBuyDelta(String rateBuyDelta) {
            this.rateBuyDelta = rateBuyDelta;
        }

        public String getRateSale() {
            return rateSale;
        }

        public void setRateSale(String rateSale) {
            this.rateSale = rateSale;
        }

        public String getRateSaleDelta() {
            return rateSaleDelta;
        }

        public void setRateSaleDelta(String rateSaleDelta) {
            this.rateSaleDelta = rateSaleDelta;
        }

        @Override
        public String toString() {
            return "Bank{" +
                    "date='" + date + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", sourceUrl='" + sourceUrl + '\'' +
                    ", codeNumeric='" + codeNumeric + '\'' +
                    ", codeAlpha='" + codeAlpha + '\'' +
                    ", rateBuy='" + rateBuy + '\'' +
                    ", rateBuyDelta='" + rateBuyDelta + '\'' +
                    ", rateSale='" + rateSale + '\'' +
                    ", rateSaleDelta='" + rateSaleDelta + '\'' +
                    '}';
        }
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

            else if(typeFirebaseTransaction.equals("setPrice"))
            {
                setPrice();
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

    public void setPrice()
    {
        URL oracle = null;
        try
        {
            oracle = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String inputLine;
        try
        {
            while ((inputLine = in.readLine()) != null)
            {
                sb.append(inputLine);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        sb.append("}");

        Log.d("MyTag", sb.toString());//OK

        banks=null;

        banks = new ArrayList<>();

        JSONObject obj1=null;

        JSONArray jArr=null;

        try
        {
            obj1 = new JSONObject(sb.toString());

            jArr = obj1.getJSONArray("banks");

           for (int i=0; i < jArr.length(); i++)
           {
               JSONObject obj = jArr.getJSONObject(i);
               Bank bank = new Bank();
               bank.setDate(obj.getString("date"));
               bank.setBankName(obj.getString("bankName"));
               bank.setSourceUrl(obj.getString("sourceUrl"));
               bank.setCodeNumeric(obj.getString("codeNumeric"));
               bank.setCodeAlpha(obj.getString("codeAlpha"));
               bank.setRateBuy(obj.getString("rateBuy"));
               bank.setRateBuyDelta(obj.getString("rateBuyDelta"));
               bank.setRateSale(obj.getString("rateSale"));
               bank.setRateSaleDelta(obj.getString("rateSaleDelta"));
               banks.add(bank);
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if(banks.size()==0)
        {
            priceUAHUSD=1.0;
        }
        else
        {
            ArrayList<Double>prices = new ArrayList<>();
            for(int i=0; i<banks.size(); i++)
            {
                if(banks.get(i).getCodeAlpha().equals("USD"))
                {
                    prices.add(Double.parseDouble(banks.get(i).getRateBuy()));
                }
            }
            if(prices.size()>0)
            {
                Collections.sort(prices);
                priceUAHUSD = prices.get(prices.size()-1);
            }

            for(int i=0; i<user.getAccounts().size(); i++)
            {
                if(user.getAccounts().get(i).getCurrency().equals("USD"))
                {
                    user.getAccounts().get(i).setPrice(priceUAHUSD);
                    Log.d("MyTag", user.getAccounts().get(i).getPrice()+"");//todo
                }
            }
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

    public Double getPriceUAHUSD() {
        return priceUAHUSD;
    }

    public void setPriceUAHUSD(Double priceUAHUSD) {
        this.priceUAHUSD = priceUAHUSD;
    }

    public int getCurTypeTrans() {
        return curTypeTrans;
    }

    public void setCurTypeTrans(int curTypeTrans) {
        this.curTypeTrans = curTypeTrans;
    }
}
