package com.mygdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Represents the primary game screen where the player interacts with different gameplay elements
 * It handles rendering of the game world, player movement, and interaction with game objects and menus for activities like studying,
 * relaxing, sleeping and eating.
 */
public class GameScreen extends ScreenAdapter {
    // Fields for managing game state and UI
    private Game game; // Reference for the main game class
    private Skin skin; // Skin for styling
    private BitmapFont font; // Font for UI Text
    private LabelStyle menuTextStyle; // Style for UI labels

    //Tables for different activity menus
    private Table studyTable;
    private Table relaxTable;
    private Table sleepTable;
    private Table eatTable;
    private Table insufficientTable;

    private Stage stage; // Stage for managing UI elements
    private Table currentTable; // Currently active table
    // Gameplay-related fields
    protected CameraManager cameraHandler; // Manages the camera for game rendering.
    protected OrthographicCamera camera; // Camera for viewing the game world.
    private SpriteBatch batch; // Batch for rendering textures
    private Player player; // Player character
    private Vector2 characterPosition; // Current position of the player
    public Preferences prefs; // Preferences for saving/loading game state
    private GameMap gameMap; // The game map
    // Tracks game statistics which include study hours, meals eaten, hours relaxed and hours slept
    private StatsTracker stats;
    private EnergyMeter energyMeter; // Tracks and displays the player's energy levels.
    private DayCycleManager dayCycleManager; // Manages the in-game day cycle and time passage

    /**
     * Contructs the game screen with references to the game object and skin
     * Initializes game components like player, map, stats tracker, energy meter and day cycle manager
     * @param game
     * @param skin
     */
    public GameScreen(Game game, Skin skin) {
        this.game = game;
        this.skin = skin;
        font = new BitmapFont();
        prefs = Gdx.app.getPreferences("game_prefs");

        // Initialization of game components
        stats = new StatsTracker();
        dayCycleManager = new DayCycleManager(game, skin);
        gameMap = new GameMap("maps/map.tmx");

        // Player spawn position initialization
        Vector2 spawnPoint = gameMap.getSpawnPoint();
        float characterX = prefs.getFloat("characterX", spawnPoint.x);
        float characterY = prefs.getFloat("characterY", spawnPoint.y);
        Vector2 characterPosition = new Vector2(characterX, characterY);

        player = new Player(new Texture("character/down_idle_1.png"), characterPosition.x, characterPosition.y, 2, gameMap);
        player.create();

        energyMeter = new EnergyMeter();

    }

