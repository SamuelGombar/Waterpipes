package sk.stuba.fei.uim.oop.Tiles;

import java.awt.*;

public class Finish extends Tile {
    public Finish() {
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
        boolean temp = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (temp) {
                    g2d.setColor(Color.WHITE);
                    temp = false;
                }
                else {
                    g2d.setColor(Color.BLACK);
                    temp = true;
                }
                g2d.fillRect(j * (this.getWidth()/6), i * (this.getHeight()/6), this.getWidth()/6, this.getHeight()/6);
            }
            temp = !temp;
        }
    }
}
