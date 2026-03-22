package Model.Global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class Configuration {
    private static String PATH = "res/";
    private static boolean debug = false; // tous les messages sont affichés si vrai
    private static boolean debugError = true;
    public enum Mode { GRAHIQUE, TEXTUEL }

    public static Mode mode = Mode.TEXTUEL;
    
    public static FileInputStream ouvre(String path){
        FileInputStream in = null;

        try{
            in = new FileInputStream(PATH + "/" + path);
        }  catch (FileNotFoundException e) {
            debugeurErreur("ERREUR : impossible de trouver le fichier " + PATH + path);
			System.exit(2);
		}

        return in;
    }

    public static FileInputStream ouvre(String path, String name){
        FileInputStream in = null;

        try{
            in = new FileInputStream(path + "/" + name);
        }  catch (FileNotFoundException e) {
			debugeurErreur("ERREUR : impossible de trouver le fichier" + path + name);
			System.exit(2);
		}

        return in;
    }

    /*
    * 0 pour ne rien afficher
    * 1 pour afficher juste les erreur
    * default tout afficher
    */
    public static void setModeAffichage(int option){
        switch (option) {
            case 0:
                debug = false;
                break;
            case 1:
                debug = false;
                debugError = true;
                break;
            default:
                debug = true;
                break;
        }
    }

    public static void debugeur(String message, Object... args){
        if(debug)
            System.out.printf(message, args);
    }

    public static void debugeurErreur(String message, Object... args){
        if(debug || debugError)
            System.err.printf(message, args);
    }
}
