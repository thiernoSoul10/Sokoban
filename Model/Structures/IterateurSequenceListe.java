package Model.Structures;


public class IterateurSequenceListe<Type extends Comparable<Type>> implements Iterateur<Type> {
    private SequenceListe<Type> seq;
    private Maillon<Type> current;
    private boolean peutSupprimer;

    public IterateurSequenceListe(SequenceListe<Type> s){
        seq = s;
        current = seq.getTete();
        peutSupprimer = false;
    }

    // renvoie vrai si l'itérateur n'est pas à la dernière position.
    public boolean aProchain(){
        return current != null && current.getSuivant() != null;
    }

    // fait avancer l'itérateur à la prochaine position et renvoie la valeur de l'élément traversé (situé juste avant l'itérateur, une fois le déplacement effectué).
    public Type prochain(){
        if(!aProchain())
            throw new RuntimeException("Pas de prochain pour ce iterateur");
        Type element = current.getElement();
        current = current.getSuivant();

        peutSupprimer = true;
        return element;
    }

    // supprime le dernier élément traversé via prochain. Ne peut être appelé qu'une seule fois après un appel à prochain, dans le cas contraire, lève une exception de type IllegalStateException.
    public void supprime(){
        if(peutSupprimer) seq.supprimePrec(current.getPrecedent());   
    }
    
}