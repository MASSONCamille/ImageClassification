package ImageClassification.classification.mlp;

/**
 * @author Alexandre Blansché
 * Neurone de biais
 * Classe singleton
 */
public class BiasNeuron extends Neuron
{
    /** Instance */
    private static BiasNeuron instance;
    
    /**
     * Constructeur...
     */
    private BiasNeuron ()
    {
        this.setActivation (1);
    }

    /**
     * @return Accès à l'instance
     */
    public static BiasNeuron getInstance ()
    {
        if (BiasNeuron.instance == null)
            BiasNeuron.instance = new BiasNeuron ();
        return BiasNeuron.instance;
    }
}
