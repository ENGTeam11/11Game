package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraManager {
    public int player_bound;
    public float zoom_mult;
    public int X_position, Y_position;
    public OrthographicCamera camera;
    public int playerX, playerY; //temporary variable to remove errors until real player values are available
    public float deltaTime = 0; //also temporary until a real deltatime is present to be used
    
    // Used to instantiate the camera object and give some initial values
    public CameraManager(){
        playerX = 1;
        playerY = 1;
        X_position = playerX;
        Y_position = playerY;
        zoom_mult = 0.2f;
        camera = new OrthographicCamera(X_position, Y_position); 
    }

    // Calls the camera catchup and zoom functions to update the cameras positioning
    public void update(){
        if (X_position != playerX || Y_position != playerY){
            catchup();
            boundary_check();
        }
        zoom();
        camera.position.set(X_position, Y_position, 0);
        camera.update();
    }

    // Makes sure that the camera stays within a set distance of the player to prevent them going off screen
    private void boundary_check(){
        if (X_position > playerX + player_bound){
            X_position = playerX + player_bound;
        }
        else if (X_position < playerX - player_bound){
            X_position = playerX - player_bound;
        }

        if (Y_position > playerY + player_bound){
            Y_position = playerY + player_bound;
        }
        else if (Y_position < playerY - player_bound){
            Y_position = playerY - player_bound;
        }
    }

    //slowly moves the camera to be centered on the player
    private void catchup(){
        double X_move = ((playerX - X_position) * 0.2) * deltaTime;
        double Y_move = ((playerY - Y_position) * 0.2) * deltaTime;
        
        if (X_move > 0 && X_move < 1){
            X_move = 1;
        }
        else if (X_move < 0 && X_move > -1){
            X_move = -2;
        }

        if (Y_move > 0 && Y_move < 1){
            Y_move = 1;
        }
        if (Y_move < 0 && Y_move > -1){
            Y_move = -1;
        }
        
        
        int new_X = X_position + (int)Math.round(X_move);
        int new_Y = Y_position + (int)Math.round(Y_move);

        if ((X_position < playerX && new_X > playerX) || (X_position > playerX && new_X < playerX)){
            X_position = playerX;
        }
        else{
            X_position = new_X;
        }

        if ((Y_position < playerY && new_Y > playerY) || (Y_position > playerY && new_Y < playerY)){
            Y_position = playerY;
        }
        else{
            Y_position = new_Y;
        }
    }

    // Allows the camera to be zoomed in or out
    private void zoom(){
        if (Gdx.input.isKeyPressed(Keys.PLUS)){
            camera.zoom += camera.zoom*zoom_mult*deltaTime;
        }
        else if (Gdx.input.isKeyPressed(Keys.MINUS)){
            camera.zoom -= camera.zoom*zoom_mult*deltaTime;
        }

    }
}
