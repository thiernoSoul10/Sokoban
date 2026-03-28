package Vue;

import java.util.Scanner;

import Model.Jeu;
import Model.Global.Configuration;
import Model.Niveaux.Niveau;
import Model.Niveaux.Solver;

public class InterfaceTextuelle {
    private Jeu jeu;

    public InterfaceTextuelle(){
        jeu = new Jeu(Configuration.ouvre("Terrains/Tests.txt"));
    }

    public void lancer(){
        Scanner s = new Scanner(System.in);
        boolean OK = true;

        while(OK){
            Niveau n = jeu.niveau();
            n.afficher_niveau();

            System.out.print("Commande (g,d,h,b,q et m pour changer de mode): ");
            char c = s.next().charAt(0);


            switch (c) {
                case 'm':
                    Solver dep = new Solver();
                    String solution = dep.solve(n);

                    if(solution != ""){
                        int coups = solution.length();
                        for(int i = 0; i < coups; i++){
                            char dir = solution.charAt(i);
                            n.deplacePousseur(dir);
                        }
                    }
                    break;
                case 'n':
                    jeu.prochainNiveau();
                    break;
                case 'q':
                    OK = false;
                    break;
                default:
                    n.deplacePousseur(c);
                    break;
            }

            if(n.niveauOk()){
                Configuration.debugeur("Vs avez termine cette Etape\n");
                if(!jeu.prochainNiveau()){
                    Configuration.debugeur("Vs avez termine Ce Jeu\n");
                    break;
                }
            }
        }

        s.close();
    }
}