package ImageClassification.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Alexandre Blansché
 * Données extraites d'une image
 */
public class ImageData extends Data
{
    private int width, height;
    
    private static double [][] getDataMatrix (BufferedImage image)
    {
        int width = image.getWidth ();
        int height = image.getHeight ();
        double [][] data = new double [width * height][3];
        int index = 0;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                int pixel = image.getRGB (x, y);
                data [index][0] = ((pixel >> 16) & 0xff) / 255.;
                data [index][1] = ((pixel >> 8) & 0xff) / 255.;
                data [index][2] = (pixel & 0xff) / 255.;
                index++;
            }
        return data;
    }

    /**
     * @param image Une image
     */
    public ImageData (BufferedImage image)
    {
        super (ImageData.getDataMatrix (image));
        this.width = image.getWidth ();
        this.height = image.getHeight ();
    }

    /**
     * @param data Les données sous la forme d'un tableau à deux dimensions
     * @param width La largeur de l'image
     * @param height La hauteur de l'image
     */
    public ImageData (double [][] data, int width, int height)
    {
        super (data);
        this.width = width;
        this.height = height;
    }

    /**
     * @param data Les données sous la forme d'un tableau à deux dimensions
     * @param width La largeur de l'image
     */
    public ImageData (double [][] data, int width)
    {
        this (data, width, data.length / width);
    }
    
    private static double [][] getDataMatrix (double [] data)
    {
        double [][] colorData = new double [data.length][3];
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < 3; j++)
                colorData [i][j] = data [i];
        return colorData;        
    }

    /**
     * @param data Les données sous la forme d'un tableau à une dimension (un seul canal de couleur)
     * @param width La largeur de l'image
     * @param height La hauteur de l'image
     */
    public ImageData (double [] data, int width, int height)
    {
        this (ImageData.getDataMatrix (data), width, height);
    }

    /**
     * @param data Les données sous la forme d'un tableau à une dimension (un seul canal de couleur)
     * @param width La largeur de l'image
     */
    public ImageData (double [] data, int width)
    {
        this (data, width, data.length / width);
    }

    /**
     * @return Une image à partir des données
     */
    public BufferedImage toImage ()
    {
        BufferedImage image = new BufferedImage (this.width, this.height, BufferedImage.TYPE_INT_RGB);
        double [][] data = this.getDataMatrix ();

        int index = 0;
        for (int x = 0; x < this.width; x++)
            for (int y = 0; y < this.height; y++)
            {
                int pixel = ((int) (data [index][0] * 255) << 16) + ((int) (data [index][1] * 255) << 8) + (int) (data [index][2] * 255);
                image.setRGB(x, y, pixel);
                index++;
            }
        return image;
    }
    
    private static boolean [] isGreaterThan (double [] alpha, double threshold)
    {
        boolean [] greaterThan = new boolean [alpha.length];
        for (int i = 0; i < alpha.length; i++)
            greaterThan [i] = alpha [i] >= threshold;
        return greaterThan;
    }

    /**
     * @param visible Visibilité des pixels de l'image
     * @return Une image avec transparence à partir des données
     */
    public BufferedImage toImage (boolean [] visible)
    {
        BufferedImage image = new BufferedImage (this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        double [][] data = this.getDataMatrix ();

        int index = 0;
        for (int x = 0; x < this.width; x++)
            for (int y = 0; y < this.height; y++)
            {
                Color c = null;
                if (visible [index])
                    c = new Color ((int) (data [index][0] * 255), (int) (data [index][1] * 255), (int) (data [index][2] * 255));
                else
                    c = new Color (0, 0, 0, 0);
                int pixel = c.getRGB ();
                image.setRGB(x, y, pixel);
                index++;
            }
        return image;
    }

    /**
     * @param alpha Niveau de transparence
     * @param threshold Seuil de transparence
     * @return Une image avec transparence à partir des données
     */
    public BufferedImage toImage (double [] alpha, double threshold)
    {
        return this.toImage (ImageData.isGreaterThan (alpha, threshold));
    }
    
    /**
     * @return La largeur de l'image
     */
    public int getWidth ()
    {
        return this.width;
    }
    
    /**
     * @return La hauteur de l'image
     */
    public int getHeight ()
    {
        return this.height;
    }
}
