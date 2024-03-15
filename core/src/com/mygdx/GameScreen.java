package com.mygdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreen extends ScreenAdapter {
    private Game game;
    private Skin skin;
    protected CameraManager cameraHandler;
    protected OrthographicCamera camera;
    private SpriteBatch batch;
    private Character character;
    private Player player;
    private Vector2 characterPosition;
    private Preferences prefs;
    private GameMap gameMap;

    public GameScreen(Game game, Skin skin) {
        this.game = game;
        this.skin = skin;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        prefs = Gdx.app.getPreferences("game_prefs");

        gameMap = new GameMap("maps/map.tmx");

        Vector2 spawnPoint = gameMap.getSpawnPoint();
        float characterX = prefs.getFloat("characterX", spawnPoint.x);
        float characterY = prefs.getFloat("characterY", spawnPoint.y);
        Vector2 characterPosition = new Vector2(characterX, characterY);

        cameraHandler = new CameraManager(characterPosition);
        camera = cameraHandler.camera;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player = new Player(new Texture("down_idle_1.png"), characterX, characterY, 2, gameMap);
        player.create();



    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            saveCharacterPosition();
            game.setScreen(new inGameMenu(game, skin));
        }
        handleInput();

        gameMap.update(delta);

        Vector2 characterPosition = getCharacterPosition();
        cameraHandler.update(delta, characterPosition);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameMap.render(camera);
        player.render(delta, camera);



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

    public Vector2 getCharacterPosition(){
        characterPosition = new Vector2(player.position.x, player.position.y);
        return characterPosition;
    }
    @Override
    public void pause(){
        System.out.println("GameScreen Pausing game, saving character position: " + player.position);
        prefs.putFloat("characterX", player.position.x);
        prefs.putFloat("characterY", player.position.y);
        prefs.flush();
    }

    private void saveCharacterPosition(){
        prefs.putFloat("characterX", player.position.x);
        prefs.putFloat("characterY", player.position.y);
        prefs.flush();
        System.out.println("Saving character position: " + player.position);

    }

    @Override
    public void resume(){
        float characterX = prefs.getFloat("characterX", 400);
        float characterY = prefs.getFloat("characterY", 300);
        player.position.set(characterX, characterY);
        System.out.println("GameScreen Resuming game, loading character position: " + player.position);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        gameMap.dispose();
        // Dispose other resources here
    }
}