package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class EnergyMeter {
    private int energy, max_energy, width;
    private Rectangle filled_bar, empty_bar;
    ShapeRenderer shape;

    public EnergyMeter() {
        energy = 100;
        max_energy = 100;
        width = 200;
        filled_bar = new Rectangle(25, Gdx.graphics.getHeight()-35, width, 15);
        empty_bar = new Rectangle(25, Gdx.graphics.getHeight()-35, width, 15);

        shape = new ShapeRenderer();
    }

    public void render(){
        reposition();

        shape.begin(ShapeType.Filled);
        shape.setColor(Color.GRAY);
        shape.rect(empty_bar.x, empty_bar.y, empty_bar.width, empty_bar.height);
        shape.end();

        Color colour = calculateBar();

        shape.begin(ShapeType.Filled);
        shape.setColor(colour);
        shape.rect(filled_bar.x, filled_bar.y, filled_bar.width, filled_bar.height);
        shape.end();

        shape.begin(ShapeType.Line);
        shape.setColor(Color.GRAY);
        shape.rect(empty_bar.x, empty_bar.y, empty_bar.width, empty_bar.height);
        shape.end();
    }

    public int getEnergy(){
        return energy;
    }

    public void loseEnergy(int cost){
        //used to subtract the energy required for an activity
        energy -= cost;
    }

    private Color calculateBar(){
        // calculates the portion of the bar that should be filled
        Color colour;
        float multiple = (float) energy / max_energy;
        if (multiple <= 0.25){
            colour = Color.RED;
        }
        else {
            colour = Color.YELLOW;
        }

        float new_width = (width * multiple);
        filled_bar.setWidth(new_width);

        return colour;
    }


    private void reposition(){
        //makes sure the meter is in the correct position 
        filled_bar.setY(Gdx.graphics.getHeight()-35);
        empty_bar.setY(Gdx.graphics.getHeight()-35);
    }
}
