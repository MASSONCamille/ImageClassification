package ImageClassification.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Alexandre Blansché
 * Panneau d'affichage d'une image
 */
public class ImagePanel extends JPanel
{
    private BufferedImage image;
    private JLabel imageLabel, textLabel;
    private JPanel imagePanel;
    private double scale;

    /**
     * @param image L'image à afficher
     * @param name Le nom du panneau
     * @param parent Le parent pour la gestion des événements
     */
    public ImagePanel (BufferedImage image, String name, MultipleViewPanel parent)
    {
        this.textLabel = new JLabel (name);
        BackgroundPanel background = new BackgroundPanel ();
        this.imagePanel = new JPanel ();
        background.add (this.imagePanel);
        this.imagePanel.setOpaque (false);
        this.imageLabel = new JLabel ();
        this.updateImage (image);
        this.imagePanel.add (this.imageLabel);
        this.imagePanel.setBorder (new EmptyBorder (-5, -5, -5, -5));
        background.setBorder (new EmptyBorder (-5, -5, -5, -5));
        this.imagePanel.addMouseMotionListener (parent);
        this.imagePanel.addMouseListener (parent);
        this.imagePanel.addMouseWheelListener (parent);
        this.setLayout (new BorderLayout ());
        this.add (this.textLabel, BorderLayout.PAGE_START);
        this.add (background, BorderLayout.CENTER);
    }

    /**
     * @param name Le nom du panneau
     * @param parent Le parent pour la gestion des événements
     */
    public ImagePanel (String name, MultipleViewPanel parent)
    {
        this (null, name, parent);
    }
    
    private static BufferedImage getScaledImage (BufferedImage image, Dimension size)
    {
        BufferedImage resized = new BufferedImage ((int) size.getWidth (), (int) size.getHeight (), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics ();

        g2.setRenderingHint (RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage (image, 0, 0, (int) size.getWidth (), (int) size.getHeight (), null);
        g2.dispose();

        return resized;
    }

    /**
     * Modification de l'image
     * @param image La nouvelle image
     */
    public void updateImage (BufferedImage image)
    {
        double scale = 1;
        this.image = image;
        if (this.image != null)
        {
            Dimension size = new Dimension (this.image.getWidth (), this.image.getHeight ());
            Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
            int maxWidth = (int) (screenSize.getWidth () / 2.5);
            int maxHeight = (int) (screenSize.getHeight () / 2.5);
            scale = Math.min (maxWidth / size.getWidth (), maxHeight / size.getHeight ());
            size = new Dimension ((int) (size.getWidth () * scale), (int) (size.getHeight () * scale));

            this.imageLabel.setIcon (new ImageIcon (ImagePanel.getScaledImage (image, size)));
            this.imageLabel.setMinimumSize (size);
            this.imageLabel.setMaximumSize (size);
            this.imageLabel.setPreferredSize (size);
            this.imageLabel.setMinimumSize (size);
            this.imageLabel.setMaximumSize (size);
            this.imagePanel.setPreferredSize (size);
            this.imagePanel.setMinimumSize (size);
            this.imagePanel.setMaximumSize (size);
            size = new Dimension ((int) size.getWidth (), this.textLabel.getPreferredSize ().height);
            this.textLabel.setPreferredSize (size);
            this.textLabel.setMinimumSize (size);
            this.textLabel.setMaximumSize (size);
        }
        this.scale = 1 / scale;
    }

    /**
     * Modification du nom
     * @param name Le nouveau nom
     */
    public void updateName (String name)
    {
        this.textLabel.setText (name);
        this.textLabel.paintImmediately (this.getVisibleRect ());
    }

    /**
     * @return L'échelle de l'image
     */
    public double getScale ()
    {
        return this.scale;
    }
}
