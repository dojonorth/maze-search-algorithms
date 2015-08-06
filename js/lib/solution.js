var helpers = require('./helpers');
var _ = require('lodash');

module.exports = function(maze) {

  // Takes an array of [x, y] coordinates for visited cells in the solution.
  // Example precanned solution path for precannedMaze1

  // return [[0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],[0,7],[0,8],[0,9],[0,10],[0,11],[0,12],[0,13],[0,14],[1,14],[2,14],[3,14],[3,13],[4,13],[5,13],[5,12],[4,12],[4,11],[3,11],[3,10],[4,10],[5,10],[6,10],[6,11],[6,12],[6,13],[6,14],[7,14],[8,14],[8,13],[8,12],[9,12],[10,12],[11,12],[12,12],[13,12],[14,12],[14,13],[14,14]];


  var m = maze.representation;

  var solution = [];
  var lastVisited = [];

  // Probably the stupidest maze solver possible

  function goInRandomDirection(cx, cy) {
    solution.push([cx, cy]);

    if (!helpers.isExit(maze, cx, cy)) {

      var shuffledDirs = _.shuffle(helpers.getAvailableDirections(m[cx][cy]));
      var nextDirection = shuffledDirs[0];

      // lets make it slightly less stupid by at least not letting it go back on itself
      if (_.isEqual([cx + nextDirection.dx, cy + nextDirection.dy], lastVisited)) {
        nextDirection = _.last(shuffledDirs);
      }

      // upgrade pinky to be slightly less dense, with at least some memory
      lastVisited = [cx, cy];

      goInRandomDirection(cx + nextDirection.dx, cy + nextDirection.dy);
    } else {
      solution.push([cx, cy])
    }
  }

  goInRandomDirection(0, 0);


  return solution;

}
