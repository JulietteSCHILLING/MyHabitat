package outils;

public class FabriqueId {
    /**
     * Attribut qui stocke l'id
     */
    private int id;
    /**
     * Attribut instance de la FabriqueId
     */
    private static FabriqueId instance = new FabriqueId();

    /**
     * getter de l'instance de la FabriqueId
     * @return l'instance de la FabriqueId
     */

    public static FabriqueId getInstance(){
        return instance;
    }

    /**
     * Constructeur prive de FabriqueId
     */
    private FabriqueId() {
        id = 0;
    }

    /**
     * retourne un id unique
     * @return un id unique
     */
    public int getId() {
        id++;
        return id;
    }

    /**
     * Fonction qui met à 0 la FabriqueIdentifiant
     */
    public void reset(){
        id = 0;
    }
}
