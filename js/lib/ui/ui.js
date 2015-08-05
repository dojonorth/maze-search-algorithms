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
      if (path.length > 0) {
        var i = 0;
        function draw() {
            var x = path[i][0] * self.cellWidth;
            var y = path[i][1] * self.cellHeight;
            self.ctx.fillRect(x, y, self.cellWidth, self.cellHeight);
            i++;
            if (i < path.length) {
              window.requestAnimationFrame(draw)
            }     
        }

        window.requestAnimationFrame(draw) 
      }
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
};


