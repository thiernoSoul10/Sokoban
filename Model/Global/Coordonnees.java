package Model.Global;

public class Coordonnees implements Comparable<Coordonnees> {
    private int x;
    private int y;

    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    public int getX(){ return x; }
    public int getY(){ return y; }

    @Override
    public int compareTo(Coordonnees o) {
        return 0;
    }
}
