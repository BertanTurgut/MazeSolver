import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      String input = "";
         System.out.println("_____________"+"\n|MAZE SOLVER|"+"\n*************"+
                           "\n(*) Load the desired maze images similar to example mazes, with the name format: \"maze_(number)\". If there is a solution to the maze, solved maze image will be stored at solutions file."+
                           "\n(*) Loaded maze images must have either .png or .jpg format, stored solutions have .png format."+
                           "\n(*) Feel free to change matrix_values at ImageManager class accordingly to manage resolution issues."+
                           "\n(*) And of course feel free to comment and share your ideas with me :)");
      do {
         System.out.println("(*) Enter the number of desired input maze to store solution, write \"exit\" to end program: ");
         input = scanner.nextLine();   
         int test_number = 0; 
         try {
            if (!input.equals("exit"))
               test_number = Integer.parseInt(input); // input maze image number
            else {
               System.out.println("Program terminated.");
               System.exit(0);
            }
         }
         catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
            continue;
         }
         String filepath_read_png = ".\\MazeSolver\\test_mazes\\maze_"+test_number+".png"; // try first
         String filepath_read_jpg = ".\\MazeSolver\\test_mazes\\maze_"+test_number+".jpg"; // try second
         String filepath_write = ".\\MazeSolver\\test_solutions\\maze_"+test_number+"_solution.png";
         BufferedImage image = null;
         try {
            image = ImageManager.loadImage(filepath_read_png);
         }
         catch (IOException e_png) {
            try {
               image = ImageManager.loadImage(filepath_read_jpg);
            }
            catch (IOException e_jpg) {
               System.out.println("Cannot read file from input filepaths "+filepath_read_png+" or "+filepath_read_jpg+"\n");
               continue;
            }
         }
         int[][] maze = ImageManager.matrixConverter(image, ImageManager.matrix_sizes[0], ImageManager.matrix_sizes[1]);
         ImageManager.matrixRegenerator(maze);
         System.out.println(Solver.mazeString(maze));
         boolean isSolved = false;
         try {
            ArrayList<Integer> suspects = Solver.start_exitFounder(maze);
            isSolved = Solver.mazeSolver(maze, suspects.get(0), suspects.get(1), suspects.get(2), suspects.get(3));
         }
         catch (NullPointerException e) {
            System.out.println("Couldn't find any target and starting points.");
         }
         if (isSolved) {
            System.out.println("Maze is solved.");
            image = ImageManager.imageConverter(maze, ImageManager.solution_image_multiplier);
            try {
               ImageManager.writeImage(filepath_write, image);
            }
            catch (IOException e) {
               System.out.println("Solution image cannot be stored at "+filepath_write);
            }
         }
         else 
            System.out.println("Maze has no solutions.");
         System.out.println("");
      } while (!input.equals("exit"));
      scanner.close();
   }
}
