package Model;

import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import Model.Niveaux.LecteurNiveaux;
import Model.Niveaux.Niveau;

public class Jeu {
    private Niveau current;
    private Deque<Niveau> niveaux;

    public Jeu(FileInputStream fin){
        niveaux = new ArrayDeque<>();
        LecteurNiveaux lecteurNiveaux = new LecteurNiveaux(fin);

        boolean OK = true;
        while(OK){
            Niveau niveau = lecteurNiveaux.lisProchainNiveau();
            if(niveau == null){ OK = false; }
            else { niveaux.addLast(niveau); }
        }

        if(!niveaux.isEmpty())
            current = niveaux.pollFirst();
    }

    public Niveau niveau(){
        return current;
    }

    public boolean prochainNiveau(){
        if(!niveaux.isEmpty()){
            current = niveaux.pollFirst();
            return true;
        }
        return false;
    }
}