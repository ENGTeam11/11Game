package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class DayCycleManager {
    private int day;
    private int hour;
    private int minute;
    private final int maxDays = 7;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch dayBatch;
    private float timeAccumulator = 0;


    public DayCycleManager() {
        this.day = 1;
        this.hour = 8;
        this.minute = 0;
        
    }

    public void setup(){
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        shapeRenderer = new ShapeRenderer();
        dayBatch = new SpriteBatch();
    }

    public void update(float deltaTime){
        timeAccumulator += deltaTime;

        while (timeAccumulator >= 1) {
            minute++;
            timeAccumulator -= 1;

            if (minute >= 60) {
            minute = 0;
            hour++;
                if (hour >= 24) {
                hour = 0;
                nextDay();
                }
            }
        }
    }

    public void addTime(int hours) {
        // Time manipulation logic...
    }

    public void nextDay() {
        day++;
    }

    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public int getDay() {
        return day;
    }

    public void render() {
        float yStart = Gdx.graphics.getHeight() - 70; // Adjust based on your energy bar's position
        float width = 65; // Width of the box
        float height = 25; // Height of each box


        // Draw background shapes

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);

        // Day box
        shapeRenderer.rect(25, yStart, width, height);

        // Time box, positioned below the Day box
        shapeRenderer.rect(25, yStart - height - 5, width, height); // 5 pixels gap between boxes
        shapeRenderer.end();

        // Draw text
        dayBatch.begin(); // Begin SpriteBatch to draw text
        font.draw(dayBatch, "Day - " + getDay(), 30, yStart + height - 7); // Adjust text alignment as needed
        font.draw(dayBatch, getTime(), 30, yStart - 12); // Time text, adjusted for the gap
        dayBatch.end();
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
