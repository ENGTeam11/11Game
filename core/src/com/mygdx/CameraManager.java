package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Manages the camera that the Gamescreen uses, facilitates the camera used, its following mechanics, the zooming mechanics and the boundary of the camera
 */
public class CameraManager {
    public int player_bound;
    public float zoom_mult;
    public Vector2 position;
    public OrthographicCamera camera;
    
    /**
     * Used to instantiate the camera object and give some initial values
     * @param player_pos the players initial position to focus on
     */ 
    public CameraManager(Vector2 player_pos){
        position = new Vector2(player_pos.x, player_pos.y);
        zoom_mult = 0.2f;
        player_bound = 400;
        camera = new OrthographicCamera(position.x, position.y); 
        camera.zoom = 0.65f;
    }

    /**
     * Calls the camera catchup and zoom functions to update the cameras positioning
     * @param delta the time since last render
     * @param player_pos the position of the player at the time
     */
    public void update(float delta, Vector2 player_pos){
        if (position.x != player_pos.x || position.y != player_pos.y){
            catchup(delta, player_pos);
            boundary_check(player_pos);
        }
        zoom(delta);
        camera.position.set(position.x, position.y, 0);
        
        camera.update();
    }

    /**
     * Makes sure that the camera stays within a set distance of the player to prevent them going off screen
     * @param player_pos position of the player
     */
    private void boundary_check(Vector2 player_pos){
        if (position.x > player_pos.x + player_bound){
            position.x = player_pos.x + player_bound;
        }
        else if (position.x < player_pos.x - player_bound){
            position.x = player_pos.x - player_bound;
        }

        if (position.y > player_pos.y + player_bound){
            position.y = player_pos.y + player_bound;
        }
        else if (position.y < player_pos.y - player_bound){
            position.y = player_pos.y - player_bound;
        }
    }

    
    /**
     * slowly moves the camera to be centered on the player
     * @param delta time passed since the last refresh
     * @param player_pos the player position at the time
     */
    private void catchup(float delta, Vector2 player_pos){
        double X_move = ((player_pos.x - position.x) * 2) * delta;
        double Y_move = ((player_pos.y - position.y) * 2) * delta;
        
        if (X_move > 0 && X_move < 0.01){
            X_move = 0.01;
        }
        else if (X_move < 0 && X_move > -0.01){
            X_move = -0.01;
        }

        if (Y_move > 0 && Y_move < 0.01){
            Y_move = 0.01;
        }
        if (Y_move < 0 && Y_move > -0.01){
            Y_move = -0.01;
        }
        
        
        float new_X = (float) (position.x + X_move);
        float new_Y = (float) (position.y + Y_move);

        if ((position.x < player_pos.x && new_X > player_pos.x) || (position.x > player_pos.x && new_X < player_pos.x)){
            position.x = player_pos.x;
        }
        else{
            position.x = new_X;
        }

        if ((position.y < player_pos.y && new_Y > player_pos.y) || (position.y > player_pos.y && new_Y < player_pos.y)){
            position.y = player_pos.y;
        }
        else{
            position.y = new_Y;
        }
    }

    /**
     * Allows the camera to be zoomed in or out
     * @param delta the time since the last refresh
     */
    private void zoom(float delta){
        if (Gdx.input.isKeyPressed(Keys.EQUALS)){
            camera.zoom -= camera.zoom*zoom_mult*delta;
        }
        else if (Gdx.input.isKeyPressed(Keys.MINUS)){
            camera.zoom += camera.zoom*zoom_mult*delta;
        }

    }
}