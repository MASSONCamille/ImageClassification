package ImageClassification.classification.mlp;

import java.util.Random;

import ImageClassification.classification.Classification;

/**
 * @author Alexandre Blansché
 * Perceptron multicouche
 */
public class MultiLayerPerceptron extends Classification
{
    /** Nombre de couches cachées */
    private static int DEFAULT_NB_HIDDEN_LAYERS = 2;

    /** Nombre de neurones par couche cachée */
    private static int DEFAULT_NB_HIDDEN_NEURONS_PER_LAYER = 10;

    /** Pas d'apprentissage par défaut */
    private static double LEARNING_STEP = .05;

    /** Génération pseudo-aléatoire de nombre */
    private static Random random = new Random (System.currentTimeMillis ());

    /** Couche d'entrée */
    private InputNeuron [] inputLayer;

    /** Couches cachées */
    private HiddenNeuron [][] hiddenLayers;

    /** "Couche" de sortie (un seul neurone) */
    private HiddenNeuron outputLayer;

    /**
     * Constructeur
     * @param nbInputs Nombre de neurones de la couche d'entrée
     * @param nbHidden Nombre de couches cachées
     * @param nbNeurons Nombre de neurones par couche cachée
     */
    public MultiLayerPerceptron (int nbInputs, int nbHidden, int nbNeurons)
    {
        /* Initialisation de la couche d'entrée */
        this.inputLayer = new InputNeuron [nbInputs];
        for (int i = 0; i < this.inputLayer.length; i++)
            this.inputLayer [i] = new InputNeuron ();
        /* Initialisation des couches cachées */
        this.hiddenLayers = new HiddenNeuron [nbHidden][nbNeurons];
        /* Première couche cachée */
        for (int j = 0; j < this.hiddenLayers [0].length; j++)
            this.hiddenLayers [0][j] = new HiddenNeuron (this.inputLayer);
        /* Autres couches cachées */
        for (int i = 1; i < this.hiddenLayers.length; i++)
            for (int j = 0; j < this.hiddenLayers [i].length; j++)
                this.hiddenLayers [i][j] = new HiddenNeuron (this.hiddenLayers [i - 1]);
        /* Initialisation de la "couche" de sortie */
        this.outputLayer = new HiddenNeuron (this.hiddenLayers [nbHidden - 1]);
    }

    /**
     * Constructeur
     * @param nbInputs Nombre de neurones de la couche d'entrée
     */
    public MultiLayerPerceptron (int nbInputs)
    {
        this (nbInputs, MultiLayerPerceptron.DEFAULT_NB_HIDDEN_LAYERS, MultiLayerPerceptron.DEFAULT_NB_HIDDEN_NEURONS_PER_LAYER);
    }

    /**
     * Fonction d'initialisation de l'apprentissage
     */
    @Override
    protected void initialization ()
    {
        /* Rien à faire... */
    }

    /**
     * Fonction d'apprentissage pour une itération
     */
    @Override
    public void learn ()
    {
        /* On récupère un objet et son label, au hasard, dans l'ensemble d'apprentissage */
        int index = MultiLayerPerceptron.random.nextInt (this.getLearningData ().getNRow ());
        double [] object = this.getLearningData ().getObject (index);
        int label = this.getLearningData ().getLabel (index);
        /* On applique une itération de la rétroprapagation */
        this.backpropagation (object, label);
    }

    /**
     * @param object Un pixel de l'image
     * @return Le degré d'appartenance à la classe positive
     */
    @Override
    public double predict (double [] object)
    {
        /* Activation du réseau de neurones */
        this.activate (object);
        /* On retourne l'activation du neurone de la couche de sortie */
        return this.outputLayer.getActivation ();
    }

    /**
     * Activation du réseau de neurone
     * @param object Un pixel de l'image
     */
    public void activate (double [] object)
    {
        for (int i = 0; i < object.length; i++) this.inputLayer[i].setActivation(object[i]);

        for (HiddenNeuron[] hiddenLayer: this.hiddenLayers) {
            for (HiddenNeuron neuronH: hiddenLayer) {
                double sum = 0;
                for (int i = 0; i < neuronH.getNbInputs(); i++) {
                    sum += neuronH.getPreviousNeuron(i).getActivation() * neuronH.getWeight(i);
                }
                neuronH.setActivation(neuronH.getActivationFunction().getActivation(sum));
            }
        }

        double sum2 = 0;
        for (int i = 0; i < this.outputLayer.getNbInputs(); i++) {
            sum2 += this.outputLayer.getPreviousNeuron(i).getActivation() * outputLayer.getWeight(i);
        }
        this.outputLayer.setActivation(this.outputLayer.getActivationFunction().getActivation(sum2));

    }

    /**
     * Rétropropagation du gradient
     * @param object Un pixel de l'image
     * @param label Le label réel du pixel
     */
    private void backpropagation (double [] object, int label)
    {
        // boucle pour modifier les poids du neurons ouput
        for (int i = 0; i < this.outputLayer.getNbInputs(); i++) {

        }

        // boucle pour la dernière couche caché





        // boucle pour les autre couche caché
    }
}
