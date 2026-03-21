package Model.Niveaux;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un coup (déplacement du pousseur dans une direction donnée)
 * pouvant également porter des marques à poser ou retirer sur le niveau.
 */
public class Coup {

    private char direction; // 'g', 'd', 'h', 'b'

    // Cases à marquer lors de l'application de ce coup
    private List<Point> casesAMarquer;
    // Cases à démarquer lors de l'application de ce coup
    private List<Point> casesADemarquer;

    public Coup(char direction) {
        this.direction = direction;
        this.casesAMarquer   = new ArrayList<>();
        this.casesADemarquer = new ArrayList<>();
    }

    /** Ajoute une case à marquer lorsque ce coup est appliqué. */
    public void ajouterMarque(int ligne, int colonne) {
        casesAMarquer.add(new Point(colonne, ligne)); // Point(x=col, y=ligne)
    }

    /** Ajoute une case à démarquer lorsque ce coup est appliqué. */
    public void retirerMarque(int ligne, int colonne) {
        casesADemarquer.add(new Point(colonne, ligne));
    }

    public char getDirection() {
        return direction;
    }

    /**
     * Applique ce coup sur le niveau réel :
     *  1. Déplace le pousseur dans la direction du coup.
     *  2. Pose / retire les marques demandées.
     * @return true si le déplacement a réussi, false sinon.
     */
    public boolean appliquer(Niveau niveau) {
        boolean ok = niveau.deplacePousseur(direction);

        // On applique les marques même si le déplacement a échoué
        // (l'IA peut vouloir marquer des cases indépendamment)
        for (Point p : casesAMarquer) {
            niveau.poserMarque(p.y, p.x);
        }
        for (Point p : casesADemarquer) {
            niveau.retirerMarque(p.y, p.x);
        }

        return ok;
    }
}
