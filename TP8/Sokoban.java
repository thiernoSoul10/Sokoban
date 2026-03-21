

import Vue.InterfaceGraphique;
import Vue.InterfaceTextuelle;

import javax.swing.SwingUtilities;

import Global.Configuration;


class Sokoban{
    public static void main(String [] args) throws Exception{
        
        switch (Configuration.mode) {
            case TEXTUEL:
                InterfaceTextuelle inter = new InterfaceTextuelle();            
                inter.lancer();
                break;
            // Swing s'exécute dans un thread séparé. En aucun cas il ne faut accéder directement
		    // aux composants graphiques depuis le thread principal. Swing fournit la méthode
		    // invokeLater pour demander au thread de Swing d'exécuter la méthode run d'un Runnable.
            default:
    		    SwingUtilities.invokeLater(new InterfaceGraphique());    
                break;
        }


    }
	
}