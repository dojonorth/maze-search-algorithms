var directions = {
  'N': {dx: 0, dy: -1},
  'S': {dx: 0, dy: 1 },
  'E': {dx: 1, dy: 0 },
  'W': {dx: -1, dy: 0}
}

module.exports.getAvailableDirections = function(bit) {
  switch(bit) {
    case 1:
      return {
        'N': directions.N
      }
      break;
    case 2:
      return {
        'S': directions.S
      }
      break;
    case 3:
      return {
        'N': directions.N,
        'S': directions.S
      }
      break;
    case 4:
      return {
        'E': directions.E
      }
      break;
    case 5:
      return {
        'E': directions.E,
        'N': directions.N
      }
      break;
    case 6:
      return {
        'E': directions.E,
        'S': directions.S
      }
      break;
    case 7:
      return {
        'N': directions.N,
        'E': directions.E,
        'S': directions.S
      }
      break;
    case 8:
      return {
        'W': directions.W
      }
      break;
    case 9:
      return {
        'N': directions.N,
        'W': directions.W
      }
      break;  
    case 10:
      return {
        'S': directions.S,
        'W': directions.W
      }
      break;  
    case 11:
      return {
        'S': directions.S,
        'W': directions.W,
        'N': directions.N
      }
      break;  
    case 12:
      return {
        'W': directions.W,
        'E': directions.E
      }
      break;  
    case 13:
      return {
        'N': directions.N,
        'W': directions.W,
        'E': directions.E
      }
      break;  
    case 14:
      return {
        'S': directions.S,
        'W': directions.W,
        'E': directions.E
      }
      break; 
    case 15:
      return {
        'N': directions.N,
        'S': directions.S,
        'W': directions.W,
        'E': directions.E
      }
      break; 
  }
}

module.exports.isExit = function(maze, cx, cy) {
  return (cx + 1) == maze.representation.length && (cy + 1) == maze.representation[0].length;
}
