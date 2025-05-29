package edu.upc.dsa.models;

public class Issue {
    String date;
    String titol;
    String informer;
    String message;

    public Issue(){

    }

    public Issue(String date, String titol, String informer, String message){
        this.date = date;
        this.titol = titol;
        this.informer = informer;
        this.message = message;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTitol(String titol){
        this.titol = titol;
    }
    public String getTitol(){
        return this.titol;
    }

    public String getInformer() {
        return informer;
    }

    public void setInformer(String informer) {
        this.informer = informer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}