package ImageClassification.data;

/**
 * @author Alexandre Blansché
 * Table de données
 */
public class Data
{
    private double [][] data;

    /**
     * @param data Les données sous la forme d'un tableau à deux dimensions
     */
    public Data (double [][] data)
    {
        this.data = data;
    }
    
    /**
     * @param index L'indice de l'objet
     * @return Un objet de la table de données
     */
    public double [] getObject (int index)
    {
        return this.data [index];
    }
    
    /**
     * @return La table de données sous la forme d'un tableau à deux dimensions
     */
    public double [][] getDataMatrix ()
    {
        return this.data;
    }
    
    /**
     * @return Le nombre de lignes (nombre d'objets)
     */
    public int getNRow ()
    {
        return this.data.length;
    }
    
    /**
     * @return Le nombre colonnes (nombre d'attributs)
     */
    public int getNCol ()
    {
        return this.data [0].length;
    }
}
