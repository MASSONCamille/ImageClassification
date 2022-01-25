package ImageClassification.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Alexandre Blansché
 * Données labellisées pour l'apprentissage
 */
public class LabeledData extends Data
{
    private int [] labels;

    /**
     * @param data Les données sous la forme d'un tableau à deux dimensions
     * @param labels Les labels des données
     */
    public LabeledData (double [][] data, int [] labels)
    {
        super (data);
        this.labels = labels;
    }

    /**
     * Aucun objet labellisé
     * @param data Les données
     */
    public LabeledData (Data data)
    {
        super (data.getDataMatrix ());
        this.labels = new int [this.getNRow ()];
        for (int i = 0; i < this.labels.length; i++)
            this.labels [i] = -1;
    }

    /**
     * @param data Les données
     * @param labels Les labels des données
     */
    public LabeledData (Data data, int [] labels)
    {
        super (data.getDataMatrix ());
        this.labels = labels;
    }

    /**
     * Aucun objet labellisé
     * @param image Une image
     */
    public LabeledData (BufferedImage image)
    {
        this (new ImageData (image));
    }

    /**
     * @param image Une image
     * @param labels Les labels des données
     */
    public LabeledData (BufferedImage image, int [] labels)
    {
        super (new ImageData (image).getDataMatrix ());
        this.labels = labels;
    }
    
    /**
     * @return Le nombre d'objets labellisés
     */
    public int getNLabeled ()
    {
        int nbLabeled = 0;
        for (int i = 0; i < this.labels.length; i++)
            if (this.labels [i] >= 0)
                nbLabeled++;
        return nbLabeled;
    }
    
    /**
     * @return Le ratio d'objets labellisés
     */
    public double getLabeledRatio ()
    {
        return (double) this.getNLabeled () / this.getNRow ();
    }
    
    /**
     * @return Un sous-ensemble des données constitué des objets labellisés
     */
    public LabeledData extractLabeledData ()
    {
        int nbLabeled = this.getNLabeled ();
        double data [][] = this.getDataMatrix ();
        double [][] extractedData = new double [nbLabeled][];
        int [] extractedLabels = new int [nbLabeled];
        int index = 0;
        for (int i = 0; i < this.labels.length; i++)
            if (this.labels [i] >= 0)
            {
                extractedData [index] = data [i];
                extractedLabels [index] = this.labels [i];
                index++;
            }
        return new LabeledData (extractedData, extractedLabels);
    }
    
    /**
     * Affectation d'un label à un objet
     * @param index L'indice de l'objet
     * @param label Le label à affecter
     */
    public void setLabel (int index, int label)
    {
        this.labels [index] = label;
    }
    
    /**
     * @param index L'indice de l'objet
     * @return Le label de l'objet
     */
    public int getLabel (int index)
    {
        return this.labels [index];
    }
    
    /**
     * @param width La largeur de l'image
     * @param height La hauteur de l'image
     * @return Une image des labels des objets
     */
    public BufferedImage toImage (int width, int height)
    {
        BufferedImage image = null;
        if (width * height == this.getNRow ())
        {
            image = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
            int index = 0;
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                {
                    Color c = null;
                    if (this.labels [index] < 0)
                        c = Color.BLACK;
                    else if (this.labels [index] == 0)
                        c = Color.RED;
                    else
                        c = Color.GREEN;
                    int pixel = c.getRGB ();
                    image.setRGB(x, y, pixel);
                    index++;
                }
        }
        return image;
    }
}
