package Model;

import java.io.FileInputStream;

import Global.Couple;
import Model.Niveaux.LecteurNiveaux;
import Model.Niveaux.Niveau;
import Model.Structures.FAP;
import Model.Structures.Iterateur;

public class Jeu {
    private Niveau current;
    private FAP<Couple<Niveau>> niveaux;
    private Iterateur<Couple<Niveau>> it;

    public Jeu(FileInputStream fin){
        niveaux = new FAP<>();
        boolean OK = true;
        LecteurNiveaux lecteurNiveaux = new LecteurNiveaux(fin);

        while(OK){
            Niveau niveau = lecteurNiveaux.lisProchainNiveau();
            if(niveau == null){ OK = false; }
            else { 
                // dans notre cas puisque la priorité est la même on enfile qu'en fin de file
                niveaux.enfiler(new Couple<>(niveau));
            }
        }

        it= niveaux.iterateur();
        if(it.aProchain())
            current = it.prochain().getValeur();
    }

    public Niveau niveau(){
        return current;
    }

    public boolean prochainNiveau(){
        if(it.aProchain()){
            current = it.prochain().getValeur();
            return true;
        }
        return false;
    }

}
