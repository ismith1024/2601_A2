package com.example.ian.comp2601_a2;

import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static android.app.PendingIntent.getActivity;

/**
Purpose: a computation should start on another thread to find a path from the start cell to the destination cell.
 */

public class ThreadWithControl extends Thread {

    private MazeActivity mainActivity;
    private boolean mRunning;
    private MazeButton[][] maze;
    private int startx;
    private int starty;
    private Queue<MazeButton> path;

    private final int infinity = Integer.MAX_VALUE;


    public ThreadWithControl(MazeButton[][] c_maze, MazeActivity c_mainact, int c_startx, int c_starty){

        this.mainActivity = c_mainact;
        this.mRunning = false;
        this.maze = c_maze;
        this.startx = c_startx;
        this.starty = c_starty;
        this.path = new LinkedList<MazeButton>();

    }

    public void quit() {
        path.clear();
        mRunning = false;
        mainActivity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   mainActivity.refreshUI();
                   Toast.makeText(mainActivity, mainActivity.getString(R.string.maze_solved),
                           Toast.LENGTH_SHORT).show();
                   mainActivity.enableButtons();
               }
           }
        );
        return;

    }

    public void run() {
        mRunning = true;
        while (mRunning){

            int solution = solve(startx, starty);

            if(solution == infinity){
                path.clear();
                 mainActivity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           mainActivity.refreshUI();
                           Toast.makeText(mainActivity, mainActivity.getString(R.string.maze_not_solved),
                                   Toast.LENGTH_SHORT).show();
                           mRunning = false;
                           mainActivity.enableButtons();
                       }
                   });
                return;

            }
        }

    }


    private int solve(int px, int py){
        path.add(maze[px][py]);

        while(!path.isEmpty()){
            MazeButton current = path.remove();

            //update main thread
            //update the UI on main thread
            mainActivity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mainActivity.refreshUI();
                   }
               }
            );

                           try {
                    Thread.sleep(200);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            if(current.isEnd()){
                quit();
                return 1;
            } else {

                current.setVisited(true);

                if(current.getx() > 0){
                    if(!maze[current.getx()-1][current.gety()].isWall() && !maze[current.getx()-1][current.gety()].isVisited() ){
                        maze[current.getx()-1][current.gety()].setVisited(true);
                        path.add(maze[current.getx()-1][current.gety()]);
                    }
                }

                if(current.getx() < MazeActivity.BUTTON_COUNT_X -1){
                    if(!maze[current.getx()+1][current.gety()].isWall() && !maze[current.getx()+1][current.gety()].isVisited() ){
                        maze[current.getx()+1][current.gety()].setVisited(true);
                        path.add(maze[current.getx()+1][current.gety()]);
                    }
                }

                if(current.gety() > 0){
                    if(!maze[current.getx()][current.gety()-1].isWall() && !maze[current.getx()][current.gety()-1].isVisited() ){
                        maze[current.getx()][current.gety()-1].setVisited(true);
                        path.add(maze[current.getx()][current.gety()-1]);
                    }
                }

                if(current.gety() < MazeActivity.BUTTON_COUNT_Y -1){
                    if(!maze[current.getx()][current.gety()+1].isWall() && !maze[current.getx()][current.gety()+1].isVisited() ){
                        maze[current.getx()][current.gety()+1].setVisited(true);
                        path.add(maze[current.getx()][current.gety()+1]);
                    }
                }

            }

        }

        return infinity;
    }



}
