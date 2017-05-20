package com.example.ian.comp2601_a2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

/* Main mazeactivity class:
*
*  Contains a grid of buttons providing a maze UI,
*  a 2-dimensional array of maze data (in MazeButton objects
*  and controls to set up and launch the maze solution.
*
*
* To change the desired number of cells per row or column:
* 1.  Add or remove buttons from the activity_maze.xml layout
* 2.  Name the buttons with the coordinates - e.g. buttonr0c6
* 3.  Change the constants BUTTON_COUNT_X and BUTTON_COUNT_Y to reflect the new layout
* 4.  The app will parse a button's coordinates out of its tag
*
* */
public class MazeActivity extends AppCompatActivity {

    private MazeActivity that = this;

    //These are public as they are intended to be available to the developer anywhere in the project
    public static final int BUTTON_COUNT_X = 10;
    public static final int BUTTON_COUNT_Y = 12;

    private int almostWhite;

    private ThreadWithControl mThread1;

    private Button[][] buttonGrid;
    private MazeButton[][] mazeData;

    private RadioButton setWallButton;
    private RadioButton setStartButton;
    private RadioButton setEndButton;
    private Button solveButton;
    private Button clearButton;

    private int startx = -1;
    private int starty = -1;
    private int endx = -1;
    private int endy = -1;

    //listener for the maze button:
    //determines the source of the click, then informs the button click handler
    //which button to manipulate
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String id = getResources().getResourceName(v.getId());
            String id2 = id.replace(getApplicationContext().getPackageName() + ":id/", "").replace(getString(R.string.buttonTitle1), "");

            String[] pieces = id2.split("c");
            int y = Integer.parseInt(pieces[0]);
            int x = Integer.parseInt(pieces[1]);

            handleButtonClick(x, y);

        }
    };

    private View.OnClickListener solveListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(startx == -1 || starty == -1 || endx == -1 || endy == -1){
                Toast.makeText(that, getString(R.string.start_end_not_set),
                        Toast.LENGTH_SHORT).show();
            } else {
            disableButtons();
            mThread1 = new ThreadWithControl(mazeData, that, startx, starty);
            mThread1.start();
        }
        }

    };

    private View.OnClickListener clearListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startx = -1;
            starty = -1;
            endx = -1;
            endy = -1;

            for(int i = 0; i < BUTTON_COUNT_X; ++i){
                for(int j = 0; j < BUTTON_COUNT_Y; ++j){
                    mazeData[i][j].clearAll(almostWhite);
                }
            }

            refreshUI();
        }

    };

    private void handleButtonClick(int x, int y){
        disableButtons();
        if(setWallButton.isChecked() && (x != startx || y != starty) && (x != endx || y != endy)){
            mazeData[x][y].setWall(almostWhite);
        } else if(setStartButton.isChecked() && x != endx && y != endy){

            //check if start is set - need to clear the existing start button
            if(startx != -1){
                mazeData[startx][starty].clearAll(almostWhite);
            }

            //if selecting the start button, clear it
            if(x == startx && y == starty){
                startx = -1;
                starty = -1;
            } else {
                //that means a button was set set the new start coordinates
                startx = x;
                starty = y;
            }

            //toggle the button's start status
            mazeData[x][y].setStart(almostWhite);
        } else if(setEndButton.isChecked() && x != startx && y != starty){

            //similarly, check the end button
            if(endx != -1){
                mazeData[endx][endy].clearAll(almostWhite);
            }

            //if selecting the end button, clear it
            if(x == endx && y == endy){
                endx = -1;
                endy = -1;
            } else {
                //that means a button was set set the new end coordinates
                endx = x;
                endy = y;
            }

            mazeData[x][y].setEnd(almostWhite);
        }
        refreshUI();
        enableButtons();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        almostWhite = getResources().getColor(R.color.almost_white);

        buttonGrid = new Button[BUTTON_COUNT_X][BUTTON_COUNT_Y];
        mazeData = new MazeButton[BUTTON_COUNT_X][BUTTON_COUNT_Y];

        setWallButton = (RadioButton) findViewById(R.id.wallButton);
        setStartButton = (RadioButton) findViewById(R.id.setStartButton);
        setEndButton = (RadioButton) findViewById(R.id.setEndButton);
        solveButton = (Button) findViewById(R.id.solveButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        for(int i = 0; i < BUTTON_COUNT_X; ++i){
            for(int j = 0; j < BUTTON_COUNT_Y; ++j){

                //generate the resource name, look up its ID, then match the button array to the button
                String buttonResourceName = getString(R.string.buttonTitle1) + j + getString(R.string.buttonTitle2) + i;
                int buttonID = getResources().getIdentifier(buttonResourceName, getString(R.string.id), getApplicationContext().getPackageName());
                buttonGrid[i][j] = (Button) findViewById(buttonID);
                buttonGrid[i][j].setBackgroundColor(almostWhite);

                //link the button to its data object
                mazeData[i][j] = new MazeButton(buttonGrid[i][j], i, j);
                buttonGrid[i][j].setOnClickListener(buttonListener);
            }
        }
        solveButton.setOnClickListener(solveListener);
        clearButton.setOnClickListener(clearListener);

    }

    private void disableButtons(){
        solveButton.setEnabled(false);
        clearButton.setEnabled(false);
        for(int i = 0; i < BUTTON_COUNT_X; ++i){
            for(int j = 0; j < BUTTON_COUNT_Y; ++j){
                buttonGrid[i][j].setEnabled(false);
            }
        }
    }

    //needs to be seen by the thread
    public void enableButtons(){
        solveButton.setEnabled(true);
        clearButton.setEnabled(true);
        for(int i = 0; i < BUTTON_COUNT_X; ++i){
            for(int j = 0; j < BUTTON_COUNT_Y; ++j){
                buttonGrid[i][j].setEnabled(true);
            }
        }
    }

    //needs public access since it is used by solve thread
    public void refreshUI(){
        for(int i = 0; i < BUTTON_COUNT_X; ++i){
            for(int j = 0; j < BUTTON_COUNT_Y; ++j){
                if(mazeData[i][j].isWall()){
                    mazeData[i][j].getButton().setBackgroundColor(Color.BLACK);
                } else if(mazeData[i][j].isStart()){
                    //solves a bug where the user could set two start points by pressing buttons too fast
                    if(i != startx && j != starty) mazeData[i][j].clearAll(almostWhite);
                    else mazeData[i][j].getButton().setBackgroundColor(Color.CYAN);
                } else if(mazeData[i][j].isEnd()){
                    //same as start button bug
                    if(i != endx && j != endy) mazeData[i][j].clearAll(almostWhite);
                    else mazeData[i][j].getButton().setBackgroundColor(Color.BLUE);
                }  else if(mazeData[i][j].isVisited()){
                    mazeData[i][j].getButton().setBackgroundColor(Color.GREEN);
                } else {
                    mazeData[i][j].getButton().setBackgroundColor(getResources().getColor(R.color.almost_white));
                }
            }
        }


    }

}
