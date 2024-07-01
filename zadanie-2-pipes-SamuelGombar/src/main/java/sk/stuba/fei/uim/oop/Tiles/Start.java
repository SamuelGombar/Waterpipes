package sk.stuba.fei.uim.oop.Tiles;

import javax.swing.*;
import java.awt.*;

public class Start extends Tile {
    public Start() {
        this.setPathCreated(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = ((Graphics2D)g.create());
        drawTile(g2d);
    }

    @Override
    public void drawTile(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.WHITE);
        JLabel start = new JLabel("START");
        start.setForeground(Color.WHITE);
    }
}
