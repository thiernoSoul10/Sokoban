package Vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import Model.Jeu;
import Model.Global.*;
import Model.Niveaux.Niveau;

import java.awt.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class NiveauGraphique extends JComponent {

    private static final int MAX_MOVEMENTS = 1000;

    private InterfaceGraphique interG;
    private Deque<Coordonnees> movements = new ArrayDeque<>(); // était SequenceInterface
    private Coordonnees        lastQueued = null;

    private int tailleCase = 40;
    private int offsetX;
    private int offsetY;

    private Image butImg;
    private Image caise_sur_bImg;
    private Image caisseImg;
    private Image murImg;
    private Image solImg;

    public Jeu jeu;

    private List<Animation>    animations = new ArrayList<>();
    private AnimationCoups     animationPousseur = null;
    private AnimationCoups     animationCaisse   = null;
    private AnimationPousseur  animPousseurImg;

    private float pousseurRenderX, pousseurRenderY;
    private float caisseRenderX,   caisseRenderY;

    private int caisseStartX, caisseStartY;
    private int caisseEndX,   caisseEndY;

    public NiveauGraphique(InterfaceGraphique ig) {
        this.interG = ig;

        setFocusable(true);
        requestFocusInWindow();

        FileInputStream fin = Configuration.ouvre("Terrains/Tests.txt");
        jeu = new Jeu(fin);

        try {
            butImg         = ImageIO.read(Configuration.ouvre("Images/But.png"));
            caise_sur_bImg = ImageIO.read(Configuration.ouvre("Images/Caisse_sur_but.png"));
            caisseImg      = ImageIO.read(Configuration.ouvre("Images/Caisse.png"));
            murImg         = ImageIO.read(Configuration.ouvre("Images/Mur.png"));
            solImg         = ImageIO.read(Configuration.ouvre("Images/Sol.png"));
        } catch (IOException e) {
            Configuration.debugeurErreur("Erreur chargement images");
            System.exit(3);
        }

        animPousseurImg = new AnimationPousseur();
        animations.add(animPousseurImg);

        new Timer(16, e -> updateAnimation(0.016f)).start();
    }

    // -------------------------------------------------------------------------
    // Gestion de la queue
    // -------------------------------------------------------------------------

    public void addMovement(Coordonnees p) {
        if (movements.size() >= MAX_MOVEMENTS) return;
        movements.addLast(p);
        lastQueued = p;
    }

    public Coordonnees getLastQueuedPosition() {
        if (lastQueued != null && !movements.isEmpty()) return lastQueued;
        return new Coordonnees(jeu.niveau().getPousseurX(), jeu.niveau().getPousseurY());
    }

    // -------------------------------------------------------------------------
    // Boucle d'animation
    // -------------------------------------------------------------------------

    public void updateAnimation(float dt) {
        animations.removeIf(a -> {
            a.update(dt);
            return a.isFinished();
        });

        if (animationPousseur != null) {
            if (animationPousseur.isFinished()) {
                animationPousseur = null;
            } else {
                pousseurRenderX = animationPousseur.getX();
                pousseurRenderY = animationPousseur.getY();
            }
        }

        if (animationCaisse != null) {
            if (animationCaisse.isFinished()) {
                animationCaisse = null;
            } else {
                caisseRenderX = animationCaisse.getX();
                caisseRenderY = animationCaisse.getY();
            }
        }

        if (animationPousseur == null && animationCaisse == null) {
            if (!movements.isEmpty()) {
                Coordonnees p = movements.pollFirst(); // était extraitTete()
                deplacePousseur(p.getX(), p.getY());
            } else {
                lastQueued = null;
                animPousseurImg.setEnMouvement(false);
                if (jeu.niveau().niveauOk()) {
                    if (jeu.prochainNiveau())
                        Configuration.debugeur("NIVEAU suivant\n");
                    else
                        Configuration.debugeur("FIN DU JEU\n");
                }
            }
        }

        repaint();
    }

    // -------------------------------------------------------------------------
    // Rendu
    // -------------------------------------------------------------------------

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D drawable = (Graphics2D) g;

        Niveau niveau = jeu.niveau();
        if (niveau == null) return;

        interG.getFrame().setTitle("Sokoban Niveau: " + niveau.nom());

        int lignes   = niveau.lignes();
        int colonnes = niveau.colonnes();

        offsetX = (getWidth()  - colonnes * tailleCase) / 2;
        offsetY = (getHeight() - lignes   * tailleCase) / 2;

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {

                int x = offsetX + j * tailleCase;
                int y = offsetY + i * tailleCase;

                drawable.drawImage(solImg, x, y, tailleCase, tailleCase, null);

                if (niveau.aMur(i, j)) {
                    drawable.drawImage(murImg, x, y, tailleCase, tailleCase, null);
                } else if (niveau.aBut(i, j)) {
                    drawable.drawImage(butImg, x, y, tailleCase, tailleCase, null);
                }

                if (niveau.aCaisse(i, j)) {
                    if (animationCaisse != null
                            && ((i == caisseStartY && j == caisseStartX)
                             || (i == caisseEndY   && j == caisseEndX))) {
                        continue;
                    }
                    if (niveau.aBut(i, j))
                        drawable.drawImage(caise_sur_bImg, x, y, tailleCase, tailleCase, null);
                    else
                        drawable.drawImage(caisseImg, x, y, tailleCase, tailleCase, null);
                }
            }
        }

        if (animationCaisse != null) {
            int cx = offsetX + (int)(caisseRenderX * tailleCase);
            int cy = offsetY + (int)(caisseRenderY * tailleCase);
            if (niveau.aBut(caisseEndY, caisseEndX))
                drawable.drawImage(caise_sur_bImg, cx, cy, tailleCase, tailleCase, null);
            else
                drawable.drawImage(caisseImg, cx, cy, tailleCase, tailleCase, null);
        }

        int px, py;
        if (animationPousseur != null) {
            px = offsetX + (int)(pousseurRenderX * tailleCase);
            py = offsetY + (int)(pousseurRenderY * tailleCase);
        } else {
            px = offsetX + niveau.getPousseurX() * tailleCase;
            py = offsetY + niveau.getPousseurY() * tailleCase;
        }
        drawable.drawImage(animPousseurImg.getCurrentImage(), px, py, tailleCase, tailleCase, null);
    }

    // -------------------------------------------------------------------------
    // Déplacement
    // -------------------------------------------------------------------------

    public boolean deplacePousseur(int x, int y) {
        Niveau n = jeu.niveau();

        int oldX = n.getPousseurX();
        int oldY = n.getPousseurY();

        int dx = x - oldX;
        int dy = y - oldY;

        if (Math.abs(dx) + Math.abs(dy) != 1) return false;

        boolean caisseBouge = n.aCaisse(oldY + dy, oldX + dx);

        if (caisseBouge) {
            caisseStartX = oldX + dx;
            caisseStartY = oldY + dy;
            caisseEndX   = caisseStartX + dx;
            caisseEndY   = caisseStartY + dy;
        }

        char dir = (dx == -1) ? 'g' : (dx == 1) ? 'd' : (dy == -1) ? 'h' : 'b';
        boolean moved = n.deplacePousseur(dir);

        if (moved) {
            if      (dx == -1) animPousseurImg.setDirection(AnimationPousseur.GAUCHE);
            else if (dx ==  1) animPousseurImg.setDirection(AnimationPousseur.DROITE);
            else if (dy == -1) animPousseurImg.setDirection(AnimationPousseur.HAUT);
            else               animPousseurImg.setDirection(AnimationPousseur.BAS);

            animPousseurImg.setEnMouvement(true);

            animationPousseur = new AnimationCoups(oldX, oldY,
                                                   n.getPousseurX(), n.getPousseurY());
            animations.add(animationPousseur);

            if (caisseBouge) {
                animationCaisse = new AnimationCoups(caisseStartX, caisseStartY,
                                                     caisseEndX,   caisseEndY);
                animations.add(animationCaisse);
            }
        }

        return moved;
    }

    // -------------------------------------------------------------------------
    // Accesseurs
    // -------------------------------------------------------------------------

    public int               getoffsetX()            { return offsetX; }
    public int               getoffsetY()            { return offsetY; }
    public int               getTailleCase()          { return tailleCase; }
    public AnimationCoups    getAnimationPousseur()   { return animationPousseur; }
    public AnimationCoups    getAnimationCaisse()     { return animationCaisse; }
    public AnimationPousseur getAnimationPousseurDep(){ return animPousseurImg; }
    public InterfaceGraphique getInterG(){ return interG; }
}