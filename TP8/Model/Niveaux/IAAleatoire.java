package Model.Niveaux;

import java.util.Random;

/**
 * IA aléatoire : détermine un coup au hasard parmi les quatre directions
 * en examinant une copie (clone) du niveau courant.
 *
 * Cette classe appartient au modèle ; elle reçoit uniquement une copie du
 * niveau afin d'éviter toute corruption de l'état réel du jeu.
 */
public class IAAleatoire {

    private static final char[] DIRECTIONS = { 'g', 'd', 'h', 'b' };
    private final Random random;

    public IAAleatoire() {
        this.random = new Random();
    }

    /**
     * Calcule le prochain coup à partir d'une copie du niveau.
     *
     * @param copieNiveau clone du niveau courant (ne pas modifier le niveau réel !)
     * @return un Coup avec une direction choisie au hasard
     */
    public Coup prochainCoup(Niveau copieNiveau) {
        char direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return new Coup(direction);
    }
}
