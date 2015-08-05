# maze-search-algorithms
Dojo to demonstrate the power of search optimisation algorithms when applied to solving a 2-dimensional Maze problem



## Javascript

### Setup

Clone the repo and cd into the js folder.

Run:

``` npm install ```

To launch the UI run :

``` npm start ```

### The UI

The UI may launch in the background.

In the UI, you can generate mazes using a back track algorithm. Alternatively, select precanned mazes which feature perfect mazes generated with the backtrack algorith and also imperfect braided mazes. The precanned mazes are there to allow you to compare your solution's efficiency with others.

The solve button is hooked up to an empty method in the solution.js file. This is where your maze solving solution should be implemented.

### Aim

Your algorithm should generate and return an array of arrays containing [x,y] coordinates for each cell visited. You can then visualise your algorithm by pressing the solve button in the UI. The UI will present you with a score for your solution for the current maze. The aim is to get the number as low as possible.


