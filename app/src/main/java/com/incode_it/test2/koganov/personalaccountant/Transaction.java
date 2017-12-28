package com.incode_it.test2.koganov.personalaccountant;

public class Transaction
{
    private String type;
    private String description;
    private Double sum;
    private String contractor;

    public Transaction()
    {
        description="";
        contractor="";
    }

    public String toString()
    {
        String str="";

        str = type + "; " + description + "; " + Double.toString(sum)+"; "+contractor;

        return str;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }
}
