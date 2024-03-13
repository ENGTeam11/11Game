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

public class inGameMenu extends ScreenAdapter {
    private Stage stage;
    private Game game;
    private Skin skin;
    private SelectBox<String> resolutionSelectBox;

    public inGameMenu(Game game, Skin skin){
        this.game = game;
        this.skin = skin;
    }

    @Override
    public void show(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton resumeButton =  new TextButton("Resume", skin);
        table.add(resumeButton).padBottom(50).row();

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


        resumeButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               game.setScreen(new GameScreen(game, skin));
               dispose();
           }
        });

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

        quitButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               Gdx.app.exit();
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


        game.setScreen(new inGameMenu(game, skin));
    }

    private void toggleFullscreen(){
        if(Gdx.graphics.isFullscreen()){
            Gdx.graphics.setWindowedMode(800,600);
        }
        else{
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        game.setScreen(new inGameMenu(game, skin));
    }


    @Override
    public void render(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(new GameScreen(game, skin));
        }
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
}
