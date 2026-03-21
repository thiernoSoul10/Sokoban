package Vue;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import Model.Global.Configuration;

public class AnimationPousseur extends Animation {

    public static final int HAUT   = 0;
    public static final int GAUCHE = 1;
    public static final int BAS    = 2;
    public static final int DROITE = 3;

    private static final float FRAME_DURATION = 0.12f;
    private static final int   NB_FRAMES      = 4;
    private static final int   NB_DIRECTIONS  = 4;

    // [direction][frame]
    private Image[][] frames = new Image[NB_DIRECTIONS][NB_FRAMES];
    private Image     imageRepos;

    private int     direction    = BAS;
    private int     currentFrame = 0;
    private float   frameElapsed = 0f;
    private boolean enMouvement  = false;

    public AnimationPousseur() {
        for (int dir = 0; dir < NB_DIRECTIONS; dir++) {
            for (int f = 0; f < NB_FRAMES; f++) {
                String path = "Images/Pousseur_" + dir + "_" + f + ".png";
                try {
                    frames[dir][f] = ImageIO.read(Configuration.ouvre(path));
                } catch (IOException e) {
                    Configuration.debugeurErreur("Erreur chargement : " + path);
                    System.exit(3);
                }
            }
        }
        try {
            imageRepos = ImageIO.read(Configuration.ouvre("Images/Pousseur.png"));
        } catch (IOException e) {
            Configuration.debugeurErreur("Erreur chargement : Images/Pousseur.png");
            System.exit(3);
        }
    }

    public void setDirection(int direction) {
        if (this.direction != direction) {
            this.direction    = direction;
            this.currentFrame = 0;
            this.frameElapsed = 0f;
        }
    }

    public void setEnMouvement(boolean enMouvement) {
        if (!enMouvement) {
            currentFrame = 0;
            frameElapsed = 0f;
        }
        this.enMouvement = enMouvement;
    }

    @Override
    protected void onUpdate(float dt) {
        if (!enMouvement) return;

        frameElapsed += dt;
        if (frameElapsed >= FRAME_DURATION) {
            frameElapsed -= FRAME_DURATION;
            currentFrame = (currentFrame + 1) % NB_FRAMES;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public Image getCurrentImage() {
        if (!enMouvement) return imageRepos;
        return frames[direction][currentFrame];
    }
}