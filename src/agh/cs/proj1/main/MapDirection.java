package agh.cs.proj1.main;



public enum MapDirection {
    NORTH(0),
    NE(1),
    EAST(2),
    SE(3),
    SOUTH(4),
    SW(5),
    WEST(6),
    NW(7);
    private int value;
    private static MapDirection [] map = new MapDirection[]{NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW};
    MapDirection(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public static MapDirection valueOf(int value){
        if (value < 8)
            return map[value];
        else throw new IllegalArgumentException("The value " + value + " is out of map range");
    }
    public String toString(){
        switch (this) {
            case EAST:
                return "East";
            case WEST:
                return "West";
            case SOUTH:
                return "South";
            case NORTH:
                return "North";
            case NE:
                return "Northeast";
            case NW:
                return "Northwest";
            case SE:
                return "Southeast";
            case SW:
                return "Southwest";
        }
        return null;
    }
    public String toShortString(){
        switch (this) {
            case EAST:
                return "→";
            case WEST:
                return "←";
            case SOUTH:
                return "↓";
            case NORTH:
                return "↑";
            case NE:
                return "↗";
            case NW:
                return "↖";
            case SE:
                return "↘";
            case SW:
                return "↙";
        }
        return null;
    }
    public MapDirection next(int k){
        return valueOf((this.getValue() + k) % 8);
    }
//    public MapDirection previous(int k){
//        return valueOf((this.getValue() - k + 8) % 8);
//    }

    public Vector2d toUnitVector(){
        switch (this){
            case NORTH:
                return new Vector2d(0,1);
            case EAST:
                return new Vector2d(1,0);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NW:
                return new Vector2d(-1, 1);
            case NE:
                return new Vector2d(1, 1);
            case SE:
                return new Vector2d(1, -1);
            case SW:
                return new Vector2d(-1, -1);
            default:
                return null;
        }
    }
}