import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Board extends JPanel implements KeyListener {

    private static final long serialVersion = 1L;

    private BufferedImage blocks, background, pause, refresh;
// block size
    private final int blockSize = 30;
//board dimension
    private final int boardWidth = 10, boardHeight = 20;
// field
    private int[][] board = new int[boardHeight][boardWidth];
// array of all possible shapes
    private Shapes[] shapes = new Shapes[7];
// current shape
    private Shapes currentShape;
// game loop
    private Timer timer;

    private  final int FPS = 60;

    private final int delay = 1000/FPS;

    private int mouseX, mouseY;

    private boolean leftClick = false;

    private Rectangle stopBounds, refreshBounds;

    private boolean pauseGame = false;

    private boolean gameOver = false;

    // buttons lapse

    private Timer buttonLapse = new Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });

    private int score = 0;

    public Board() {

        try {
            URL resource = Board.class.getResource("/resources/sprites.png");
            blocks = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
        shapes[0] = new Shapes(blocks.getSubimage(0,0,blockSize,blockSize),new int[][]{
                {1,1,1,1} // I Shape
        }, this, 1);

        shapes[1] = new Shapes(blocks.getSubimage(blockSize,0,blockSize,blockSize),new int[][]{
                {1,1,0},
                {0,1,1} // Z Shape
        }, this, 2);

        shapes[2] = new Shapes(blocks.getSubimage(blockSize*2,0,blockSize,blockSize),new int[][]{
                {0,1,1},
                {1,1,0} // S Shape
        }, this, 3);

        shapes[3] = new Shapes(blocks.getSubimage(blockSize*3,0,blockSize,blockSize),new int[][]{
                {1,1,1},
                {0,0,1} // J Shape
        }, this, 4);

        shapes[4] = new Shapes(blocks.getSubimage(blockSize*4,0,blockSize,blockSize),new int[][]{
                {1,1,1},
                {1,0,0} // L Shape
        }, this, 5);

        shapes[5] = new Shapes(blocks.getSubimage(blockSize*5,0,blockSize,blockSize),new int[][]{
                {1,1,1},
                {0,1,0} // T Shape
        }, this, 6);

        shapes[6] = new Shapes(blocks.getSubimage(blockSize*6,0,blockSize,blockSize),new int[][]{
                {1,1},
                {1,1} // 0 Shape
        }, this, 7);

       setNextShape();

    }
    public void update(){

        currentShape.update();
        if (gameOver)
            timer.stop();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        currentShape.render(g);

        for (int row = 0; row < board.length; row++)
            for (int column = 0; column < board[row].length; column++)
                if (board[row][column] !=0)
                g.drawImage(blocks.getSubimage((board[row][column]-1)*blockSize,0, blockSize, blockSize),
                        column*blockSize, row*blockSize, null);

        for (int i = 0; i < boardHeight; i++){
            g.drawLine(0, i*blockSize, boardWidth*blockSize, i*blockSize);

        }
        for (int j = 0; j < boardWidth; j++){
            g.drawLine(j*blockSize, 0,j*blockSize, boardHeight*blockSize);
        }
    }

    public void setNextShape(){
        int index = (int)(Math.random()*shapes.length);

        Shapes newShape = new Shapes(shapes[index].getBlock(),
                shapes[index].getCoordinates(), this, shapes[index].getColor());
        currentShape = newShape;

        for (int row = 0; row < currentShape.getCoordinates().length; row++)
            for (int column = 0; column < currentShape.getCoordinates()[row].length; column++)
                if (currentShape.getCoordinates()[row][column] != 0){
                    if (board[row][column + 3] != 0)
                        gameOver = true;
                }
    }
    public int getBlockSize(){
        return blockSize;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
        currentShape.setDeltaX(-1);
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            currentShape.setDeltaX(1);
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.speedDown();
        if (e.getKeyCode() == KeyEvent.VK_UP)
            currentShape.rotate();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.normalSpeed();
    }
    public int[][] getBoard(){
        return board;
    }
}