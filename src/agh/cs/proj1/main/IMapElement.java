package agh.cs.proj1.main;

/**
 * The interface responsible for describing of elements which may be placed on the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author xenofiodor
 *
 */

public interface IMapElement{
    /**
     * Returns the current position of an element.
     *
     * @return Vector of position  or null if the position is not defined.
     */
    Vector2d getPosition();
}
