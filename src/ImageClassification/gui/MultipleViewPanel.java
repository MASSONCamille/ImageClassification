package ImageClassification.gui;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import ImageClassification.classification.Classification;
import ImageClassification.classification.mlp.MultiLayerPerceptron;
import ImageClassification.data.ImageData;
import ImageClassification.data.LabeledData;

/**
 * @author Alexandre Blansché
 * Panneau principal avec les quatre vues sur l'image
 */
public class MultipleViewPanel extends JPanel implements MouseInputListener, MouseWheelListener
{
    private static final int TIME = 500;
    private static final int RADIUS = 5;
    private static final double THRESHOLD_STEP = .01;
    
    private String fileName;
    private BufferedImage image;
    private ImageData data;
    private LabeledData labeledData;
    private Classification classification;
    private double [] pred;
    private ImagePanel learningPanel, predPanel, cutPanel;
    private double threshold;

    /**
     * @param image L'image
     * @param fileName Le nom de fichier
     */
    public MultipleViewPanel (BufferedImage image, String fileName)
    {
        this.threshold = .5;
        this.fileName = fileName;
        this.image = image;
        this.data = new ImageData (this.image);
        this.labeledData = new LabeledData (this.data);
        this.classification = new MultiLayerPerceptron (this.data.getNCol ());
        this.setLayout (new GridLayout (2, 2));
        ImagePanel imagePanel = new ImagePanel (this.image, "Image " + this.fileName, this);
        this.add (imagePanel);
        this.learningPanel = new ImagePanel ("Ensemble d'apprentissage", this);
        this.predPanel = new ImagePanel ("Prédictions", this);
        this.cutPanel = new ImagePanel ("Sélection", this);
        this.add (this.learningPanel);
        this.add (this.predPanel);
        this.add (this.cutPanel);
        this.updateClassification ();
    }

    private void update ()
    {
        BufferedImage learningImage = this.labeledData.toImage (this.image.getWidth (), this.image.getHeight ());
        this.learningPanel.updateImage (learningImage);
        double learningRatio = Math.round (10000. * this.labeledData.getLabeledRatio ()) / 100.;
        this.learningPanel.updateName ("Ensemble d'apprentissage (" + learningRatio + " %)");
    }

    private void updateClassification ()
    {
        this.predPanel.updateName ("Prédictions (en cours)");
        Main.getInstance ().repaint ();
        this.update ();
        this.classification.setLearningData (this.labeledData.extractLabeledData ());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future <?> future = executor.submit (this.classification);
        try
        {
            future.get (MultipleViewPanel.TIME, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException | InterruptedException | ExecutionException e)
        {
            executor.shutdownNow ();
            future.cancel (true);
            try
            {
                if (!executor.awaitTermination (MultipleViewPanel.TIME / 10, TimeUnit.MILLISECONDS))
                    System.exit (0);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
        
        this.pred = this.classification.predict (this.data);

        BufferedImage predImage = new ImageData (this.pred, this.image.getWidth (), this.image.getHeight ()).toImage ();
        this.predPanel.updateImage (predImage);
        this.predPanel.updateName ("Prédictions (" + this.classification.getNbIterations () + " itérations)");
        this.updateSelection ();
    }

    private void updateSelection ()
    {
        BufferedImage cutImage = this.data.toImage (this.pred, this.threshold);
        this.cutPanel.updateImage (cutImage);
        int nbVisible = 0;
        for (int i = 0; i < this.pred.length; i++)
            if (this.pred [i] >= this.threshold)
                nbVisible++;
        double cutRatio = Math.round (10000. * nbVisible / this.pred.length) / 100.;
        this.cutPanel.updateName ("Sélection (" + cutRatio + " %) - Seuil : " + Math.round (100 * this.threshold) / 100.);
    }

    private double distance (int x1, int y1, int x2, int y2)
    {
        int diffX = x1 - x2;
        int diffY = y1 - y2;
        return Math.sqrt (diffX * diffX + diffY * diffY);
    }

    private int getIndex (int x, int y)
    {
        return y + x * this.image.getHeight ();
    }
    
    private void getExemples (int mouseX, int mouseY, int radius, int label)
    {
        if ((mouseX >= 0) && (mouseY >= 0) && (mouseX < this.image.getWidth ()) && (mouseY < this.image.getHeight ()))
        {
            int minX = Math.max (0, mouseX - radius);
            int maxX = Math.min (this.image.getWidth (), mouseX + radius);
            int minY = Math.max (0, mouseY - radius);
            int maxY = Math.min (this.image.getHeight (), mouseY + radius);

            for (int x = minX; x < maxX; x++)
                for (int y = minY; y < maxY; y++)
                    if (distance (x, y, mouseX, mouseY) < radius)
                        this.labeledData.setLabel (this.getIndex (x, y), label);
            this.update ();
        }        
    }

    private void getExemples (MouseEvent arg0)
    {
        int label = -1;
        boolean event = SwingUtilities.isLeftMouseButton (arg0) || SwingUtilities.isRightMouseButton (arg0) || SwingUtilities.isMiddleMouseButton (arg0);
        int radius = MultipleViewPanel.RADIUS;
        if (SwingUtilities.isLeftMouseButton (arg0))
            label = 1;
        else if (SwingUtilities.isRightMouseButton (arg0))
            label = 0;
        else if (SwingUtilities.isMiddleMouseButton (arg0))
            radius = 2 * MultipleViewPanel.RADIUS;
        if (event)
        {
            double scale = ((ImagePanel) arg0.getComponent ().getParent ().getParent ()).getScale ();
            int mouseX = (int) (arg0.getX () * scale);
            int mouseY = (int) (arg0.getY () * scale);
            this.getExemples (mouseX, mouseY, radius, label);
        }
    }

    @Override
    public void mouseClicked (MouseEvent arg0)
    {
    }

    @Override
    public void mouseEntered (MouseEvent arg0)
    {
    }

    @Override
    public void mouseExited (MouseEvent arg0)
    {
    }

    @Override
    public void mousePressed (MouseEvent arg0)
    {
        this.getExemples (arg0);
    }

    @Override
    public void mouseReleased (MouseEvent arg0)
    {
        if (SwingUtilities.isLeftMouseButton (arg0) || SwingUtilities.isRightMouseButton (arg0) ||
                SwingUtilities.isMiddleMouseButton (arg0))
            this.updateClassification ();
    }

    @Override
    public void mouseDragged (MouseEvent arg0)
    {
        this.getExemples (arg0);
    }

    @Override
    public void mouseMoved (MouseEvent arg0)
    {
    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent arg0)
    {
        if ((arg0.getWheelRotation () > 0) && (this.threshold < 1))
        {
            this.threshold += MultipleViewPanel.THRESHOLD_STEP;
            this.updateSelection ();
        }
        if ((arg0.getWheelRotation () < 0) && (this.threshold > 0))
        {
            this.threshold -= MultipleViewPanel.THRESHOLD_STEP;
            this.updateSelection ();
        }
    }
}
