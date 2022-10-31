package outils;

public class FabriqueId {
    private int id;
    private static FabriqueId instance = new FabriqueId();

    public static FabriqueId getInstance(){
        return instance;
    }

    private FabriqueId() {
        id = 0;
    }

    public int getId() {
        id++;
        return id;
    }

    public void reset(){
        id = 0;
    }
}
