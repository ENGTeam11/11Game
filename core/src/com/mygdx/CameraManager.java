package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraManager {
    public int player_bound;
    public float zoom_mult;
    public Vector2 position;
    public OrthographicCamera camera;
    
    // Used to instantiate the camera object and give some initial values
    public CameraManager(Vector2 player_pos){
        position = new Vector2(player_pos.x, player_pos.y);
        zoom_mult = 0.2f;
        //player_bound = 400;
        camera = new OrthographicCamera(position.x, position.y); 
    }

    // Calls the camera catchup and zoom functions to update the cameras positioning
    public void update(float delta, Vector2 player_pos){
        if (position.x != player_pos.x || position.y != player_pos.y){
            catchup(delta, player_pos);
            boundary_check(player_pos);
        }
        zoom(delta);
        camera.position.set(position.x, position.y, 0);
        
        camera.update();
    }

    // Makes sure that the camera stays within a set distance of the player to prevent them going off screen
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

    //slowly moves the camera to be centered on the player
    private void catchup(float delta, Vector2 player_pos){
        double X_move = ((player_pos.y - position.x) * 0.2) * delta;
        double Y_move = ((player_pos.y - position.y) * 0.2) * delta;
        
        if (X_move > 0 && X_move < 1){
            X_move = 1;
        }
        else if (X_move < 0 && X_move > -1){
            X_move = -1;
        }

        if (Y_move > 0 && Y_move < 1){
            Y_move = 1;
        }
        if (Y_move < 0 && Y_move > -1){
            Y_move = -1;
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

    // Allows the camera to be zoomed in or out
    private void zoom(float delta){
        if (Gdx.input.isKeyPressed(Keys.EQUALS)){
            camera.zoom += camera.zoom*zoom_mult*delta;
        }
        else if (Gdx.input.isKeyPressed(Keys.MINUS)){
            camera.zoom -= camera.zoom*zoom_mult*delta;
        }

    }
}