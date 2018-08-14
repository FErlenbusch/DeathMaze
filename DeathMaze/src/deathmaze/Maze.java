package deathmaze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <h1>Maze</h1>
 * This Class organizes the graph based maze composed of Cells (nodes) and Walls (edges), and keeps
 * track of the appropriate values for the logic of the Death Maze game.
 *
 * @author Fred Erlenbusch
 * @version 1.0
 * @since 2018-07-16
 */
public class Maze {

    /**
     * The width of the game. The number of rooms wide in the maze.
     */
    private int width;
    /**
     * The height of the game. The number of rooms tall in the maze.
     */
    private int height;
    /**
     * The visibility range of all the characters in the maze.
     */
    private int visRange;
    /**
     * The cell that contains the player.
     */
    private Cell player;
    /**
     * The cell that contains the key.
     */
    private Cell key;
    /**
     * The cell that contains the exit.
     */
    private Cell exit;
    /**
     * The grid (graph) of the rooms in the maze.
     */
    private Cell[][] grid;
    /**
     * A list of all the monsters in the maze.
     */
    private List<Cell> mobs;
    /**
     * A flag for is the player is alive.
     */
    private Boolean alive;
    /**
     * A flag for if the player has escaped the maze.
     */
    private Boolean escaped;
    /**
     * The Random number generator used by the maze.
     */
    private Random rand;
    /**
     * The message picked up by the UI to output to the user.
     */
    private String msg;

    /**
     * Constructor for the Maze.
     *
     * @param	width	# of rooms width wise
     * @param height # of rooms height wise
     * @param mobCnt	# of monsters in the maze
     * @param	visRange	How far character's can see from their current location
     *
     */
    public Maze(int width, int height, int mobCnt, int visRange) {
        this.width = width;
        this.height = height;
        this.visRange = visRange;
        this.rand = new Random();
        this.grid = new Cell[width][height];
        this.mobs = new ArrayList<>();
        this.alive = true;
        this.escaped = false;
        this.msg = "";

        initMaze();

        this.player = grid[rand.nextInt(width)][rand.nextInt(height)];

        carvePassages();
        initMobs(mobCnt);
        initExit();
        initKey();
        markVisibleRooms();
    }

    /**
     * Initialize the maze.
     * <p>
     * Generates the maze's walls and rooms where the rooms are nodes and the walls are edges.
     */
    private void initMaze() {

// Generate all cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = new Cell(x, y);
            }
        }

