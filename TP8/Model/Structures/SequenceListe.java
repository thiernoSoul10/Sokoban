package Model.Structures;

import java.lang.String;

public class SequenceListe<Type extends Comparable<Type>> implements SequenceInterface<Type>{
    private Maillon<Type> tete;
    private Maillon<Type> queue;
    private int nbElement;

    public SequenceListe(Maillon<Type> m){
        tete = m;
        queue = tete;
        nbElement = 1;
    }

    public SequenceListe(){
        tete = new Maillon<>();
        queue = tete;
        nbElement = 0;
    }

    // getters
    public Maillon<Type> getTete(){ return tete; }

    public Maillon<Type> getQueue(){ return queue; }

    public int getNbElement(){ return nbElement; }

    // insère l'élément nommé element en début de séquence
    public void insereTete(Type element){
        if(estVide()){
            tete.setElement(element);
            nbElement++;
            return;
        }
        
        Maillon<Type> nouveau = new Maillon<>(element, tete);

        tete.setPrecedent(nouveau);
        
        tete = nouveau;
        nbElement++;
    }

    public void insert(Type element){
        if(this.estVide()){
            this.insereTete(element);
            return;
        }

        if(element.compareTo(this.queue.getElement()) == 0){
            this.insereQueue(element); // pour nous permettre d'inserer chaque element d'une file
                                      //  de priorité équivalente à la fin
            return;
        }
        Maillon<Type> tmp = this.getTete();

        while (tmp != null && element.compareTo(tmp.getElement()) < 0) {
            tmp = tmp.getSuivant();
        }

        if(tmp == this.getTete()){
            this.insereTete(element);
            return;
        }

        if(tmp == null){
            insereQueue(element);
            return;
        }

        Maillon<Type> prec = tmp.getPrecedent();
        Maillon<Type> nouveau = new Maillon<>(element);

        nouveau.setSuivant(tmp);
        nouveau.setPrecedent(prec);
        prec.setSuivant(nouveau);
        tmp.setPrecedent(nouveau);
        nbElement++;
    }

    // insère l'élément nommé element en fin de séquence (en dernière position) ;
    public void insereQueue(Type element){

        if(estVide()){
            insereTete(element);
            return;
        }

        Maillon<Type> nouveauMaillon = new Maillon<>(element);

        // Insertion à la fin
        queue.setSuivant(nouveauMaillon);
        nouveauMaillon.setPrecedent(queue);
        queue = nouveauMaillon;

        nbElement++;
    }

    // extrait et renvoie la valeur de l'élément situé en début de séquence;
    public Type extraitTete(){
        if(estVide()){
            throw new RuntimeException("Séquence vide");
        }

        Type val = tete.getElement();

        if(nbElement == 1){
            nbElement--;
            return val;
        }

        Maillon<Type> ancien = tete;

        tete = tete.getSuivant();
        tete.setPrecedent(null);

        ancien.setSuivant(null);

        nbElement--;
        return val;
    }

    public Type dernier(){
        if(estVide()){
            throw new RuntimeException("Séquence vide");
        }

        return queue.getElement();
    }

    // renvoie vrai si et seulement si la séquence est vide.
    public boolean estVide(){
        return nbElement == 0;
    }

    public String toString(){
        String s = "";
        Maillon<Type> tmp = tete;

        while(tmp != null){
            s += tmp.getElement();
            tmp = tmp.getSuivant();
            if(tmp != null) s+= "--->";
        }

        return s;
    }

    public Iterateur<Type> iterateur(){
        return new IterateurSequenceListe<>(this);
    }

    public void supprimePrec(Maillon<Type> m){

        if(m == tete){
            tete = m.getSuivant();

            if(tete != null)
                tete.setPrecedent(null);
        }
        else if(m == queue){
            queue = m.getPrecedent();

            if(queue != null)
                queue.setSuivant(null);
        }
        else{
            Maillon<Type> prec = m.getPrecedent();
            Maillon<Type> suiv = m.getSuivant();

            prec.setSuivant(suiv);
            suiv.setPrecedent(prec);
        }

        m.setSuivant(null);
        m.setPrecedent(null);

        nbElement--;
    }
}