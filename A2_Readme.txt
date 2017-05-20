==========
COMP 2601 winter 2016
Assignment #2: Threads
Submitted 2016-02-26
Ian Smith #100910972
==========

Assumptions:

Maze cell coordinates are obtained by parsing the integers out of the id of the corresponding Button object.  This required a call to getContext().  I am assuming that this is safe to run on other devices (due to the getContext() call as opposed to hard coded context string).

==========

UI Instructions:

- The app is delivered with an empty maze area
- The UI controls are a SOLVE button, a CLEAR button, and a radio button group to SET WALL, SET START, or SET END

- When setting up the maze, you can set wall, start, or end at any time

- To set walls, select the SET WALL button:
    - Selecting an empty cell sets that cell to a wall
    - Selecting a wall resets that cell to empty
    - If start or end are set, these cannot be overwritten with a wall

- To set start, select the SET START button:
    - Selecting an empty cell sets that cell to start
    - Selecting start on an existing start cell sets that cell to empty (and requires you to find a new start location prior to solving the maze)
    - Selecting an empty cell with a start already set somewhere else moves start to the new cell
    - End cannot be overwritten with start
    
- To set end, select the SET END button:
    - Selecting an empty cell sets that cell to end
    - Selecting end on an existing end cell sets that cell to empty (and requires you to find a new end location prior to solvign the maze)
    - Selecting an empty cell with a end already set somewhwere else moves end to the new cell
    - Start cannot be overwritten with end   
  
- To clear the maze, select the CLEAR button

- To solve the maze, select the SOLVE button
    - The maze will not try to solve without both a start and end somewhere
    - The buttons will not respond while the maze is solving
    - As the algorithm visits cells these are marked green
    - The app will report success or failure when the solution thread exits
    - When the solution thread ends, you can clear the maze and try again.

==========
Algorithm

I implemented the maze traversal using a simple breadth-first search.  The BFS is iterative and uses a linked list to 
enqueue child nodes of the current nodes, reporting success when the end node is dequeued or failure when the queue is empty.

The topic of improving this algorithm to solve mazes is an interesting one, but it is one that is perhaps best explored in depth during an algorithms class, as opposed to navigating a 9x12 grid in a mobile apps class.

==========
Device

The display is tested and optimized to run on a Samsung Galaxy Note 2:

              OS Version: 3.0.31-1736364(I317MVLUDNH3)
              OS API Level: 19
              Device: t0ltecan
              Model (and Product): SGH-I317M (t0ltevl)
              RELEASE: 4.4.2
              BRAND: samsung
              DISPLAY: KOT49H.I317MVLUDNH3
              HARDWARE: smdk4x12
              Build ID: KOT49H
              MANUFACTURER: samsung
              SERIAL: 42f74c15f4d99f9d
              USER: dpi
              HOST: SWHD4403

==========
Design Requirement Cross-Reference

-----
R1.1) The UI grid should consist of Button instances and have at least 10 cells along the short screen dimension and obviously more in the long direction. The cells should appear square rather than rectangular. The cells should fill the screen, save maybe for a "solve" button somewhere. Make the "game" appealing for the size of device you plan to test on. Try to make the margin between the buttons minimal. 

The app uses a grid layout and background color to implement a contiguous maze display area.  The maze is 10 cells by 12.

-----
R1.2) When the user clicks on an empty cell (or button) it should toggle to being a "wall". Conversely, if the user clicks on a wall cell it should become an empty cell. The user should be able to move the start cell by toggling it off and then clicking on another cell. Similarly the user should be able to move the destination cell. The start, destination, empty, wall, and path cells should all have a different colour and easily distinguished.

See UI instructions.  Walls, Start, and End cells can all be set or cleared using the radio buttons.

-----
R1.3) When the user clicks the "SOLVE" button (or selects SOLVE in some other way) a computation should start on another thread to find a path from the start cell to the destination cell.

The solution runs on a separate thread launched by the SOLVE button listener

-----
R1.4) The path found by the solver should connect the start to destination cell. Connection should be by adjacent cells (top, left, right, bottom) and not diagonally. The path, obviously, should never go through a wall. The solver should always find a path if one exists and the program should not crash if none exists.

The "Path" (consisting of all visited cells) is marked green by means of successive UI refresh calls run on the main thread.

-----
R1.5) While the solve computation is taking place the cell buttons should be disabled. That is, the user should not be able to toggle cells until the solve computation is complete.

Button listeners are disabled as the solve thread is launched, and re-enabled afterwards.

-----
R1.6) During the computation to solve the maze the UI should continuously update to show the partial path constructed thus far. The UI should update whenever the status of a cell changes from being on the proposed path or not. Thus we should see the partial path being constructed as the solver attempts to find a path from the start to the destination.

refreshUI() method calls are run on the main thread after maze cells are dequeued.

-----
R1.7) The solver should employ a strategy that guarantees to make progress and not just randomly wander around. For example you could use a recursive strategy to construct a path from the source cell to the destination cell.

An iterative breadth-first search is used.

-----
R1.8) The progress updates, or speed of path finding, should be visually appealing. If it happens too fast put the solver thread to sleep for an appropriate amount of time.

The solve thread is put to sleep for 200 ms after UI refreshes

-----
R1.9) When the user rotates their device between landscape and portrait mode the current computation should continue and not be interupted. The layout need not change, nor be appropriate for one of the orientations, but the computation should keep running and the UI keep updating. Rev1) As an option is would be OK just to restrict the app to only allow landscape or protrait orientation. That is, not allow a rotate.)

This is accomplished by locking screen location in the manifest file

-----
R1.10) It should not be an unwieldy task for a programmer to change the number of cells in a row. The app user need not be able to do this, but a programmer maintaining, or modifying, the code should be able to change the desired number of cells per row in a reasonably straight forward manner. Your code organization should accommodate this. Your code should contain comments on how this could be done.

See comments at the top of MainActivity class.  The number of rows and columns are set in public static final ints which are intended to be visible to any ofther part of the code which may need them in the future.

(Of course, modifying the number of buttons would also require a change to the layout XML.)


==========
References:
  - In-class exercises (bank accounts and threads)
  - http://stackoverflow.com/questions/16380026/implementing-bfs-in-java
    - The overall idea came from this thread but the code is mine.
  - http://stackoverflow.com/questions/582185/android-disable-landscape-mode

