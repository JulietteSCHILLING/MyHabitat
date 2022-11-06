package habitat;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Piece implements Parcelable {
    private Habitat habitat;
    private String nom;
    private ArrayList<Mur> murs;

    public Piece(String nom, Habitat habitat) {
        this.nom = nom;
        this.habitat = habitat;
        murs = new ArrayList<>(4);

        //On créé les murs
        Mur murN = new Mur(this, Orientation.NORD, this.getHabitat());
        Mur murE = new Mur(this, Orientation.EST, this.getHabitat());
        Mur murS = new Mur(this, Orientation.SUD, this.getHabitat());
        Mur murO = new Mur(this, Orientation.OUEST, this.getHabitat());
        setMurs(murS, murO, murN, murE);
    }

    public Piece(JSONObject jsonObjectPiece){
        murs = new ArrayList<Mur>();
        try {
            nom = (String) jsonObjectPiece.get("Nom");
            JSONArray Jmurs = jsonObjectPiece.getJSONArray("Murs");
            for(int j=0; j<4; j++){
                JSONObject Jmur = Jmurs.getJSONObject(j);
                Mur mur = new Mur(Jmur);
                addMur(mur);
                mur.setHabitat(habitat);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected Piece(Parcel in) {
        //habitat = new Habitat();
        nom = new String();
        nom = in.readString();
        murs = new ArrayList<Mur>(4);
        in.readList(murs, Mur.class.getClassLoader());
    }

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public void setMurs(Mur... murs) {
        for(int i=0; i< murs.length; i++){
            this.murs.add(murs[i]);
        }
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    public Mur getMurOrientation(Orientation orientation){
        Mur result = murs.get(0);
        for(Mur mur : murs){
            if(mur.getOrientation().equals(orientation)){
                result = mur;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Piece{nom=" + nom +
                ", murs=" + murs.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeList(murs);
    }

    public void setCorrectly() {
        for(Mur mur : murs){
            mur.setPiece(this);
            mur.setHabitat(habitat);
        }
    }

    public void addMur(Mur e){
        murs.add(e);
        e.setPiece(this);
    }

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
}
