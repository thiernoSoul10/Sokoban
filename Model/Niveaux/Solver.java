package Model.Niveaux;

import java.util.*;
import Model.Global.Coordonnees;
import Model.Global.Configuration;

public class Solver {

    public String solve(Niveau niveau){

        Coordonnees pousseur = new Coordonnees(niveau.getPousseurX(), niveau.getPousseurY());

        HashSet<Coordonnees> caisses = new HashSet<>();
        for(Caisse c : niveau.getCaisses())
            caisses.add(new Coordonnees(c.getCoordonnees().getX(), c.getCoordonnees().getY()));

        ArrayList<Coordonnees> buts = niveau.getButCoordonnees();

        DeadlockDetector detector = new DeadlockDetector(niveau);

        Etat start = new Etat(pousseur, caisses, 0, null);

        PriorityQueue<Etat> open = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.getG() + e.manhattan(buts))
        );

        HashMap<Etat, Integer> bestCost = new HashMap<>();

        open.add(start);

        while(!open.isEmpty()){

            Etat curr = open.poll();

            if(bestCost.containsKey(curr) && bestCost.get(curr) <= curr.getG())
                continue;

            bestCost.put(curr, curr.getG());

            if(curr.getCaisses().size() == buts.size() &&
               curr.getCaisses().containsAll(buts)){

                String sol = reconstruireChemin(curr, niveau);
                Configuration.debugeur("Solution: \n\t%s\n", sol);
                return sol;
            }

            for(Etat e : curr.getNeighbors(niveau, detector)){
                open.add(e);
            }
        }

        Configuration.debugeur("Pas de solution\n");
        return "";
    }

    private Map<Coordonnees, String> bfsPath(
            Coordonnees start,
            HashSet<Coordonnees> caisses,
            Niveau niveau){

        Map<Coordonnees, String> paths = new HashMap<>();
        Queue<Coordonnees> q = new LinkedList<>();

        q.add(start);
        paths.put(start, "");

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        char[] moves = {'g','d','h','b'};

        while(!q.isEmpty()){
            Coordonnees cur = q.poll();
            String p = paths.get(cur);

            for(int k=0;k<4;k++){
                int nx = cur.getX() + dirs[k][0];
                int ny = cur.getY() + dirs[k][1];

                Coordonnees next = new Coordonnees(nx, ny);

                if(paths.containsKey(next)) continue;
                if(niveau.aMur(ny, nx)) continue;
                if(caisses.contains(next)) continue;

                paths.put(next, p + moves[k]);
                q.add(next);
            }
        }

        return paths;
    }

    private String reconstruireChemin(Etat goal, Niveau niveau){

        List<Etat> states = goal.getPathStates();
        StringBuilder solution = new StringBuilder();

        for(int i = 0; i < states.size() - 1; i++){

            Etat curr = states.get(i);
            Etat next = states.get(i+1);

            Coordonnees pousseur = curr.getPousseur();
            HashSet<Coordonnees> caisses = curr.getCaisses();

            Coordonnees from = null;
            Coordonnees to = null;

            for(Coordonnees c : curr.getCaisses()){
                if(!next.getCaisses().contains(c)){
                    from = c;
                    break;
                }
            }

            for(Coordonnees c : next.getCaisses()){
                if(!curr.getCaisses().contains(c)){
                    to = c;
                    break;
                }
            }

            int dx = to.getX() - from.getX();
            int dy = to.getY() - from.getY();

            Coordonnees target = new Coordonnees(
                from.getX() - dx,
                from.getY() - dy
            );

            Map<Coordonnees, String> paths = bfsPath(pousseur, caisses, niveau);

            String path = paths.get(target);
            if(path == null) return "";

            solution.append(path);

            if(dx == -1) solution.append('g');
            else if(dx == 1) solution.append('d');
            else if(dy == -1) solution.append('h');
            else if(dy == 1) solution.append('b');
        }

        return solution.toString();
    }
}