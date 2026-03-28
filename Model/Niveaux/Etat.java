package Model.Niveaux;

import java.util.*;
import Model.Global.Coordonnees;

public class Etat {

    private Coordonnees pousseur;
    private HashSet<Coordonnees> caisses;

    private int g;
    protected Etat parent;

    public Etat(Coordonnees pousseur, HashSet<Coordonnees> caisses, int g, Etat parent) {
        this.pousseur = new Coordonnees(pousseur.getX(), pousseur.getY());

        this.caisses = new HashSet<>();
        for(Coordonnees c : caisses){
            this.caisses.add(new Coordonnees(c.getX(), c.getY()));
        }

        this.g = g;
        this.parent = parent;
    }

    public Coordonnees getPousseur(){ return pousseur; }
    public HashSet<Coordonnees> getCaisses(){ return caisses; }
    public int getG(){ return g; }

    private HashSet<Coordonnees> atteignables(Niveau niveau){

        HashSet<Coordonnees> visited = new HashSet<>();
        Queue<Coordonnees> q = new LinkedList<>();

        q.add(pousseur);
        visited.add(pousseur);

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        while(!q.isEmpty()){
            Coordonnees cur = q.poll();

            for(int[] d : dirs){
                int nx = cur.getX() + d[0];
                int ny = cur.getY() + d[1];

                Coordonnees next = new Coordonnees(nx, ny);

                if(visited.contains(next)) continue;
                if(niveau.aMur(ny, nx)) continue;
                if(caisses.contains(next)) continue;

                visited.add(next);
                q.add(next);
            }
        }

        return visited;
    }

    public List<Etat> getNeighbors(Niveau niveau, DeadlockDetector detector){

        List<Etat> voisins = new ArrayList<>();
        HashSet<Coordonnees> accessibles = atteignables(niveau);

        int[][] dirs = {
            {-1,0}, {1,0}, {0,-1}, {0,1}
        };

        for(Coordonnees caisse : caisses){

            for(int[] d : dirs){

                int dx = d[0];
                int dy = d[1];

                Coordonnees pousseurPos = new Coordonnees(
                    caisse.getX() - dx,
                    caisse.getY() - dy
                );

                if(!accessibles.contains(pousseurPos))
                    continue;

                Coordonnees dest = new Coordonnees(
                    caisse.getX() + dx,
                    caisse.getY() + dy
                );

                if(niveau.aMur(dest.getY(), dest.getX()) || caisses.contains(dest))
                    continue;

                HashSet<Coordonnees> newCaisses = new HashSet<>(caisses);
                newCaisses.remove(caisse);
                newCaisses.add(dest);

                if(detector.deadlock2x2(newCaisses))
                    continue;

                Coordonnees newPousseur = new Coordonnees(
                    caisse.getX(),
                    caisse.getY()
                );

                voisins.add(new Etat(newPousseur, newCaisses, g + 1, this));
            }
        }

        return voisins;
    }

    public int manhattan(ArrayList<Coordonnees> buts){

        int h = 0;

        for(Coordonnees c : caisses){

            int best = Integer.MAX_VALUE;

            for(Coordonnees b : buts){
                int d = Math.abs(c.getX() - b.getX()) +
                        Math.abs(c.getY() - b.getY());

                if(d < best) best = d;
            }

            h += best;
        }

        return h;
    }

    public List<Etat> getPathStates(){
        List<Etat> path = new ArrayList<>();
        Etat cur = this;

        while(cur != null){
            path.add(cur);
            cur = cur.parent;
        }

        Collections.reverse(path);
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Etat)) return false;
        Etat e = (Etat) obj;
        return caisses.equals(e.caisses) && pousseur.equals(e.pousseur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caisses, pousseur);
    }
}