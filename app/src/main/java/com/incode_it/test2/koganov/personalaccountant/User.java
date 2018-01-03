package com.incode_it.test2.koganov.personalaccountant;

import java.util.ArrayList;

public class User
{
    private String email;
    private ArrayList<Account> accounts;
    private ArrayList<String> categoriesTransaction;//name from Firebase

    public User()
    {
        accounts = new ArrayList<>();
        categoriesTransaction = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<String> getCategoriesTransaction() {
        return categoriesTransaction;
    }

    public void setCategoriesTransaction(ArrayList<String> categoriesTransaction) {
        this.categoriesTransaction = categoriesTransaction;
    }
}
