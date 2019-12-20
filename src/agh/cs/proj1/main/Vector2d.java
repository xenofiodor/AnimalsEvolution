package agh.cs.proj1.main;



public class Vector2d {
    final int x;
    final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return String.format("(%d, %d)", this.x, this.y);
    }
    //    public static String toString(int x_, int y_){
//        return String.format("(%d, %d)",x_, y_);
//    }
    public boolean precedes(Vector2d v){
        return (v.x >= this.x && v.y >= this.y);
    }
    public boolean follows(Vector2d v){
        return (v.x <= this.x && v.y <= this.y);
    }
    public Vector2d upperRight(Vector2d v){
        return new Vector2d(Math.max(v.x, this.x), Math.max(v.y, this.y));
    }
    public Vector2d lowerLeft(Vector2d v){
        return new Vector2d(Math.min(v.x, this.x), Math.min(v.y, this.y));
    }
    public Vector2d add(Vector2d v){
        return new Vector2d(this.x + v.x, this.y + v.y);
    }
    public Vector2d subtract(Vector2d v){
        return new Vector2d(this.x - v.x, this.y - v.y);
    }
    public boolean equals(Object obj){
        if (obj instanceof Vector2d)
            return (((Vector2d)obj).x == this.x && ((Vector2d)obj).y == this.y);
        return false;
    }
    public Vector2d opposite(){
        return new Vector2d(-1*this.x, -1 * this.y);
    }
    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }
}