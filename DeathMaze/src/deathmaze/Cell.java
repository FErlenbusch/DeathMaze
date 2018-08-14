package deathmaze;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Cell</h1>
 * This class is an implementation of a graph's node used for the Death Maze game.
 *
 * @author Fred Erlenbusch
 * @version 1.0
 * @since 2018-07-16
 */
public class Cell {

    /**
     * The x coordinate for this node.
     */
    private int x;
    /**
     * The y coordinate for this node.
     */
    private int y;
    /**
     * The North edge for this node.
     */
    private Wall north;
    /**
     * The West edge for this node
     */
    private Wall west;
    /**
     * The South edge for this node.
     */
    private Wall south;
    /**
     * The East edge for this node.
     */
    private Wall east;
    /**
     * The value of this node used by the maze class.
     *
     * @see	Maze
     */
    private String value;
    /**
     * A flag for if this node has been visited/seen by the player.
     */
    private boolean visited;
    /**
     * A flag for is this node is part of the maze. Primarily used by Prim's MST algorithm during
     * the generation of the maze.
     */
    private boolean partOfMaze;

    /**
     * The constructor for a this class.
     *
     * @param x	The x coordinate for this node.
     * @param y	The y coordinate for this node.
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.north = null;
        this.west = null;
        this.south = null;
        this.east = null;
        this.value = "";
        this.partOfMaze = false;
        this.visited = false;
    }

    /**
     * The copy constructor for this class. Creates a new instance from an existing instance.
     *
     * @param that	The instance to be copied.
     */
    public Cell(Cell that) {
        this.x = that.x;
        this.y = that.y;
        this.north = that.north;
        this.west = that.west;
        this.south = that.south;
        this.east = that.east;
        this.value = that.value;
        this.partOfMaze = that.partOfMaze;
        this.visited = that.visited;
    }

    /**
     * Determines the valid wall around this wall. Used by Prim's MST algorithm to build the maze. A
     * valid wall has not a passage, and has another node on the other side of it.
     *
     * @return	A list of valid walls around this node.
     */
    public List<Wall> getValidWalls() {
        List<Wall> validWalls = new ArrayList<>();

        if (!north.isPassage() && !(north.getSide1() == null || north.getSide2() == null)) {
            validWalls.add(north);
        }

        if (!west.isPassage() && !(west.getSide1() == null || west.getSide2() == null)) {
            validWalls.add(west);
        }

        if (!south.isPassage() && !(south.getSide1() == null || south.getSide2() == null)) {
            validWalls.add(south);
        }

        if (!east.isPassage() && !(east.getSide1() == null || east.getSide2() == null)) {
            validWalls.add(east);
        }

        return validWalls;
    }

    /**
     * Generates a list of adjacent nodes a character can move to from this node. An adjacent room
     * is where the wall between this room and the next is a passage, and that there is a room on
     * the other side of the wall.
     *
     * @return	A list of adjacent nodes.
     */
    public List<Cell> getAdjRooms() {
        List<Cell> adjRooms = new ArrayList<>();

        if (north.isPassage() && north.getOther(this) != null) {
            adjRooms.add(north.getOther(this));
        }

        if (west.isPassage() && west.getOther(this) != null) {
            adjRooms.add(west.getOther(this));
        }

        if (south.isPassage() && south.getOther(this) != null) {
            adjRooms.add(south.getOther(this));
        }

        if (east.isPassage() && east.getOther(this) != null) {
            adjRooms.add(east.getOther(this));
        }

        return adjRooms;
    }

    /**
     * Generates the hash value of this node.
     *
     * @return	The hash value of this node.
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Determines if this node is equal to another.
     *
     * @return	True if the given node is equal to this one.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Cell other = (Cell) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }


    /*
* ------------------------- Getters and Setters -------------------------
     */
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the north
     */
    public Wall getNorth() {
        return north;
    }

    /**
     * @param north the north to set
     */
    public void setNorth(Wall north) {
        this.north = north;
    }

    /**
     * @return the west
     */
    public Wall getWest() {
        return west;
    }

    /**
     * @param west the west to set
     */
    public void setWest(Wall west) {
        this.west = west;
    }

    /**
     * @return the south
     */
    public Wall getSouth() {
        return south;
    }

    /**
     * @param south the south to set
     */
    public void setSouth(Wall south) {
        this.south = south;
    }

    /**
     * @return the east
     */
    public Wall getEast() {
        return east;
    }

    /**
     * @param east the east to set
     */
    public void setEast(Wall east) {
        this.east = east;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the partOfMaze
     */
    public boolean isPartOfMaze() {
        return partOfMaze;
    }

    /**
     * @param partOfMaze the partOfMaze to set
     */
    public void setPartOfMaze(boolean partOfMaze) {
        this.partOfMaze = partOfMaze;
    }
}
