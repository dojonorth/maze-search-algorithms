// To try this out, in the node repl:

// > node
// > require('./maze')(5, 5).display()


var mazeGeneratorFactory = require('./maze-generator-factory');
var mazeViewer = require('./maze-viewer');


var mazeProblemOne = [[2,5,2,3,7,3,1,6,7,3,3,5,6,3,5],[4,10,3,5,12,6,3,9,10,3,5,12,8,6,13],[12,6,5,12,12,14,3,1,6,3,9,10,3,9,12],[12,12,12,12,12,12,6,3,9,4,6,7,1,6,9],[14,9,10,9,12,12,12,6,5,12,12,10,5,12,4],[12,2,7,5,12,12,10,9,12,10,13,4,10,9,12],[12,6,9,12,12,12,2,5,10,5,10,11,3,3,13],[12,10,5,12,10,11,3,13,4,10,5,6,3,1,12],[12,6,9,10,5,4,6,9,12,6,9,12,6,3,9],[10,9,6,1,12,10,11,3,9,12,6,13,12,2,5],[6,7,9,6,9,6,3,3,3,9,8,12,12,6,13],[8,12,6,9,6,13,6,3,7,3,3,13,12,12,12],[6,9,10,5,12,12,12,4,10,3,5,8,12,12,8],[14,3,5,12,8,12,10,11,3,5,10,5,12,10,5],[10,1,10,11,3,9,2,3,3,11,1,10,11,3,9]];
var mazeProblemTwo = [[2,5,6,5,6,3,1,6,3,3,7,5,2,3,5],[4,10,9,12,14,3,3,9,6,5,12,10,3,3,9],[14,3,3,9,8,6,3,5,12,12,10,3,3,3,5],[12,6,3,3,5,12,4,10,9,10,3,3,3,3,13],[12,12,6,5,12,12,10,3,3,3,5,6,3,3,9],[12,10,9,12,10,9,6,5,6,3,13,10,3,3,5],[10,3,5,12,6,5,12,10,9,4,14,3,3,1,12],[4,6,9,12,12,10,9,6,3,11,13,6,3,3,9],[12,12,2,13,14,3,5,8,6,5,8,10,3,3,5],[12,10,3,9,12,4,12,6,9,12,6,5,6,3,9],[14,7,3,1,10,13,12,12,4,12,12,10,9,6,1],[12,10,3,7,1,12,10,11,9,12,12,2,3,11,5],[12,2,5,12,6,9,2,5,6,9,10,3,3,5,12],[10,5,12,12,12,6,3,13,12,6,7,1,6,9,12],[2,11,9,10,11,9,2,9,10,9,10,3,11,3,9]];
var mazeProblemThree = [[2,5,2,3,7,3,3,3,5,6,3,1,6,7,1],[4,10,3,5,12,2,5,6,9,10,3,3,9,14,5],[10,7,5,12,10,5,14,9,6,5,6,3,5,12,12],[6,9,8,10,3,9,12,6,13,12,10,5,12,12,12],[12,6,3,7,1,6,9,12,12,10,3,9,12,8,12],[12,12,4,10,5,10,5,8,14,3,5,2,11,3,13],[12,10,13,4,10,5,10,5,10,5,10,5,6,5,12],[14,1,12,10,5,14,1,12,6,9,4,10,9,12,12],[14,5,10,5,12,10,5,12,12,2,15,3,1,12,12],[8,12,6,9,10,5,12,12,10,3,9,6,3,9,12],[6,9,14,3,3,9,12,10,3,3,3,9,4,6,9],[10,5,12,6,3,3,13,6,3,3,1,6,11,9,4],[6,9,12,10,5,6,11,9,6,3,3,9,6,3,13],[14,5,12,4,12,10,1,6,9,6,5,2,13,4,12],[8,10,11,9,10,3,3,11,3,9,10,3,9,10,9]];
var mazeProblemFour = [[2,3,3,5,4,6,7,3,3,5,6,5,6,3,5],[6,3,1,12,12,12,10,3,5,10,9,12,10,5,12],[12,6,3,9,14,9,4,6,9,2,3,11,1,12,12],[14,9,2,3,11,5,12,10,3,5,6,3,3,9,12],[10,5,6,5,2,11,11,5,4,12,12,6,7,1,12],[4,10,9,10,3,5,6,9,12,10,9,8,12,6,13],[14,5,4,6,3,9,12,6,11,5,6,3,9,12,8],[12,10,9,12,4,6,9,10,5,12,14,3,5,10,5],[12,6,5,12,12,12,6,3,9,12,8,6,13,4,12],[14,9,10,9,12,12,10,3,5,10,5,12,10,13,12],[12,2,7,5,10,11,3,3,9,6,9,12,2,9,12],[10,3,9,10,3,3,5,4,6,11,3,9,6,3,13],[6,5,6,7,3,5,12,10,9,6,3,3,9,6,9],[12,10,9,10,5,8,12,6,5,10,5,6,5,10,5],[10,3,3,1,10,3,11,9,10,3,9,8,10,3,9]];
var mazeProblemFive = [[2,3,5,6,3,3,7,5,6,3,3,3,7,3,5],[6,5,12,12,6,3,9,8,10,5,4,6,9,4,12],[12,12,12,12,10,3,5,6,5,10,13,10,5,14,9],[12,10,9,10,5,4,10,9,14,1,12,4,12,10,5],[10,5,6,5,10,15,3,1,14,5,14,9,10,5,8],[6,9,12,10,5,8,6,5,8,10,9,6,5,14,5],[10,3,9,4,12,6,9,10,3,3,3,9,12,8,12],[6,3,7,13,12,10,7,5,2,7,7,1,10,3,13],[14,1,12,8,10,5,12,12,6,9,12,6,3,3,9],[8,6,13,6,3,9,12,12,12,2,13,12,6,3,5],[6,9,8,12,4,6,9,12,14,5,8,10,9,4,12],[12,6,3,9,10,9,6,9,8,10,7,5,6,11,9],[12,10,3,3,3,5,14,3,3,5,12,12,10,5,4],[14,1,6,5,6,9,10,3,1,12,12,10,1,10,13],[10,3,9,10,11,3,3,3,3,9,10,3,3,3,9]];

var x, y;

var maze;

function display() {
  mazeViewer(maze);
}

module.exports = function(_x, _y) {
  mazeGenerator = mazeGeneratorFactory(_x, _y, 'recursive-backtracker');
  x = _x;
  y = _y;

  maze = {
    representation: mazeGenerator(0, 0),
    getY: function() {
      return y;
    },
    getX: function() {
      return x
    },
    display: display
  };

  return maze;
}