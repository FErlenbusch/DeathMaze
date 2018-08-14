package test.deathmaze;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import deathmaze.Cell;
import deathmaze.Wall;

public class WallTest extends TestCase {

    private Cell cell1;
    private Cell cell2;
    private Wall wall;

    @Before
    @Override
    public void setUp() {
        cell1 = new Cell(1, 1);
        cell2 = new Cell(2, 2);

        wall = new Wall(cell1);
        wall.setSide2(cell2);
    }

    @Test
    public void testGetOther() {
        assertEquals(wall.getOther(cell1), cell2);
        assertEquals(wall.getOther(cell2), cell1);
        assertEquals(wall.getOther(new Cell(3, 3)), null);
    }

    @Test
    public void testEquals() {
        Wall temp = new Wall(cell1);
        temp.setSide2(cell2);

        assertTrue(wall.equals(wall));
        assertFalse(wall == null);
        assertTrue(wall.equals(temp));

        wall.setSide2(cell1);
        assertFalse(wall.equals(temp));

        wall.setSide2(null);
        assertFalse(wall.equals(temp));

        wall.setSide1(cell2);
        assertFalse(wall.equals(temp));

        wall.setSide1(null);
        assertFalse(wall.equals(temp));

        wall.setPassage(true);
        assertFalse(wall.equals(temp));

    }

    @Test
    public void testHashTo() {
        Wall temp = new Wall(cell1);
        temp.setSide2(cell2);
        int code = temp.hashCode();
        assertTrue(wall.hashCode() == code);

        wall.setSide2(null);
        temp.setSide2(null);
        code = temp.hashCode();
        assertTrue(wall.hashCode() == code);

        wall.setSide1(null);
        temp.setSide1(null);
        code = temp.hashCode();
        assertTrue(wall.hashCode() == code);

        wall.setPassage(true);
        temp.setPassage(true);
        code = temp.hashCode();
        assertTrue(wall.hashCode() == code);
    }

    @Test
    public void testPassage() {
        assertFalse(wall.isPassage());
        wall.setPassage(true);
        assertTrue(wall.isPassage());
    }
}