// Generate walls and link them to appropriate cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];

                initNorth(cell, x, y);
                initWest(cell, x, y);
                initSouth(cell, x, y);
                initEast(cell, x, y);
            }
        }
    }

    /**
     * Initialize a cell's North wall and link it to appropriate adjacent cell.
     *
     * @param	cell	The cell to initialize the North Wall to.
     * @param	x	The x coordinate on the grid of the cell.
     * @param	y	The y coordinate on the grid of the cell.
     */
    private void initNorth(Cell cell, int x, int y) {
        if (cell.getNorth() == null) {
            cell.setNorth(new Wall(cell));

            if (y > 0 && grid[x][y - 1].getSouth() == null) {
                grid[x][y - 1].setSouth(cell.getNorth());
                cell.getNorth().setSide2(grid[x][y - 1]);
            }
        }
    }

    /**
     * Initialize a cell's West wall and link it to appropriate adjacent cell.
     *
     * @param	cell	The cell to initialize the West Wall to.
     * @param	x	The x coordinate on the grid of the cell.
     * @param	y	The y coordinate on the grid of the cell.
     */
    private void initWest(Cell cell, int x, int y) {
        if (cell.getWest() == null) {
            cell.setWest(new Wall(cell));

            if (x != 0 && grid[x - 1][y].getEast() == null) {
                grid[x - 1][y].setEast(cell.getWest());
                cell.getWest().setSide2(grid[x - 1][y]);
            }
        }
    }

    /**
     * Initialize a cell's South wall and link it to appropriate adjacent cell.
     *
     * @param cell The cell to initialize the South Wall to.
     * @param x The x coordinate on the grid of the cell.
     * @param	y The y coordinate on the grid of the cell.
     */
    private void initSouth(Cell cell, int x, int y) {
        if (cell.getSouth() == null) {
            cell.setSouth(new Wall(cell));

            if (y < height - 1 && grid[x][y + 1].getNorth() == null) {
                grid[x][y + 1].setNorth(cell.getSouth());
                cell.getSouth().setSide2(grid[x][y + 1]);
            }
        }
    }

    /**
     * Initialize a cell's East wall and link it to appropriate adjacent cell.
     *
     * @param cell The cell to initialize the East Wall to.
     * @param x The x coordinate on the grid of the cell.
     * @param y The y coordinate on the grid of the cell.
     */
    private void initEast(Cell cell, int x, int y) {
        if (cell.getEast() == null) {
            cell.setEast(new Wall(cell));

            if (x < width - 1 && grid[x + 1][y].getWest() == null) {
                grid[x + 1][y].setWest(cell.getEast());
                cell.getEast().setSide2(grid[x + 1][y]);
            }
        }
    }

    /**
     * Carves the passages into the maze.
     * <p>
     * Uses Prim's minimum spanning tree algorithm to generate the maze.
     */
    private void carvePassages() {
        List<Wall> walls = new ArrayList<>();
        player.setPartOfMaze(true);
        player.setValue("P");
        player.setVisited(true);

        walls.addAll(player.getValidWalls());

        while (!walls.isEmpty()) {
            Wall current = walls.get(rand.nextInt(walls.size()));

            if (current.getSide1() != null && !current.getSide1().isPartOfMaze()) {
                current.setPassage(true);
                current.getSide1().setPartOfMaze(true);
                walls.addAll(current.getSide1().getValidWalls());
            } else if (current.getSide2() != null && !current.getSide2().isPartOfMaze()) {
                current.setPassage(true);
                current.getSide2().setPartOfMaze(true);
                walls.addAll(current.getSide2().getValidWalls());
            }

            walls.remove(current);
        }
    }

    /**
     * Generates the starting point for all the mobs.
     *
     * @param cnt The number of mobs to initialize.
     */
    private void initMobs(int cnt) {
        for (int i = 0; i < cnt; i++) {
            Cell mob = grid[rand.nextInt(width)][rand.nextInt(height)];

            while (mob.equals(player) && mobs.contains(mob) && !mob.isPartOfMaze()) {
                mob = grid[rand.nextInt(width)][rand.nextInt(height)];
            }

            mob.setValue("M");
            mobs.add(mob);
        }
    }

    /**
     * Initializes the exit somewhere randomly on a boarder cell.
     */
    private void initExit() {
        exit = grid[rand.nextInt(width)][rand.nextInt(height)];

        while (!validExit()) {
            exit = grid[rand.nextInt(width)][rand.nextInt(height)];
        }
    }

    /**
     * Initializes the key somewhere randomly not where a mob is, not where the the player is, and
     * at least half the map away from the exit.
     */
    private void initKey() {
        key = grid[rand.nextInt(width)][rand.nextInt(height)];

        while (!validKey()) {
            key = grid[rand.nextInt(width)][rand.nextInt(height)];
        }
    }

    /**
     * Moves the all the mobs at once.
     *
     * @param playerRoom	The room the player was last in to enable the monsters to move towards the
     * player if within visible range of the monsters
     */
    private void moveMobs(Cell playerRoom) {
        List<Cell> tempMobs = new ArrayList<>();

        while (!mobs.isEmpty()) {
            Cell mob = mobs.remove(0);
            List<Cell> adjRooms = mob.getAdjRooms();
            List<Cell> visibleRooms = getVisibleRooms(mob);

            if (!mob.equals(player)) {
                if (visibleRooms.contains(playerRoom)) {
                    mob = moveCharacter(mob, getNextRoom(mob, playerRoom), "M");
                } else if (!adjRooms.isEmpty()) {
                    mob = moveCharacter(mob, adjRooms.get(rand.nextInt(adjRooms.size())), "M");
                }
            }

            tempMobs.add(mob);
        }

        mobs = tempMobs;
    }

    /**
     * Moves a character (player or mob) from the current cell to the next, and returns the new
     * location of the character.
     *
     * @param current The current cell the character is in.
     * @param next The cell the character will be moved to.
     * @param value The type of character being moved.
     *
     * @return The cell the character is in after the move.
     */
    private Cell moveCharacter(Cell current, Cell next, String value) {
        next.setValue(value);
        current.setValue("");

        return next;
    }

    /**
     * Moves the player in a given direction.
     *
     * @param direction The direction to move the player.
     */
    public void movePlayer(String direction) {
        if (alive && !escaped) {
            Cell startRoom = new Cell(player);

            if (direction.equals("N") && player.getNorth().isPassage()) {
                player = moveCharacter(player, player.getNorth().getOther(player), "P");
            } else if (direction.equals("W") && player.getWest().isPassage()) {
                player = moveCharacter(player, player.getWest().getOther(player), "P");
            } else if (direction.equals("S") && player.getSouth().isPassage()) {
                player = moveCharacter(player, player.getSouth().getOther(player), "P");
            } else if (direction.equals("E") && player.getEast().isPassage()) {
                player = moveCharacter(player, player.getEast().getOther(player), "P");
            } else {
                msg = "Invalid Move: Why are you running into walls?";
            }

            moveMobs(startRoom);
            markVisibleRooms();
        }

        setMazeState();
    }

    /**
     * Determines the current game state, sets any appropriate values, and sets the appropriate
     * messages for the user.
     */
    public void setMazeState() {
        if (mobs.contains(player)) {
            alive = false;
        }

        if (key.equals(player)) {
            key = new Cell(-1, -1);
            msg = "You found the key!";
        }

        if (key.getX() == -1 && exit.equals(player)) {
            escaped = true;
        } else if (exit.equals(player)) {
            msg = "You found the exit, but you don't have the key!";
        }

        if (alive && escaped) {
            msg = "Congratulations you've escaped the Death Maze!";
        }

        if (!alive && !escaped) {
            msg = "GAME OVER: You got killed by a Monster!";
        }
    }

    /**
     * Validates the initial placement of the exit.
     *
     * @return	The validity of the exit.
     */
    private boolean validExit() {
        return ((exit.isPartOfMaze() && !exit.equals(player) && !mobs.contains(exit))
                && ((exit.getX() == 0 || exit.getX() == width - 1) || (exit.getY() == 0 || exit.getY() == height - 1)));
    }

    /**
     * Validates the initial placement of the key.
     *
     * @return	The validity of the key.
     */
    private boolean validKey() {
        return ((key.isPartOfMaze() && !key.equals(player) && !mobs.contains(key))
                && ((exit.getX() == 0 && key.getX() >= width / 2) || (exit.getX() == width - 1 && key.getX() <= width / 2)
                || (exit.getY() == 0 && key.getY() >= height / 2) || (exit.getY() == height - 1 && key.getY() <= height / 2)));
    }

    /**
     * Validates if the given room is empty.
     *
     * @param	room The room to be tested if it's empty.
     *
     * @return	If the room is empty (true) or it's not (false).
     */
    public boolean isRoomEmpty(Cell room) {
        return !(player.equals(room) || mobs.contains(room) || (key.equals(room)) || exit.equals(room));
    }

    /**
     * Changes the boolean flag for a list of rooms visible to the player and changes their
     * visibility flag to true.
     */
    public void markVisibleRooms() {
        List<Cell> visibleRooms = getVisibleRooms(player);

        for (Cell room : visibleRooms) {
            room.setVisited(true);
        }
    }

    /**
     * Gets a list of rooms that are visible from the given room defined by the visibility range
     * (global int visRange).
     *
     * @param	room	The room which visibility is centered on.
     *
     * @return	A list of rooms visible from the given room.
     */
    public List<Cell> getVisibleRooms(Cell room) {
        List<Cell> rooms = new ArrayList<>();
        Cell current = room;
        int cnt = 0;

        rooms.add(current);

        while (current.getNorth().isPassage() && cnt++ < visRange) {
            current = current.getNorth().getOther(current);
            rooms.add(current);
        }

        current = room;
        cnt = 0;

        while (current.getSouth().isPassage() && cnt++ < visRange) {
            current = current.getSouth().getOther(current);
            rooms.add(current);
        }

        current = room;
        cnt = 0;

        while (current.getWest().isPassage() && cnt++ < visRange) {
            current = current.getWest().getOther(current);
            rooms.add(current);
        }

        current = room;
        cnt = 0;

        while (current.getEast().isPassage() && cnt++ < visRange) {
            current = current.getEast().getOther(current);
            rooms.add(current);
        }

        return rooms;
    }

    /**
     * Determines which room next to the given monster is closest to the player.
     *
     * @param	mob	The monster which needs to move closer to the player.
     * @param	playerRoom	The room the player is in.
     *
     * @return	The room the monster needs to move to.
     */
    public Cell getNextRoom(Cell mob, Cell playerRoom) {
        if (mob.getX() == playerRoom.getX()) {
            if (playerRoom.getY() - mob.getY() < 0) {
                return grid[mob.getX()][mob.getY() - 1];
            } else if (playerRoom.getY() - mob.getY() > 0) {
                return grid[mob.getX()][mob.getY() + 1];
            } else {
                return mob;
            }
        } else {
            if (playerRoom.getX() - mob.getX() < 0) {
                return grid[mob.getX() - 1][mob.getY()];
            } else if (playerRoom.getX() - mob.getX() > 0) {
                return grid[mob.getX() + 1][mob.getY()];
            } else {
                return mob;
            }
        }
    }


    /*
* ------------------------- Getters and Setters -------------------------
     */
    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the visRange
     */
    public int getVisRange() {
        return visRange;
    }

    /**
     * @param visRange the visRange to set
     */
    public void setVisRange(int visRange) {
        this.visRange = visRange;
    }

    /**
     * @return the player
     */
    public Cell getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(Cell player) {
        this.player = player;
    }

    /**
     * @return the key
     */
    public Cell getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Cell key) {
        this.key = key;
    }

    /**
     * @return the exit
     */
    public Cell getExit() {
        return exit;
    }

    /**
     * @param exit the exit to set
     */
    public void setExit(Cell exit) {
        this.exit = exit;
    }

    /**
     * @return the grid
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * @return the mobs
     */
    public List<Cell> getMobs() {
        return mobs;
    }

    /**
     * @param mobs the mobs to set
     */
    public void setMobs(List<Cell> mobs) {
        this.mobs = mobs;
    }

    /**
     * @return the alive
     */
    public Boolean getAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    /**
     * @return the escaped
     */
    public Boolean getEscaped() {
        return escaped;
    }

    /**
     * @param escaped the escaped to set
     */
    public void setEscaped(Boolean escaped) {
        this.escaped = escaped;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        String newMsg = msg;
        msg = "";
        return newMsg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
