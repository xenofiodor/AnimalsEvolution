package agh.cs.proj1.main;

public class Grass extends AbstractWorldMapElement{

    public Grass(GrassField map, Vector2d position){
        super(map, position);
    }

    public String toString(){
        return "*";
    }
}
