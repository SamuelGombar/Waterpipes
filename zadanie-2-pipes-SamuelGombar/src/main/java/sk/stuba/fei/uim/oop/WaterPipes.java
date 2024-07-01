package sk.stuba.fei.uim.oop;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.Tiles.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WaterPipes extends JFrame implements KeyListener, MouseMotionListener, MouseListener, ActionListener {
    @Getter
    private final JPanel board;
    private int size;
    @Getter @Setter
    private int level;
    @Getter @Setter
    private boolean mazeCreated;
    @Getter @Setter
    private Tile[][] tileSet;
    @Getter @Setter
    private ArrayList<Tile> realPath;
    @Getter @Setter
    private ArrayList<Tile> blankPath;
    @Getter @Setter
    private JComboBox<String> chooseSize;
    @Getter @Setter
    private JLabel levelText;

    public WaterPipes() throws HeadlessException {
        super();
        this.board = new JPanel();
        this.size = 8;
        this.level = 1;
        this.mazeCreated = false;
        this.tileSet = new Tile[this.size][this.size];
        this.realPath = new ArrayList<>();
        this.blankPath = new ArrayList<>();
        this.chooseSize = new JComboBox<>();
        this.levelText = new JLabel();
        this.createWindow();
        this.createMenu();
        this.createInfo();
        this.createNewBoard();
        this.addListeners();
    }
    public void createWindow() {
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createMenu() {
        JPanel menu = new JPanel();
        this.add(menu, BorderLayout.PAGE_END);
        menu.setLayout(new GridLayout(2, 3));
        menu.setBackground(Color.YELLOW);

        updateText();
        menu.add(this.levelText);
        menu.add(new JLabel(" "));
        menu.add(new JLabel(" "));

        JButton reset = new JButton("Reset");
        reset.addActionListener( e -> this.resetGame());
        menu.add(reset);

        JButton check = new JButton("Check");
        check.addActionListener( e -> this.checkFlow());
        menu.add(check);

        this.chooseSize = new JComboBox<>(new String[]{"8x8", "10x10", "12x12", "14x14"});
        this.chooseSize.setFocusable(false);
        reset.setFocusable(false);
        check.setFocusable(false);
        menu.add(this.chooseSize);
    }

    public void createInfo() {
        JPanel info = new JPanel();
        this.add(info, BorderLayout.PAGE_START);
        info.setLayout(new GridLayout(1, 3));
        info.setBackground(Color.YELLOW);

        info.add(new JLabel("ESC - Exit"));
        info.add(new JLabel("R - Reset"));
        info.add(new JLabel("ENTER - Check the flow"));
    }

    public void createNewBoard() {
        setMazeCreated(false);
        newBlankMatrix();
        createMaze();
        this.board.setLayout(new GridLayout(this.size, this.size));
        makeRealPath();
        int startRow = this.realPath.get(0).getRow();
        int startColumn = this.realPath.get(0).getColumn();
        int finishRow = this.realPath.get(this.realPath.size() - 1).getRow();
        int finishColumn = this.realPath.get(this.realPath.size() - 1).getColumn();
        makeStartOpening(startRow, startColumn);
        makeFinishOpening(finishRow, finishColumn);
        applyCorrectRotations();
        this.add(this.board, BorderLayout.CENTER);
        this.repaint();
        this.setVisible(true);
    }

    public void newBlankMatrix() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Tile current = new BlankTile();
                this.tileSet[i][j] = current;
                current.setRow(i);
                current.setColumn(j);
            }
        }
    }

    public void createMaze() {
        Random rand = new Random();
        int i = rand.nextInt(this.size);
        int j = 0;

        Tile startTile = this.tileSet[i][j];
        this.tileSet[i][j].setPathCreated(true);
        this.blankPath = new ArrayList<>();
        randomizedDFS(startTile);
        this.blankPath.add(startTile);
        Collections.reverse(this.blankPath);
    }

    public void randomizedDFS(Tile tile) {
        tile.setVisited(true);
        Tile nextTile = randomUnvisitedTile(tile);
        while (nextTile != null) {
            randomizedDFS(nextTile);
            if (this.isMazeCreated()) {
                this.blankPath.add(nextTile);
                nextTile.setPathCreated(true);
                return;
            }
            nextTile = randomUnvisitedTile(tile);
        }
    }

    public Tile randomUnvisitedTile(Tile tile) {
        int i = tile.getRow();
        int j = tile.getColumn();
        ArrayList<Tile> neighbourTiles = new ArrayList<>();
        if ((i - 1) >= 0) {
            if (!this.tileSet[i - 1][j].isVisited()) {
                neighbourTiles.add(this.tileSet[i - 1][j]);
            }
        }
        if ((i + 1) <= (this.size - 1)) {
            if (!this.tileSet[i + 1][j].isVisited()) {
                neighbourTiles.add(this.tileSet[i + 1][j]);
            }
        }
        if ((j - 1) >= 0) {
            if (!this.tileSet[i][j - 1].isVisited()) {
                neighbourTiles.add(this.tileSet[i][j - 1]);
            }
        }
        if ((j + 1) <= (this.size - 1)) {
            if (!this.tileSet[i][j + 1].isVisited()) {
                neighbourTiles.add(this.tileSet[i][j + 1]);
            }
        }
        if (neighbourTiles.size() == 0) {
            return null;
        }

        Random rand = new Random();
        Tile unvisitedTile = neighbourTiles.get(rand.nextInt(neighbourTiles.size()));
        if (unvisitedTile.getColumn() == this.size - 1) {
            this.setMazeCreated(true);
            unvisitedTile.setPathCreated(true);
            this.blankPath.add(unvisitedTile);
            return null;
        }
        return unvisitedTile;
    }

    public void makeRealPath() {
        Tile current;
        this.realPath.addAll(this.blankPath);
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                current = new BlankTile();
                if (this.tileSet[i][j].isPathCreated()) {
                    for (int k = 0; k < this.blankPath.size(); k++) {
                        if ((i == this.blankPath.get(k).getRow()) && (j == this.blankPath.get(k).getColumn())) {
                            if (k == 0) {
                                current = new Start();
                                this.realPath.set(k, current);
                            }
                            else if (k == this.blankPath.size() - 1) {
                                current = new Finish();
                                this.realPath.set(k, current);
                            }
                            else {
                                if (this.blankPath.get(k - 1).getRow() == this.blankPath.get(k + 1).getRow() ||
                                        this.blankPath.get(k - 1).getColumn() == this.blankPath.get(k + 1).getColumn()) {
                                    current = new IPipe(this.size);
                                    this.realPath.set(k, current);
                                }
                                else {
                                    current = new LPipe(this.size);
                                    this.realPath.set(k, current);
                                }
                            }
                        }
                    }
                }
                else {
                    current = new BlankTile();
                }
                current.setRow(i);
                current.setColumn(j);
                current.randomRotation();
                this.board.add(current);
            }
        }
    }

    public void makeStartOpening(int y, int x) {
        if (this.realPath.get(1).getRow() == (y - 1)) {
            this.realPath.get(0).setOpeningNorth(true);
        }
        else if (this.realPath.get(1).getRow() == (y + 1)) {
            this.realPath.get(0).setOpeningSouth(true);
        }
        else if (this.realPath.get(1).getColumn() == (x + 1)) {
            this.realPath.get(0).setOpeningEast(true);
        }
    }

    public void makeFinishOpening(int y, int x) {
        if (this.realPath.get(this.realPath.size() - 2).getRow() == (y - 1)) {
            this.realPath.get(this.realPath.size() - 1).setOpeningNorth(true);
        }
        else if (this.realPath.get(this.realPath.size() - 2).getRow() == (y + 1)) {
            this.realPath.get(this.realPath.size() - 1).setOpeningSouth(true);
        }
        else if (this.realPath.get(this.realPath.size() - 2).getColumn() == (x - 1)) {
            this.realPath.get(this.realPath.size() - 1).setOpeningWest(true);
        }
    }

    public void applyCorrectRotations() {
        Tile current;
        int currentRow;
        int currentColumn;
        int previousRow;
        int previousColumn;
        int nextRow;
        int nextColumn;
        for (int i = 1; i < this.realPath.size() - 1; i++) {
            current = this.realPath.get(i);
            currentRow = this.realPath.get(i).getRow();
            currentColumn = this.realPath.get(i).getColumn();
            previousRow = this.realPath.get(i - 1).getRow();
            previousColumn = this.realPath.get(i - 1).getColumn();
            nextRow = this.realPath.get(i + 1).getRow();
            nextColumn = this.realPath.get(i + 1).getColumn();


            if (current instanceof IPipe) {
                if (previousRow == nextRow) {
                    current.setFirstCorrectRotation(Rotation.DEGREES90);
                    current.setSecondCorrectRotation(Rotation.DEGREES270);
                }
                if (previousColumn == nextColumn) {
                    current.setFirstCorrectRotation(Rotation.DEFAULT);
                    current.setSecondCorrectRotation(Rotation.DEGREES180);
                }
            }
            if (current instanceof LPipe) {
                if ((previousColumn == currentColumn) && (previousRow == (currentRow + 1))) {
                    if (nextColumn == currentColumn + 1) {
                        current.setFirstCorrectRotation(Rotation.DEFAULT);
                    } else {
                        current.setFirstCorrectRotation(Rotation.DEGREES90);
                    }
                }
                if ((previousRow == currentRow) && (previousColumn == (currentColumn - 1))) {
                    if (nextRow == currentRow + 1) {
                        current.setFirstCorrectRotation(Rotation.DEGREES90);
                    } else {
                        current.setFirstCorrectRotation(Rotation.DEGREES180);
                    }
                }
                if ((previousColumn == currentColumn) && (previousRow == (currentRow - 1))) {
                    if (nextColumn == currentColumn - 1) {
                        current.setFirstCorrectRotation(Rotation.DEGREES180);
                    } else {
                        current.setFirstCorrectRotation(Rotation.DEGREES270);
                    }
                }
                if ((previousRow == currentRow) && (previousColumn == (currentColumn + 1))) {
                    if (nextRow == currentRow - 1) {
                        current.setFirstCorrectRotation(Rotation.DEGREES270);
                    } else {
                        current.setFirstCorrectRotation(Rotation.DEFAULT);
                    }
                }
            }
        }
    }

    public void addListeners() {
        this.addKeyListener(this);
        this.board.addMouseMotionListener(this);
        this.board.addMouseListener(this);
        this.chooseSize.addActionListener(this);
    }

    public void resetGame() {
        this.tileSet = new Tile[this.size][this.size];
        this.realPath = new ArrayList<>();
        this.blankPath = new ArrayList<>();
        this.board.removeAll();
        this.level = 1;
        this.updateText();
        this.createNewBoard();
    }

    public void nextLevel() {
        this.tileSet = new Tile[this.size][this.size];
        this.realPath = new ArrayList<>();
        this.blankPath = new ArrayList<>();
        this.board.removeAll();
        this.level++;
        this.updateText();
        this.createNewBoard();

    }

    public void checkFlow() {
        for (int i = 1; i < this.realPath.size() - 1; i++) {
            if (this.realPath.get(i).isFlow()) {
                for (int j = 1; j < this.realPath.size() - 1; j++) {
                    this.realPath.get(j).setFlow(false);
                }
                break;
            }
            if (this.realPath.get(i).checkCorrectRotation()) {
                this.realPath.get(i).setFlow(true);
            }
            else {
                break;
            }
        }
        this.repaint();
        if (this.realPath.get(this.realPath.size() - 2).isFlow()) {
            nextLevel();
        }
    }

    public void updateText() {
        this.levelText.setText("Level " + this.level);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(this.chooseSize.getItemAt(this.chooseSize.getSelectedIndex()));
        switch(this.chooseSize.getItemAt(this.chooseSize.getSelectedIndex())) {
            case "8x8":
                this.size = 8;
                break;
            case "10x10":
                this.size = 10;
                break;
            case "12x12":
                this.size = 12;
                break;
            case "14x14":
                this.size = 14;
                break;
        }
        resetGame();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component current = this.board.getComponentAt(e.getPoint());
        if (current instanceof Start) {
            return;
        }
        if (current instanceof Finish) {
            return;
        }
        if (current instanceof Tile) {
            System.out.println(e.getPoint());
            ((Tile) current).rotate();
            ((Tile) current).setHighlight(true);
            this.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.dispose();
                System.exit(0);
                break;
            case KeyEvent.VK_ENTER:
                this.checkFlow();
                break;
            case KeyEvent.VK_R:
                resetGame();
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.board.getComponentAt(e.getPoint());
        if (!(current instanceof Tile)) {
            return;
        }
        ((Tile) current).setHighlight(true);
        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /*
    JSlider slider = new JSlider(JSlider.VERTICAL, 0, 50, 22);
    slider.setMinorTickingSpacing(10);
    slider.setMajorTickingSpacing(10);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    this.add(slider);
    slider.addChangeListener(this);     //MUSIS IMPLEMENTOVAT CHANGE LISTENER, metoda stateChanged(),

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println(((JSlider) e.getSource()).getValue());
    }
     */
}
