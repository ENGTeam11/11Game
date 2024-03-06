package com.mygdx.pages;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Project;

public class MainMenu implements Screen {
    private Project game;
    private Stage stage;
    public MainMenu(Project game){
        this.game = game;
    }
    @Override
    public void show() {
        //Sets up stage for the buttons
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Sets style for the buttons below
        TextButtonStyle styleTemp = new TextButtonStyle();
        styleTemp.fontColor = Color.WHITE;
        styleTemp.font = game.font;
        //Buttons Added to stage
        TextButton tempTxtButton = new TextButton("Play",styleTemp);
        stage.addActor(tempTxtButton);
        tempTxtButton = new TextButton("Settings", styleTemp);
        stage.addActor(tempTxtButton);
        tempTxtButton = new TextButton("Exit", styleTemp);
        stage.addActor(tempTxtButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'resize'");
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }
    
}
