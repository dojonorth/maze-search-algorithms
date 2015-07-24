var _ = require('lodash');

var maze;

var x, y;

function generateMaze(cx, cy) {
  var dir = getDir();
  var shuffledDirs = _.shuffle(dir);
  for (var i in shuffledDirs) {
    var nx = cx + shuffledDirs[i].dx;
    var ny = cy + shuffledDirs[i].dy;

    if (between(nx, x) && between(ny, y) && (maze[nx][ny] === 0)) {

      maze[cx][cy] += shuffledDirs[i].bit;
      maze[nx][ny] += shuffledDirs[i].opposite.bit;

      generateMaze(nx, ny);

    }
  };
  return maze;
}

function between(v, upper) {
  return (v >= 0) && (v < upper);
}

function getDir() {
  var dir = {
    N: {bit: 1, dx: 0, dy: -1},
    S: {bit: 2, dx: 0, dy: 1},
    E: {bit: 4, dx: 1, dy: 0},
    W: {bit: 8, dx: -1, dy: 0}
  };

  dir.N.opposite = dir.S;
  dir.S.opposite = dir.N;
  dir.E.opposite = dir.W;
  dir.W.opposite = dir.E;

  return dir;
};

module.exports = function(_x, _y) {
  x = _x;
  y = _y;

  maze = new Array(x);
  for (var i = 0; i < x; i++) {
    maze[i] =  Array.apply(null, Array(y)).map(function() { return 0 });
  }
  return generateMaze;
}
