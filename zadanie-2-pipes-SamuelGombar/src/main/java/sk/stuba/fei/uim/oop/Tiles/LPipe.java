package sk.stuba.fei.uim.oop.Tiles;

import java.awt.*;

public class LPipe extends Tile {
    private final double size;
    public LPipe(double size) {
        this.size = size;
        this.setOpeningNorth(false);
        this.setOpeningSouth(true);
        this.setOpeningEast(true);
        this.setOpeningWest(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = ((Graphics2D)g.create());
        applyRotation(g2d, this.size);
        drawTile(g2d);
        drawHighlight(g);
        drawFlow(g);
    }

    @Override
    public void drawTile(Graphics2D g2d) {
        g2d.drawPolyline(new int[]{this.getWidth() / 3, this.getWidth() / 3, this.getWidth()},
                new int[]{this.getHeight(), this.getHeight() / 3, this.getHeight() / 3}, 3);
        g2d.drawPolyline(new int[]{2 * (this.getWidth() / 3), 2 * (this.getWidth() / 3), this.getWidth()},
                new int[]{this.getHeight(), 2 * (this.getHeight() / 3), 2 * (this.getHeight() / 3)}, 3);
    }
}
