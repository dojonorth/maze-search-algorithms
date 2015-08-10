// Maze solving solution by Steve Monks
//
// This technique should yield the optimal path through any solvable regluar grid maze, although the approach may not be in the spirit of the
// challenge.
//
// Technique:
// Starting at the target location (in this case, the exit) the technique weights the location with an initial value (1 in this implementation),
// then, for every adjacent node we repeat the process with an incremented weighting. The process is repeated until there are no more
// unweighted nodes. One important consideration is that all the nodes for each weighting value should be populated per pass through the
// weighting algorithm. If you attempt this recursively you will most likely end up with a non optimal path.
//
// Once the weighting has been completed you can determine the path from the start position by repeatedly looking at the adjacent nodes
// and moving to the one with the lowest weighting.
//
//
// Apologies for the formatting, I don't normally program in Javascript, so I don't seem to have an editor installed that understands
// how tabbing should work in these files, so it seems to be all over the place.



// Contains utilities to get the available open directions from a cell and to check if a cell is the exit
var helpers = require('./helpers');

module.exports = function(maze) {

  // maze.representation contains a two dimensional array of cells making up the representation of the maze.

  // You need to return an array of [x, y] coordinates for visited cells in the solution.
  // Each time you visit a cell, you must push it to the array.
  // The last item in the array will be the [x, y] of the exit tile.

  // Example precanned solution path for precannedMaze1
  // return [[0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[0,7],[0,8],[0,9],[0,10],[0,11],[0,12],[0,13],[0,14],[1,14],[2,14],[3,14],[3,13],[4,13],[5,13],[5,12],[4,12],[4,11],[3,11],[3,10],[4,10],[5,10],[6,10],[6,11],[6,12],[6,13],[6,14],[7,14],[8,14],[8,13],[8,12],[9,12],[10,12],[11,12],[12,12],[13,12],[14,12],[14,13],[14,14]];



    // enumerate some values to match the direction codes used in the maze representation.
  // Performing binary AND's with these and the direction codes in most languages using integer values will
  // be the fastest way to interpret the available directions.
  var UP = 1;
  var DOWN = 2;
  var LEFT = 8;
  var RIGHT = 4;


    // I use a 1 dimensional array to represent the weights, this returns an index into that array
    // given x,y coordinates.
  function toIX(maze,x,y)
  {
    return maze.getY()*x+y;

  }


    // returns the linked directions from an x,y location in the maze
  function getXY(maze, x, y)
  {
    return (maze.representation[x][y]);
  }


    // validates that the given x,y coordinate lies within the maze. Returns 1 if true otherwise 0
  function clip(maze,x,y)
  {
    if (x < 0)
      return 0;

    if (x >= maze.getX())
      return 0;

    if (y < 0)
      return 0;

    if (y >= maze.getY())
      return 0;

  return 1;   
  }


    // returns the weight at a specific location in the maze
  function getWeight(maze,weights,x,y)
  {

    if (clip(maze,x,y) == 0)
      return 0;

    return weights[toIX(maze,x,y)];
  }


    // fills an array with a specific value - used to initialise the weights array to a known unvisited value
  function populate(maze, arr, val)
  {
    for (var x = 0; x < maze.getX(); x++) {
      for (var y = 0; y < maze.getY(); y++) {
        arr[toIX(maze,x,y)] = val;
      }
    }
  }



    // Recursive weighting algorithm. This is what I wrote during the Dojo, it will produce weights for a valid path, but
    // it wont be an optimal one. Retained for reference purposes only.
  function weightCell(maze,x,y,weight,weights)
  {
    var ix = toIX(maze,x,y);

    if (weights[ix] != 0) {
        return;
    }

    weights[ix] = weight;
    var dirs = getXY(maze,x,y);

    if ((dirs & UP) == UP) {
      weightCell(maze,x,y-1,weight+1,weights);
    }
    if ((dirs & DOWN) == DOWN) {
      weightCell(maze,x,y+1,weight+1,weights);
    }
    if ((dirs & LEFT) == LEFT) {
      weightCell(maze,x-1,y,weight+1,weights);
    }
    if ((dirs & RIGHT) == RIGHT) {
      weightCell(maze,x+1,y,weight+1,weights);
    }
  }



    // checks if the given node is valid and unweighted and if so adds
    // it to the 'next_weights' list.
    // Note that if a node is added, it gets temporarily marked with an invalid weight
    // of -1, this is to prevent the node being added more than once to 'next_weights'
    // as only nodes with a weight of 0 get added. The invalid weight will be overwritten
    // on the next pass.
  function checkAndAddNeighbour(maze, weights, next_weights, xy, offset)
  {
    var x = xy[0] + offset[0];
    var y = xy[1] + offset[1];

    // ensure node is within the bounds of the maze
    if (clip(maze,x,y) == 0)
      return;

    // check that node is unweighted
    var map_ix = toIX(maze,x,y);
    var weight = weights[map_ix];
    if (weight != 0)
      return;

    // mark cell as dirty, will get rewritten with actual weight on the next pass
    weights[map_ix] = -1;

    // add node to 'next_weights' list.
    next_weights.push([x,y]);
  }



    // for a given node, this method determines its unweighted neighbours and adds them
    // to the 'next_weights' list.
  function getNextWeights(maze, weights, xy, next_weights)
  {
    var x = xy[0];
    var y = xy[1];

    // create an array of coordinate offsets from this point
    var offsets = [];
    offsets[UP] = [0,-1];
    offsets[DOWN] = [0,1];
    offsets[LEFT] = [-1,0];
    offsets[RIGHT] = [1,0];

    // get the possible directions from this point
    var dirs = getXY(maze,x,y);

    // for each possible direction add any unweighted neighbour to the 'next_weights' list
    if ((dirs & UP) == UP) {
        checkAndAddNeighbour(maze,weights,next_weights,xy,offsets[UP]);
    }
    if ((dirs & DOWN) == DOWN) {
        checkAndAddNeighbour(maze,weights,next_weights,xy,offsets[DOWN]);
    }
    if ((dirs & LEFT) == LEFT) {
        checkAndAddNeighbour(maze,weights,next_weights,xy,offsets[LEFT]);
    }
    if ((dirs & RIGHT) == RIGHT) {
        checkAndAddNeighbour(maze,weights,next_weights,xy,offsets[RIGHT]);
    }
  }


    // This method should weight the maze in an optimal manner.
    // For a given list of nodes, it weights them with the current value in one pass,
    // then it runs through the list again, building a list of unweighted neighbouring nodes,
    // which is then fed back into the weighting process for the next weighting value.
    // this process is repeated until there are no more nodes.

    function weightCellsForBestPath(maze,weights,target_x,target_y)
    {
      var weight = 1;
      var weight_list = [[target_x,target_y]];
      do {
        var next_weights = [];

        // set weights at this level
        for (var ix = 0; ix < weight_list.length; ix++) {
            var xy = weight_list[ix];
            var map_ix = toIX(maze, xy[0],xy[1]);
            weights[map_ix] = weight;
        }

        // determine weight locations at next level
        for (var ix = 0; ix < weight_list.length; ix++) {
            var xy = weight_list[ix];
            getNextWeights(maze, weights, xy, next_weights);
        }
        ++weight;
        weight_list = next_weights;

      } while(weight_list.length != 0);
    }



    // The final phase. Beginning at the start location, this walks the weighted node array
    // moving to the connected node with the lowest weight. Eventually this will lead to
    // the target (in this case the exit). Note it's important to still observe the valid
    // connections between nodes. I forgot about this during the Dojo and ended with an
    // algorithm that plotted a route from start to exit, but went through varies walls
    // on its way.

  function getPath(maze,weights,x,y)
  {
    var path = [];
    var cnt = 0;

    do 
    {
      {
          // this is an array of weights for neighbours in direction code order
          // we're going to populate this for just UP,DOWN,LEFT and RIGHT codes
          // where there is a connection
        var neighbours = [0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0];

        // add the current location to the final path
        path.push([x,y]);

            // check if we're at the exit and bail out if we are.
        if (x == maze.getX()-1 && y == maze.getY()-1) {
          console.log("**solved in "+cnt+" STEPS**");
          return path;
        }

            // get the possible direction code from this node
        var posdir = getXY(maze,x,y);

        // fill in the neighbour weights for each possible direction
        if ((posdir & UP) != 0) {
          neighbours[UP] = getWeight(maze,weights,x,y-1);
        }
        if ((posdir & DOWN) != 0) {
          neighbours[DOWN] = getWeight(maze,weights,x,y+1);
        }
        if ((posdir & LEFT) != 0) {
          neighbours[LEFT] = getWeight(maze,weights,x-1,y);
        }
        if ((posdir & RIGHT) != 0) {
          neighbours[RIGHT] = getWeight(maze,weights,x+1,y);
        }

            // Now we're going to run through the neighbours array
            // and pick the one with the lowest weight.

            // array of the four directions (used as indices into neighbours) we want to test
        var ix = [LEFT, RIGHT, UP, DOWN];

        // index of best (lowest weighted) neighbour. -1 is an invalid value
        // indicating no valid neighbour.
        var best_ix = -1;

        // the weight of the best neighbour
        var best_weight = 0;

        for (var n = 0; n < 4; n++) {
          var w = neighbours[ix[n]];
          if (w != 0) {
            if (w < best_weight || best_weight == 0) { // pick the first neighbour we encounter or any that is better.
              best_ix = n;
              best_weight = w;
            }
          }
        }

            // If a best neighbour has been found adjust the coordinates
            // and move to that location on the next iteration.
        if (best_ix != -1) {
          var dir = ix[best_ix];
          if (dir == LEFT) {
            x = x -1;
          }
          else if (dir == RIGHT) {
            x = x + 1;
          }
          else if (dir == UP)
          {
            y = y -1;
          }
          else if (dir == DOWN) {
            y = y +1;
          }
          else {
              // one of those "shouldn't happen" scenarios. If it does,
              // which it shouldn't, we bail out with the path so far.
            return path;
          }
        }
      }

    } while(1);

  }


    // get target coordinates (exit point)
    var x = maze.getX()-1;
    var y = maze.getY()-1;


    // 1st step - weight the maze
    var weights = [];

    // pre clear the weights to zero
    populate(maze,weights,0);


    // Non optimal recursive weighting algorithm. See notes above.
    //weightCell(maze,x,y,1,weights);

    // optimal, non recursive weighting algorithm.
    weightCellsForBestPath(maze,weights,x,y);

    // 2nd step - use the weights to determine the optimal path
    var path = getPath(maze,weights,0,0);



  return path;
}
