package Model.Structures;

public class Maillon<Type>{
    private Type element;
    private Maillon<Type> suivant;
    private Maillon<Type> prec;

    public Maillon(){
        this.element = null;
        this.suivant = null;
        this.prec = null;
    }
    
    public Maillon(Type element){
        this.element = element;
        this.suivant = null;
        this.prec = null;
    }

    public Maillon(Type element, Maillon<Type> suivant){
        this.element = element;
        this.suivant = suivant;
        this.prec = null;
    }

    public void setElement(Type element){
        this.element = element;
    }

    public void setSuivant(Maillon<Type> suivant){
        this.suivant = suivant;
    }

    public void setPrecedent(Maillon<Type> prec){
        this.prec = prec;
    }

    public Type getElement(){ return element; }
    
    public Maillon<Type> getSuivant(){ return suivant; }

    public Maillon<Type> getPrecedent(){ return prec; }
}