package Model.Niveaux;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Stack;

import Model.Global.Configuration;
import Model.Global.Coordonnees;
import Model.Structures.FAP;
import Model.Structures.SequenceInterface;
import Model.Structures.SequenceListe;

public class Niveau {

    // Attribus:
    private int lignes;
    private int colonnes;
    private String nom;
    private char[][] cases;
    private boolean pousseurAjoute = false;
    private int pousseurX;
    private int pousseurY;
    private ArrayList<Point> butsCoordonnees;
    SequenceListe<Coordonnees> caissesCoordonnees;
    private boolean niveauFini = false;

    public Niveau(int lignes, int colonnes){
        this.lignes = lignes;
        this.colonnes = colonnes;

        cases = new char[lignes][colonnes];
        butsCoordonnees = new ArrayList<>();
        caissesCoordonnees = new SequenceListe<>();
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
        if(i < lignes && j < colonnes){
            caissesCoordonnees.insereQueue(new Coordonnees(j, i));
            cases[i][j] = '$';
        }
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

    public SequenceListe<Coordonnees> getButCoordonnees(){
        SequenceListe<Coordonnees> seq = new SequenceListe<>();

        for(Point p: butsCoordonnees){
            seq.insereQueue(new Coordonnees(p.x, p.y));
        }

        return seq;
    }

    public SequenceListe<Coordonnees> getCaissesCoordonnees(){
        return caissesCoordonnees;
    }

    /*
    * Renvoie une liste des cases accessibles par le pousseur en un mouvement
    */
    public SequenceInterface<Coordonnees> casesAtteignable(int x, int y){
        SequenceInterface<Coordonnees> seq = new SequenceListe<>();
        
        // case de bas
        if(!aMur(y - 1, x))
            seq.insereTete(new Coordonnees(x, y - 1));
        
        // case du bas
        if(!aMur(y + 1, x))
            seq.insereTete(new Coordonnees(x, y + 1));

        // case de droite
        if(!aMur(y, x + 1))
            seq.insereTete(new Coordonnees(x + 1, y));

        // case de gauche
        if(!aMur(y, x - 1))
            seq.insereTete(new Coordonnees(x - 1, y));
        return seq;
    }

    /*
    * Entrées: Position initiale de la caisse et la cible de la caisse
    * Sortie: La caisse est déplacé vers la cible s'il existe un chemin
    *         et return true
    *         la fonction retourne false sinon
    
    public void deplacementAutomatique(Coordonnees position, Coordonnees cible){
        // placement du pousseur sur la case adequate pour pouvoir pousser la caisse
        // boolean accessible = deplacementAutomatiquePousseur(position);

        // si caisse accessible, on la déplace;
        if(accessible){

        } else {
            return;
        }
    }
*/
    /*
    * Entrées: Position de la caisse
    * Sortie: Le pousseur est déplacé vers la cible s'il existe un chemin
    *         et return true
    *         la fonction retourne false sinon
    */
    public SequenceInterface<Coordonnees> deplacementAutomatiquePousseur(Coordonnees cible){
        boolean[][] visites = new boolean[lignes][colonnes];
        Coordonnees[][] parent = new Coordonnees[lignes][colonnes];
        FAP<Coordonnees> file = new FAP<>();

        int startX = pousseurX;
        int startY = pousseurY;

        file.enfiler(new Coordonnees(startX, startY));
        visites[startY][startX] = true;

        while (file.size() > 0) {
            Coordonnees cur = file.defiler();

            if(cur.getX() == cible.getX() && cur.getY() == cible.getY()){
                return reconstruireChemin(parent, cur);
            }

            SequenceInterface<Coordonnees> voisins = casesAtteignable(cur.getX(), cur.getY());

            while (!voisins.estVide()) {

                    Coordonnees next = voisins.extraitTete();       
                    int nx = next.getX();
                    int ny = next.getY();

                    if(nx < 0 || ny < 0 || nx >= colonnes || ny >= lignes || visites[ny][nx])
                        continue;

                    visites[ny][nx] = true;
                    parent[ny][nx] = cur;
                    file.enfiler(next);
            }

        }
        return null; // pas de chemin trouvée;
    }

    private SequenceInterface<Coordonnees> reconstruireChemin(Coordonnees[][] parent, Coordonnees end){
        Coordonnees cur = end;

        SequenceInterface<Coordonnees> chemin = new SequenceListe<>();

        while(cur != null){
            chemin.insereTete(cur);
            cur = parent[cur.getY()][cur.getX()];
        }

        return chemin;
    }

    /*
    * Entrées: Position initiale de la caisse et la cible de la caisse
    *          le pousseur doit être collé à la caisse
    * Sortie: La caisse est déplacé vers la cible d'un mouvement di une case voisine est vide
    *         et return true
    *         la fonction retourne false sinon
    */
    public boolean deplacementAutomatiqueCaisse(Coordonnees position, Coordonnees cible){
        

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
