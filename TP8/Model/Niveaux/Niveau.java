package Model.Niveaux;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;

import Global.Configuration;

public class Niveau implements Cloneable {

    // Attributs:
    private int lignes;
    private int colonnes;
    private String nom;
    private char[][] cases;
    private boolean pousseurAjoute = false;
    private int pousseurX;
    private int pousseurY;
    private ArrayList<Point> butsCoordonnees;
    private boolean niveauFini = false;
    private boolean[][] marques;

    public Niveau(int lignes, int colonnes){
        this.lignes = lignes;
        this.colonnes = colonnes;

        cases = new char[lignes][colonnes];
        butsCoordonnees = new ArrayList<>();
        marques = new boolean[lignes][colonnes];
    }

    // ---------------------------------------------------------------
    // Clone profond
    // ---------------------------------------------------------------
    @Override
    public Niveau clone() {
        try {
            Niveau copie = (Niveau) super.clone();

            // Copie profonde du tableau de cases
            copie.cases = new char[lignes][colonnes];
            for (int i = 0; i < lignes; i++)
                copie.cases[i] = this.cases[i].clone();

            // Copie profonde de la liste des buts
            copie.butsCoordonnees = new ArrayList<>();
            for (Point p : this.butsCoordonnees)
                copie.butsCoordonnees.add(new Point(p));

            // Copie profonde des marques
            copie.marques = new boolean[lignes][colonnes];
            for (int i = 0; i < lignes; i++)
                copie.marques[i] = this.marques[i].clone();

            return copie;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloneable implémenté, ne devrait pas arriver", e);
        }
    }

    public boolean niveauOk(){
        niveauFini = true;
        for(Point p: butsCoordonnees){
            if(!aCaisse(p.y, p.x)){
                niveauFini = false;
                return niveauFini;
            }
        }
        return niveauFini;
    }

    public void fixeNom(String s){
        this.nom = s.startsWith(";") ? s.substring(1).trim() : s;
    }

    /** supprime le contenu de la case à la ligne i et à la colonne j */
    public void videCase(int i, int j){
        if (i < lignes && j < colonnes) {
            cases[i][j] = ' ';
        }
    }

    /** ajoute un mur à la case à la ligne i et à la colonne j */
    public void ajouteMur(int i, int j){
        if(i < lignes && j < colonnes) cases[i][j] = '#';
        else Configuration.debugeur("Hors des limites du terrain");
    }

    /** ajoute un pousseur à la case à la ligne i et à la colonne j */
    public void ajoutePousseur(int i, int j){
        if(!pousseurAjoute){
            if(i < lignes && j < colonnes){
                cases[i][j] = '@'; pousseurAjoute = true;
                pousseurX = j;
                pousseurY = i;
            }
            else 
                Configuration.debugeur("Hors des limites du terrain");
        }
        else { Configuration.debugeur("Pousseur deja Ajoute"); }
    }

    /** ajoute une Caisse à la case à la ligne i et à la colonne j */
    public void ajouteCaisse(int i, int j){
        if(i < lignes && j < colonnes) cases[i][j] = '$';
        else Configuration.debugeur("Hors des limites du terrain");
    }

    /** ajoute un But à la case à la ligne i et à la colonne j */
    public void ajouteBut(int i, int j){
        if(i < lignes && j < colonnes){
            cases[i][j] = '.';
            Point p = new Point(j, i);
            if(!butsCoordonnees.contains(p))
                butsCoordonnees.add(p);
        }
        else Configuration.debugeur("Hors des limites du terrain");
    }

    /** ajoute un But et une Caisse à la case à la ligne i et à la colonne j */
    public void ajouteButEtCaisse(int i, int j){
        if(i < lignes && j < colonnes){
            cases[i][j] = '*';
            Point p = new Point(j, i);
            if(!butsCoordonnees.contains(p))
                butsCoordonnees.add(p);
        }
        else Configuration.debugeur("Hors des limites du terrain");
    }

    /** ajoute un But et le Pousseur à la case à la ligne i et à la colonne j */
    public void ajouteButEtPousseur(int i, int j){
        if(!pousseurAjoute){
            if(i < lignes && j < colonnes){
                cases[i][j] = '+';
                pousseurAjoute = true;
                pousseurX = j;   // mise à jour des coordonnées
                pousseurY = i;
                Point p = new Point(j, i);
                if(!butsCoordonnees.contains(p))
                    butsCoordonnees.add(p);
            }
            else Configuration.debugeur("Hors des limites du terrain");
        } else { Configuration.debugeur("Pousseur deja Ajoute"); }
    }

    /**renvoie le nombre lignes*/
    public int lignes(){
        return lignes;
    }

    /**renvoie le nombre de colonnes*/
    public int colonnes(){
        return colonnes;
    }

    /**renvoie le nom du niveau*/
    public String nom(){
        return nom;
    }

    /** renvoie vrai si la case à la ligne i et à la colonne j est vide  */
    public boolean estVide(int l, int c){
        return cases[l][c] == ' ';
    }

    /** renvoie vrai si la case à la ligne i et à la colonne j contient un mur. */
    public boolean aMur(int l, int c){
        return cases[l][c] == '#';
    }

