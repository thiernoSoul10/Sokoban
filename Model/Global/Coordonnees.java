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
        int cmpX = Integer.compare(this.x, o.x);
        if(cmpX != 0) return cmpX;
        return Integer.compare(this.y, o.y);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Coordonnees)) return false;

        Coordonnees o = (Coordonnees) obj;
        return this.x == o.getX() && this.y == o.getY();
    }

    @Override
    public int hashCode(){
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "Point(" + x + "," + y + "):" + hashCode();
    }
}
