package edu.upc.dsa.models;

public class Intercambio {
    private int tarrosMiel;
    private int flores;

    public Intercambio(){}

    public Intercambio(int tarrosMiel, int Flores){
        this.tarrosMiel = tarrosMiel;
        this.flores = Flores;
    }

    public void setTarrosMiel(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }

    public void setFlores(int flores) {
        this.flores = flores;
    }

    public int getFlores() {
        return flores;
    }

    public int getTarrosMiel() {
        return tarrosMiel;
    }
}
