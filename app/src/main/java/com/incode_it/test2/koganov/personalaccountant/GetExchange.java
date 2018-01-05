package com.incode_it.test2.koganov.personalaccountant;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetExchange
{
    private String url;
    private StringBuilder sb;

    public GetExchange(String url) {
        this.url = url;
        sb = new StringBuilder();
    }

    public void getJSON1()
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
        Log.d("MyTag", sb.toString());//todo OK
    }

    public void getJSON()
    {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection(); //Cast exception todo

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            StringBuilder
                    sb = new StringBuilder("");
            String line;
            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
                sb.append(line + "\n");
                Log.d("MyTag", sb.toString());
            }
            br.close();
//            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
//        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StringBuilder getSb() {
        return sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    public void newAsync()
    {
        new MyAsync().execute();
    }

    class MyAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unused)
        {
            getJSON1();//todo

            return null;
        }
        @Override
        protected void onProgressUpdate(Void... items) {

        }
        @Override
        protected void onPostExecute(Void unused) {

        }
    }
}
