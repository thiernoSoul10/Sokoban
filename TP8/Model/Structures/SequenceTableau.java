package Model.Structures;

public class SequenceTableau<Type> implements SequenceInterface<Type>{
    int TAILLE;
    int nbElement;
    int tete;
    int queue;
    Object[] tab;
    int current;

    public SequenceTableau(){
        TAILLE = 100;
        queue = 50;
        tete = 50;
        nbElement = 0;
        current = tete;
        tab = new Object[TAILLE];
    }

    public SequenceTableau(int TAILLE){
        this.TAILLE = TAILLE;
        queue = TAILLE/2;
        tete = queue;
        nbElement = 0;
        current = tete;
        tab = new Object[TAILLE];
    }

    private void doubleSize(){
        int nouvelleTaille = this.TAILLE * 2;
        Object[] newTab = new Object[nouvelleTaille];

        int j = tete;
        int i = tete;
        int k = 0;

        while(k != nbElement){

            if(j == this.TAILLE) j = 0;

            newTab[i] = tab[j];

            j++;
            i++;
            k++;
        }

        this.tab = newTab;
        this.TAILLE = nouvelleTaille;

        // la tête ne change pas
        // la queue devient la position après le dernier élément
        this.queue = i;
    }


    // insère l'élément nommé element en début de séquence
    public void insereTete(Type element){
        if(nbElement == this.TAILLE){ 
            // tableau remplis
            this.doubleSize();
        }

        // déplacement circulaire de la tête
        tete = (tete - 1 + this.TAILLE) % this.TAILLE;

        tab[tete] = element;
        nbElement++;
    }

    public void insert(Type element){
        
    }

    // insère l'élément nommé element en fin de séquence (en dernière position) ;
    public void insereQueue(Type element){

        if(nbElement == this.TAILLE){ 
            // Sequence Pleine
            this.doubleSize();
        }

        tab[queue] = element;

        // déplacement circulaire de la queue
        queue = (queue + 1) % this.TAILLE;

        nbElement++;
    }

    // extrait et renvoie la valeur de l'élément situé en début de séquence;
    public Type extraitTete(){
        if(estVide()){
            throw new RuntimeException("Séquence vide");
        }

        @SuppressWarnings("unchecked")
        Type element = (Type) tab[tete];

        nbElement--;

        // déplacement circulaire de la tête
        tete = (tete + 1) % this.TAILLE;

        return element;
    }

    public Type dernier(){
        if(estVide()){
            throw new RuntimeException("Séquence vide");
        }

       @SuppressWarnings("unchecked")
        Type element = (Type) tab[queue];

        return element;
    }

    public int getNbElement(){
        return nbElement;
    }

    // renvoie vrai si et seulement si la séquence est vide.
    public boolean estVide(){
        return nbElement == 0;
    }

    public String toString(){
        String s="";
        int i = tete;
        int count = 0;

        while (count < nbElement) {
            
            s += tab[i];
            i++;

            if(i >= this.TAILLE) i = 0; // on revient à 0

            count++;
        }
        return s;
    }

    public Iterateur<Type> iterateur(){
        return new IterateurSequenceTab<>(this);
    }

    public void supprime(int pos){

        int i = pos;

        while(i != queue){

            int next = (i + 1) % TAILLE;

            tab[i] = tab[next];

            i = next;
        }

        queue = (queue - 1 + TAILLE) % TAILLE;

        nbElement--;
    }
}