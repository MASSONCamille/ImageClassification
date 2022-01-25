package ImageClassification.classification;

import ImageClassification.data.Data;
import ImageClassification.data.LabeledData;

/**
 * @author Alexandre Blansché
 * Classe abstraite représentant une méthode de classification
 */
public abstract class Classification implements Runnable
{
    private LabeledData data;
    private int nbIterations;

    /**
     * @param object Un pixel de l'image
     * @return Le degré d'appartenance à la classe positive
     */
    public abstract double predict (double [] object);

    /**
     * @param data La table de données
     * @return Le degré d'appartenance de chaque pixel à la classe positive
     */
    public double [] predict (Data data)
    {
        double [] pred = new double [data.getNRow ()];
        for (int i = 0; i < pred.length; i++)
        {
            double [] object = data.getObject (i);
            pred [i] = this.predict (object);
        }
        return pred;
    }

    /**
     * Fonction d'initialisation de l'apprentissage
     */
    protected abstract void initialization ();

    /**
     * Fonction d'apprentissage pour une itération
     */
    protected abstract void learn ();

    /**
     * Affectation des données d'apprentissage
     * @param data Les données d'apprentissage
     */
    public void setLearningData (LabeledData data)
    {
        this.data = data;
    }

    /**
     * @return Les données d'apprentissage
     */
    public LabeledData getLearningData ()
    {
        return this.data;
    }

    /**
     * @return Le nombre d'itération pour l'apprentissage
     */
    public int getNbIterations ()
    {
        return this.nbIterations;
    }


    /**
     * Fonction d'apprentissage
     * Si les données d'apprentissage existent
     * On initialise l'algorithme
     * Et on répète la fonction d'apprentissage jusqu'à ce que le temps soit écoulé
     */
    @Override
    public void run ()
    {
        if (this.data.getNRow () > 0)
        {
            this.nbIterations = 0;
            try
            {
                this.initialization ();
            }
            catch (Exception e)
            {
            }
            while (!Thread.currentThread ().isInterrupted ())
                try
            {
                    this.nbIterations++;
                    this.learn ();
            }
            catch (Exception e)
            {
            }
        }
    }
}
