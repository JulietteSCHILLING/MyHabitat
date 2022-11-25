package habitat;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class Habitat implements Parcelable {

    /**
     * Attribut qui stocke l'ensemble des pieces de l'habitat
     */
    private ArrayList<Piece> pieces;
    /**
     * Attribut qui stocke l'ensemble des ouvertures de l'habitat
     */
    private ArrayList<Ouverture> ouvertures;


    /**
     * Constructeur d'Habitat
     */
    public Habitat() {
        this.pieces = new ArrayList<Piece>();
        this.ouvertures = new ArrayList<Ouverture>();
    }

    /**
     * Constructeur d'Habitat quand on récupère via intent
     * @param in
     */
    protected Habitat(Parcel in) {
        pieces = new ArrayList<Piece>();
        ouvertures = new ArrayList<Ouverture>();
        in.readList(pieces, Piece.class.getClassLoader());
        in.readList(ouvertures, Ouverture.class.getClassLoader());
    }

    /**
     * Creator utile implementant Parcelable
     */
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


    /**
     * getter des pieces
     * @return les pieces
     */
    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    /**
     * setter des pieces
     * @param pieces
     */
    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    /**
     * getter des ouvertures
     * @return les ouvertures
     */
    public ArrayList<Ouverture> getOuvertures() {
        return ouvertures;
    }

    /**
     * setter des ouvertures
     * @param ouvertures
     */
    public void setOuvertures(ArrayList<Ouverture> ouvertures) {
        this.ouvertures = ouvertures;
    }

    /**
     * toString d'Habitat
     * @return le toString d'Habitat
     */
    @Override
    public String toString() {
        return "Habitat{" +
                "pieces=" + pieces.toString() +
                "; ouvertures=" + ouvertures.toString() +
                '}';
    }

    /**
     * fonction implementant Parcelable
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * fonction pour ecrire pour Parcelable
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(pieces);
        dest.writeList(ouvertures);
    }

    /**
     * Set correctement les elements de habitat
     * Fonction utile quand on recupere via Intent pour re-set correctement
     */
    public void setCorrectly(){
        for(Piece piece : pieces){
            piece.setCorrectly();
        }
    }

    /**
     * Fonction qui ajoute une piece à l'habitat
     * @param piece
     */
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    /**
     * Fonction qui ajoute une ouverture à l'habitat
     * @param ouverture
     */
    public void addOuverture(Ouverture ouverture){
        ouvertures.add(ouverture);
        for(Piece piece : pieces){
            for(Mur mur : piece.getMurs()){
                if(ouverture.getMurDepart().getId() == mur.getId()){
                    ouverture.setMurDepart(mur);
                }
                if(ouverture.getMurArrivee().getId() == mur.getId()){
                    ouverture.setMurArrivee(mur);
                }
            }
        }
    }

    /**
     * Fonction qui enlève une ouverture à l'habitat
     * @param ouverture
     */
    public void removeOuverture(Ouverture ouverture){
        ouvertures.remove(ouverture);
    }

    /**
     * Fonction qui enlève une pièce à l'habitat
     * @param piece
     */
    public void removePiece(Piece piece){
        pieces.remove(piece);
        //On vérifie qu'il n'existe pas d'ouvertures
        for(Mur mur : piece.getMurs()){
            ArrayList<Ouverture> ouvertureArrayList = getOuvertureDeMur(mur);
            if(!ouvertureArrayList.isEmpty()){
                for(Ouverture ouverture : ouvertureArrayList){
                    removeOuverture(ouverture);
                }
            }
        }
    }

    /**
     * Fonction qui retourne le JSON de habitat
     * @return le JSON de habitat
     */
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

    /**
     * Fonction qui permet de reinitialiser habitat
     */
    public void reset(){
        pieces.clear();
        ouvertures.clear();
    }

    /**
     * Fonction equals
     * @param o
     * @return true si egaux, faux sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    /**
     * Retourne le hashCode de habitat
     * @return le hashCode de habitat
     */
    @Override
    public int hashCode() {
        return Objects.hash(pieces, ouvertures);
    }

    /**
     * Fonction qui permet de retrouver toutes les ouvertures du mur
     * @param mur
     * @return les ouvertures du mur
     */
    public ArrayList<Ouverture> getOuvertureDeMur(Mur mur){
        ArrayList<Ouverture> ouvertureArrayList = new ArrayList<Ouverture>();
        for(Ouverture ouverture : ouvertures){
            if(ouverture.getMurDepart().getId() == (mur.getId()) || ouverture.getMurArrivee().getId() == (mur.getId())){
                ouvertureArrayList.add(ouverture);
            }
        }
        for(Ouverture ouverture : ouvertureArrayList) {
            for (Piece piece : pieces) {
                for (Mur mur1 : piece.getMurs()) {
                    if (ouverture.getMurDepart().getId() == mur1.getId()) {
                        ouverture.setMurDepart(mur1);
                    }
                    if (ouverture.getMurArrivee().getId() == mur1.getId()) {
                        ouverture.setMurArrivee(mur1);
                    }
                }
            }
        }
        return ouvertureArrayList;
    }
}
