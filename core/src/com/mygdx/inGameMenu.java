package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Represents the in-game menu (pause menu) screen, providing options like resume, change resolutions,
 * toggle fullscreen, and quit game
 */
public class inGameMenu extends ScreenAdapter {
    private Stage stage; // The stage for managing actors, handling UI components.
    private Game game; // Reference to the main game objects to switch screen.
    private Skin skin; // The skip for styling UI components
    private GameScreen prevScreen; // Reference to the previour screen to return after resume
    private SelectBox<String> resolutionSelectBox; // Dropdown for selecting screen resolutions

    /**
     * Constrcuts the in-game menu
     * @param game Reference to the main game object for screen management
     * @param skin UI for styling
     * @param prevScreen Reference to the previously active screen for returning on resume
     */
    public inGameMenu(Game game, Skin skin, GameScreen prevScreen){
        this.game = game;
        this.skin = skin;
        this.prevScreen = prevScreen;
    }

    @Override
    public void show(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Set the stage to process UI input

        Table table = new Table();
        table.setFillParent(true); // Make the table fill the screen
        stage.addActor(table); // Add the table to the stage

        // Create UI components adn add them to the table
        TextButton resumeButton =  new TextButton("Resume", skin);
        table.add(resumeButton).padBottom(50).row();

        // Populate resolution choices
        Array<String> resolutions = new Array<>();
        resolutions.add("800x600");
        resolutions.add("1024x768");
        resolutions.add("1280x720");
        resolutions.add("1920x1080");
        resolutions.add("2560x1440");
        resolutions.add("3840x2160");

        resolutionSelectBox = new SelectBox<>(skin);
        resolutionSelectBox.setItems(resolutions);
        table.add(resolutionSelectBox).padBottom(20).row();



        TextButton applyButton = new TextButton("Apply", skin);
        table.add(applyButton).padBottom(20).row();

        TextButton fullscreen = new TextButton("Fullscreen", skin);
        table.add(fullscreen).padBottom(20).row();

        TextButton quitButton = new TextButton("Quit Game", skin);
        table.add(quitButton).padBottom(20).row();

        // Add listeners to buttons for functionality
        resumeButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               game.setScreen(prevScreen); //Return to the previous screen
               dispose(); // Dispose of this screen's resources
           }
        });

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

        quitButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               Gdx.app.exit(); // Exit the application
           }
        });
    }

    /**
     * Applies the selected resolution from the resolutionSelectBox
     */
    private void applyResolution() {
        String selectedResolution = resolutionSelectBox.getSelected();

        String[] resolutionParts = selectedResolution.split("x");
        int width = Integer.parseInt(resolutionParts[0]);
        int height = Integer.parseInt(resolutionParts[1]);
        Gdx.graphics.setWindowedMode(width, height);

        System.out.println("resolution is now: " + selectedResolution);


        game.setScreen(new inGameMenu(game, skin, prevScreen)); // Refresh the screen to apply resolution.
    }

    /**
     * Toggles between fullscreen and windowed mode
     */
    private void toggleFullscreen(){
        if(Gdx.graphics.isFullscreen()){
            Gdx.graphics.setWindowedMode(800,600); // Return to default windows resolution
        }
        else{
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // Set to fullscreen
        }
        game.setScreen(new inGameMenu(game, skin, prevScreen)); // Refresh the screen to apply mode change
    }


    @Override
    public void render(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(prevScreen); // Return to the previous screen on ESC press
        }
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1); // Set background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw(); // Draw the stage
    }
}
