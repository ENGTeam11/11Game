package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player {
    //variables
    private static final float FRAME_TIME = 1 / 5f; //5 fps
    private final Texture texture;
    private float elapsed_time;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> runRight;
    private Animation<TextureRegion> runLeft;
    private Animation<TextureRegion> runUp;
    private Animation<TextureRegion> runDown;
    private SpriteBatch batch;
    public Vector2 position;
    private float speed;
    private GameMap gameMap;
    private float character_width = 13;
    private float character_height = 19;
    //


    //movement variables
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    // creating players starting variables used in gamescreen class
    public Player(Texture texture, float x, float y, float speed, GameMap gameMap){
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.gameMap = gameMap;

    }

    // initialising animations of character within create method
    public void create(){
        TextureAtlas allAnimations = new TextureAtlas(Gdx.files.internal("allAnimationsAtlas.atlas"));

        //idle TextureAtlas
        idle = new Animation<>(FRAME_TIME, allAnimations.findRegions("down_idle"));
        idle.setPlayMode(Animation.PlayMode.LOOP);

        //run right TextureAtlas
        runRight = new Animation<>(FRAME_TIME, allAnimations.findRegions("right_walk"));
        runRight.setPlayMode(Animation.PlayMode.LOOP);

        //run left animation
        runLeft = new Animation<>(FRAME_TIME, allAnimations.findRegions("left_walk"));
        runLeft.setPlayMode(Animation.PlayMode.LOOP);

        //run up animation
        runUp = new Animation<>(FRAME_TIME, allAnimations.findRegions("up_walk"));
        runUp.setPlayMode(Animation.PlayMode.LOOP);

        //run down animation
        runDown = new Animation<>(FRAME_TIME, allAnimations.findRegions("down_walk"));
        runDown.setPlayMode(Animation.PlayMode.LOOP);


        batch = new SpriteBatch();

    }


    public void render(float delta, OrthographicCamera cam){
        elapsed_time += delta;
        float scale = 3f;


        //position debugging
//        System.out.println("Player position: " + position);

        // setting animation booleans to match movement
        Animation<TextureRegion> animation = idle;
        if (isMovingRight){
            animation = runRight;
        }
        if (isMovingLeft){
            animation = runLeft;
        }
        if (isMovingDown){
            animation = runDown;
        }
        if (isMovingUp){
            animation = runUp;
        }
        // looping animation
        TextureRegion currentFrame = animation.getKeyFrame(elapsed_time, true);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(currentFrame, position.x,position.y, currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        batch.end();
    }



    public void dispose(){
        batch.dispose();
        texture.dispose();
    }
    public float getWidth() {
        return this.character_width;
    }
    public float getHeight() {
        return this.character_height;
    }
    //moving boolean statements for animations and the position calculations
    public void moveUp(){
        Vector2 newPosition = new Vector2(position.x, position.y + speed);
        if (gameMap.canPlayerMove(newPosition, getWidth(), getHeight())) {
            position = newPosition;
            isMovingUp = true;
        }
    }

    public void moveDown() {
        Vector2 newPosition = new Vector2(position.x, position.y - speed);
        if (gameMap.canPlayerMove(newPosition, getWidth(), getHeight())) {
            position = newPosition;
            isMovingDown = true;
        }
    }

    public void moveLeft() {
        Vector2 newPosition = new Vector2(position.x - speed, position.y);
        if (gameMap.canPlayerMove(newPosition, getWidth(), getHeight())) {
            position = newPosition;
            isMovingLeft = true;
        }
    }

    public void moveRight() {
        Vector2 newPosition = new Vector2(position.x + speed, position.y);
        if (gameMap.canPlayerMove(newPosition, getWidth(), getHeight())) {
            position = newPosition;
            isMovingRight = true;
        }
    }


    // resetting animation booleans when movement has stopped etc
    public void stopMovingUp(){
        isMovingUp = false;
    }
    public void stopMovingDown (){
        isMovingDown = false;
    }

    public void stopMovingRight(){
        isMovingRight = false;
    }

    public void stopMovingLeft(){
        isMovingLeft = false;
    }

}