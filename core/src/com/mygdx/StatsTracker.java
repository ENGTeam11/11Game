package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StatsTracker {
    public int hoursSlept, minutesSlept, hoursStudied, hoursRelaxed, mealsEaten;
    
    private BitmapFont font; // Font used to draw text
    private SpriteBatch batch; // Separate SpriteBatch for drawing text

    public StatsTracker(){
        hoursSlept = 0;
        minutesSlept = 0;
        hoursStudied = 0;
        hoursRelaxed = 0;
        mealsEaten = 0;
    }

    public void setup(){
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    public void addSleep(int hours, int minutes){
        hoursSlept += hours;
        minutesSlept += minutes;
        if (minutesSlept >= 60){
            hoursSlept += 1;
            minutesSlept -= 60;
        }

    }
    public void addStudy(int hours){
        hoursStudied += hours;
    }
    public void addRelax(int hours){
        hoursRelaxed += hours;
    }
    public void mealAte(){
        mealsEaten++;
    }

    public int getSleepHours(){
        return hoursSlept;
    }
    public int getSleepMinutes(){
        return minutesSlept;
    }
    public int getStudy(){
        return hoursStudied;
    }
    public int getRelax(){
        return hoursRelaxed;
    }
    public int getMeals(){
        return mealsEaten;
    }

    public void render(){
        float width = 125;
        float x = Gdx.graphics.getWidth() - width;

        batch.begin();
        font.draw(batch, "Time slept: "+String.format("%02d:%02d", getSleepHours(), getSleepMinutes()), x, 15);
        font.draw(batch, "Hours studying: "+getStudy(), x, 35);
        font.draw(batch, "Hours relaxing: "+getRelax(), x, 55);
        font.draw(batch, "Meals eaten: "+getMeals(), x, 75);
        batch.end();
    }

    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}
