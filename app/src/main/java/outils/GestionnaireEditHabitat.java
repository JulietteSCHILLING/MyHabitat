package outils;

import android.widget.EditText;
import android.widget.ImageButton;
import habitat.Mur;
import habitat.Piece;

import java.util.HashMap;

public class GestionnaireEditHabitat {

    /**
     * Attribut qui stocke une Piece en fonction de l'EditPiece associe
     */
    private HashMap<EditText, Piece> hmapEditText;
    /**
     * Attribut qui stocke le Mur en fonction de l'ImageButton associe
     */
    private HashMap<ImageButton, Mur> hmapEditMur;

    /**
     * Constructeur de GestionnaireEditHabitat
     */
    public GestionnaireEditHabitat() {
        hmapEditText = new HashMap<EditText, Piece>();
        hmapEditMur = new HashMap<ImageButton, Mur>();
    }

    /**
     * Ajoute l'element <editText, piece> au Gestionnaire
     * @param editText
     * @param piece
     */
    public void addEditText(EditText editText, Piece piece){
        hmapEditText.put(editText, piece);
    }

    /**
     * getter de Piece en fonction d'editText
     * @param editText
     * @return
     */
    public Piece getPiece(EditText editText){
        return hmapEditText.get(editText);
    }

    /**
     * Ajoute l'element <imageButton, mur> au Gestionnaire
     * @param imageButton
     * @param mur
     */
    public void addEditMur(ImageButton imageButton, Mur mur){
        hmapEditMur.put(imageButton, mur);
    }

    /**
     * getter de mur en fonction de imageButton
     * @param imageButton
     * @return
     */
    public Mur getMur(ImageButton imageButton){
        return hmapEditMur.get(imageButton);
    }

    /**
     * Fonction qui reset le Gestionnaire
     */
    public void reset(){
        hmapEditText.clear();
        hmapEditMur.clear();
    }

}
