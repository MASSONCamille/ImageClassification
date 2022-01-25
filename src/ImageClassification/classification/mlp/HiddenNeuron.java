package ImageClassification.classification.mlp;

import java.util.Random;

/**
 * @author Alexandre Blansché
 * Neurone d'une couche cachée ou de la couche de sortie
 */
public class HiddenNeuron extends Neuron
{
    /** Génération pseudo-aléatoire de nombre */
    private static Random random = new Random (System.currentTimeMillis ());
    
    /** Valeur maximale des poids initiaux */
    private static final double MAX_VALUE = .001;
    
    /** Fonction d'activation */
    private ActivationFunction activationFunction;
    
    /** Couche de neurone précédente */
    private Neuron [] previousLayer;
    
    /** Poids des connexions des neurones de la couche précédente */
    private double [] weights;
    
    /** Erreur estimée du neurone */
    private double error;
    
    /**
     * @param previousLayer Les neurones de la couche précédente
     * @param activationFunction La fonction d'activation du neurone
     */
    public HiddenNeuron (Neuron [] previousLayer, ActivationFunction activationFunction)
    {
        /* On affecte la fonction d'activation */
        this.activationFunction = activationFunction;
        /* On récupère les neurones de la couche précédente... */
        this.previousLayer = new Neuron [previousLayer.length + 1];
        for (int i = 0; i < previousLayer.length; i++)
            this.previousLayer [i] = previousLayer [i];
        /* ... et le neurone de biais */
        this.previousLayer [previousLayer.length] = BiasNeuron.getInstance ();
        /* Initialisation aléatoire des poids */
        this.weights = new double [this.previousLayer.length];
        for (int i = 0; i < this.weights.length; i++)
            this.weights [i] = HiddenNeuron.initWeight ();
    }

    /**
     * @param previousLayer Les neurones de la couche précédente
     * La fonction d'activation par défaut est la fonction sigmoïde
     */
    public HiddenNeuron (Neuron [] previousLayer)
    {
        this (previousLayer, SigmoidFunction.getInstance ());
    }
    
    /**
     * @return La fonction d'activation
     */
    public ActivationFunction getActivationFunction ()
    {
        return this.activationFunction;
    }
    
    /**
     * @return L'erreur de ce neurone à propager lors de l'apprentissage
     */
    public double getError ()
    {
        return this.error;
    }
    
    /**
     * @return Le nombre d'entrées du neurone
     */
    public int getNbInputs ()
    {
        return this.weights.length;
    }
    
    /**
     * @param index L'indice du neurone auquel on veut accéder
     * @return Le neurone de la couche précédente
     */
    public Neuron getPreviousNeuron (int index)
    {
        return this.previousLayer [index];
    }
    
    /**
     * @param index L'indice du poids auquel on veut accéder
     * @return Le poids de la connexion
     */
    public double getWeight (int index)
    {
        return this.weights [index];
    }
    
    /**
     * @param error L'erreur du neurone
     */
    public void setError (double error)
    {
        this.error = error;
    }
    
    /**
     * @param weight Le nouveau poids de la connexion
     * @param index L'indice du poids que l'on souhaite changer
     */
    public void setWeight (double weight, int index)
    {
        this.weights [index] = weight;
    }
    
    /**
     * @return Initialisation d'un poids entre -HiddenNeuron.MAX_VALUE et HiddenNeuron.MAX_VALUE
     */
    private static double initWeight ()
    {
        return HiddenNeuron.random.nextDouble () * 2 * HiddenNeuron.MAX_VALUE - HiddenNeuron.MAX_VALUE;
    }
}
