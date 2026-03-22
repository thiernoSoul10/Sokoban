package Model.Global;

public class Couple<Type> implements Comparable<Couple<Type>> {
    
    private Type valeur;
    private int priority;

    @Override
    public int compareTo(Couple<Type> c){
        return Integer.compare(this.priority, c.getPriority());
    }

    public Couple(Type valeur, int priority){
        this.valeur = valeur;
        this.priority = priority;
    }

    public Couple(Type valeur){
        this.valeur = valeur;
        this.priority = 0;
    }

    public void setPriority(int priority){ this.priority = priority; }

    public void setValeur(Type valeur){ this.valeur = valeur; }

    public int getPriority(){ return this.priority; }

    public Type getValeur(){ return this.valeur; }
}
