package com.mygdx;

public class StatsTracker {
    public int hoursSlept, minutesSlept, hoursStudied, hoursRelaxed, mealsEaten;
    
    private BitmapFont font; // Font used to draw text
    private ShapeRenderer shape; // Used to draw background boxes for text
    private SpriteBatch batch; // Separate SpriteBatch for drawing text

    public StatsTracker(){
        hoursSlept = 0;
        minutesSlept = 0;
        hoursStudied = 0;
        hoursRelaxed = 0;
        mealsEaten = 0;
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

    public int getSleep(){
        return hoursSlept;
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

    }
}
