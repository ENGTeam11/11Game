package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StatsTracker {
    public int hoursSlept, minutesSlept, hoursStudied, hoursRelaxed, mealsEaten;
    
    private BitmapFont font; // Font used to draw text
    private SpriteBatch batch;// Separate SpriteBatch for drawing text
    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();
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

    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}
