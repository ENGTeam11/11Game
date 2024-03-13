package com.mygdx.pages;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.CameraManager;
import com.mygdx.Project;
import com.badlogic.gdx.Gdx;

public class GameScreen implements Screen{

    private Project game;
    private Stage stage;
    private Rectangle test;
    private CameraManager cameraHandler;
    Camera camera;
    public GameScreen(Project game){
        this.game = game;
    }

    @Override
    public void show() {
        //Set up stage for game screen
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        cameraHandler = new CameraManager(null);
        camera = cameraHandler.camera;

        test = new Rectangle(0,0,50,50);


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        test.x++;
        test.y++;
        cameraHandler.update(delta, null);
        game.batch.begin();
        game.batch.end();
        game.shape.begin(ShapeType.Line);
        game.shape.rect(test.x,test.y,test.width,test.height);
        game.shape.end();
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }

}