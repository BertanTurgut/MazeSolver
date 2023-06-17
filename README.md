# MazeSolver
**This program takes a rectengular well-boundaried maze image and stores the image of solved maze map at solutions directory.**
### How To Use?
1. Select images' of mazes, similar to the example ones, and load them to **test_mazes** directory with the name format "maze_(number)".
2. While program is running, you will be asked to enter an input number for desired maze to be solved or simply to enter "exit" to terminate program.
3. If the maze is solvable and compatible with the programs' requirements (see [technical remarks](#technical-remarks)), the solved image will be stored at **test_solutions** directory with the name format "maze_(number)_solution".
### Technical Remarks
* Loaded images must be either in .png or .jpg format to be succesfully read.
* Stored solutions will have .png format.
* In order for program to process the maze succesfully:
  * maze should be in a rectengular shape
  * maze should be well-boundaried, meaning outermost walls should be clearly defined 
  * there should be at least two gaps at the outermost walls in order for `Solver.start_exitFounder(int[][] maze)` to find two candidate points to match
* In some cases resolution scope of the program or brightness limit value (which is used for seperating blank spaces from maze walls) may lead the program to wrong directions. In such cases, you may have to manually calibrate the final static data of `ImageManager`:
  ```java
   public static final float brightness_limit = 0.5f; // adjustable, must be between 0.0f - 1.0f
   public static final int[] matrix_sizes = {200, 200}; // adjustable, two indexes must be the same value
   public static final int[] coordinate_types = {0, 1, 2, 5}; // DO NOT TOUCH
   public static final int solution_image_multiplier = 10; // DO NOT TOUCH
  ```
  1. Brightness value is calculated in [HSB Color Model](https://www.learnui.design/blog/the-hsb-color-system-practicioners-primer.html), thus `brightness_limit` must be a float between 0.0f - 1.0f. Decreasing the limit will make image processing possibly more selective, on the other hand increasing it will make program to possibly classify more pixels as walls. 
  2. After different test cases I have observed that {200, 200} for `matrix_sizes` are suitable values for being default resolution, since they handle the majority of the mazes succesfully. But for exceptionally detailed mazes, where blank spaces and walls are very small, you may need to increase the resolution values. **_Note that two values of `matrix_sizes` must be equal. Besides increasing resolution values will cause program to run slower and possibly process simpler graphs wrong. For these reasons I recommend you to change these values carefully and slowly_.**

 





