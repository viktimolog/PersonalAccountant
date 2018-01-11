package com.incode_it.test2.koganov.personalaccountant;

public class Transaction
{
    private String dateTime;
    private String type;
    private String category;
    private String description;
    private Double sum;
    private String recipient;


    public Transaction()
    {
        dateTime="";
        description="";
        recipient="";
    }

    public Transaction(String dateTime, String type, String category, String description, Double sum, String recipient) {
        this.dateTime = dateTime;
        this.type = type;
        this.category = category;
        this.description = description;
        this.sum = sum;
        this.recipient = recipient;
    }

    public String toString()
    {
        String str="";

        if(type.equals("Income"))
        {
            str += (char)9660;
        }
        else
        {
            str+= (char)9650;
        }

//        str += dateTime + "\n" + type + "; " + category +";" + description + "; " + Double.toString(sum)+"; "+recipient;

        str += dateTime + "\n" + "Type: " + type + "\n" + "Category: " + category +"\n" + "Description: " + description + "\n"
                + "Sum: " + Double.toString(sum)+"\n"+"Partner: " + recipient;

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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
