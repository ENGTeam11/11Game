package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private Vector2 position;
    private float speed;

    //movement variables
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    // creating players starting variables used in gamescreen class
    public Player(Texture texture, float x, float y, float speed){
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.speed = speed;
    }

    // initialising animations of character within create method
    public void create(){
        TextureAtlas allAnimations = new TextureAtlas(Gdx.files.internal("allAnimationsAtlas.atlas"));

        //idle TextureAtlas
        idle = new Animation<>(FRAME_TIME, allAnimations.findRegions("left_idle"));
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


    public void render(float delta){
        elapsed_time += delta;

        //position debugging
        System.out.println("Player position: " + position);

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


        batch.begin();
        // Change 0.7f to what scale you like
        batch.draw(currentFrame, position.x, position.y, currentFrame.getRegionWidth() / 0.7f, currentFrame.getRegionHeight() / 0.7f);
        batch.end();
    }



    public void dispose(){
        batch.dispose();
        texture.dispose();
    }

    //moving boolean statements for animations and the position calculations
    public void moveUp(){
        position.y += speed;
        isMovingUp = true;
    }

    public void moveDown(){
        position.y -= speed;
        isMovingDown = true;
    }

    public void moveLeft(){
        position.x -= speed;
        isMovingLeft = true;
    }

    public void moveRight(){
        position.x += speed;
        isMovingRight = true;
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