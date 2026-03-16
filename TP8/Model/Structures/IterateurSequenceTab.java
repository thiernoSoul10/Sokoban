package Model.Structures;

public class IterateurSequenceTab<Type> implements Iterateur<Type> {
    private SequenceTableau<Type> seq;
    private int current;
    private int count;
    private boolean peutSupprimer;

    public IterateurSequenceTab(SequenceTableau<Type> s){
        seq = s;
        current = seq.tete;
        count = 0;
        peutSupprimer = false;
    }

    // renvoie vrai si l'itérateur n'est pas à la dernière position.
    public boolean aProchain(){
        return count < seq.nbElement;
    }

    // fait avancer l'itérateur à la prochaine position et renvoie la valeur de l'élément traversé (situé juste avant l'itérateur, une fois le déplacement effectué).
    public Type prochain(){
        if(!aProchain())
            throw new RuntimeException("Pas de prochain pour ce iterateur");
        
        @SuppressWarnings("unchecked")
        Type element = (Type) seq.tab[current];
        current = (current +1)%seq.TAILLE;
        count++;

        peutSupprimer = true;
        return element;
    }

    // supprime le dernier élément traversé via prochain. Ne peut être appelé qu'une seule fois après un appel à prochain, dans le cas contraire, lève une exception de type IllegalStateException.
    public void supprime(){
        if(peutSupprimer) seq.supprime(current -1); 
    }
    
}