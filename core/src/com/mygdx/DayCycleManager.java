package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Manages and displays an in-game day and time system. It provides functionality
 * to update time, advance days, and render the current day and time.
 */
public class DayCycleManager {
    private int day; // Current day in the game
    private int hour; // Current hour in the game
    private int minute; // Current minute in the game
    private final int maxDays = 7; // Maximum number of day until the game ends
    private BitmapFont font; // Font used to draw day and time text
    private ShapeRenderer shapeRenderer; // Used to draw background boxes for day and time
    private SpriteBatch dayBatch; // Separate SpriteBatch for drawing text
    private float timeAccumulator = 0; // Accumulates real-time seconds to update in-game time

    /**
     * Initialize the DayCycleManager with starting day set to day - 1 and time set to 8:00
     */
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
    /**
     * Updates the in-game time based on real-time seconds passed.
     * Increments in-game minutes accordingly, adjusting hours and days as needed
     * @param deltaTime The time in seconds that has passsed since the last frome
     */
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

    /**
     * Advances the in-game day counter by one, effectively skipping to the next day
     */
    public void nextDay() {
        day++;
    }

    /**
     * Returns a string representation of the current in-game time in "HH:MM" format
     * @return A string that represents the current in-game time.
     */
    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * @return The current day as an integer
     */
    public int getDay() {
        return day;
    }

    /**
     * Renders the current day and time onto the screen which includes drawing two background
     * boxes and the text inside them.
     */
    public void render() {
        float yStart = Gdx.graphics.getHeight() - 70;
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
        dayBatch.begin();
        font.draw(dayBatch, "Day - " + getDay(), 30, yStart + height - 7);
        font.draw(dayBatch, getTime(), 30, yStart - 12); // Time text, adjusted for the gap
        dayBatch.end();
    }

    /**
     * Dispose of the resources used by the class, to prevent memory leaks.
     */
    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
