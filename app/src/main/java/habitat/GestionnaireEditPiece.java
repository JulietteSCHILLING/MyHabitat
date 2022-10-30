package habitat;

import android.widget.EditText;

import java.util.HashMap;

public class GestionnaireEditPiece {

    private HashMap<EditText, Piece> hmap;

    public GestionnaireEditPiece() {
        hmap = new HashMap<EditText, Piece>();
    }

    public void add(EditText editText, Piece piece){
        hmap.put(editText, piece);
    }

    public Piece getPiece(EditText editText){
        return hmap.get(editText);
    }

    public void reset(){
        hmap.clear();
    }

}
