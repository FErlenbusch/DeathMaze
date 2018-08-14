package test.deathmaze;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import deathmaze.Cell;
import deathmaze.Wall;

public class CellTest extends TestCase {

    private Cell center;
    private Cell up;
    private Cell down;
    private Cell left;
    private Cell right;

    @Before
    @Override
    public void setUp() {
        center = new Cell(2, 2);
        up = new Cell(1, 2);
        down = new Cell(3, 2);
        left = new Cell(2, 1);
        right = new Cell(2, 4);

        Wall north = new Wall(center);
        Wall south = new Wall(center);
        Wall west = new Wall(center);
        Wall east = new Wall(center);

        north.setSide2(up);
        south.setSide2(down);
        west.setSide2(left);
        east.setSide2(right);

        north.setPassage(true);
        south.setPassage(true);
        west.setPassage(true);
        east.setPassage(true);

        center.setNorth(north);
        center.setSouth(south);
        center.setWest(west);
        center.setEast(east);

        up.setSouth(north);
        down.setNorth(south);
        left.setEast(west);
        right.setWest(east);

    }

    @Test
    public void testCloneConstructor() {
        center = new Cell(up);
        assertEquals(center, up);
    }

    @Test
    public void testValidWalls() {
        List<Wall> walls = new ArrayList<>();

        assertEquals(center.getValidWalls(), walls);

        center.getNorth().setPassage(false);
        walls.add(center.getNorth());
        assertEquals(center.getValidWalls(), walls);

        center.getWest().setPassage(false);
        walls.add(center.getWest());
        assertEquals(center.getValidWalls(), walls);

        center.getSouth().setPassage(false);
        walls.add(center.getSouth());
        assertEquals(center.getValidWalls(), walls);

        center.getEast().setPassage(false);
        walls.add(center.getEast());
        assertEquals(center.getValidWalls(), walls);

    }

    @Test
    public void testAdjacentRooms() {
        List<Cell> rooms = new ArrayList<>();
        rooms.add(up);
        rooms.add(left);
        rooms.add(down);
        rooms.add(right);

        assertEquals(center.getAdjRooms(), rooms);
    }

    @Test
    public void testGettersSetters() {
        center.setX(5);
        assertEquals(center.getX(), 5);

        center.setY(5);
        assertEquals(center.getY(), 5);

        center.setValue("Value");
        assertEquals(center.getValue(), "Value");

        center.setPartOfMaze(true);
        assertTrue(center.isPartOfMaze());

        center.setVisited(true);
        assertTrue(center.isVisited());
    }

    @Test
    public void testEquals() {
        assertTrue(center.equals(center));
        assertFalse(center == null);
        assertFalse(center.equals(new Wall(center)));
        assertFalse(center.equals(up));
        assertFalse(center.equals(left));
        assertTrue(center.equals(new Cell(2, 2)));
    }
}
