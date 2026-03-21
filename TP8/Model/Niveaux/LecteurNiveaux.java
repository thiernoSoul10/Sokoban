package Model.Niveaux;

import java.util.*;

import Model.Global.Configuration;

import java.io.*;


public class LecteurNiveaux{

    private Scanner s;

    public LecteurNiveaux(InputStream in){
        s = new Scanner(in);
    }
    /** renvoie un niveau lu ou null si la fin du flux a été atteinte.*/
    public Niveau lisProchainNiveau(){
        
        if(s.hasNextLine()){

            boolean lectureTerminee = false;
            boolean erreur = false;
            Niveau niveau;
            int l = 0, c = 0;
            List<String> lignes = new ArrayList<>();
            String nom = "";
            

            while(!lectureTerminee && !erreur){
                try{
                    String ligne = s.nextLine();
                    if(ligne.isEmpty()){ lectureTerminee = true;}
                    else if(ligne.charAt(0) == ';'){
                        nom = ligne; // On copie la ligne dans nom et on ne la prends pas en compte 
                        continue;
                    } else { lignes.add(ligne); }

                    if(c < ligne.length()) c = ligne.length();
                } catch(NoSuchElementException e) {
                    lectureTerminee = true;
                } catch(Exception e){
                    
                    Configuration.debugeurErreur("Erreur de lecture du niveau");
                    erreur = true;
                }
            }

            if(!erreur && !lignes.isEmpty()){
                l = lignes.size();
                // c = lignes.get(0).length(); // en partant du principe que toutes les lignes ont la même taille.

                niveau = new Niveau(l, c);
                niveau.fixeNom(nom);

                int k = 0;
                for(int i=0; i<l; i++){
                    String ligne = lignes.get(i);
                    
                    ecrireLigne(niveau, ligne, k);
                    k++;
                }

                return niveau;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public void ecrireLigne(Niveau niveau, String ligne, int ligneNumero){
        int colonne = 0;
        for(char c: ligne.toCharArray()){
            switch(c){
                case ' ': niveau.videCase(ligneNumero, colonne);
                break;
                case '#': niveau.ajouteMur(ligneNumero, colonne); break;
                case '@': niveau.ajoutePousseur(ligneNumero, colonne); break;
                case '$': niveau.ajouteCaisse(ligneNumero, colonne); break;
                case '*': niveau.ajouteButEtCaisse(ligneNumero, colonne); 
                break;
                case '+': niveau.ajouteButEtPousseur(ligneNumero, colonne); 
                break;
                case '.': niveau.ajouteBut(ligneNumero, colonne); break;
                default: break; // Caractère inconnu devrait lever une exception
            }
            colonne++;
        }
    }
}