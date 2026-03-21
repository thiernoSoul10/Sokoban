package Vue;
import java.util.Scanner;

import Model.Jeu;
import Model.Global.Configuration;

public class InterfaceTextuelle {
    private Jeu jeu; 

    public InterfaceTextuelle(){

        jeu = new Jeu(Configuration.ouvre("Terrains/prog.txt"));
    }

    public void lancer(){
        Scanner s = new Scanner(System.in);

        while(true){

            jeu.niveau().afficher_niveau();
            
            System.out.print("Commande (g,d,h,b,q): ");

            char c = s.next().charAt(0);

            if(c=='q') break;

            jeu.niveau().deplacePousseur(c);

            if(jeu.niveau().niveauOk()){
                Configuration.debugeur("Vs avez termine cette Etape\n");
                if(!jeu.prochainNiveau())
                    Configuration.debugeur("Vs avez termine Ce Jeu\n");
            }

        }

        s.close();
    }
}
