package ImageClassification.classification.mlp;

/**
 * @author Alexandre Blansché
 * Fonction d'activation sigmoïde
 * Classe singleton
 */
public class SigmoidFunction extends ActivationFunction
{
    /** Instance */
    private static SigmoidFunction instance = null;
    
    /**
     * @return Accès à l'instance
     */
    public static SigmoidFunction getInstance ()
    {
        if (SigmoidFunction.instance == null)
            SigmoidFunction.instance = new SigmoidFunction ();
        return SigmoidFunction.instance;
    }

    /**
     * @param input Les valeurs en entrée du neurone
     * @return L'activation du neurone en fonction de la somme pondérée des entrées
     */
    @Override
    public double getActivation (double input)
    {
        /* Calcul de la sigmoïde */
        return 1 / (1 + Math.exp (-input));
    }

    /**
     * @param output Valeur de sortie du neurone
     * @return La dérivée de l'activation selon la sortie du neurone
     */
    @Override
    public double getDerivative (double output)
    {
        /* Calcul de la dérivée de la sigmoïde */
        return output * (1 - output);
    }
}
