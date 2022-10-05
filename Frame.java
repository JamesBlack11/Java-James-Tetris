import javax.swing.*;

public class Frame {
public static final int WIDTH = 307, HEIGHT = 630;
    private JFrame frame;
    private Board board;


public Frame() {
    frame = new JFrame("Tetris Game");
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    board = new Board();
    frame.add(board);
    frame.addKeyListener(board);


}
    public static void main(String[] args) {
    new Frame();
    }
}



