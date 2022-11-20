package habitat;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import outils.FabriqueId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Habitat implements Parcelable {

    private ArrayList<Piece> pieces;

    public Habitat() {
        this.pieces = new ArrayList<Piece>();
        //createHabitat();
    }

    protected Habitat(Parcel in) {
        pieces = new ArrayList<Piece>();
        in.readList(pieces, Piece.class.getClassLoader());
    }

    public static final Creator<Habitat> CREATOR = new Creator<Habitat>() {
        @Override
        public Habitat createFromParcel(Parcel in) {
            return new Habitat(in);
        }

        @Override
        public Habitat[] newArray(int size) {
            return new Habitat[size];
        }
    };

    public void createHabitat(){
        Piece piece1 = new Piece("p1", this);
        Mur murN = new Mur(piece1, Orientation.NORD, this);
        Mur murE = new Mur(piece1, Orientation.EST, this);
        Mur murS = new Mur(piece1, Orientation.SUD, this);
        Mur murO = new Mur(piece1, Orientation.OUEST, this);
        piece1.setMurs(murS, murO, murN, murE);
        pieces.add(piece1);
    }

    public Habitat(Piece... pieces) {
        this.pieces = new ArrayList<>(Arrays.asList(pieces));
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public String toString() {
        return "Habitat{" +
                "pieces=" + pieces.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(pieces);
    }

    //Fonction utile quand on recupere via Intent pour re-set correctement
    public void setCorrectly(){
        for(Piece piece : pieces){
            piece.setHabitat(this);
            piece.setCorrectly();
        }
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
        piece.setHabitat(this);
    }

    public void removePiece(Piece piece){
        pieces.remove(piece);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Piece piece : pieces){
            jsonArray.put(piece.toJSON());
        }
        try {
            jsonObject.put("Pieces", jsonArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public void reset(){
        pieces.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
