package Model.Niveaux;

import java.io.*;


public class RedacteurNiveaux{

    PrintStream p;

    public RedacteurNiveaux(OutputStream out){
        p = new PrintStream(out);
    }

    /*
     * Entrée: Un niveau de Sokoban
     * Sortie: rien du tout
     * Effet: le niveau passé en paramètre est écrit sur le flux de sortie out de cette class. */
    public void ecrisNiveau(Niveau n){
        int lignes = n.lignes();
        int colonnes = n.colonnes();

        for(int i = 0; i < lignes; i++){
            for(int j = 0; j < colonnes; j++){
                
                if(n.aBut(i, j) && n.aCaisse(i, j))  p.print('*');
                else if(n.aBut(i, j) && n.aPousseur(i, j))p.print('+');
                else if(n.estVide(i, j))   p.print(' ');
                else if(n.aMur(i, j))      p.print('#');
                else if(n.aBut(i, j))      p.print('.');
                else if(n.aCaisse(i, j))   p.print('$');
                else if(n.aPousseur(i, j)) p.print('@');
            }
            p.println();
        }
        p.print("; " + n.nom());
        p.println();
        p.println();
    }
}