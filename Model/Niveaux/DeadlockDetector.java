package Model.Niveaux;

import java.util.*;
import Model.Global.Coordonnees;

public class DeadlockDetector {

    private Niveau niveau;

    public DeadlockDetector(Niveau n){
        this.niveau = n;
    }

    private boolean estMurSafe(int y, int x){
        if(y < 0 || y >= niveau.lignes() || x < 0 || x >= niveau.colonnes())
            return true;
        return niveau.aMur(y, x);
    }

    public boolean deadlock2x2(HashSet<Coordonnees> caisses){

        for(Coordonnees c : caisses){

            int x = c.getX();
            int y = c.getY();

            int[][] squares = {
                {0,0, 1,0, 0,1, 1,1},
                {0,0, -1,0, 0,1, -1,1},
                {0,0, 1,0, 0,-1, 1,-1},
                {0,0, -1,0, 0,-1, -1,-1}
            };

            for(int[] s : squares){

                Coordonnees c1 = new Coordonnees(x+s[0], y+s[1]);
                Coordonnees c2 = new Coordonnees(x+s[2], y+s[3]);
                Coordonnees c3 = new Coordonnees(x+s[4], y+s[5]);
                Coordonnees c4 = new Coordonnees(x+s[6], y+s[7]);

                boolean allBlocked =
                    (estMurSafe(c1.getY(), c1.getX()) || caisses.contains(c1)) &&
                    (estMurSafe(c2.getY(), c2.getX()) || caisses.contains(c2)) &&
                    (estMurSafe(c3.getY(), c3.getX()) || caisses.contains(c3)) &&
                    (estMurSafe(c4.getY(), c4.getX()) || caisses.contains(c4));

                if(allBlocked){

                    if(!niveau.estBut(c1) &&
                       !niveau.estBut(c2) &&
                       !niveau.estBut(c3) &&
                       !niveau.estBut(c4)){

                        return true;
                    }
                }
            }
        }

        return false;
    }
}