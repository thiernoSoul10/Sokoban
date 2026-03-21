package Controleur;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Global.Configuration;
import Vue.*;

public class EcouteurDeClavier extends KeyAdapter{
    private InterfaceGraphique interG;
    private NiveauGraphique aireDeDessin;
    private AnimationJeuAutomatique animationIA;

    public EcouteurDeClavier(InterfaceGraphique ig, NiveauGraphique aireDeDessin, AnimationJeuAutomatique animationIA){
        this.aireDeDessin = aireDeDessin;
        this.interG = ig;
        this.animationIA = animationIA;
    }

	@Override
	public void keyPressed(KeyEvent e) {

        char direction = ' ';

        switch (e.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_ESCAPE:
                interG.toggleFullscreen();                  
                return;
            case java.awt.event.KeyEvent.VK_LEFT:   direction = 'g';
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:  direction = 'd';
                break;
            case java.awt.event.KeyEvent.VK_UP:     direction = 'h';
                break;
            case java.awt.event.KeyEvent.VK_DOWN:   direction = 'b';
                break;
            case java.awt.event.KeyEvent.VK_I:
                // Bascule l'animation IA (démarre/arrête)
                animationIA.basculer();
                return;
            case java.awt.event.KeyEvent.VK_A:
            case java.awt.event.KeyEvent.VK_Q:
                System.exit(0);
                return;
            default:
                return;
        }
        
		Configuration.debugeur("Le bouton du clavier a été pressé\n");

        if(aireDeDessin.getNiveauJeu().deplacePousseur(direction))        
            if(aireDeDessin.niveauTermine()){
                if(aireDeDessin.jeu.prochainNiveau())
                    Configuration.debugeur("NIVEAU suivant\n");
                else
                    Configuration.debugeur("Felicitation Vous avez FINI LE JEU\n");
            }

        aireDeDessin.repaint();
	}

    
}
