package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import sun.tools.jconsole.Tab;

/**
 * Represents the main menu screen of the game, providing options to start the game,
 * access settings, or exit the application.
 */
public class MenuScreen implements Screen {
    //variables
    private Stage stage; // The stage that holds and manages UI elements
    private Game game; // Reference to the maain game object to allow screen switching
    private Skin skin; // The skin used for styling UI components

    /**
     * Constructs the menu screen with the game context and skin for UI styling
     * @param game The main game object for managing screens
     * @param skin The skin for styling
     */
    public MenuScreen(Game game, Skin skin){
        this.game = game;
        this.skin = skin;
    }

    @Override
    public void show() {
        // Create and configure the stage with a viewport matching the screen size
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage); // Set the stage to process UI input events

        // Create a layout table and set it to fill the stage
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Defining the buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton settingButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Calculate size for buttons
        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        // Setting button sizes
        startButton.setSize(buttonWidth, buttonHeight);
        settingButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // Add buttons in table with appropriate spacing
        table.add(startButton).padBottom(20).row(); // Add start button with bottom padding
        table.add(settingButton).padBottom(20).row();
        table.add(exitButton).padBottom(20).row(); // Add exit button with bottom padding

        // Start button: Resets game preferences and switches to game screen
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences preferences = Gdx.app.getPreferences("game_prefs");
                preferences.clear();
                preferences.flush();
                game.setScreen(new GameScreen(game, skin));
            }
        });

        // Exit button: Closes the game
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Settings button: Switches to the settings screen
        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingScreen(game, skin));
            }
        });


        stage.addActor(table);
    }



    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    /**
     * Dispose of the resources used, to prevent memory leaks
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
