package com.mygdx;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraManager {
    public int player_bound;
    public float zoom;
    public int X_position, Y_position;
    public OrthographicCamera camera;
    
    public void setup(){
        //X_position = player.X
        //Y_position = player.Y
        zoom = 1;
        camera = new OrthographicCamera(X_position, Y_position); 
    }

    public void update(){
        //camera.position.set(player.X, player.Y, 0)
        camera.update();

    }

}
