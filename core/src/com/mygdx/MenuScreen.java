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


public class MenuScreen implements Screen {
    //variables
    private Stage stage;
    private Game game;
    private Skin skin;

    public MenuScreen(Game game, Skin skin){
        this.game = game;
        this.skin = skin;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // defining the buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton settingButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //calculate size for buttons
        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        //setting button sizes
        startButton.setSize(buttonWidth, buttonHeight);
        settingButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        // add buttons in table with spacing
        table.add(startButton).padBottom(20).row(); // Add start button with bottom padding
        table.add(settingButton).padBottom(20).row();
        table.add(exitButton).padBottom(20).row(); // Add exit button with bottom padding

        //start button function
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences preferences = Gdx.app.getPreferences("game_prefs");
                preferences.clear();
                preferences.flush();
                game.setScreen(new GameScreen(game, skin));
            }
        });

        //exit button function
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