    /**
     * Sets up UI elements and initializes gameplay systems\
     * Called when the game screen becomes active.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();

        // Create all the interaction menus and the stage they use to display
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        menuTextStyle = new LabelStyle(font, Color.WHITE); 
        createStudy();
        createRelax();
        createSleep();
        createEat();
        createInsufficient();
        currentTable = null;
        
        characterPosition = getCharacterPosition();
        
        cameraHandler = new CameraManager(characterPosition);
        camera = cameraHandler.camera;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);

        // Setup gameplay components
        energyMeter.setup();
        dayCycleManager.setup();
        stats.setup();

    }

    /**
     * The core game loop, processes input, updates the game state, and renders the game world.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        // Handle Input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            saveCharacterPosition();
            game.setScreen(new inGameMenu(game, skin, this));
        }
        handleInput();

        // Update game components
        gameMap.update(delta);
        dayCycleManager.update(delta);

        // Update character position
        Vector2 characterPosition = getCharacterPosition();

        gameMap.insideCheck(characterPosition, player.getWidth(), player.getHeight());
        gameMap.insideCheck(characterPosition, player.getWidth(), player.getHeight());

        checkInteract(); // Check for interactions

        // Update the camera based on player movement
        cameraHandler.update(delta, characterPosition);
        batch.setProjectionMatrix(camera.combined);

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game elements
        gameMap.render(camera);
        player.render(delta, camera);
        energyMeter.render();
        dayCycleManager.render();
        stats.render();

        // Draw UI Elements
        batch.begin();
        if (currentTable != null){
            font.draw(batch, "press ENTER \n" + 
                                " to interact", characterPosition.x+20, characterPosition.y - 20);
        }
        batch.end();

        stage.act(delta);
	    stage.draw();



    }

    /**
     * Handles player input for movement and interactions
     */
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.moveDown();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }

        // Stop the player if no keys are pressed
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.stopMovingUp();
            player.stopMovingDown();
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.stopMovingLeft();
            player.stopMovingRight();
        }

        // Interaction handling
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && stage.getActors().size == 0 && currentTable!= null){
            stage.addActor(currentTable);
        }
    }

    /**
     * Retrieves and updates the character's current position
     * @return the current position of the player character
     */
    public Vector2 getCharacterPosition(){
        characterPosition = new Vector2(player.position.x, player.position.y);
        return characterPosition;
    }

    /**
     * Pauses the game, saving the player's current position
     * The method is called automatically when the game is paused, such as when the application loses focus
     */
    @Override
    public void pause(){
        // Logs the pausing action with the current player position
        System.out.println("GameScreen Pausing game, saving character position: " + player.position);
        // Saves the player's current position to preferences
        prefs.putFloat("characterX", player.position.x);
        prefs.putFloat("characterY", player.position.y);
        // Commits the changes to preferences
        prefs.flush();
    }

    /**
     * Saves the player's current position
     * This method is useful for manual calls to save the player position
     */
    private void saveCharacterPosition(){
        // Saves the player's current position to preferences
        prefs.putFloat("characterX", player.position.x);
        prefs.putFloat("characterY", player.position.y);
        // Commits the changes to preferences
        prefs.flush();
        System.out.println("Saving character position: " + player.position);

    }

    /**
     * Resumes the game, loading the player's saved position
     * This method is called automatically when the game resumes from a paused state
     */
    @Override
    public void resume(){
        // Retrieves the player's saved position from preferences
        float characterX = prefs.getFloat("characterX", 1955);
        float characterY = prefs.getFloat("characterY", 1511);
        // Sets the player's position to the retrieved values
        player.position.set(characterX, characterY);
        System.out.println("GameScreen Resuming game, loading character position: " + player.position);
    }

    /**
     * Adjusts the camera's viewport size in response to the window being resized
     * @param width The new width of the window
     * @param height The new height of the window
     */
    @Override
    public void resize(int width, int height) {
        // Adjusts the camera's viewport to match the new window size
        camera.setToOrtho(false, width, height);
    }

    /**
     * Disposes of the game's resources
     * This method is called when the game is closing to ensure that memory leaks do not occur
     */
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        player.dispose();
        gameMap.dispose();
        dayCycleManager.dispose();
        energyMeter.dispose();
        stats.dispose();
        // Dispose other resources here
    }

    /**
     * checks if the player is within any of the interactable regions and sets the currentTable according to which area they are in
     */
    private void checkInteract(){
        // Checks for interaction areas and sets the current table accordingly
        if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "study_interact")){
            currentTable = studyTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "relax_interact")){
            currentTable = relaxTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "eat_interact")){
            currentTable = eatTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "sleep_interact")){
            currentTable = sleepTable;
        }
        else {
            currentTable = null;
        }

    }


    /**
     * creates the menu for studying
     */
    private void createStudy(){
        // Initialize the table that holds the study menu UI components
        studyTable = new Table();
        // Create a title label and add it to the table
        Label studyTitle = new Label("Study for how many hours?", menuTextStyle);
        // Initialize a slider for selecting the number of study hours (1 to 5)
        Slider studyTime = new Slider(1, 5, 1, false ,skin);
        // Initialize buttons for confirming or cancelling the study action
        TextButton studyConfirm = new TextButton("Confirm", skin);
        TextButton studyCancel = new TextButton("Cancel", skin);
        // Labels to display the selected number of hours and the corresponding energy cost
        Label studyVal = new Label(Float.toString(studyTime.getValue()), menuTextStyle);
        Label studyCost = new Label("Cost: " + Float.toString(studyTime.getValue() * 15), menuTextStyle);

        // Add all the labels and sliders to the table
        studyTable.add(studyTitle);
        studyTable.row();
        studyTable.add(studyTime);
        studyTable.add(studyVal);
        studyTable.row();
        studyTable.add(studyCost);
        studyTable.row();
        studyTable.add(studyConfirm);
        studyTable.add(studyCancel);
        studyTable.setFillParent(true);
        studyTable.center();

        // Add a listener to the confirm button to apply the study action
        studyCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                studyTable.remove();
            }
        });

        // Add a listener to the confirm button to apply the study action
        studyConfirm.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Retrieve the selected number of study hours and calculate the energy cost
                int timePassed = (int) studyTime.getValue();
                int cost = (int) (15 * timePassed);
                // Check if the player has enough energy to study
                if(cost <= energyMeter.getEnergy()){
                    // Deduct the energy cost and add the study hours to the player's stats
                    energyMeter.loseEnergy(cost);
                    stats.addStudy((int) timePassed);
                    // Advance the in-game time accordingly
                    dayCycleManager.addTime(timePassed, 0);
                    studyTable.remove(); // Remove the study menu
                }
                else{
                    // If not enough energy, display the insufficient energy popup
                    stage.addActor(insufficientTable);
                    studyTable.remove();
                }
            }
        });

        // Add a listener to the slider to update the displayed values when the slider is moved
        studyTime.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                studyVal.setText(Float.toString(studyTime.getValue())); // Update the displayed hours
                studyCost.setText("Cost: " + Float.toString(studyTime.getValue() * 15));  // Update the cost
            }
        });
     
    }

    /**
     * creates the menu for recreational interactions
     */
    private void createRelax(){
        // Initialize the table for relaxation menu
        relaxTable = new Table();
        // Create and style the title label for relaxation
        Label relaxTitle = new Label("relax for how many hours?", menuTextStyle);
        // Slider for selecting relaxation duration
        Slider relaxTime = new Slider(1, 5, 1, false ,skin);
        // Buttons for confirming or cancelling the relaxation
        TextButton relaxConfirm = new TextButton("Confirm", skin);
        TextButton relaxCancel = new TextButton("Cancel", skin);
        // Labels for displaying the selected time and the energy cost of relaxation
        Label relaxVal = new Label(Float.toString(relaxTime.getValue()), menuTextStyle);
        Label relaxCost = new Label("Cost: " + Float.toString(relaxTime.getValue() * 7), menuTextStyle);

        // Add all labels, buttons and slider to the table
        relaxTable.add(relaxTitle);
        relaxTable.row();
        relaxTable.add(relaxTime);
        relaxTable.add(relaxVal);
        relaxTable.row();
        relaxTable.add(relaxCost);
        relaxTable.row();
        relaxTable.add(relaxConfirm);
        relaxTable.add(relaxCancel);
        // Set table to fill the parent container and center its content
        relaxTable.setFillParent(true);
        relaxTable.center();

        // Listener for the cancel button to remove the table from the stage
        relaxCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                relaxTable.remove(); // Remove the table
            }
        });

        // Listener for the confirm button to apply the relaxation action
        relaxConfirm.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int timePassed = (int) relaxTime.getValue();
                int cost = (int) (7 * timePassed); // Calculate the energy cost
                if(cost <= energyMeter.getEnergy()){
                    // If the player has enough energy, deduct the cost and update stats and day cycle
                    energyMeter.loseEnergy(cost);
                    stats.addRelax((int) timePassed);
                    dayCycleManager.addTime(timePassed, 0);
                    relaxTable.remove(); // Remove the table from the stage
                }
                else{
                    // If not enough energy, show the insufficient energy table
                    stage.addActor(insufficientTable);
                    relaxTable.remove(); // Remove the relax table from the stage
                }
            }
        });

        // Listener for the slider to update the selected values and cost dynamically
        relaxTime.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                relaxVal.setText(Float.toString(relaxTime.getValue())); // Update the displayed time
                relaxCost.setText("Cost: " + Float.toString(relaxTime.getValue() * 7)); // Update the cost
            }
        });
     
    }

    /**
     * creates the menu for sleeping
     */
    private void createSleep(){
        // Initialize the table for sleep menu
        sleepTable = new Table();
        // Create and style the title label for the sleep prompt
        Label sleepTitle = new Label("Sleep until 8am?", menuTextStyle);
        // Buttons for confirming or refusing sleep action
        TextButton sleepYes = new TextButton("Yes", skin);
        TextButton sleepNo = new TextButton("No", skin);

        // Add all the labels and buttons to the table
        sleepTable.add(sleepTitle);
        sleepTable.row();
        sleepTable.add(sleepYes);
        sleepTable.add(sleepNo);
        // Set the table to fill the parent container and center its content
        sleepTable.setFillParent(true);
        sleepTable.center();

        // Listener for the "Yes" button to precess with sleep
        sleepYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Calculates the hours and minutes to sleep based on current time
                int sleepHour;
                int sleepMinute;
                int currentHour = dayCycleManager.getHour();
                int currentMinute = dayCycleManager.getMinute();
                int totalTime = currentMinute + 60*currentHour; 
                if (currentHour >= 8){
                    // If its past 8am, calculate time to next 8am
                    int sleepTime = 24*60 - totalTime;
                    sleepHour = sleepTime / 60;
                    sleepMinute = sleepTime % 60;
                    sleepHour += 8; // Adjust to wake up at 8am next day
                }
                else {
                    // If it's before 8am, calculate time to 8am same day
                    int sleepTime = 8*60 - totalTime;
                    sleepHour = sleepTime / 60;
                    sleepMinute = sleepTime % 60;
                }
                // Update game stats for sleep time and advance game time
                stats.addSleep(sleepHour, sleepMinute);
                dayCycleManager.addTime(sleepHour, sleepMinute);

                // Reset player's energy to full after sleeping
                energyMeter.resetEnergy();

                // Remove the sleep menu table from the stage
                sleepTable.remove();
            } 
        });

        // Listener for the "No" button to cancel the sleep action
        sleepNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTable.remove(); // Remove the sleep menu table from the stage
            } 
        });
    }

    /**
     * creates the menu for the eating interaction
     */
    private void createEat(){
        // Initialize the table for the eating menu
        eatTable = new Table();
        // Create and style the title label for the eating prompt
        Label eatTitle = new Label("Eat?", menuTextStyle);
        // Buttons for confirming or refusing the action to eat
        TextButton eatYes = new TextButton("Yes", skin);
        TextButton eatNo = new TextButton("No", skin);

        // Add all components to the table
        eatTable.add(eatTitle);
        eatTable.row();
        eatTable.add(eatYes);
        eatTable.add(eatNo);
        // Set the table to fill the parent container and center its content
        eatTable.setFillParent(true);
        eatTable.center();

        // Listener for the "Yes" button to proceed with eating
        eatYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Advance the game time by 30 minutes to simulate the time taken to eat
                dayCycleManager.addTime(0, 30);
                // Update the player's stats to reflect a meal eaten
                stats.mealAte();
                eatTable.remove(); // Remove the eating menu table from the stage
            } 
        });

        // Listener for the "No" button to cancel the eating action
        eatNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Simply remove the eating menu table from the stage without taking any action
                eatTable.remove();
            } 
        });
    }

    /**
     * creates the popup for when there isn't enough energy for an activity
     */
    private void createInsufficient(){
        // Initialize the table for the insufficient energy message
        insufficientTable = new Table();
        // Create and style the title label for the insufficient energy popup
        Label insufficientTitle = new Label("Not enough energy", menuTextStyle);
        // Create the OK button to close the popup
        TextButton insufficientOk = new TextButton("OK", skin);

        // Add the title and OK button to the table
        insufficientTable.add(insufficientTitle);
        insufficientTable.row();
        insufficientTable.add(insufficientOk);
        // Set the table to fill the parent container and center its content
        insufficientTable.setFillParent(true);
        insufficientTable.center();

        // Add a listener to the OK button to remove the popup when clicked
        insufficientOk.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                insufficientTable.remove(); // Remove the popup table from the stage
            } 
        });
    }
}