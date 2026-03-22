package Model.Structures;

// la plus plus grande valeur est prioritaire.
public class FAP<E extends Comparable<E>> {
    // la priorité est la valeur elle même
    private SequenceInterface<E> seq; // on utilisera la séquence liste
    int nbElement;

    public FAP(){
        seq = new SequenceListe<>();
        nbElement = 0;
    }

    public void enfiler(E e){
        seq.insert(e);
        nbElement++;
    }

    public E defiler(){
        if(nbElement > 0) nbElement--;
        
        return seq.extraitTete();
    }

    public int size(){
        return nbElement;
    }

    public SequenceInterface<E> getSeq(){ return this.seq; }

    public Iterateur<E> iterateur(){
        return seq.iterateur();
    }
}
