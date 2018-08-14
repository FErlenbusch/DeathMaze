/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deathmaze;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * <h1>DeathMazeUI</h1>
 * This is the User Interface for the Death Maze game. This UI is built on the JavaFX framework.
 *
 * @author Fred Erlenbusch
 * @version 1.0
 * @since 2018-07-16
 */
public class DeathMaze extends Application {

    /**
     * The size, in pixels, of the rooms.
     */
    private static final int ROOM_SIZE = 10;
    /**
     * The size, in pixels, of the walls.
     */
    private static final int WALL_SIZE = 2;
    /**
     * The path to the CSS for the application.
     */
    private static final String STYLE_SHEET = "/styles/UIStyle.css";
    /**
     * The path to the application's icon.
     */
    private static final String ICON_PATH = "/images/maze.png";
    /**
     * The id name for a title element
     */
    private static final String TITLE = "title";
    /**
     * The data for the maze used in this game.
     */
    private Maze deathMaze;
    /**
     * The Pane for the map of the maze. This is declared globally to enable it to be refreshed.
     */
    private GridPane map;
    /**
     * The Label used to give notifications to the users on the current status of their game or when
     * events happen.
     */
    private Label notification;
    /**
     * The chosen width of the map for this game.
     */
    private int width = 25;
    /**
     * The chosen height of the map for this game.
     */
    private int height = 25;
    /**
     * The chosen number of monsters in this game.
     */
    private int mobCnt = 3;
    /**
     * The chosen visibility range of the characters in this game.
     */
    private int visRange = 3;
    /**
     * The Primary Stage for this applications.
     */
    private Stage primaryStage;

    /**
     * The main class for this application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method that starts the UI. Required by JavaFX.
     *
     * @param	primaryStage	The primary stage determined by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Death Maze");

        initMap();

        GridPane root = initRoot();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(DeathMaze.class.getResource(STYLE_SHEET).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(ICON_PATH));
        primaryStage.show();
    }

    /**
     * Initializes the map used by the UI
     */
    private void initMap() {
        deathMaze = new Maze(width, height, mobCnt, visRange);
        map = new GridPane();
        paintMap();
    }

    /**
     * Initializes the root pane for the UI.
     *
     * @return	The Root Pane of the UI.
     */
    private GridPane initRoot() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.add(initControls(), 0, 0);
        root.add(map, 0, 1);
        root = createEventFilter(root);

        root.setGridLinesVisible(false);

