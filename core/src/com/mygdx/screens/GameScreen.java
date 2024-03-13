package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {
    private Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Character character;
    private Player player;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,600);

        Texture playerTexture = new Texture(Gdx.files.internal("LightBandit_Idle_0.png"));
        player = new Player(new Texture("LightBandit_Idle_0.png"), 400,300,2);
        player.create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.setProjectionMatrix(camera.combined);


        player.render(delta);

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.moveDown();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }

        // Stop the player if no keys are pressed
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.stopMovingUp();
            player.stopMovingDown();
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.stopMovingLeft();
            player.stopMovingRight();
        }
    }




//    private void handleInput() {
//        // Check for input and update player movement
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            player.moveUp();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            player.moveDown();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            player.moveLeft();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            player.moveRight();
//        } else {
//            // Stop player movement if no keys are pressed
//            player.stopMovingUp();
//            player.stopMovingDown();
//            player.stopMovingLeft();
//            player.stopMovingRight();
//        }
//    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        // Dispose other resources here
    }
}
