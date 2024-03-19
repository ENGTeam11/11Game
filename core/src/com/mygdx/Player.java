package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the player character in the game, managing its rendering, animations,
 * and movement.
 */
public class Player {
    // Animations frame rate
    private static final float FRAME_TIME = 1 / 5f; //5 fps

    // Players texture image
    private final Texture texture;

    // Time elapsed since the animation started playing
    private float elapsed_time;

    // Animations
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> runRight;
    private Animation<TextureRegion> runLeft;
    private Animation<TextureRegion> runUp;
    private Animation<TextureRegion> runDown;

    // SpriteBatch for rendering textures
    private SpriteBatch batch;

    // Current position of the player in the game world
    public Vector2 position;

    // Speed of players movement
    private float speed;

    // Reference to the game map to check for collisions etc
    private GameMap gameMap;

    // Dimensions of the character for collision and rendering etc
    private float character_width = 13;
    private float character_height = 19;



    // Movement booleans to determine the current directions of movement
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    /**
     * Initializes a new instance of the Player class with its starting position, speed, and game map context.
     *
     * @param texture The texture for the player's character.
     * @param x The initial X coordinate of the player.
     * @param y The initial Y coordinate of the player.
     * @param speed The movement speed of the player.
     * @param gameMap The game map the player interacts with.
     */
    public Player(Texture texture, float x, float y, float speed, GameMap gameMap){
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.gameMap = gameMap;
    }

    /**
     * Loads the animations for the player from a TextureAtlas.
     */
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

    /**
     * Renders the player at its current position, with the appropriate animation based on movement.
     *
     * @param delta Time elapsed since the last render call.
     * @param cam The game's camera for correct rendering based on the camera's projection matrix.
     */
    public void render(float delta, OrthographicCamera cam){
        elapsed_time += delta;
        float scale = 3f;


        //position debugging
        // System.out.println("Player position: " + position);

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


    /**
     * Disposes of the resources used by the player to free up memory.
     */
    public void dispose(){
        batch.dispose();
        texture.dispose();
    }

    /**
     * Returns the width of the player character.
     *
     * @return The character width.
     */
    public float getWidth() {
        return this.character_width;
    }

    /**
     * Returns the height of the player character.
     *
     * @return The character height.
     */
    public float getHeight() {
        return this.character_height;
    }

    /**
     * Moves the player upwards if no obstruction is detected.
     */
    public void moveUp(){
        Vector2 newPosition = new Vector2(position.x, position.y + speed);
        if (! gameMap.isInArea(newPosition, getWidth(), getHeight(), "collision_layer")) {
            position = newPosition;
            isMovingUp = true;
        }
    }

    /**
     * Moves the player downwards if no obstruction is detected.
     */
    public void moveDown() {
        Vector2 newPosition = new Vector2(position.x, position.y - speed);
        if (! gameMap.isInArea(newPosition, getWidth(), getHeight(), "collision_layer")) {
            position = newPosition;
            isMovingDown = true;
        }
    }

    /**
     * Moves the player to the left if no obstruction is detected.
     */
    public void moveLeft() {
        Vector2 newPosition = new Vector2(position.x - speed, position.y);
        if (! gameMap.isInArea(newPosition, getWidth(), getHeight(), "collision_layer")) {
            position = newPosition;
            isMovingLeft = true;
        }
    }

    /**
     * Moves the player to the right if no obstruction is detected.
     */
    public void moveRight() {
        Vector2 newPosition = new Vector2(position.x + speed, position.y);
        if (! gameMap.isInArea(newPosition, getWidth(), getHeight(), "collision_layer")) {
            position = newPosition;
            isMovingRight = true;
        }
    }


    /**
     * Stops the upward movement of the player.
     */
    public void stopMovingUp(){
        isMovingUp = false;
    }
    /**
     * Stops the downward movement of the player.
     */
    public void stopMovingDown (){
        isMovingDown = false;
    }
    /**
     * Stops the rightward movement of the player.
     */
    public void stopMovingRight(){
        isMovingRight = false;
    }
    /**
     * Stops the leftward movement of the player.
     */
    public void stopMovingLeft(){
        isMovingLeft = false;
    }

}