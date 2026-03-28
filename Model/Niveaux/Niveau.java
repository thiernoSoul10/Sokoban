package Model.Niveaux;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Model.Global.Configuration;
import Model.Global.Coordonnees;

public class Niveau {

    private int lignes;
    private int colonnes;
    private int nbCaisse;
    private String nom;
    private char[][] cases;
    private boolean pousseurAjoute = false;
    private int pousseurX;
    private int pousseurY;
    private ArrayList<Coordonnees> butsCoordonnees;
    private ArrayList<Caisse> caisses;

    public Niveau(int lignes, int colonnes) {
        this.lignes   = lignes;
        this.colonnes = colonnes;
        nbCaisse      = 1;
        cases         = new char[lignes][colonnes];
        butsCoordonnees = new ArrayList<>();
        caisses         = new ArrayList<>();
    }

    // -----------------------------------------------------------------------
    // Etat du niveau
    // -----------------------------------------------------------------------

    public boolean niveauOk() {
        for (Coordonnees b : butsCoordonnees) {
            if (!aCaisse(b.getY(), b.getX())) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------------------------------------------
    // Construction
    // -----------------------------------------------------------------------

    public void fixeNom(String s) {
        this.nom = s.startsWith(";") ? s.substring(1).trim() : s;
    }

    public void videCase(int i, int j) {
        if (i < lignes && j < colonnes) cases[i][j] = ' ';
    }

    public void ajouteMur(int i, int j) {
        if (i < lignes && j < colonnes) cases[i][j] = '#';
        else Configuration.debugeur("Hors des limites du terrain");
    }

    public void ajoutePousseur(int i, int j) {
        if (!pousseurAjoute) {
            if (i < lignes && j < colonnes) {
                cases[i][j]  = '@';
                pousseurAjoute = true;
                pousseurX    = j;
                pousseurY    = i;
            } else Configuration.debugeur("Hors des limites du terrain");
        } else Configuration.debugeur("Pousseur deja Ajoute");
    }

    public void ajouteCaisse(int i, int j) {
        if (i < lignes && j < colonnes) {
            cases[i][j] = '$';
            Caisse c = new Caisse(nbCaisse, j, i);
            if (!caisses.contains(c)) {
                caisses.add(c);
                nbCaisse++;
            }
        } else Configuration.debugeur("Hors des limites du terrain");
    }

    public void ajouteBut(int i, int j) {
        if (i < lignes && j < colonnes) {
            cases[i][j] = '.';
            Coordonnees but = new Coordonnees(j, i);
            if (!butsCoordonnees.contains(but)) butsCoordonnees.add(but);
        } else Configuration.debugeur("Hors des limites du terrain");
    }

    public boolean estBut(Coordonnees c){
        return aBut(c.getY(), c.getX());
    }

    public void ajouteButEtCaisse(int i, int j) {
        if (i < lignes && j < colonnes) {
            cases[i][j] = '*';
            Coordonnees but = new Coordonnees(j, i);
            if (!butsCoordonnees.contains(but)) butsCoordonnees.add(but);
            Caisse c = new Caisse(nbCaisse, j, i);
            if (!caisses.contains(c)) {
                caisses.add(c);
                nbCaisse++;
            }
        } else Configuration.debugeur("Hors des limites du terrain");
    }

    public void ajouteButEtPousseur(int i, int j) {
        if (!pousseurAjoute) {
            if (i < lignes && j < colonnes) {
                cases[i][j]  = '+';
                pousseurAjoute = true;
                pousseurX    = j;
                pousseurY    = i;
                Coordonnees but = new Coordonnees(j, i);
                if (!butsCoordonnees.contains(but)) butsCoordonnees.add(but);
            } else Configuration.debugeur("Hors des limites du terrain");
        } else Configuration.debugeur("Pousseur deja Ajoute");
    }

    // -----------------------------------------------------------------------
    // Accesseurs
    // -----------------------------------------------------------------------

    public int    lignes()    { return lignes;   }
    public int    colonnes()  { return colonnes; }
    public String nom()       { return nom;      }

    /** Retourne la liste des caisses (non destructif, for-each compatible). */
    public List<Caisse> getCaisses() { return caisses; }

    public boolean estVide(int l, int c)   { return cases[l][c] == ' '; }
    public boolean aMur(int l, int c)      { return cases[l][c] == '#'; }
    public boolean aBut(int l, int c)      { return cases[l][c] == '.' || cases[l][c] == '+' || cases[l][c] == '*'; }
    public boolean aPousseur(int l, int c) { return cases[l][c] == '@' || cases[l][c] == '+'; }
    public boolean aCaisse(int l, int c)   { return cases[l][c] == '$' || cases[l][c] == '*'; }

    public int getPousseurX() { return pousseurX; }
    public int getPousseurY() { return pousseurY; }
    public void setPousseurX(int i) { this.pousseurX = i; }
    public void setPousseurY(int i) { this.pousseurY = i; }

    public ArrayList<Coordonnees> getButCoordonnees() { return butsCoordonnees; }
    public Coordonnees getPousseurCoordonnees()        { return new Coordonnees(pousseurX, pousseurY); }

    // -----------------------------------------------------------------------
    // Déplacement
    // -----------------------------------------------------------------------

    public boolean deplacePousseur(char cote) {
        int j = pousseurX;
        int i = pousseurY;

        int nj = j, ni = i;
        switch (cote) {
            case 'g': nj--; break;
            case 'd': nj++; break;
            case 'h': ni--; break;
            case 'b': ni++; break;
            default: return false;
        }

        int nnj = nj, nni = ni;
        switch (cote) {
            case 'g': nnj--; break;
            case 'd': nnj++; break;
            case 'h': nni--; break;
            case 'b': nni++; break;
        }

        if (ni < 0 || ni >= lignes || nj < 0 || nj >= colonnes) return false;
        if (aMur(ni, nj)) return false;

        if (aCaisse(ni, nj)) {
            if (nni < 0 || nni >= lignes || nnj < 0 || nnj >= colonnes) return false;
            if (aMur(nni, nnj) || aCaisse(nni, nnj)) return false;
        }

        boolean pousseurSurBut  = aBut(i, j);
        boolean caisseDevant    = aCaisse(ni, nj);
        boolean caisseEstSurBut = caisseDevant && aBut(ni, nj);

        if (caisseDevant) {
            // Met à jour les coordonnées de la caisse déplacée
            for (Caisse c : caisses) {
                if (c.getCoordonnees().getX() == nj && c.getCoordonnees().getY() == ni) {
                    c.getCoordonnees().setX(nnj);
                    c.getCoordonnees().setY(nni);
                    break;
                }
            }

            cases[nni][nnj] = aBut(nni, nnj) ? '*' : '$';
            cases[ni][nj]   = caisseEstSurBut  ? '.' : ' ';
        }

        pousseurAjoute = false;
        if (aBut(ni, nj)) ajouteButEtPousseur(ni, nj);
        else              ajoutePousseur(ni, nj);

        if (pousseurSurBut) ajouteBut(i, j);
        else                videCase(i, j);

        pousseurX = nj;
        pousseurY = ni;
        return true;
    }

    // -----------------------------------------------------------------------
    // Navigation / utilitaires
    // -----------------------------------------------------------------------

    public char getDeplacementDir(Coordonnees c) {
        int dx = c.getX() - pousseurX;
        int dy = c.getY() - pousseurY;
        if (Math.abs(dx) + Math.abs(dy) != 1) return ' ';
        if (dx == -1) return 'g';
        if (dx ==  1) return 'd';
        if (dy == -1) return 'h';
        return 'b';
    }

    /** Retourne les cases voisines accessibles (non-mur) sous forme de List. */
    public List<Coordonnees> casesAtteignable(int x, int y) {
        List<Coordonnees> list = new ArrayList<>(4);
        if (y - 1 >= 0       && !aMur(y - 1, x)) list.add(new Coordonnees(x, y - 1));
        if (y + 1 < lignes   && !aMur(y + 1, x)) list.add(new Coordonnees(x, y + 1));
        if (x + 1 < colonnes && !aMur(y, x + 1)) list.add(new Coordonnees(x + 1, y));
        if (x - 1 >= 0       && !aMur(y, x - 1)) list.add(new Coordonnees(x - 1, y));
        return list;
    }

    public boolean peutPousserCaisse(Coordonnees caisse) {
        int x = caisse.getX(), y = caisse.getY();
        if (!(aMur(y, x-1) || aCaisse(y, x-1)) && x == pousseurX-1 && y == pousseurY) return true;
        if (!(aMur(y, x+1) || aCaisse(y, x+1)) && x == pousseurX+1 && y == pousseurY) return true;
        if (!(aMur(y+1, x) || aCaisse(y+1, x)) && x == pousseurX   && y == pousseurY+1) return true;
        if (!(aMur(y-1, x) || aCaisse(y-1, x)) && x == pousseurX   && y == pousseurY-1) return true;
        return false;
    }

    public boolean deplacementAutomatique() { return false; }

    // -----------------------------------------------------------------------
    // Copie
    // -----------------------------------------------------------------------

    public Niveau copie() {
        Niveau n = new Niveau(lignes, colonnes);
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                switch (cases[i][j]) {
                    case ' ': n.videCase(i, j);          break;
                    case '#': n.ajouteMur(i, j);         break;
                    case '@': n.ajoutePousseur(i, j);    break;
                    case '$': n.ajouteCaisse(i, j);      break;
                    case '*': n.ajouteButEtCaisse(i, j); break;
                    case '+': n.ajouteButEtPousseur(i, j); break;
                    case '.': n.ajouteBut(i, j);         break;
                }
            }
        }
        n.setNom(nom);
        return n;
    }

    public void setNom(String s) { nom = s; }

    // -----------------------------------------------------------------------
    // Affichage
    // -----------------------------------------------------------------------

    public void afficher_niveau() {
        PrintStream p = new PrintStream(System.out);
        Configuration.debugeur("\nSokoban Niveau: %s\n\n", nom());
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if      (aBut(i,j) && aCaisse(i,j))    p.print('*');
                else if (aBut(i,j) && aPousseur(i,j))  p.print('+');
                else if (estVide(i,j))                  p.print(' ');
                else if (aMur(i,j))                     p.print('#');
                else if (aBut(i,j))                     p.print('.');
                else if (aCaisse(i,j))                  p.print('$');
                else if (aPousseur(i,j))                p.print('@');
            }
            p.println();
        }
    }

    // -----------------------------------------------------------------------
    // equals
    // -----------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Niveau)) return false;
        Niveau autre = (Niveau) obj;
        if (autre.lignes != lignes || autre.colonnes != colonnes) return false;
        for (int i = 0; i < lignes; i++)
            for (int j = 0; j < colonnes; j++)
                if (cases[i][j] != autre.cases[i][j]) return false;
        return true;
    }
}
