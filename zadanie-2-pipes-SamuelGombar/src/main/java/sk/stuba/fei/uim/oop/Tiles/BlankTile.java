package sk.stuba.fei.uim.oop.Tiles;

import java.awt.*;

public class BlankTile extends Tile {
    public BlankTile() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void drawTile(Graphics2D g2d) {
    }
}
