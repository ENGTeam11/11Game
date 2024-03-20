package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

/**
 * A class to manage the energy needed in the game, 
 * facilitates the rendering of the bar, storing the energy, and energy loss functions
 */
public class EnergyMeter {
    private int energy, max_energy, width;
    private Rectangle filled_bar, empty_bar;
    private ShapeRenderer shape;
    private SpriteBatch batch;
    private BitmapFont font;

    

    public EnergyMeter() {
        energy = 100;
        max_energy = 100;
        width = 200;
        filled_bar = new Rectangle(25, Gdx.graphics.getHeight()-35, width, 15);
        empty_bar = new Rectangle(25, Gdx.graphics.getHeight()-35, width, 15);

        
    }

    public void setup () {
        shape = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    /**
     * draws the energy bar
     */
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

        batch.begin();
        font.draw(batch, "Energy: "+energy+"/"+max_energy, 25, Gdx.graphics.getHeight()-5);
        batch.end();
    }

    public int getEnergy(){
        return energy;
    }

    public void resetEnergy(){
        energy = max_energy;
    }

    /**
     * used to subtract the energy required for an activity
     * @param cost the amount of energy to subtract
     */
    public void loseEnergy(int cost){
        energy -= cost;
    }

    /**
     * calculates the portion of the bar that should be filled
     */
    private Color calculateBar(){
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

    /**
     * makes sure the meter is in the correct position 
     */
    private void reposition(){
        filled_bar.setY(Gdx.graphics.getHeight()-35);
        empty_bar.setY(Gdx.graphics.getHeight()-35);
    }

    public void dispose() {
        font.dispose();
        shape.dispose();
        batch.dispose();
    }
}
