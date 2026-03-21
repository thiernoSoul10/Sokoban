package Controleur;

import javax.swing.Timer;

import Global.Configuration;
import Model.Niveaux.Coup;
import Model.Niveaux.IAAleatoire;
import Model.Niveaux.Niveau;
import Vue.NiveauGraphique;

/**
 * Animation qui gère le déplacement automatique du pousseur calculé par l'IA.
 *
 * À intervalles réguliers (délai configurable), elle :
 *  1. Récupère une copie (clone) du niveau courant.
 *  2. Demande à l'IAAleatoire le prochain coup.
 *  3. Applique ce coup sur le vrai niveau.
 *  4. Demande un repaint du composant.
 *  5. Vérifie si le niveau est terminé.
 */
public class AnimationJeuAutomatique {

    /** Délai entre deux coups automatiques en millisecondes. */
    private static final int DELAI_MS = 500;

    private final NiveauGraphique composant;
    private final IAAleatoire ia;
    private final Timer timer;
    private boolean actif;

    public AnimationJeuAutomatique(NiveauGraphique composant) {
        this.composant = composant;
        this.ia        = new IAAleatoire();
        this.actif     = false;

        this.timer = new Timer(DELAI_MS, e -> jouerUnCoup());
        this.timer.setRepeats(true);
    }

    /** Démarre l'animation IA. */
    public void demarrer() {
        if (!actif) {
            actif = true;
            timer.start();
            Configuration.debugeur("AnimationIA : démarrée\n");
        }
    }

    /** Arrête l'animation IA. */
    public void arreter() {
        if (actif) {
            actif = false;
            timer.stop();
            Configuration.debugeur("AnimationIA : arrêtée\n");
        }
    }

    /** Bascule l'état de l'animation (démarre si arrêtée, et inversement). */
    public void basculer() {
        if (actif) arreter();
        else       demarrer();
    }

    public boolean estActif() { return actif; }

    // ------------------------------------------------------------------
    // Logique interne
    // ------------------------------------------------------------------

    private void jouerUnCoup() {
        Niveau niveau = composant.getNiveauJeu();
        if (niveau == null) {
            arreter();
            return;
        }

        // 1. Fournir uniquement une COPIE du niveau à l'IA
        Niveau copie = niveau.clone();

        // 2. L'IA détermine le coup
        Coup coup = ia.prochainCoup(copie);

        // 3. Appliquer le coup sur le VRAI niveau
        coup.appliquer(niveau);

        // 4. Rafraîchir l'affichage
        composant.repaint();

        // 5. Vérifier si le niveau est terminé
        if (composant.niveauTermine()) {
            if (composant.jeu.prochainNiveau()) {
                Configuration.debugeur("AnimationIA : niveau suivant\n");
            } else {
                Configuration.debugeur("AnimationIA : félicitations, jeu terminé !\n");
                arreter();
            }
        }
    }
}
