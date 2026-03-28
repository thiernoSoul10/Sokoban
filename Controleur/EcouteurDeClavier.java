package Controleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Model.Global.Configuration;
import Model.Global.Coordonnees;
import Model.Niveaux.Solver;
import Vue.InterfaceGraphique;
import Vue.NiveauGraphique;

public class EcouteurDeClavier extends KeyAdapter {

    private InterfaceGraphique interG;
    private NiveauGraphique    aireDeDessin;

    public EcouteurDeClavier(InterfaceGraphique ig, NiveauGraphique aireDeDessin) {
        this.interG       = ig;
        this.aireDeDessin = aireDeDessin;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;
        Coordonnees last;


        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: interG.toggleFullscreen(); return;
            case KeyEvent.VK_LEFT:   dx = -1; break;
            case KeyEvent.VK_RIGHT:  dx =  1; break;
            case KeyEvent.VK_UP:     dy = -1; break;
            case KeyEvent.VK_DOWN:   dy =  1; break;
            case KeyEvent.VK_M:  // on arrête les animations
                aireDeDessin.getAnimationPousseur().DesactivateAnimation();
                aireDeDessin.getAnimationCaisse().DesactivateAnimation();
                return;
            case KeyEvent.VK_R:
                aireDeDessin.getAnimationPousseur().activateAnimation();
                aireDeDessin.getAnimationCaisse().activateAnimation();
                return;
            case KeyEvent.VK_S:
                Solver dep = new Solver();
                    String solution = dep.solve(aireDeDessin.jeu.niveau());

                    if(solution != ""){
                        int coups = solution.length();
                        for(int i = 0; i < coups; i++){
                            dx = 0;
                            dy = 0;
                            char dir = solution.charAt(i);
                            switch (dir) {
                                case 'g':   dx = -1; break;
                                case 'd':  dx =  1; break;
                                case 'h':     dy = -1; break;
                                case 'b':   dy =  1; break;
                            }
                            last = aireDeDessin.getLastQueuedPosition();
                            aireDeDessin.addMovement(new Coordonnees(last.getX() + dx, last.getY() + dy));

                        }
                    }
                    return;
            case KeyEvent.VK_N: aireDeDessin.jeu.prochainNiveau(); break; // prochain niveau 
            case KeyEvent.VK_A:
            case KeyEvent.VK_Q:      System.exit(0); return;
            default: return;
        }

        Configuration.debugeur("Le bouton du clavier a été pressé\n");

        // On part de la DERNIÈRE position enqueued (pas de la position du modèle),
        // ce qui évite les sauts incohérents pendant une animation en cours.
        last = aireDeDessin.getLastQueuedPosition();
        aireDeDessin.addMovement(new Coordonnees(last.getX() + dx, last.getY() + dy));
    }
}