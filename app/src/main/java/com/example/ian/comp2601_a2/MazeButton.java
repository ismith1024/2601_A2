package com.example.ian.comp2601_a2;

import android.graphics.Color;
import android.widget.Button;

/**
Purpose: stores maze data for a corresponding Button
 Mostly contains getters and setters for the maze status flags
 */

public class MazeButton {
    private Button button;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;
    private boolean visited;
    private int x;
    private int y;

    public MazeButton(Button b, int cx, int cy){
        button = b;
        x = cx;
        y = cy;
        isEnd = false;
        isStart = false;
        isWall = false;
    }

    public void setVisited(Boolean b){
        visited = b;
    }

    public void setWall(int aw){
        isEnd = false;
        isStart = false;
        isWall = !this.isWall();
        if(this.isWall())
            button.setBackgroundColor(Color.BLACK);
        else
            button.setBackgroundColor(aw);
    }

    public void clearAll(int aw){
        isEnd = false;
        isStart = false;
        isWall = false;
        visited = false;
        button.setBackgroundColor(aw);
    }


    public void setStart(int aw){
        isEnd = false;
        isStart = !this.isStart();
        isWall = false;
        if(this.isStart())
            button.setBackgroundColor(Color.CYAN);
        else
            button.setBackgroundColor(aw);
    }

    public void setEnd(int aw){
        isEnd = !this.isEnd();
        isStart = false;
        isWall = false;
        if(this.isEnd())
            button.setBackgroundColor(Color.BLUE);
        else
            button.setBackgroundColor(aw);
    }

    public Button getButton(){return button;}
    public boolean isWall(){return isWall;}
    public boolean isStart(){return isStart;}
    public boolean isEnd(){return isEnd;}
    public boolean isVisited(){return visited;}
    public int getx(){return x;}
    public int gety(){return y;}

}
