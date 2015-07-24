

module.exports = function(maze) {
    for (var i = 0; i < maze.getY(); i++) {
        for (var j = 0; j < maze.getX(); j++) {
            process.stdout.write((maze.representation[j][i] & 1) == 0 ? "+---" : "+   ");
        }
        process.stdout.write("+\n");
        // draw the west edge
        for (var j = 0; j < maze.getX(); j++) {
            process.stdout.write((maze.representation[j][i] & 8) == 0 ? "|   " : "    ");
        }
        process.stdout.write("|\n");
    }
    // draw the bottom line
    for (var j = 0; j < maze.getX(); j++) {
        process.stdout.write("+---");
    }
    process.stdout.write("+\n");
}
