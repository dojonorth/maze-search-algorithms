var _ = require('lodash');

module.exports = function(doc, elemId, maze) {

  this.canvas = doc.getElementById(elemId);
  this.canvas.width = 600;
  this.canvas.height = 600;

  this.ctx = this.canvas.getContext('2d');
  this.horizCells = maze.getY();
  this.vertCells = maze.getX();

  var ratio;

  if (this.horizCells > this.vertCells) {
    ratio = this.vertCells / this.horizCells;
    this.canvas.width = this.canvas.width * ratio;
  } else if (this.vertCells > this.horizCells) {
    ratio = this.horizCells / this.vertCells;
    this.canvas.height = this.canvas.height * ratio;
  }

  this.width = this.canvas.width;
  this.height = this.canvas.height;
  this.maze = maze;
  this.cellWidth = this.width / this.vertCells;
  this.cellHeight = this.height / this.horizCells;

  var self = this;

  self.ctx.strokeStyle = "rgb(0, 0, 0)";
  self.ctx.fillStyle = "rgba(255, 0, 0, 0.1)";

  return {
    width: function() {
      return self.width;
    },

    height: function() {
      return self.height;
    },

    draw: function() {
      this.drawMaze();
    },

    drawSolution: function(path) {
      // N: 1
      // S: 2
      // E: 4
      // W: 8
      // Example solution path for precannedMaze1
      // path = [2,2,2,2,2,2,2,2,2,2,2,4,4,2,2,8,2,4,4,1,4,4,1,8,1,8,1,4,4,4,2,2,2,2,4,4,1,1,4,4,4,4,4,4,2,2];
      var currentX = 0;
      var currentY = 0;
      self.ctx.fillRect(currentX, currentY, self.cellWidth, self.cellHeight);
      var i = 0;

      function draw() {
          var dir = path[i];
          currentX = currentX + calculateNextPosX(dir);
          currentY = currentY + calculateNextPosY(dir);
          self.ctx.fillRect(_.clone(currentX), _.clone(currentY), self.cellWidth, self.cellHeight);
          i++;
          if (i < path.length) {
            window.requestAnimationFrame(draw)
          }     
      }

      window.requestAnimationFrame(draw)

    },

    drawMaze: function() {
        var maze = self.maze;

        colX = maze.getY();
        dimensions = maze.getX();

        var xPos = 0;
        var yPos = 0;

        for (var i = 0; i < colX; i++) {
            xPos = 0;
            // draw the north edge
            for (var j = 0; j < dimensions; j++) {
                if ((maze.representation[j][i] & 1) == 0 && j != 0) {
                    this.drawLine(xPos, yPos, xPos + self.cellHeight, yPos); // horizontal
                }
                xPos += self.cellHeight;
            }

            xPos = 0;
            // draw the west edge
            for (var j = 0; j < dimensions; j++) {
                if ((maze.representation[j][i] & 8) == 0) {
                    this.drawLine(xPos, yPos, xPos, yPos + self.cellWidth); // vertical
                }
                xPos += self.cellHeight;

            }
            this.drawLine(xPos, yPos, xPos, yPos + self.cellWidth); // vertical
            yPos += self.cellWidth;
        }
        // draw the bottom line

        xPos = 0; // reset x pos to western edge

        for (var j = 0; j < dimensions; j++) {
            if (j != dimensions - 1) {
              this.drawLine(xPos, yPos, xPos + self.cellHeight, yPos); // horizontal
            }
            xPos += self.cellHeight;
        }

        maze.display();
    },

    drawLine: function(x1, y1, x2, y2) {
      self.ctx.beginPath();
      self.ctx.moveTo(x1, y1);
      self.ctx.lineTo(x2, y2);
      self.ctx.stroke();
    },

    clear: function() {
      self.ctx.clearRect(0, 0, self.canvas.width, self.canvas.height);
    }
  };

  function calculateNextPosX(dir) {
      var multiplier;
      var shiftBy = 0;
      if (dir === 4) {
          multiplier = 1;
          shiftBy = (multiplier * this.cellWidth);
      } else if (dir === 8) {
          multiplier = -1;
          shiftBy = (multiplier * this.cellWidth);
      }
      return shiftBy;
  }

  function calculateNextPosY(dir) {
      var multiplier;
      var shiftBy = 0;
      if (dir === 1) {
          multiplier = -1;
          shiftBy = (multiplier * this.cellHeight);
      } else if (dir === 2) {
          multiplier = 1;
          shiftBy = (multiplier * this.cellHeight);
      }
      return shiftBy;
  }
};


