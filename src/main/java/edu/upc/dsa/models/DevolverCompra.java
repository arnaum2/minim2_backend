package edu.upc.dsa.models;

public class DevolverCompra {

    private int tarrosMiel;

    public DevolverCompra (){}

    public DevolverCompra(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }

    public int getTarrosMiel() {
        return tarrosMiel;
    }

    public void setTarrosMiel(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }
}
