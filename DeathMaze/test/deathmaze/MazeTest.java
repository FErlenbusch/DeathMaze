package deathmaze;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import deathmaze.Cell;
import deathmaze.Maze;

public class MazeTest extends TestCase {

    private Maze maze;
    private int width;
    private int height;
    private int mobCnt;
    private int visRange;

    @Before
    @Override
    public void setUp() {
        width = 5;
        height = 5;
        mobCnt = 3;
        visRange = 3;
        maze = new Maze(width, height, mobCnt, visRange);
    }

    @Test
    public void testInits() {
        assertEquals(maze.getGrid().length, 5);
        assertEquals(maze.getGrid()[0].length, 5);
    }

    @Test
    public void testMovePlayerNorth() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getNorth().isPassage()) {
                flag = true;
                Cell north = maze.getPlayer().getNorth().getOther(maze.getPlayer());
                maze.movePlayer("N");
                assertEquals(north, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testMovePlayerSouth() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getSouth().isPassage()) {
                flag = true;
                Cell south = maze.getPlayer().getSouth().getOther(maze.getPlayer());
                maze.movePlayer("S");
                assertEquals(south, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testMovePlayerWest() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getWest().isPassage()) {
                flag = true;
                Cell west = maze.getPlayer().getWest().getOther(maze.getPlayer());
                maze.movePlayer("W");
                assertEquals(west, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testMovePlayerEast() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getEast().isPassage()) {
                flag = true;
                Cell east = maze.getPlayer().getEast().getOther(maze.getPlayer());
                maze.movePlayer("E");
                assertEquals(east, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testMovePlayerFail() {
        boolean flag = false;

        while (!flag) {
            if (!maze.getPlayer().getEast().isPassage()) {
                flag = true;
                Cell east = maze.getPlayer().getEast().getOther(maze.getPlayer());
                maze.movePlayer("E");
                assertNotSame(east, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testPlayerDead() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getEast().isPassage()) {
                flag = true;
                maze.setAlive(false);
                Cell east = maze.getPlayer().getEast().getOther(maze.getPlayer());
                maze.movePlayer("E");
                assertFalse(maze.getAlive());
                assertNotSame(east, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testPlayerEscaped() {
        boolean flag = false;

        while (!flag) {
            if (maze.getPlayer().getEast().isPassage()) {
                flag = true;
                maze.setEscaped(true);
                Cell east = maze.getPlayer().getEast().getOther(maze.getPlayer());
                maze.movePlayer("E");
                assertTrue(maze.getEscaped());
                assertNotSame(east, maze.getPlayer());
            } else {
                maze = new Maze(width, height, mobCnt, visRange);
            }
        }
    }

    @Test
    public void testGettersSetters() {
        maze.setWidth(6);
        assertEquals(6, maze.getWidth());

        maze.setHeight(6);
        assertEquals(6, maze.getHeight());

        maze.setVisRange(6);
        assertEquals(6, maze.getVisRange());

        maze.setKey(new Cell(1, 1));
        assertEquals(new Cell(1, 1), maze.getKey());

        maze.setExit(new Cell(1, 1));
        assertEquals(new Cell(1, 1), maze.getExit());

        maze.setMsg("Message");
        assertEquals("Message", maze.getMsg());
        assertEquals("", maze.getMsg());

        maze.setPlayer(new Cell(1, 1));
        assertEquals(new Cell(1, 1), maze.getPlayer());

        List<Cell> mobs = new ArrayList<>();
        mobs.add(new Cell(1, 1));
        mobs.add(new Cell(2, 2));
        mobs.add(new Cell(3, 3));
        maze.setMobs(mobs);
        assertEquals(mobs, maze.getMobs());

        maze.setMazeState();
        assertEquals(new Cell(-1, -1), maze.getKey());
        assertFalse(maze.getAlive());

        assertTrue(maze.isRoomEmpty(new Cell(4, 4)));

        Cell[][] grid = new Cell[5][5];
        maze.setGrid(grid);
        assertEquals(grid, maze.getGrid());
    }

    @Test
    public void testGetNextRoom() {
        assertEquals(new Cell(2, 2), maze.getNextRoom(new Cell(2, 1), new Cell(2, 3)));
        assertEquals(new Cell(2, 2), maze.getNextRoom(new Cell(2, 3), new Cell(2, 1)));
        assertEquals(new Cell(2, 2), maze.getNextRoom(new Cell(1, 2), new Cell(3, 2)));
        assertEquals(new Cell(2, 2), maze.getNextRoom(new Cell(3, 2), new Cell(1, 2)));
    }
}
