// To try this out, in the node repl:

// > node
// > require('./maze')(5, 5).display()


var mazeGeneratorFactory = require('./maze-generator-factory');
var mazeViewer = require('./maze-viewer');

var x, y;

var maze;

function display() {
  mazeViewer(maze);
}

module.exports = function(_x, _y, type) {
  if (!type) {
    type = 'recursive-backtracker';
  }
  mazeGenerator = mazeGeneratorFactory(_x, _y, type);
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
