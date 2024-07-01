package sk.stuba.fei.uim.oop.Tiles;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.Rotation;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public abstract class Tile extends JPanel {
    @Setter @Getter
    private boolean flow;
    @Setter @Getter
    private boolean pathCreated;
    @Setter @Getter
    private int row;
    @Setter @Getter
    private int column;
    @Setter @Getter
    private boolean visited;
    @Setter @Getter
    private boolean highlight;
    @Setter @Getter
    private boolean openingNorth;
    @Setter @Getter
    private boolean openingSouth;
    @Setter @Getter
    private boolean openingEast;
    @Setter @Getter
    private boolean openingWest;
    @Setter @Getter
    private Rotation rotation;
    @Setter @Getter
    private Rotation firstCorrectRotation;
    @Setter @Getter
    private Rotation secondCorrectRotation;
    @Setter @Getter
    private Random rand;

    public Tile() {
        this.rand = new Random();
        this.rotation = randomRotation();
    }

    public boolean checkCorrectRotation() {
        if (this.rotation == this.firstCorrectRotation) {
            return true;
        }
        else {
            return this.rotation == this.secondCorrectRotation;
        }
    }

    public Rotation randomRotation() {
        Rotation[] rotations = Rotation.values();
        return rotations[this.rand.nextInt(rotations.length)];
    }

    public void rotate() {
        this.rotateOpenings();
        this.nextRotation();
    }

    public void nextRotation() {
        this.rotation = this.rotation.next();
    }

    public void rotateOpenings() {
        boolean temp = this.isOpeningNorth();
        this.setOpeningNorth(this.isOpeningWest());
        this.setOpeningWest(this.isOpeningSouth());
        this.setOpeningSouth(this.isOpeningEast());
        this.setOpeningEast(temp);
    }

    public void applyRotation(Graphics2D g2d, double size) {
        switch (this.getRotation()) {
            case DEGREES90:
                g2d.rotate(Math.toRadians(90), 500 / size / 2, 500 / size / 2);
                break;
            case DEGREES180:
                g2d.rotate(Math.toRadians(180), 500 / size / 2, 500 / size / 2);
                break;
            case DEGREES270:
                g2d.rotate(Math.toRadians(270), 500 / size / 2, 500 / size / 2);
                break;
            default:
                break;
        }
    }

    public void drawHighlight(Graphics g) {
        if (this.isHighlight()) {
            g.setColor(Color.RED);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawRect(0, 0, this.getWidth(), this.getHeight());
            this.setHighlight(false);
        }
    }

    public void drawFlow(Graphics g) {
        if (this.isFlow()) {
            g.setColor(Color.BLUE);
            ((Graphics2D) g).setStroke((new BasicStroke(3)));
            g.drawRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    public abstract void drawTile(Graphics2D g2d);
}
