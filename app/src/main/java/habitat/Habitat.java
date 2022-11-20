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
    private ArrayList<Ouverture> ouvertures;

    public Habitat() {
        this.pieces = new ArrayList<Piece>();
        this.ouvertures = new ArrayList<Ouverture>();
    }

    protected Habitat(Parcel in) {
        pieces = new ArrayList<Piece>();
        ouvertures = new ArrayList<Ouverture>();
        in.readList(pieces, Piece.class.getClassLoader());
        in.readList(ouvertures, Ouverture.class.getClassLoader());
    }

    public Habitat(Piece... pieces) {
        this.pieces = new ArrayList<>(Arrays.asList(pieces));
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


    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public ArrayList<Ouverture> getOuvertures() {
        return ouvertures;
    }

    public void setOuvertures(ArrayList<Ouverture> ouvertures) {
        this.ouvertures = ouvertures;
    }

    @Override
    public String toString() {
        return "Habitat{" +
                "pieces=" + pieces.toString() +
                "; ouvertures=" + ouvertures.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(pieces);
        dest.writeList(ouvertures);
    }

    //Fonction utile quand on recupere via Intent pour re-set correctement
    public void setCorrectly(){
        for(Piece piece : pieces){
            piece.setCorrectly();
        }
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void addOuverture(Ouverture ouverture){
        ouvertures.add(ouverture);
    }

    public void removeOuverture(Ouverture ouverture){
        ouvertures.remove(ouverture);
    }

    public void removePiece(Piece piece){
        pieces.remove(piece);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArrayPiece = new JSONArray();
        JSONArray jsonArrayOuvertures = new JSONArray();
        for(Piece piece : pieces){
            jsonArrayPiece.put(piece.toJSON());
        }
        for(Ouverture ouverture : ouvertures){
            jsonArrayOuvertures.put(ouverture.toJSON());
        }
        try {
            jsonObject.put("Pieces", jsonArrayPiece);
            jsonObject.put("Ouvertures", jsonArrayOuvertures);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public void reset(){
        pieces.clear();
        ouvertures.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, ouvertures);
    }
}