    /** renvoie vrai si la case à la ligne i et à la colonne j contient un but.  */
    public boolean aBut(int l, int c) {
        return (cases[l][c] == '.' ||  cases[l][c] == '+' || cases[l][c] == '*');
    }

    /** renvoie vrai si la case à la ligne i et à la colonne j contient un pousseur.  */
    public boolean aPousseur(int l, int c){
        return (cases[l][c] == '@' || cases[l][c] == '+');
    }

    /** renvoie vrai si la case à la ligne i et à la colonne j contient une caisse.  */
    public boolean aCaisse(int l, int c){
        return (cases[l][c] == '$' || cases[l][c] == '*');
    }

    /**
     * Déplace le pousseur dans la direction donnée (g, d, h, b).
     * Gère correctement : pousseur sur but, caisse sur but, mise à jour des coordonnées.
     */
    public boolean deplacePousseur(char cote){

        int j = getPousseurX();
        int i = getPousseurY();

        int ni = i;
        int nj = j;

        // Direction cible
        switch (cote) {
            case 'g': nj--; break;
            case 'd': nj++; break;
            case 'h': ni--; break;
            case 'b': ni++; break;
            default: return false;
        }

        // Case d'après dans la même direction (pour pousser une caisse)
        int nni = ni;
        int nnj = nj;
        switch (cote) {
            case 'g': nnj--; break;
            case 'd': nnj++; break;
            case 'h': nni--; break;
            case 'b': nni++; break;
        }

        // les limites de la case cible
        if(ni < 0 || ni >= lignes || nj < 0 || nj >= colonnes)
            return false;

        // Impossible : mur devant
        if(aMur(ni, nj))
            return false;

        // Impossible : caisse devant et (mur ou caisse ou hors-limites) derrière
        if(aCaisse(ni, nj)){
            if(nni < 0 || nni >= lignes || nnj < 0 || nnj >= colonnes)
                return false;
            if(aMur(nni, nnj) || aCaisse(nni, nnj))
                return false;
        }

        // --- Déplacement possible ---

        boolean pousseurSurBut = aBut(i, j);
        boolean caisseDevant   = aCaisse(ni, nj);
        boolean caisseEstSurBut = caisseDevant && aBut(ni, nj); // caisse sur but ('*')

        // 1. Pousser la caisse si nécessaire
        if(caisseDevant){
            if(aBut(nni, nnj))
                ajouteButEtCaisse(nni, nnj);  // caisse arrive sur un but
            else
                ajouteCaisse(nni, nnj);        // caisse arrive sur une case vide

            // Libérer la case de la caisse :
            // si elle était sur un but, le but reste ; sinon la case devient vide
            if(caisseEstSurBut)
                ajouteBut(ni, nj);
            else
                videCase(ni, nj);
        }

        // 2. Placer le pousseur sur sa nouvelle case
        pousseurAjoute = false;
        if(aBut(ni, nj))
            ajouteButEtPousseur(ni, nj);   // nouvelle case contient un but
        else
            ajoutePousseur(ni, nj);

        // 3. Libérer l'ancienne case du pousseur
        if(pousseurSurBut)
            ajouteBut(i, j);   // le but était là, on le remet
        else
            videCase(i, j);

        // 4. Mettre à jour les coordonnées du pousseur
        pousseurX = nj;
        pousseurY = ni;

        return true;
    }

    public int getPousseurX(){ return pousseurX; }
    public int getPousseurY(){ return pousseurY; }

    public void setPousseurX(int i){  this.pousseurX = i; }
    public void setPousseurY(int i){  this.pousseurY = i; }

    // ---------------------------------------------------------------
    // Marques
    // ---------------------------------------------------------------

    /** Pose une marque sur la case (ligne i, colonne j). */
    public void poserMarque(int i, int j) {
        if (i >= 0 && i < lignes && j >= 0 && j < colonnes)
            marques[i][j] = true;
    }

    /** Retire la marque de la case (ligne i, colonne j). */
    public void retirerMarque(int i, int j) {
        if (i >= 0 && i < lignes && j >= 0 && j < colonnes)
            marques[i][j] = false;
    }

    /** Retourne vrai si la case (ligne i, colonne j) est marquée. */
    public boolean aMarque(int i, int j) {
        if (i >= 0 && i < lignes && j >= 0 && j < colonnes)
            return marques[i][j];
        return false;
    }

    public void afficher_niveau(){
        int lignes = lignes();
        int colonnes = colonnes();
        PrintStream p = new PrintStream(System.out);

        for(int i = 0; i < lignes; i++){
            for(int j = 0; j < colonnes; j++){
                
                if(aBut(i, j) && aCaisse(i, j))  p.print('*');
                else if(aBut(i, j) && aPousseur(i, j))p.print('+');
                else if(estVide(i, j))   p.print(' ');
                else if(aMur(i, j))      p.print('#');
                else if(aBut(i, j))      p.print('.');
                else if(aCaisse(i, j))   p.print('$');
                else if(aPousseur(i, j)) p.print('@');
            }
            p.println();
        }

        Configuration.debugeur("Sokoban Niveau: %s\n\n", nom());

    }
}
