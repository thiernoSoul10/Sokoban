package Vue;
import java.util.Scanner;

import Model.Jeu;
import Model.Global.Configuration;
import Model.Global.Coordonnees;
import Model.Niveaux.Niveau;
import Model.Structures.SequenceInterface;

public class InterfaceTextuelle {
    private Jeu jeu; 

    public InterfaceTextuelle(){

        jeu = new Jeu(Configuration.ouvre("Terrains/Tests.txt"));
    }

    public void lancer(){
        Scanner s = new Scanner(System.in);

        while(true){

            Niveau n = jeu.niveau();
            n.afficher_niveau();
            
            System.out.print("Commande (g,d,h,b,q et m pour changer de mode): ");

            char c = s.next().charAt(0);

            if(c=='q') break;

            if(c == 'm'){
                SequenceInterface<Coordonnees> seqC = n.getCaissesCoordonnees();
                SequenceInterface<Coordonnees> chemin = n.deplacementAutomatiquePousseur(seqC.dernier());
                if(chemin == null) System.out.println("Pas de chemin vers (" + seqC.dernier().getX() + ", " + seqC.dernier().getY() +")");
                while (chemin != null && !chemin.estVide()) {
                    Coordonnees p = chemin.extraitTete();
                    int dx = p.getX() - n.getPousseurX();
                    int dy = p.getY() - n.getPousseurY();


                         if (dx == -1) n.deplacePousseur('g');
                    else if (dx ==  1) n.deplacePousseur('d');
                    else if (dy == -1) n.deplacePousseur('h');
                    else if (dy ==  1) n.deplacePousseur('b');

                    n.afficher_niveau();
                }
            } else {
                n.deplacePousseur(c);
            }

            if(n.niveauOk()){
                Configuration.debugeur("Vs avez termine cette Etape\n");
                if(!jeu.prochainNiveau())
                    Configuration.debugeur("Vs avez termine Ce Jeu\n");
            }

        }

        s.close();
    }
}
