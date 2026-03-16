package Model.Structures;



public interface SequenceInterface<Type>{
    

    // insère l'élément nommé element en début de séquence
    public void insereTete(Type element);

    // insère l'élément nommé element en fin de séquence (en dernière position) ;
    public void insereQueue(Type element);

    public void insert(Type element);

    // extrait et renvoie la valeur de l'élément situé en début de séquence;
    public Type extraitTete();

    // renvoie vrai si et seulement si la séquence est vide.
    public boolean estVide();

    public String toString();

    public Iterateur<Type> iterateur();
}
