package Controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Model.Global.Coordonnees;
import Vue.NiveauGraphique;

public class EcouteurDeSouris extends MouseAdapter {

    private NiveauGraphique aireDeDessin;

    public EcouteurDeSouris(NiveauGraphique aireDeDessin) {
        this.aireDeDessin = aireDeDessin;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Coordonnées de la case cliquée
        int gx = (e.getX() - aireDeDessin.getoffsetX()) / aireDeDessin.getTailleCase();
        int gy = (e.getY() - aireDeDessin.getoffsetY()) / aireDeDessin.getTailleCase();

        // Position de départ = dernière case enqueued (ou position courante si queue vide)
        Coordonnees start = aireDeDessin.getLastQueuedPosition();
        int cx = start.x;
        int cy = start.y;

        // Décompose le chemin en pas unitaires : d'abord horizontalement, puis verticalement
        int stepX = Integer.compare(gx, cx);
        int stepY = Integer.compare(gy, cy);

        while (cx != gx) {
            cx += stepX;
            aireDeDessin.addMovement(new Coordonnees(cx, cy));
        }
        while (cy != gy) {
            cy += stepY;
            aireDeDessin.addMovement(new Coordonnees(cx, cy));
        }
    }
}