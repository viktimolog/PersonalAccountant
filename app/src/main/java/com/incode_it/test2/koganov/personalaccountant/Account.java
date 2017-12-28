package com.incode_it.test2.koganov.personalaccountant;

import java.util.ArrayList;

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
        price = 1;//TODO
    }

    public Account(String name, double sum, String currency) {
        transactions = new ArrayList<>();
        this.price = 1;//TODO
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

    public String toString()//TODO name + sum + sum/kurs
    {
        String str="";
        if(!currency.equals("UAH"))
        {
            str = name + "("+currency+")"+":"+"\n" +  Double.toString(sum) + " " + currency + " / " + Double.toString(sum/price) + " UAH";
        }
        else
        {
            str = name + "("+currency+")"+":"+"\n" +  Double.toString(sum) + " " + currency;
        }

        return str;
    }
}

