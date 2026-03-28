package Model.Niveaux;

import Model.Global.Coordonnees;

public class Caisse implements Comparable<Caisse> {
    private Coordonnees coordonnees;
    private int numero;

    public Caisse(int numero, int x, int y){
        this.numero = numero;
        coordonnees = new Coordonnees(x, y);
    }

    void setCoordonnees(int x, int y){
        coordonnees.setX(x);
        coordonnees.setY(y);
    }

    public int getNumero(){ return numero; }
    public Coordonnees getCoordonnees(){ return coordonnees; }

    @Override
    public int compareTo(Caisse o){
        if((numero == o.numero) || (coordonnees.getX() == o.getCoordonnees().getX() && coordonnees.getY() == o.getCoordonnees().getY())) return 0;  // on reste simple
        return 1;
    }
}
