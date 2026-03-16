package Model.Structures;


public interface Iterateur<Type> {
    // renvoie vrai si l'itérateur n'est pas à la dernière position.
    public boolean aProchain();

    // fait avancer l'itérateur à la prochaine position et renvoie la valeur de l'élément traversé (situé juste avant l'itérateur, une fois le déplacement effectué).
    public Type prochain();

    // supprime le dernier élément traversé via prochain. Ne peut être appelé qu'une seule fois après un appel à prochain, dans le cas contraire, lève une exception de type IllegalStateException.
    public void supprime();
}
