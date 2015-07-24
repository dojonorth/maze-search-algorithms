var defaultMazeGenerator = require('./default-maze-generator')

module.exports = function(x, y, type) {
  if (type == 'recursive-backtracker') {
    return defaultMazeGenerator(x, y);
  }
  return null;
}
