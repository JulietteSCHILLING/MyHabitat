package habitat;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Piece implements Parcelable {
    /**
     * Attribut qui stocke le nom de la Piece
     */
    private String nom;
    /**
     * Attribut qui stocke les 4 murs de la Piece
     */
    private ArrayList<Mur> murs;

    /**
     * Constructeur de Piece avec uniquement le nom
     * @param nom
     */
    public Piece(String nom) {
        this.nom = nom;
        murs = new ArrayList<>(4);

        //On créé les murs
        Mur murN = new Mur(this, Orientation.NORD);
        Mur murE = new Mur(this, Orientation.EST);
        Mur murS = new Mur(this, Orientation.SUD);
        Mur murO = new Mur(this, Orientation.OUEST);
        setMurs(murS, murO, murN, murE);
    }

    /**
     * Constructeur de Piece a partir du JSON
     * @param jsonObjectPiece
     */
    public Piece(JSONObject jsonObjectPiece){
        murs = new ArrayList<Mur>();
        try {
            nom = (String) jsonObjectPiece.get("Nom");
            JSONArray Jmurs = jsonObjectPiece.getJSONArray("Murs");
            for(int j=0; j<4; j++){
                JSONObject Jmur = Jmurs.getJSONObject(j);
                Mur mur = new Mur(Jmur);
                mur.setPiece(this);
                addMur(mur);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructeur de la Piece pour Parcelable
     * @param in
     */
    protected Piece(Parcel in) {
        //habitat = new Habitat();
        nom = new String();
        nom = in.readString();
        murs = new ArrayList<Mur>(4);
        in.readList(murs, Mur.class.getClassLoader());
    }

    /**
     * Creator de Piece pour Parcelable
     */
    public static final Creator<Piece> CREATOR = new Creator<Piece>() {
        @Override
        public Piece createFromParcel(Parcel in) {
            return new Piece(in);
        }

        @Override
        public Piece[] newArray(int size) {
            return new Piece[size];
        }
    };

    /**
     * getter du nom de la Piece
     * @return le nom de la Piece
     */
    public String getNom() {
        return nom;
    }

    /**
     * setter du nom de la Piece
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * getter des murs de la Piece
     * @return
     */
    public ArrayList<Mur> getMurs() {
        return murs;
    }

    /**
     * Setter des murs de la Piece
     * @param murs
     */
    public void setMurs(Mur... murs) {
        for(int i=0; i< murs.length; i++){
            this.murs.add(murs[i]);
        }
    }

    /**
     * getter d'un mur de la Piece en fonction de l'orientation
     * @param orientation
     * @return
     */
    public Mur getMurOrientation(Orientation orientation){
        Mur result = murs.get(0);
        for(Mur mur : murs){
            if(mur.getOrientation().equals(orientation)){
                result = mur;
            }
        }
        return result;
    }

    /**
     * Fonction toString de Piece
     * @return le toString de la Piece
     */
    @Override
    public String toString() {
        return "Piece{nom=" + nom +
                ", murs=" + murs.toString() +
                '}';
    }

    /**
     * Fonction utile pour Parcelable
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Fonction qui ecrit la Piece pour Parcelable
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeList(murs);
    }

    /**
     * Fonction qui set la piece aux murs
     */
    public void setCorrectly() {
        for(Mur mur : murs){
            mur.setPiece(this);
        }
    }

    /**
     * Fonction qui ajoute un Mur e à la Piece
     * @param e
     */
    public void addMur(Mur e){
        murs.add(e);
        e.setPiece(this);
    }

    /**
     * Fonctio qui genere le JSON de la Piece
     * @return
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Mur mur : murs){
            jsonArray.put(mur.toJSON());
        }
        try {
            jsonObject.put("Nom", getNom());
            jsonObject.put("Murs", jsonArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    /**
     * Fonction equals de Piece
     * @param o
     * @return true si egaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(nom, ((Piece) o).getNom()) && Objects.equals(murs, ((Piece) o).getMurs());
    }

    /**
     * Fonction qui retourne le hashCode de Piece
     * @return le hashCode de Piece
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom, murs);
    }
}
