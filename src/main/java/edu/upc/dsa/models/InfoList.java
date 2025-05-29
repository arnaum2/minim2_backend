package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class InfoList {
    private List<Info> ranking;
    private int posicionUsuario;

    public InfoList() {}

    public InfoList(List<Info> ranking, int posicionUsuario) {
        this.ranking = ranking;
        this.posicionUsuario = posicionUsuario;
    }

    public List<Info> getRanking() {
        return ranking;
    }

    public void setRanking(List<Info> ranking) {
        this.ranking = ranking;
    }

    public int getPosicionUsuario() {
        return posicionUsuario;
    }

    public void setPosicionUsuario(int posicionUsuario) {
        this.posicionUsuario = posicionUsuario;
    }
}