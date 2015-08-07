# North Dojo: Amazing Algorithms
This dojo is specifically designed to make participants think about how you might approach algorithm design for solving different types of Mazes. Also it's intended to encourage participants to think about how they might improve upon the design of their algorithms so that their algorithm finds a quicker path.

## Setup Instructions

### Java

To get started you'll need:

  * Install of IntelliJ 14 Community Edition ([Download IntelliJ](https://www.jetbrains.com/idea/download/))
  * Install latest version of Java 8 ([Download jdk8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))

Verify that java is installed with the following command in the terminal:

```
➜  ~  java -version
java version "1.8.0_11"
Java(TM) SE Runtime Environment (build 1.8.0_11-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.11-b03, mixed mode)
```

If you need assistance please let us know during the dojo.

#### IntelliJ project

* Clone the repo into a folder of your choice.
* Open IntelliJ and click "IntelliJ menu -> Preferences..."
* Search for plugins and install the JavaFX plugin
* Once installed, create a new Project using "File -> New Project"
* Select the "JavaFX Application" project making sure you select the root folder of the maze-search-algorithms repository
* Once the project is opened right click the root folder in the project view and select "Open Module Settings..."
* Make sure in the module settings window you have the Java 8 SDK added and the Java 8 SDK selected in the modules menu
* Exit and save your module options
* Right-click the java/src/main folder in the Project view and select "Mark Directory As -> Sources Root"
* Right-click the java/src/test folder in the Project view and select "Mark Directory As -> Test Sources Root"

## User Interface

The Java user interface allows you to randomly generate either a [Recursive Backtrack](http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking) Maze or a [Braid](https://www.reddit.com/r/proceduralgeneration/comments/37x0hr/braid_maze_generation_algorithms/) Maze. The Java UI supports up to 60x60 realistically since that the maximum size dimension maze you can fit on your screen. It only currently supports square mazes.

### Pre-generated Mazes
There are some pre-generated mazes you can select from the dropdown. If you have "Custom" selected in the dropdown then it will automatically generate a new maze using the current algorithm you have selected so that you can try out your maze solving algorithm. 

### Competition Mazes
We have two competition mazes of size 60x60 you can try for the final exercise. Recursive Backtracking and Braid maze.

### How to start the UI?

Right-click the MazeUI class in the Project view and select "Run MazeUI.main()"

You should see the UI displayed after a few seconds

## Javascript

Clone the repo and cd into the js folder.

Run:

``` npm install ```

To launch the UI run :

``` npm start ```

It might be a good idea to increase the maximum callstack in node, for larger mazes. Run:

``` node --stack-size=100000 ```

### The UI

The UI may launch in the background.

In the UI, you can generate mazes using a back track algorithm. Alternatively, select precanned mazes which feature perfect mazes generated with the backtrack algorith and also imperfect braided mazes. The precanned mazes are there to allow you to compare your solution's efficiency with others.

The solve button is hooked up to an empty method in the solution.js file. This is where your maze solving solution should be implemented.

### Aim

Your algorithm should generate and return an array of arrays containing [x,y] coordinates for each cell visited. You can then visualise your algorithm by pressing the solve button in the UI. The UI will present you with a score for your solution for the current maze. The aim is to get the number as low as possible.

## Data Structure

### Direction Enum
If you take a look under the hood you'll notice that at the core of the data structure is a DIR enum e.g.

```
        public static DIR[] toArray(int availableDirections) {
            if (availableDirections == 1) {
                return new DIR[]{DIR.N};
            } else if (availableDirections == 2) {
                return new DIR[]{DIR.S};
            } else if (availableDirections == 3) {
                return new DIR[]{DIR.N, DIR.S};
            } else if (availableDirections == 4) {
                return new DIR[]{DIR.E};
            } else if (availableDirections == 5) {
                return new DIR[]{DIR.E, DIR.N};
            } else if (availableDirections == 6) {
                return new DIR[]{DIR.E, DIR.S};
            } else if (availableDirections == 7) {
                return new DIR[]{DIR.N, DIR.S, DIR.E};
            } else if (availableDirections == 8) {
                return new DIR[]{DIR.W};
            } else if (availableDirections == 9) {
                return new DIR[]{DIR.W, DIR.N};
            } else if (availableDirections == 10) {
                return new DIR[]{DIR.W, DIR.S};
            } else if (availableDirections == 11) {
                return new DIR[]{DIR.W, DIR.S, DIR.N};
            } else if (availableDirections == 12) {
                return new DIR[]{DIR.W, DIR.E};
            } else if (availableDirections == 13) {
                return new DIR[]{DIR.N, DIR.E, DIR.W};
            } else if (availableDirections == 14) {
                return new DIR[]{DIR.S, DIR.E, DIR.W};
            }
            return new DIR[0];  //To change body of created methods use File | Settings | File Templates.
        }

        public static DIR toTraversalDirection(int direction) throws Exception {
            if (direction == 1) { // only available is north
                return DIR.N;
            } else if (direction == 2) { // only available is west
                return DIR.S;
            } else if (direction == 4) { // only available is east
                return DIR.E;
            } else if (direction == 8) { // only available is south
                return DIR.W;
            }
            throw new Exception("Invalid current state: current[" + direction + "]");
        }
    }
```

The DIR enum essentially represents the Direction of traversal as a 4 bit integer i.e. 1 = North, 2 = West, 4 = East and 8 = South. This is done this way because performing bit arithmetic operations is extremely quick. For example, the position of walls for a cell where that only has one exit is simply a bitwise NOT of that direction e.g. Bitwise Not of N == S, E, W. That is fundamentally a fast operation in the CPU.

Representing data structures by using bit representations is a classic technique for increasing the speed of applications because we keep the application footprint slow and we make arithmetic operations fast (since we're just shifting bits around in memory and caches). If we were to use objects instead, for example, we'd have to look up the references to those objects in the in-memory cache and follow references to the data, which would be significantly slower than shifting bits in place (since you have to perform many operations as opposed to a few).

## Expected output
The expected output for your Solver should be to calculate the path, so for every traversal you make you'll need to store it in either a Path object (in the Java exercise) containing x and y coordinates or represent it as a json object with x and y values representing the position of the traversal in the maze.


## Solution
Your solver should calculate a legitimate unbroken path taken from Entrance (top left) to Exit (bottom right) to be considered as a solution to the problem. Unlike garden mazes, without the aid of a ladder, powertool, shovel, brute force or a stick of dynamite, you should not traverse through, under or over walls when solving it.

# Types of Mazes

## Recursive Backtrack
So called 'perfect mazes' because there is exactly one, and only one route between every corner in the maze.

## Braid
Braid mazes are essentially mazes that have few to no dead ends. They can produce complex mazes when designed correctly (although sometimes they make it too easy!).

# Exercises

## Exercise 1
Think of an algorithm to solve any recursive backtracker maze. We recommend you pair on this exercise and come up with pseudocode for generating your algorithm to solve any recursive backtracking maze.

## Exercise 2
Implement it so that it will display a solution using the Solve button on the User Interface.

## Exercise 3
Think of another algorithm (or re-use your existing algorithm) to solve a braid maze

## Bonus
Think of ways you can improve this algorithm to reduce the number of steps required to calculate a path

## Competition
Run your algorithm against both competition mazes. Add up the efficiency of your algorithm on both mazes. Lowest score wins. Highest score loses - GO!

p.s. no hardcoding path directions - we will be checking :) - good luck!



