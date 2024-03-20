package com.mygdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * a class that stores the statistics of all total activities done by the players, Including:
 * -hours studied
 * -hours relaxed
 * -time slept
 * -meals eaten
 */
public class StatsTracker {
    public int hoursSlept, minutesSlept, hoursStudied, hoursRelaxed, mealsEaten;
    
    private BitmapFont font; // Font used to draw text
    private SpriteBatch batch;// Separate SpriteBatch for drawing text
    private ShapeRenderer shapeRenderer;// used to create background shapes

    /**
     * Initialises the StatsTracker class, sets all attributes to 0
     */
    public StatsTracker(){
        hoursSlept = 0;
        minutesSlept = 0;
        hoursStudied = 0;
        hoursRelaxed = 0;
        mealsEaten = 0;
    }

    /**
     * sets up the trackers shape renderer, font and sprite batch to work with the current screen
     */
    public void setup(){
        font = new BitmapFont();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * increases the hours and minutes slept by the given values
     * @param hours the number of hours to add
     * @param minutes the number of minutes to add
     */
    public void addSleep(int hours, int minutes){
        hoursSlept += hours;
        minutesSlept += minutes;
        if (minutesSlept >= 60){ //makes it so that 60 minutes is converted into 1 hour
            hoursSlept += 1;
            minutesSlept -= 60;
        }
    }
    /**
     * increases hoursStudied attribute by given amount
     * @param hours the number of hours to increase by
     */
    public void addStudy(int hours){
        hoursStudied += hours;
    }
    /**
     * increases hoursRelaxed attribute by given amount
     * @param hours the number of hours to increase by
     */
    public void addRelax(int hours){
        hoursRelaxed += hours;
    }

    /**
     * increases mealsEaten attribute by 1
     */
    public void mealAte(){
        mealsEaten++;
    }

    /**
     * gives the hours the student has slept for
     * @return integer hoursSlept
     */
    public int getSleepHours(){
        return hoursSlept;
    }
    /**
     * gives the minutes the student has slept for
     * @return integer minutesSlept
     */
    public int getSleepMinutes(){
        return minutesSlept;
    }
    /**
     * gives the hours the student has studied for
     * @return integer hoursStudied
     */
    public int getStudy(){
        return hoursStudied;
    }
    /**
     * gives the hours the student has relaxed for
     * @return integer hoursRelaxed
     */
    public int getRelax(){
        return hoursRelaxed;
    }
    /**
     * gives the number of meals the student has eaten
     * @return integer mealsEaten
     */
    public int getMeals(){
        return mealsEaten;
    }

    /**
     * renders the statistics of the players interactions in the bottom left of the screen with a background
     */
    public void render(){
        float height = 90;
        float width = 125;
        float x = 30;
        float y = 30;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x - 5, y - 20, width + 5, height);
        shapeRenderer.end();
        batch.begin();
        font.draw(batch, "Time slept: "+String.format("%02d:%02d", getSleepHours(), getSleepMinutes()), x, y);
        font.draw(batch, "Hours studying: "+getStudy(), x, y + 20);
        font.draw(batch, "Hours relaxing: "+getRelax(), x, y + 40);
        font.draw(batch, "Meals eaten: "+getMeals(), x, y + 60);
        batch.end();
    }

    /**
     * Dispose of the resources used by the class, to prevent memory leaks.
     */
    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}
