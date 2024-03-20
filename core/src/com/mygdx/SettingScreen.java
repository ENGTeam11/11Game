package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Represents the settings screen in the game, allowing the user to change game settings such
 * as screen resolution and toggle fullscreen mode
 */
public class SettingScreen extends ScreenAdapter {
    private Stage stage; //  The stage for managing and displaying UI components
    private Game game; // The main game object for changing screen
    private Skin skin; // The skin for styling
    private SelectBox<String> resolutionSelectBox; // Dropdown for selecting screen resolutions

    /**
     * Constructs the settings screen with references to the main game object and skin
     * @param game The game object to facilitates screen changes
     * @param skin The skin for styling
     */
    public SettingScreen(Game game, Skin skin) {
        this.game = game;
        this.skin = skin;
    }
    @Override
    public void show() {
        // Initialize the stage and set it to process user input
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create a layout table for organizing UI components
        Table table = new Table();
        table.setFillParent(true); // Make the table fill the screen
        stage.addActor(table); // Add the table to the stage

        // Populate resolution choices for the select box.
        Array<String> resolutions = new Array<>();
        resolutions.add("800x600");
        resolutions.add("1024x768");
        resolutions.add("1280x720");
        resolutions.add("1920x1080");
        resolutions.add("2560x1440");
        resolutions.add("3840x2160");

        // Initialize the resolution box and set its items
        resolutionSelectBox = new SelectBox<>(skin);
        resolutionSelectBox.setItems(resolutions);
        table.add(resolutionSelectBox).padBottom(20).row();

        // Create buttons for applying settings and toggling fullscreen
        TextButton applyButton = new TextButton("Apply", skin);
        table.add(applyButton).padBottom(20).row();
        TextButton fullscreen = new TextButton("Fullscreen", skin);
        table.add(fullscreen).padBottom(20).row();

        // Add listeners to buttons for handling user interactions
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyResolution(); // Apply the selected resolution
            }
        });

        fullscreen.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               toggleFullscreen(); // Toggle fullscreen mode
           }
        });
    }

    /**
     * Applies the selected screen resolution from the resolutionSelectBox
     */
    private void applyResolution() {
        String selectedResolution = resolutionSelectBox.getSelected();

        String[] resolutionParts = selectedResolution.split("x");
                  int width = Integer.parseInt(resolutionParts[0]);
                  int height = Integer.parseInt(resolutionParts[1]);
                  Gdx.graphics.setWindowedMode(width, height); // Set the screen resolution

        System.out.println("resolution is now: " + selectedResolution);


        game.setScreen(new MenuScreen(game, skin)); // Return to the menu screen.
    }

    /**
     * Toggles the screen between fullscreen and windowed mode
     */
    private void toggleFullscreen(){
        if(Gdx.graphics.isFullscreen()){
            Gdx.graphics.setWindowedMode(800,600); // Set to windowed mode with default size
        }
        else{
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // Set to fullscreen
        }
        game.setScreen(new MenuScreen(game, skin)); // Return to the menu screen
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a solid color
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Adjust the stage's viewport when the screen size changes
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    /**
     * Dispose of the resources used, to prevent memory leaks
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
