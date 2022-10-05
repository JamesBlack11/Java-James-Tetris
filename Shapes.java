import java.awt.*;
import java.awt.image.BufferedImage;

public class Shapes {

    private BufferedImage block;
    private int[][] coordinates;
    private Board board;
    private int deltaX = 0;
    private int x, y;
    private int color;
    private boolean collision = false, moveX = false;

    private int normalSpeed = 600,speedDown = 60, currentSpeed;
    private long time, lastTime;


    public Shapes(BufferedImage block, int[][] coordinates, Board board, int color){
     this.block = block;
     this.coordinates = coordinates;
     this.board = board;
     this.color = color;

     currentSpeed = normalSpeed;
     time = 0;
     lastTime = System.currentTimeMillis();
     x = 3;
     y = 0;



    }
    public void update(){
        time += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (collision)
        {
            for (int row = 0; row < coordinates.length; row++)
                for (int column = 0; column < coordinates[row].length; column++)
                    if (coordinates[row][column] !=0)
                        board.getBoard()[y + row][x + column] = color;

                    checkLine();
            board.setNextShape();
        }

        if (!(x+deltaX+coordinates[0].length > 10) && !(x + deltaX < 0)){

            for (int row = 0; row < coordinates.length; row++)
                for (int column = 0; column < coordinates[row].length; column++)
                    if (coordinates[row][column] !=0){
                        if (board.getBoard()[y + row][x + deltaX + column] !=0)
                            moveX = false;
                    }
            if (moveX)
            x += deltaX;
        }


        if (!(y+ 1 + coordinates.length > 20))
        {

            for (int row = 0; row < coordinates.length; row++)
                for (int column = 0; column < coordinates[row].length; column++)
                    if (coordinates[row][column] !=0)
                    {
                        if(board.getBoard()[y + row + 1][column + x] !=0)
                            collision = true;
                    }

            if (time > currentSpeed)
            {
                y++;
                time = 0;
            }
        } else {
            collision = true;
        }



        deltaX = 0;
        moveX = true;
    }
    public void render(Graphics g) {
    for (int row = 0; row < coordinates.length; row++)
        for (int column = 0; column < coordinates[row].length; column++)
            if (coordinates[row][column] != 0)
                g.drawImage(block, column*board.getBlockSize() + x*board.getBlockSize(),
                        row*board.getBlockSize() + y*board.getBlockSize(), null);
    }
    private void checkLine(){
        int height = board.getBoard().length-1;

        for (int i = height; i > 0; i--){
            int count = 0;
            for (int j = 0; j < board.getBoard()[0].length; j++){
                if (board.getBoard()[i][j] !=0)
                    count ++;
                board.getBoard()[height][j] = board.getBoard()[i][j];
            }
            if(count < board.getBoard()[0].length)
                height--;
        }
    }

    public void rotate(){
        if (collision)
            return;

        int[][] rotateMatrix = null;

        rotateMatrix = getTranspose(coordinates);

        rotateMatrix = getReverseMatrix(rotateMatrix);

        if (x + rotateMatrix[0].length > 10 || y + rotateMatrix.length > 20)
            return;


        for (int row = 0; row < rotateMatrix.length; row++){
            for (int column = 0; column < rotateMatrix.length; column++){
                if (board.getBoard()[y + row][x + column] != 0){
                    return;
                }
            }
        }

        coordinates = rotateMatrix;
    }

    private int[] [] getTranspose(int[][] matrix){
        int[][] newMatrix = new int[matrix[0].length][matrix.length];
            for(int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[i].length; j++)
                    newMatrix[j][i] = matrix[i][j];
                return newMatrix;
    }

    private int[][] getReverseMatrix(int [][] matrix){
        int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++){
            int[] mat = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length- i - 1] = mat;
        }
        return matrix;
    }
   public void setDeltaX(int deltaX){
        this.deltaX = deltaX;

   }
   public void normalSpeed(){
        currentSpeed = normalSpeed;
   }
   public void speedDown(){
        currentSpeed = speedDown;
    }

    public BufferedImage getBlock(){
        return block;
    }

    public int[][] getCoordinates(){
        return coordinates;
    }

    public int getColor(){
        return color;
    }
}
