package com.mygdx.pages;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Project;

public class MainMenu implements Screen {
    private Project game;
    private Stage stage;
    private VerticalGroup verticalContainer;
    public MainMenu(Project game){
        this.game = game;
    }
    @Override
    public void show() {
        //Sets up stage for the screen
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Sets up the vertical container for the buttons
        verticalContainer = new VerticalGroup();
        verticalContainer.setSize(50, 200);
        verticalContainer.space(20);
        verticalContainer.setPosition(Gdx.graphics.getWidth()/2-verticalContainer.getWidth()/2, Gdx.graphics.getHeight()/2-verticalContainer.getHeight());
        stage.addActor(verticalContainer);

        //Sets style for the buttons below
        TextButtonStyle styleTemp = new TextButtonStyle();
        styleTemp.fontColor = Color.WHITE;
        styleTemp.font = game.font;
        
        //Play button initialization
        TextButton tempTxtButton = new TextButton("Play",styleTemp);
        tempTxtButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y){
                game.setScreen(new GameScreen(game));
            }
        });
        verticalContainer.addActor(tempTxtButton);

        //Exit button initialization
        tempTxtButton = new TextButton("Exit", styleTemp);
        tempTxtButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y){
                System.exit(0);
            }
        });
        verticalContainer.addActor(tempTxtButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}
