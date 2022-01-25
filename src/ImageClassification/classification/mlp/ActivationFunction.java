package ImageClassification.classification.mlp;

/**
 * @author Alexandre Blansché
 * Classe abstraite pour la fonction d'activation d'un neurone
 */
public abstract class ActivationFunction
{

    /**
     * @param input Les valeurs en entrée du neurone
     * @return L'activation du neurone en fonction de la somme pondérée des entrées
     */
    public abstract double getActivation (double input);
    
    /**
     * @param output Valeur de sortie du neurone
     * @return La dérivée de l'activation selon la sortie du neurone
     */
    public abstract double getDerivative (double output);
}
