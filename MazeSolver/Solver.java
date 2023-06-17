import java.util.ArrayList;

public class Solver {
   enum Direction {NORTH, SOUTH, EAST, WEST}

   /**
    * Returns whether the input maze with specified input coordinates can be solved or not.
    * @param maze int[][] which represents a maze formed by 0s and 1s
    * @param xStart  x-coordinate of the starting point 
    * @param yStart  y-coordinate of the starting point
    * @param xTarget x-coordinate of the target point
    * @param yTarget y-coordinate of the target point
    * @return whether the maze is solvable or not
    */
   public static boolean mazeSolver(int[][] maze, int xStart, int yStart, int xTarget, int yTarget) {
      maze[yStart][xStart] = ImageManager.coordinate_types[3];
      ArrayList<Direction> directions = movableDirections(maze, xStart, yStart);
      if (xStart == xTarget && yStart == yTarget) {
         System.out.println(mazeString(maze));
         return true;
      }
      for (int i = 0; i < directions.size(); i++) {
         switch (directions.get(i)) {
            case NORTH:
               maze[yStart-1][xStart] = ImageManager.coordinate_types[3];
               if (mazeSolver(maze, xStart, yStart-1, xTarget, yTarget)) {
                  return true;
               }
               else {
                  maze[yStart-1][xStart] = ImageManager.coordinate_types[0];
               }
               break;
            case SOUTH:
               maze[yStart+1][xStart] = ImageManager.coordinate_types[3];
               if (mazeSolver(maze, xStart, yStart+1, xTarget, yTarget)) {
                  return true;
               }
               else {
                  maze[yStart+1][xStart] = ImageManager.coordinate_types[0];
               }
               break;
            case EAST:
               maze[yStart][xStart+1] = ImageManager.coordinate_types[3];
               if (mazeSolver(maze, xStart+1, yStart, xTarget, yTarget)) {
                  return true;
               }
               else {
                  maze[yStart][xStart+1] = ImageManager.coordinate_types[0];
               }
               break;
            case WEST:
               maze[yStart][xStart-1] = ImageManager.coordinate_types[3];
               if (mazeSolver(maze, xStart-1, yStart, xTarget, yTarget)) {
                  return true;
               }
               else {
                  maze[yStart][xStart-1] = ImageManager.coordinate_types[0];
               }
               break;
         }
      }
      maze[yStart][xStart] = ImageManager.coordinate_types[0];
      return false;
   }

   /**
    * Returns an array list of coordinates for possible start and target locations.
    * Coordinates are listed in the order: {x_start, y_start, x_end, y_end}.
    * In order for this function to work properly maze must be well boundaried.
    * @param maze 2D input maze
    * @return ArrayList of possible start and target coordinates, null if cannot find
    */
   public static ArrayList<Integer> start_exitFounder(int[][] maze) {
      ArrayList<Integer> start_exit_coordinates = new ArrayList<>();
      for (int i = 0; i < maze.length; i++) {
         if (maze[i][0] == 0) {
            start_exit_coordinates.add(0);
            start_exit_coordinates.add(i);
            break;
         }
      }
      for (int i = 0; i < maze.length; i++) {
         if (maze[i][maze[i].length-1] == 0) {
            start_exit_coordinates.add(maze[i].length-1);
            start_exit_coordinates.add(i);
            break;
         }
      }
      for (int j = 1; j < maze[0].length-1; j++) {
         if (maze[0][j] == 0) {
            start_exit_coordinates.add(j);
            start_exit_coordinates.add(0);
            break;
         }
      }
      for (int j = 0; j < maze[0].length-1; j++) {
         if (maze[maze.length-1][j] == 0) {
            start_exit_coordinates.add(j);
            start_exit_coordinates.add(maze.length-1);
            break;
         }        
      }
      for (int coordinate : start_exit_coordinates)
         if (coordinate != 0)
            return start_exit_coordinates;
      return null;
   }

   /***
    * Returns the movable directions at the input x/y coordinates from the input maze in the form ArrayList<Direction>.
    * @param maze int[][] which represents a maze formed by 0s and 1s
    * @param x    x-coordinate of the current position
    * @param y    y-coordinate of the current position
    * @return movable directions' array-list
    */
   public static ArrayList<Direction> movableDirections(int[][] maze, int x, int y) {
      ArrayList<Direction> directions = new ArrayList<Direction>();
      if (x < maze[y].length-1 && maze[y][x+1] == ImageManager.coordinate_types[0])
         directions.add(Direction.EAST);
      if (x > 0 && maze[y][x-1] == ImageManager.coordinate_types[0])
         directions.add(Direction.WEST);
      if (y < maze.length-1 && maze[y+1][x] == ImageManager.coordinate_types[0])
         directions.add(Direction.SOUTH);
      if (y > 0 && maze[y-1][x] == ImageManager.coordinate_types[0]) 
         directions.add(Direction.NORTH);
      return directions;
   }

   /**
    * Returns the input maze as a String. 
    * 0-> empty space | 1-> wall | 2-> generated wall | 5-> taken path
    * @param maze int[][] which represents a maze formed by 0s and 1s
    * @return input maze
    */
   public static String mazeString(int[][] maze) {
      String str = "";
         for (int i=0; i < maze.length; i++) {
            str += "\n";
            for (int j=0; j < maze[i].length; j++) {
               str += maze[i][j] + " ";
         }
      }
      return str;
   }
}
