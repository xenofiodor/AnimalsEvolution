package agh.cs.proj1.main;

public abstract class AbstractWorldMapElement {
    protected Vector2d position;
    protected GrassField map;

    public Vector2d getPosition() {
        return this.position;
    }

    AbstractWorldMapElement() {
    };

    AbstractWorldMapElement(GrassField map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
    }
}
