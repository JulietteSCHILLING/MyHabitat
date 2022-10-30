package habitat;

import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;

public class GestionnaireEditHabitat {

    private HashMap<EditText, Piece> hmapEditText;
    private HashMap<ImageButton, Mur> hmapEditMur;

    public GestionnaireEditHabitat() {
        hmapEditText = new HashMap<EditText, Piece>();
        hmapEditMur = new HashMap<ImageButton, Mur>();
    }

    public void addEditText(EditText editText, Piece piece){
        hmapEditText.put(editText, piece);
    }

    public Piece getPiece(EditText editText){
        return hmapEditText.get(editText);
    }

    public void addEditMur(ImageButton imageButton, Mur mur){
        hmapEditMur.put(imageButton, mur);
    }

    public Mur getMur(ImageButton imageButton){
        return hmapEditMur.get(imageButton);
    }



    public void reset(){
        hmapEditText.clear();
        hmapEditMur.clear();
    }

}
