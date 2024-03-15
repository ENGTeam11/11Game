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

public class SettingScreen extends ScreenAdapter {
    private Stage stage;
    private Game game;
    private Skin skin;
    private SelectBox<String> resolutionSelectBox;

    public SettingScreen(Game game, Skin skin) {
        this.game = game;
        this.skin = skin;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

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

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyResolution();
            }
        });

        fullscreen.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               toggleFullscreen();
           }
        });
    }

    private void applyResolution() {
        String selectedResolution = resolutionSelectBox.getSelected();

        String[] resolutionParts = selectedResolution.split("x");
                  int width = Integer.parseInt(resolutionParts[0]);
                  int height = Integer.parseInt(resolutionParts[1]);
                  Gdx.graphics.setWindowedMode(width, height);

        System.out.println("resolution is now: " + selectedResolution);


        game.setScreen(new MenuScreen(game, skin));
    }

    private void toggleFullscreen(){
        if(Gdx.graphics.isFullscreen()){
            Gdx.graphics.setWindowedMode(800,600);
        }
        else{
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        game.setScreen(new MenuScreen(game, skin));
    }

    @Override
    public void render(float delta) {
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
