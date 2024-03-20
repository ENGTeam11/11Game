package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Represents the end screen displayed to the player upon completing the game
 */
public class EndScreen extends ScreenAdapter {
    private Game game; // Reference to the main game object for screen switching
    private Skin skin; // Skin for styling UI
    private Stage stage; // Stage to host UI elements
    private LabelStyle menuTextStyle; // Style for text displayed on the menu
    private BitmapFont font; // Font for rendering text

    /**
     * Constructor initializing the end screen with the game and skin
     * @param game Reference to the main game object
     * @param skin skin for styling
     */
    public EndScreen(Game game, Skin skin){
        this.game = game;
        this.skin = skin;
        font = new BitmapFont(); // Initialize the font
    }

    @Override
    public void show(){
        // Create a new stage with a viewport matching the screen dimensions
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        // Set the stage to process user input
        Gdx.input.setInputProcessor(stage);

        // Define the style for labels used in this screen
        menuTextStyle = new LabelStyle(font, Color.WHITE);
        // Setup table for layout
        Table table = new Table();
        table.setFillParent(true);  // Table fills the entire stage
        table.center(); // Align items to the center

        // Create UI Components
        Label message = new Label("Game Complete", menuTextStyle); // Game completion message
        TextButton menuButton = new TextButton("Main menu", skin); // Button to return to main menu
        TextButton exitButton = new TextButton("Exit game", skin); // Button to exit the game

        // Add UI components to the table
        table.add(message);
        table.row(); // Start new row for the next component
        table.add(menuButton).padTop(20); // Add padding for spacing
        table.row(); // New row for the next component
        table.add(exitButton).padTop(10);

        // Set size for the message and buttons
        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;
        message.setSize(buttonWidth, buttonHeight);
        menuButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Add listeners for button actions
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, skin)); // Switch to the main menu screen
            }
        });
        
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application
            }
        });

        // Add the layout table to the stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Dispose of the resources, to prevent memory leaks
     */
    @Override
    public void dispose(){
        stage.dispose();
    }
}
