package ImageClassification.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Alexandre Blansché
 * Fond en damier pour les images avec transparence
 */
public class BackgroundPanel extends JPanel
{
    private static final int SIZE = 10;
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        int width = this.getWidth ();
        int height = this.getHeight ();
        Color dark = new Color (102, 102, 102);
        Color light = new Color (153, 153, 153);
        BufferedImage image = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                boolean xbool = x % (BackgroundPanel.SIZE * 2) < BackgroundPanel.SIZE;
                boolean ybool = y % (BackgroundPanel.SIZE * 2) < BackgroundPanel.SIZE;
                Color col = dark;
                if (xbool == ybool)
                    col = light;
                image.setRGB(x, y, col.getRGB ());
            }
        g.drawImage(image, 0, 0, this);
    }
}
