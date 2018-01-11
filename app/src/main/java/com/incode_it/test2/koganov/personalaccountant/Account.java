package com.incode_it.test2.koganov.personalaccountant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Account
{
    private double price;
    private String name;
    private double sum;
    private String currency;
    private ArrayList<Transaction> transactions;

    public Account()
    {
        transactions = new ArrayList<>();
    }

    public Account(String name, double sum, String currency) {
        transactions = new ArrayList<>();
        this.price = 1;
        this.name = name;
        this.sum = sum;
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUSDfromUAH(Double sum)
    {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(sum/price);
        return format;
    }

    public String getUAHfromUSD(Double sum)
    {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(sum*price);
        return format;
    }

    public String getFormatSum(Double sum)
    {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(sum);
        return format;
    }

    public String toString()
    {
        String str="";
        if(!currency.equals("UAH"))//not UAH
        {
            str = name + "("+currency+")"+":"+"\n" +  getFormatSum(sum) + " " + currency + " / " + getUAHfromUSD(sum) + " UAH";
        }
        else
        {
            str = name + "("+currency+")"+":"+"\n" +  getFormatSum(sum) + " " + currency;
        }

        return str;
    }
}

