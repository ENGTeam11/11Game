package com.mygdx;

import java.lang.reflect.Array;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends ScreenAdapter {
    private Game game;
    private Skin skin;
    BitmapFont font;

    private LabelStyle menuTextStyle;
    private Table studyTable;
    private Table relaxTable;
    private Table sleepTable;
    private Table eatTable;
    private Table insufficientTable;
    private Stage stage;
    private Table currentTable;

    protected CameraManager cameraHandler;
    protected OrthographicCamera camera;

    private SpriteBatch batch;
    private Character character;

    private Player player;
    private Vector2 characterPosition;

    public Preferences prefs;
    private GameMap gameMap;

    private EnergyMeter energyMeter;
    private DayCycleManager dayCycleManager;

    public GameScreen(Game game, Skin skin) {
        this.game = game;
        this.skin = skin;

        font = new BitmapFont();

        prefs = Gdx.app.getPreferences("game_prefs");

        //create all the interaction menus and the stage they use to display
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        menuTextStyle = new LabelStyle(font, Color.WHITE); 
        createStudy();
        createRelax();
        createSleep();
        createEat();
        createInsufficient();
        currentTable = null;

        dayCycleManager = new DayCycleManager();
        gameMap = new GameMap("maps/map.tmx");

        Vector2 spawnPoint = gameMap.getSpawnPoint();
        float characterX = prefs.getFloat("characterX", spawnPoint.x);
        float characterY = prefs.getFloat("characterY", spawnPoint.y);
        Vector2 characterPosition = new Vector2(characterX, characterY);

        player = new Player(new Texture("down_idle_1.png"), characterPosition.x, characterPosition.y, 2, gameMap);
        player.create();

        energyMeter = new EnergyMeter();

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        
        characterPosition = getCharacterPosition();
        
        cameraHandler = new CameraManager(characterPosition);
        camera = cameraHandler.camera;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            saveCharacterPosition();
            game.setScreen(new inGameMenu(game, skin, this));
        }
        handleInput();

        gameMap.update(delta);
        dayCycleManager.update(delta);

        Vector2 characterPosition = getCharacterPosition();
        
        gameMap.insideCheck(characterPosition, player.getWidth(), player.getHeight());
        gameMap.insideCheck(characterPosition, player.getWidth(), player.getHeight());

        checkInteract();
        
        cameraHandler.update(delta, characterPosition);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameMap.render(camera);
        player.render(delta, camera);
        energyMeter.render();
        dayCycleManager.render();

        batch.begin();
        if (currentTable != null){
            font.draw(batch, "press ENTER \n" + 
                                " to interact", characterPosition.x+20, characterPosition.y - 20);
        }
        batch.end();

        stage.act(delta);
	    stage.draw();



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

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && stage.getActors().size == 0 && currentTable!= null){
            stage.addActor(currentTable);
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
        dayCycleManager.dispose();
        // Dispose other resources here
    }

    /**
     * checks if the player is within any of the interactable regions and sets the currentTable according to which area they are in
     */
    private void checkInteract(){
        if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "study_interact")){
            currentTable = studyTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "relax_interact")){
            currentTable = relaxTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "eat_interact")){
            currentTable = eatTable;
        }
        else if(gameMap.isInArea(characterPosition, player.getWidth(), player.getHeight(), "sleep_interact")){
            currentTable = sleepTable;
        }
        else {
            currentTable = null;
        }

    }


    /**
     * creates the menu for studying
     */
    private void createStudy(){
        studyTable = new Table();
        Label studyTitle = new Label("Study for how many hours?", menuTextStyle);
        Slider studyTime = new Slider(1, 5, 1, false ,skin);
        TextButton studyConfirm = new TextButton("Confirm", skin);
        TextButton studyCancel = new TextButton("Cancel", skin);
        Label studyVal = new Label(Float.toString(studyTime.getValue()), menuTextStyle);
        Label studyCost = new Label("Cost: " + Float.toString(studyTime.getValue() * 15), menuTextStyle);

        studyTable.add(studyTitle);
        studyTable.row();
        studyTable.add(studyTime);
        studyTable.add(studyVal);
        studyTable.row();
        studyTable.add(studyCost);
        studyTable.row();
        studyTable.add(studyConfirm);
        studyTable.add(studyCancel);
        studyTable.setFillParent(true);
        studyTable.center();

        studyCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                studyTable.remove();
            }
        });

        studyConfirm.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int cost = (int) (15 * studyTime.getValue());
                if(cost <= energyMeter.getEnergy()){
                    energyMeter.loseEnergy(cost);
                studyTable.remove();
                }
                else{
                    stage.addActor(insufficientTable);
                    studyTable.remove();
                }
            }
        });

        studyTime.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                studyVal.setText(Float.toString(studyTime.getValue()));
                studyCost.setText("Cost: " + Float.toString(studyTime.getValue() * 15));
            }
        });
     
    }

    /**
     * creates the menu for recreational interactions
     */
    private void createRelax(){
        relaxTable = new Table();
        Label relaxTitle = new Label("relax for how many hours?", menuTextStyle);
        Slider relaxTime = new Slider(1, 5, 1, false ,skin);
        TextButton relaxConfirm = new TextButton("Confirm", skin);
        TextButton relaxCancel = new TextButton("Cancel", skin);
        Label relaxVal = new Label(Float.toString(relaxTime.getValue()), menuTextStyle);
        Label relaxCost = new Label("Cost: " + Float.toString(relaxTime.getValue() * 7), menuTextStyle);

        relaxTable.add(relaxTitle);
        relaxTable.row();
        relaxTable.add(relaxTime);
        relaxTable.add(relaxVal);
        relaxTable.row();
        relaxTable.add(relaxCost);
        relaxTable.row();
        relaxTable.add(relaxConfirm);
        relaxTable.add(relaxCancel);
        relaxTable.setFillParent(true);
        relaxTable.center();

        relaxCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                relaxTable.remove();
            }
        });

        relaxConfirm.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int cost = (int) (7 * relaxTime.getValue());
                if(cost <= energyMeter.getEnergy()){
                    energyMeter.loseEnergy(cost);
                    relaxTable.remove();
                }
                else{
                    stage.addActor(insufficientTable);
                    relaxTable.remove();
                }
            }
        });

        relaxTime.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                relaxVal.setText(Float.toString(relaxTime.getValue()));
                relaxCost.setText("Cost: " + Float.toString(relaxTime.getValue() * 7));
            }
        });
     
    }

    /**
     * creates the menu for sleeping
     */
    private void createSleep(){
        
        sleepTable = new Table();
        Label sleepTitle = new Label("Sleep until next day?", menuTextStyle);
        TextButton sleepYes = new TextButton("Yes", skin);
        TextButton sleepNo = new TextButton("No", skin);
        
        sleepTable.add(sleepTitle);
        sleepTable.row();
        sleepTable.add(sleepYes);
        sleepTable.add(sleepNo);
        sleepTable.setFillParent(true);
        sleepTable.center();

        sleepYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // code to advance day and set time back to start
                sleepTable.remove();
            } 
        });

        sleepNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTable.remove();
            } 
        });
    }

    /**
     * creates the menu for the eating interaction
     */
    private void createEat(){
        eatTable = new Table();
        Label eatTitle = new Label("Eat?", menuTextStyle);
        TextButton eatYes = new TextButton("Yes", skin);
        TextButton eatNo = new TextButton("No", skin);

        eatTable.add(eatTitle);
        eatTable.row();
        eatTable.add(eatYes);
        eatTable.add(eatNo);
        eatTable.setFillParent(true);
        eatTable.center();

        eatYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // code to advance time and increase eat count
                eatTable.remove();
            } 
        });

        eatNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                eatTable.remove();
            } 
        });
    }

    /**
     * creates the popup for when there isnt enough energy for an activity
     */
    private void createInsufficient(){
        insufficientTable = new Table();
        Label insufficientTitle = new Label("Not enough energy", menuTextStyle);
        TextButton insufficientOk = new TextButton("Ok", skin);

        insufficientTable.add(insufficientTitle);
        insufficientTable.row();
        insufficientTable.add(insufficientOk);
        insufficientTable.setFillParent(true);
        insufficientTable.center();
        
        insufficientOk.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                insufficientTable.remove();
            } 
        });
    }
}