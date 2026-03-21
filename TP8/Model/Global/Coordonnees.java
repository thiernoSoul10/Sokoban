package Model.Global;

public class Coordonnees implements Comparable<Coordonnees> {
    public int x;
    public int y;

    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Coordonnees o) {
        return 0;
    }
}
