package agh.cs.proj1.main;
/**
 * The interface responsible for registration of changes on the map.
 *
 * @author xenofiodor
 *
 */

public interface IPositionChangeObserver {
    /**
     * Take down the change of an object's position.
     *
     * @param oldPosition
     *              the old position of an object
     * @param newPosition
     *              the new position of an object
     *
     */
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
