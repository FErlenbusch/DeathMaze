package deathmaze;

/**
 * <h1>Wall</h1>
 * This class is an implementation of a graph's edge used for the Death Maze game.
 *
 * @author Fred Erlenbusch
 * @version 1.0
 * @since 2018-07-16
 */
public class Wall {

    /**
     * The node on one side of the edge.
     */
    private Cell side1;
    /**
     * The node on the other side of the edge.
     */
    private Cell side2;
    /**
     * A flag for is this edge is a passage in the maze the player can travel through.
     */
    private boolean passage;

    /**
     * The constructor for this class.
     *
     * @param	side1	A node in this edge.
     */
    public Wall(Cell side1) {
        this.side1 = side1;
        this.side2 = null;
        this.passage = false;
    }

    /**
     * Given a known node of this edge it returns the other unknown node.
     *
     * @param	side	The known side of this edge.
     * @return	The unknown side of this edge.
     */
    public Cell getOther(Cell side) {
        if (side1.equals(side)) {
            return side2;
        } else if (side2.equals(side)) {
            return side1;
        } else {
            return null;
        }
    }

    /**
     * Generates the hash value of this edge.
     *
     * @return	The hash value of this edge.
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (passage ? 1231 : 1237);
        result = prime * result + ((side1 == null) ? 0 : side1.hashCode());
        result = prime * result + ((side2 == null) ? 0 : side2.hashCode());
        return result;
    }

    /**
     * Determines is this edge is equal to a given edge.
     *
     * @return	if edges are equal or not.
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
        Wall other = (Wall) obj;
        if (passage != other.passage) {
            return false;
        }
        if (side1 == null) {
            if (other.side1 != null) {
                return false;
            }
        } else if (!side1.equals(other.side1)) {
            return false;
        }
        if (side2 == null) {
            if (other.side2 != null) {
                return false;
            }
        } else if (!side2.equals(other.side2)) {
            return false;
        }
        return true;
    }

    /*
* ------------------------- Getters and Setters -------------------------
     */
    /**
     * @return the side1
     */
    public Cell getSide1() {
        return side1;
    }

    /**
     * @param side1 the side1 to set
     */
    public void setSide1(Cell side1) {
        this.side1 = side1;
    }

    /**
     * @return the side2
     */
    public Cell getSide2() {
        return side2;
    }

    /**
     * @param side2 the side2 to set
     */
    public void setSide2(Cell side2) {
        this.side2 = side2;
    }

    /**
     * @return the passage
     */
    public boolean isPassage() {
        return passage;
    }

    /**
     * @param passage the passage to set
     */
    public void setPassage(boolean passage) {
        this.passage = passage;
    }
}
