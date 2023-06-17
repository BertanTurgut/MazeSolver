import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageManager {
   public static final float brightness_limit = 0.5f;
   public static final int[] matrix_sizes = {200, 200};
   public static final int[] coordinate_types = {0, 1, 2, 5};
   public static final int solution_image_multiplier = 10;

   /**
    * Reads the input filepath and returns a buffered image.
    * @param filepath filepath to retrieve file from
    * @return buffered image
    */
   public static BufferedImage loadImage(String filepath) throws IOException{           
      try {
         File input_image = new File(filepath);
         BufferedImage buffered_image = ImageIO.read(input_image);
         System.out.println("Loading completed.");
         return buffered_image;
      }
      catch (IOException e) {
         throw e;
      }
   }  

   /**
    * Writes the input image to the input filepath.
    * @param filepath filepath to write the image
    * @param image    buffered image
    */
   public static void writeImage(String filepath, BufferedImage image) throws IOException{
      try {
         File file = new File(filepath);
         ImageIO.write(image, "png", file);
         System.out.println("Storing completed.");
      }
      catch (IOException e) {
         throw e;
      }
   }

   /**
    * Converts the input 2D array into a buffered image which has size of input matrix times multiplier.
    * @param matrix 2D array matrix
    * @param multiplier manages output image resolution
    * @return buffered image of the input matrix
    */
   public static BufferedImage imageConverter(int[][] matrix, int multiplier) {
      int width = matrix[0].length*multiplier;
      int height = matrix.length*multiplier;
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      for (int i = 0; i < matrix.length; i++) {
         for (int j = 0; j < matrix[i].length; j++) {
            if (matrix[i][j] == coordinate_types[1]) {
               for (int a = i*multiplier; a < i*multiplier + multiplier; a++) {
                  for (int b = j*multiplier; b < j*multiplier + multiplier; b++) {
                     image.setRGB(b, a, Color.black.getRGB());
                  }
               }
            }
            else if (matrix[i][j] == coordinate_types[0] || matrix[i][j] == coordinate_types[2]) {
               for (int a = i*multiplier; a < i*multiplier + multiplier; a++) {
                  for (int b = j*multiplier; b < j*multiplier + multiplier; b++) {
                     image.setRGB(b, a, Color.white.getRGB());
                  }
               }
            }
            else if (matrix[i][j] == coordinate_types[3]) {
               for (int a = i*multiplier; a < i*multiplier + multiplier; a++) {
                  for (int b = j*multiplier; b < j*multiplier + multiplier; b++) {
                     image.setRGB(b, a, Color.red.getRGB());
                  }
               }
            }
         }
      }
      return image;
   }

   /**
    * Returns a regenarated matrix which is isomorphic to the input matrix.
    * This proccess is done in order to allow the maze solving algorithm to run a lot faster.
    * @param matrix 2D array representation of a maze
    * @return regenerated isomorphic form of input matrix
    */
   public static int[][] matrixRegenerator(int[][] matrix) {
      int generationCounter;
      do {
         generationCounter = 0;
         for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               if (matrix[i][j] == coordinate_types[1] || matrix[i][j] == coordinate_types[2]) {
                  for (String direction : pointRegeneration(matrix, j, i)) {
                     switch (direction) {
                        case "up":
                           matrix[i-1][j] = 3;
                           generationCounter++;
                           break;
                        case "down":
                           matrix[i+1][j] = 3;
                           generationCounter++;
                           break;
                        case "right":
                           matrix[i][j+1] = 3;
                           generationCounter++;
                           break;
                        case "left":
                           matrix[i][j-1] = 3;
                           generationCounter++;
                           break;
                     }
                  }
               }
            }
         }
         for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++) 
               if (matrix[i][j] == 3)
                  matrix[i][j] = coordinate_types[2];
      } while(generationCounter > 0);
      return matrix;
   }

   /**
    * Returns valid generation directions for the point in the input matrix with input coordinates in the form of String[].
    * @param matrix 2D array representation of a maze
    * @param x      x-coordinate of the target point
    * @param y      y-coordinate of the target point
    * @return       String[] of valid generation directions for the input point
    */
   public static String[] pointRegeneration(int[][] matrix, int x, int y) {
      String[] validGenerations = {"", "", "", ""};
      if (y > 1 && x > 0 && x < matrix[y].length-1 && matrix[y-1][x] == coordinate_types[0] && matrix[y-2][x] == coordinate_types[0] && matrix[y-2][x+1] == coordinate_types[0] && matrix[y-2][x-1] == coordinate_types[0]) 
         validGenerations[0] = "up";
      if (y < matrix.length-2 && x > 0 && x < matrix[y].length-1 && matrix[y+1][x] == coordinate_types[0] && matrix[y+2][x] == coordinate_types[0] && matrix[y+2][x+1] == coordinate_types[0] && matrix[y+2][x-1] == coordinate_types[0])
         validGenerations[1] = "down";
      if (x > 1 && y > 0 && y < matrix.length-1 && matrix[y][x-1] == coordinate_types[0] && matrix[y][x-2] == coordinate_types[0] && matrix[y+1][x-2] == coordinate_types[0] && matrix[y-1][x-2] == coordinate_types[0]) 
         validGenerations[2] = "left";  
      if (x < matrix[y].length-2 && y > 0 && y < matrix.length-1 && matrix[y][x+1] == coordinate_types[0] && matrix[y][x+2] == coordinate_types[0] && matrix[y+1][x+2] == coordinate_types[0] && matrix[y-1][x+2] == coordinate_types[0])
         validGenerations[3] = "right";
      return validGenerations; 
   }

   /**
    * Converts the input buffered image into a 2D int array with input height and width and returns it.
    * Forming a maze, 0s represent passable coordinates and 1s represent walls.
    * @param image  buffered image
    * @param width  width for 2D array map
    * @param height height for 2D array map
    * @return 2D array map of the buffered image
    */
   public static int[][] matrixConverter(BufferedImage image, int width, int height) {
      image = boundaryFinder(image);
      float ratio = (float) image.getWidth()/image.getHeight();
      width = (int) (ratio*height);
      System.out.println(height + " " + width);
      int[][] maze = new int[height][width];
      float xStep = (float) image.getWidth()/width;
      float yStep = (float) image.getHeight()/height;
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            if (isAreaDark(image, (int)(j*xStep), (int)(i*yStep), (int)((j+1)*xStep), (int)((i+1)*yStep)))
               maze[i][j] = coordinate_types[1];  
         }
      }
      return maze;
   }

   /**
    * Returns a buffered image which is cropped version of the input maze image accordingly 
    * to the possible boundaries' coordinates.
    * @param mazeImage buffered image of a maze
    * @return cropped version of the maze image
    */
   public static BufferedImage boundaryFinder(BufferedImage mazeImage) {
      int minX = mazeImage.getWidth()+1;
      int maxX = -1;
      int minY = mazeImage.getHeight()+1;
      int maxY = -1;
      for (int i = 0; i < mazeImage.getHeight(); i++) {
         for (int j = 0; j < mazeImage.getWidth(); j++) {
            if (getPixelBrightness(mazeImage, j, i) < brightness_limit && j < minX)
               minX = j;
            if (getPixelBrightness(mazeImage, j, i) < brightness_limit && i < minY)
               minY = i;       
         }
      }
      for (int i = mazeImage.getHeight()-1; i >= 0; i--) {
         for (int j = mazeImage.getWidth()-1; j >= 0; j--) {
            if (getPixelBrightness(mazeImage, j, i) < brightness_limit && j > maxX)
               maxX = j;
            if (getPixelBrightness(mazeImage, j, i) < brightness_limit && i > maxY )
               maxY = i;          
         }
      }
      BufferedImage boundariedImage =new BufferedImage(maxX-minX+1, maxY-minY+1, BufferedImage.TYPE_INT_RGB);
      for (int i = minY; i <= maxY; i++)
         for (int j = minX; j <= maxX; j++)
            boundariedImage.setRGB(j-minX, i-minY, mazeImage.getRGB(j, i));
      return boundariedImage;
   }

   /**
    * Returns whether the area defined by the input coordinates from the input buffered image is dark or not.
    * @param image   buffered image
    * @param x_start starting abscissa limit for the area
    * @param y_start starting ordinate limit for the area
    * @param x_end   ending abscissa limit for the area
    * @param y_end   ending ordinate limit for the area
    * @return whether the area is dark or not
    */
   public static boolean isAreaDark(BufferedImage image, int x_start, int y_start, int x_end, int y_end) {
      if (x_end > image.getWidth())
         x_end = image.getWidth();
      if (y_end > image.getHeight()) 
         y_end = image.getHeight();
      for (int i = y_start; i < y_end; i++) {
         for (int j = x_start; j < x_end; j++) {
            if (getPixelBrightness(image, j, i) < brightness_limit)
               return true;
         }
      }
      return false;
   }

   /**
    * Returns the brightness value of the pixel with the input coordinates from the input image.
    * @param image buffered image
    * @param x     x-coordinate of the current pixel
    * @param y     y-coordinate of the current pixel
    * @return
    */
   public static float getPixelBrightness(BufferedImage image, int x, int y) {
      Color color = new Color(image.getRGB(x, y), true);
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      return hsb[2];
   }
}