        return root;
    }

    /**
     * Initializes the pane that contains the game's controls, the controls, and their
     * functionality.
     *
     * @return	The Pane containing the controls.
     */
    private GridPane initControls() {
        GridPane controls = new GridPane();
        controls.setAlignment(Pos.CENTER);
        controls.setHgap(5);
        controls.setVgap(10);
        controls.setPadding(new Insets(0));

        Label title = new Label("Welcome to the Death Maze");
        title.setId(TITLE);
        GridPane.setHalignment(title, HPos.CENTER);
        controls.add(title, 0, 0);

        notification = new Label();
        notification.setId("notification");
        controls.add(notification, 0, 2);
        GridPane.setHalignment(notification, HPos.CENTER);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(initButtons());
        controls.add(hbBtn, 0, 1);

        controls.setGridLinesVisible(false);

        return controls;
    }

    /**
     * Initializes the game's buttons and their functionality.
     *
     * @return	A list of the game's buttons.
     */
    private List<Button> initButtons() {
        List<Button> buttons = new ArrayList<>();
        Button startBtn = new Button("New Maze");
        Button upBtn = new Button("Move Up");
        Button downBtn = new Button("Move Down");
        Button leftBtn = new Button("Move Left");
        Button rightBtn = new Button("Move Right");
        Button instructionsBtn = new Button("Instructions");
        Button exitBtn = new Button("Quit");

        startBtn.setTooltip(new Tooltip("Enter"));
        upBtn.setTooltip(new Tooltip("\u2191"));
        downBtn.setTooltip(new Tooltip("\u2193"));
        leftBtn.setTooltip(new Tooltip("\u2190"));
        rightBtn.setTooltip(new Tooltip("\u2192"));
        instructionsBtn.setTooltip(new Tooltip("I"));
        exitBtn.setTooltip(new Tooltip("Esc"));

        startBtn.setOnAction(e -> showNewMazeSettings());
        upBtn.setOnAction(e -> move("N"));
        downBtn.setOnAction(e -> move("S"));
        leftBtn.setOnAction(e -> move("W"));
        rightBtn.setOnAction(e -> move("E"));
        instructionsBtn.setOnAction(e -> showInstructions());
        exitBtn.setOnAction(e -> System.exit(0));

        buttons.add(startBtn);
        buttons.add(upBtn);
        buttons.add(downBtn);
        buttons.add(leftBtn);
        buttons.add(rightBtn);
        buttons.add(instructionsBtn);
        buttons.add(exitBtn);

        for (Button btn : buttons) {
            btn.getTooltip().setContentDisplay(ContentDisplay.BOTTOM);
        }

        return buttons;
    }

    /**
     * Initializes a pane containing the game map's legend.
     *
     * @return	The pane containing the legend.
     */
    private GridPane initLegend() {
        int cnt = 0;

        GridPane legend = new GridPane();
        legend.setAlignment(Pos.CENTER);
        legend.setHgap(25);
        legend.setVgap(0);
        legend.setPadding(new Insets(0));

        Label player = new Label("Player");
        GridPane.setHalignment(player, HPos.CENTER);
        legend.add(player, cnt, 1);

        Rectangle playerColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        playerColor.setFill(Color.BLUE);
        GridPane.setHalignment(playerColor, HPos.CENTER);
        legend.add(playerColor, cnt++, 2);

        Label monster = new Label("Monster");
        GridPane.setHalignment(monster, HPos.CENTER);
        legend.add(monster, cnt, 1);

        Rectangle monsterColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        monsterColor.setFill(Color.FIREBRICK);
        GridPane.setHalignment(monsterColor, HPos.CENTER);
        legend.add(monsterColor, cnt++, 2);

        Label key = new Label("Key");
        GridPane.setHalignment(key, HPos.CENTER);
        legend.add(key, cnt, 1);

        Rectangle keyColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        keyColor.setFill(Color.GOLD);
        GridPane.setHalignment(keyColor, HPos.CENTER);
        legend.add(keyColor, cnt++, 2);

        Label exit = new Label("Exit");
        GridPane.setHalignment(exit, HPos.CENTER);
        legend.add(exit, cnt, 1);

        Rectangle exitColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        exitColor.setFill(Color.GREENYELLOW);
        GridPane.setHalignment(exitColor, HPos.CENTER);
        legend.add(exitColor, cnt++, 2);

        Label keyMonster = new Label("Monster + Key");
        GridPane.setHalignment(keyMonster, HPos.CENTER);
        legend.add(keyMonster, cnt, 1);

        Rectangle keyMonsterColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        keyMonsterColor.setFill(Color.DARKORANGE);
        GridPane.setHalignment(keyMonsterColor, HPos.CENTER);
        legend.add(keyMonsterColor, cnt++, 2);

        Label exitMonster = new Label("Monster + Exit");
        GridPane.setHalignment(exitMonster, HPos.CENTER);
        legend.add(exitMonster, cnt, 1);

        Rectangle exitMonsterColor = new Rectangle(ROOM_SIZE, ROOM_SIZE);
        exitMonsterColor.setFill(Color.LIGHTSALMON);
        GridPane.setHalignment(exitMonsterColor, HPos.CENTER);
        legend.add(exitMonsterColor, cnt++, 2);

        Label title = new Label("Legend");
        GridPane.setHalignment(title, HPos.CENTER);
        title.setId("legend");
        legend.add(title, 0, 0, cnt, 1);

        legend.setGridLinesVisible(false);

        return legend;
    }

    /**
     * Creates the key event listener for the root pane. This allows player to control the game with
     * keyboard as well as through the UI.
     *
     * @param root	The root pane of this application.
     * @return	The root pane with key event listeners.
     */
    private GridPane createEventFilter(GridPane root) {
        root.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                showNewMazeSettings();
            }

            if (keyEvent.getCode() == KeyCode.UP) {
                move("N");
            }

            if (keyEvent.getCode() == KeyCode.DOWN) {
                move("S");
            }

            if (keyEvent.getCode() == KeyCode.LEFT) {
                move("W");
            }

            if (keyEvent.getCode() == KeyCode.RIGHT) {
                move("E");
            }

            if (keyEvent.getCode() == KeyCode.I) {
                showInstructions();
            }

            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

        return root;
    }

    /**
     * Creates another window from the main window to display setting's controls for starting a new
     * game.
     */
    private void showNewMazeSettings() {
        Stage newMazeSettingsStage = new Stage();
        newMazeSettingsStage.setTitle("Death Maze - New Maze");

        final int oldWidth = width;
        final int oldHeight = height;
        final int oldMobCnt = mobCnt;
        final int oldVisRange = visRange;

        GridPane settings = new GridPane();
        settings.setAlignment(Pos.CENTER);
        settings.setHgap(10);
        settings.setVgap(10);
        settings.setPadding(new Insets(10));

        Label title = new Label("New Game Settings");
        GridPane.setHalignment(title, HPos.CENTER);
        title.setId(TITLE);

        settings.add(title, 0, 0, 2, 1);

        Label widthValue = new Label("Width: " + width);
        GridPane.setHalignment(widthValue, HPos.CENTER);

        Slider widthSlider = new Slider(10, 50, width);
        widthSlider.setBlockIncrement(1);
        GridPane.setHalignment(widthSlider, HPos.CENTER);

        widthSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal,
                Number newVal) -> {
            width = newVal.intValue();
            widthValue.setText("Width: " + width);
        });

        settings.add(widthValue, 0, 1);
        settings.add(widthSlider, 1, 1);

        Label heightValue = new Label("Height: " + height);
        GridPane.setHalignment(heightValue, HPos.CENTER);

        Slider heightSlider = new Slider(10, 50, height);
        heightSlider.setBlockIncrement(1);
        GridPane.setHalignment(heightSlider, HPos.CENTER);

        heightSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> {
            height = newVal.intValue();
            heightValue.setText("Height: " + height);
        });

        settings.add(heightValue, 0, 2);
        settings.add(heightSlider, 1, 2);

        Label mobValue = new Label("# of Monsters: " + mobCnt);
        GridPane.setHalignment(mobValue, HPos.CENTER);

        Slider mobSlider = new Slider(1, 10, mobCnt);
        mobSlider.setBlockIncrement(1);
        GridPane.setHalignment(mobSlider, HPos.CENTER);

        mobSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> {
            mobCnt = newVal.intValue();
            mobValue.setText("# of Monsters: " + mobCnt);
        });

        settings.add(mobValue, 0, 3);
        settings.add(mobSlider, 1, 3);

        Label visValue = new Label("Visibility Range: " + visRange);
        GridPane.setHalignment(visValue, HPos.CENTER);

        Slider visSlider = new Slider(1, 5, visRange);
        visSlider.setBlockIncrement(1);
        GridPane.setHalignment(visSlider, HPos.CENTER);

        visSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> {
            visRange = newVal.intValue();
            visValue.setText("Visibility Range: " + visRange);
        });

        settings.add(visValue, 0, 4);
        settings.add(visSlider, 1, 4);

        Button escBtn = new Button("Close");
        escBtn.setTooltip(new Tooltip("Esc"));
        escBtn.setOnAction(e
                -> {
            resetValues(oldWidth, oldHeight, oldMobCnt, oldVisRange);
            newMazeSettingsStage.close();
        });
        escBtn.getTooltip().setContentDisplay(ContentDisplay.BOTTOM);
        GridPane.setHalignment(escBtn, HPos.CENTER);

        Button startBtn = new Button("Start New Maze");
        startBtn.setTooltip(new Tooltip("Enter"));
        startBtn.setOnAction(e -> {
            primaryStage.close();
            newMazeSettingsStage.close();
            start(primaryStage);
        });
        startBtn.getTooltip().setContentDisplay(ContentDisplay.BOTTOM);
        GridPane.setHalignment(startBtn, HPos.CENTER);

        settings.add(escBtn, 0, 5);
        settings.add(startBtn, 1, 5);

        settings.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                resetValues(oldWidth, oldHeight, oldMobCnt, oldVisRange);
                newMazeSettingsStage.close();
            }

            if (keyEvent.getCode() == KeyCode.ENTER) {
                primaryStage.close();
                newMazeSettingsStage.close();
                start(primaryStage);
            }
        });

        settings.setGridLinesVisible(false);

        Scene newMazeSettingsScene = new Scene(settings);
        newMazeSettingsScene.getStylesheets().add(DeathMaze.class.getResource(STYLE_SHEET).toExternalForm());

        newMazeSettingsStage.setScene(newMazeSettingsScene);
        newMazeSettingsStage.getIcons().add(new Image(ICON_PATH));
        newMazeSettingsStage.show();
    }

    /**
     * Creates another window from the main window to display the game's instructions and its
     * legend.
     */
    private void showInstructions() {
        Stage instructionStage = new Stage();
        instructionStage.setTitle("Death Maze - Instructions");

        VBox box = new VBox();
        box.setSpacing(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));

        Label title = new Label("Instructions");
        title.setId(TITLE);

        List<Node> elems = box.getChildren();

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> instructionStage.close());

        elems.add(title);
        elems.add(new Label("You've woken up in a strange maze, and you have no idea \n"
                + "how you've gotten here. You smell a foul oder, and you \n"
                + "can hear the sounds of several large creatures, and it \n"
                + "everything about this maze is extremely unsettling. All \n"
                + "you know is that you need to get out of here as fast as \n"
                + "you can, and if you don't starve to death you're probably\n"
                + "going to be killed by on of those creatures you hear... \n"
                + "	\n"
                + "1. Avoid the monsters, if you see them then they see you \n"
                + " and they'll come after you. \n"
                + "2. Find the key, the exit is locked and you can't escape \n"
                + " without being open the door.	\n"
                + "3. Find the exit, and escape as fast as you can.	\n"
                + "	\n"
                + "-- You can only see 3 spaces in a line of site from your \n"
                + " current location.	\n"
                + "-- Monsters will only appear in you line of vision	\n"
                + "-- You're smart enough to remember what you've seen. \n"
                + " -- The layout of the maze, as you traverse it. \n"
                + " -- Where the exit is.	\n"
                + " -- Where the key is.	\n\n"));
        elems.add(initLegend());
        elems.add(closeBtn);

        box.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                instructionStage.close();
            }
        });

        Scene instructionsScene = new Scene(box);
        instructionsScene.getStylesheets().add(DeathMaze.class.getResource(STYLE_SHEET).toExternalForm());

        instructionStage.setScene(instructionsScene);
        instructionStage.getIcons().add(new Image(ICON_PATH));
        instructionStage.show();
    }

    /**
     * Resets global values to current values for the map when the New Maze Settings dialogue exits
     * without starting a new maze.
     *
     * @param width	What the current width should be.
     * @param height	What the current height should be.
     * @param mobCnt	What the current mobCnt should be.
     * @param visRange	What the current visRange should be.
     */
    private void resetValues(int width, int height, int mobCnt, int visRange) {
        this.width = width;
        this.height = height;
        this.mobCnt = mobCnt;
        this.visRange = visRange;
    }

    /**
     * Paints the current map to the pane containing the map.
     */
    private void paintMap() {
        map.getChildren().clear();
        map.setAlignment(Pos.CENTER);
        map.setHgap(0);
        map.setVgap(0);
        map.setPadding(new Insets(10, 10, 10, 10));

        Cell[][] grid = deathMaze.getGrid();
        List<Cell> visibleRooms = deathMaze.getVisibleRooms(deathMaze.getPlayer());

        for (int y = 0; y < height * 3; y++) {

            if (y % 3 == 0 || y % 3 == 2) {
                paintNorthSouthWalls(grid, y);
            } else if (y % 3 == 1) {
                paintRoomsAndWalls(visibleRooms, grid, y);
            }
        }

    }

    /**
     * Paints the rooms, and the walls to the East and West of the rooms in a given row.
     *
     * @param visibleRooms	The rooms visible to the player.
     * @param grid	The current map's grid.
     * @param y	The y coordinate of the row to be generated.
     */
    private void paintRoomsAndWalls(List<Cell> visibleRooms, Cell[][] grid, int y) {
        for (int x = 0; x < width * 3; x++) {
            Cell room = grid[x / 3][y / 3];

            if ((x % 3 == 0 || x % 3 == 2)) {
                paintEastWestWalls(room, x, y);
            } else if (x % 3 == 1) {
                paintRoom(visibleRooms, room, x, y);
            }
        }
    }

    /**
     * Paints the walls to the North and South of a given row their appropriate colors.
     *
     * @param grid	The current map's grid.
     * @param y	The y coordinate of the row to be generated.
     */
    private void paintNorthSouthWalls(Cell[][] grid, int y) {
        for (int x = 0; x < width * 3; x++) {
            Cell room = grid[x / 3][y / 3];
            Rectangle wall = new Rectangle(WALL_SIZE, WALL_SIZE);

            wall.setFill(Color.BLACK);

            if (x % 3 == 1) {
                wall.setWidth(ROOM_SIZE);

                if (((y % 3 == 0 && room.getNorth().isPassage() && room.getNorth().getOther(room).isVisited())
                        || (y % 3 == 2 && room.getSouth().isPassage() && room.getSouth().getOther(room).isVisited()))
                        && room.isVisited()) {
                    wall.setFill(Color.WHITE);
                }
            }

            map.add(wall, x, y);
        }
    }

    /**
     * Paints a given wall determined by it's x and y coordinate the appropriate color if it's an
     * wall to the East or West of a room.
     *
     * @param room	The room of the given wall.
     * @param x	The x coordinate of the wall.
     * @param y	The y coordinate of the wall.
     */
    private void paintEastWestWalls(Cell room, int x, int y) {
        Rectangle wall = new Rectangle(WALL_SIZE, ROOM_SIZE);

        wall.setFill(Color.BLACK);

        if (((x % 3 == 0 && room.getWest().isPassage() && room.getWest().getOther(room).isVisited())
                || (x % 3 == 2 && room.getEast().isPassage() && room.getEast().getOther(room).isVisited()))
                && room.isVisited()) {
            wall.setFill(Color.WHITE);
        }

        map.add(wall, x, y);
    }

    /**
     * Paints a given room the appropriate color.
     *
     * @param visableRooms	Rooms visible to the player.
     * @param cRoom	The room to be painted.
     * @param x	The x coordinate of the room.
     * @param y	The y coordinate of the room.
     */
    private void paintRoom(List<Cell> visableRooms, Cell cRoom, int x, int y) {
        Rectangle rRoom = new Rectangle(x, y, ROOM_SIZE, ROOM_SIZE);

        rRoom.setFill(Color.BLACK);

        if (cRoom.isPartOfMaze() && !deathMaze.isRoomEmpty(cRoom)) {
            if (deathMaze.getMobs().contains(cRoom) && visableRooms.contains(cRoom)) {
                rRoom = setMobColors(rRoom, cRoom);
            } else if (deathMaze.getPlayer().equals(cRoom)) {
                rRoom.setFill(Color.BLUE);
            } else if (cRoom.isVisited()) {
                rRoom = setGoalColors(rRoom, cRoom);
            }
        } else if (cRoom.isPartOfMaze() && cRoom.isVisited()) {
            rRoom.setFill(Color.WHITE);
        }

        map.add(rRoom, x, y);
    }

    /**
     * Sets a room to the appropriate Monster color.
     *
     * @param rRoom	The UI element to be colored.
     * @param cRoom	The room being represented.
     * @return	The colored UI element.
     */
    private Rectangle setMobColors(Rectangle rRoom, Cell cRoom) {
        if (deathMaze.getKey().equals(cRoom)) {
            rRoom.setFill(Color.DARKORANGE);
        } else if (deathMaze.getExit().equals(cRoom)) {
            rRoom.setFill(Color.LIGHTSALMON);
        } else {
            rRoom.setFill(Color.FIREBRICK);
        }

        return rRoom;
    }

    /**
     * Sets a room to the appropriate goal color.
     *
     * @param rRoom	The UI element to be colored.
     * @param cRoom	The room being represented.
     * @return	The colored UI element.
     */
    private Rectangle setGoalColors(Rectangle rRoom, Cell cRoom) {
        if (deathMaze.getKey().equals(cRoom)) {
            rRoom.setFill(Color.GOLD);
        } else if (deathMaze.getExit().equals(cRoom)) {
            rRoom.setFill(Color.GREENYELLOW);
        } else {
            rRoom.setFill(Color.WHITE);
        }

        return rRoom;
    }

    /**
     * The method used by the UI to move the player, update the map after the move, and to get
     * display any notifications to the user that may be generated in response to the move.
     *
     * @param dir	The direction to move the player.
     */
    private void move(String dir) {
        deathMaze.movePlayer(dir);
        paintMap();
        notification.setText(deathMaze.getMsg());
    }
}
