package habitat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import outils.FabriqueId;

import java.util.Objects;

public class Mur implements Parcelable {
    /**
     * Attribut qui stocke la piece du mur
     */
    private Piece piece;
    /**
     * Attribut qui stocke l'orientation du mur
     */
    private Orientation orientation;
    /**
     * Attribut qui stocke l'id du mur
     * Utile pour la sauvegarde de la photo associee au mur
     */
    private int id;

    /**
     * Constructeur de Mur avec la piece et l'orientation associes
     * @param piece
     * @param orientation
     */
    public Mur(Piece piece, Orientation orientation) {
        this.piece = piece;
        this.orientation = orientation;
        id = FabriqueId.getInstance().getId();
    }

    /**
     * Constructeur de Mur à partir du JSON
     * @param jsonObjectMur
     */
    public Mur(JSONObject jsonObjectMur){
        try {
            id = (int) jsonObjectMur.get("Id");
            String Jorientation = (String) jsonObjectMur.get("Orientation");
            switch (Jorientation){
                case "NORD" :
                    orientation = Orientation.NORD;
                    break;
                case "SUD" :
                    orientation = Orientation.SUD;
                    break;
                case "EST" :
                    orientation = Orientation.EST;
                    break;
                case "OUEST" :
                    orientation = Orientation.OUEST;
                    break;
                default:
                    orientation = Orientation.SUD; //Par défaut
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructeur de Mur pour Parcelable
     * @param in
     */
    protected Mur(Parcel in) {
        orientation = (Orientation) in.readSerializable();
        id = in.readInt();
        piece = new Piece("");
    }

    /**
     * Creator de Mur pour Parcelable
     */
    public static final Creator<Mur> CREATOR = new Creator<Mur>() {
        @Override
        public Mur createFromParcel(Parcel in) {
            return new Mur(in);
        }

        @Override
        public Mur[] newArray(int size) {
            return new Mur[size];
        }
    };

    /**
     * getter de la piece associee au mur
     * @return la piece associee au mur
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * setter de la piece associee au mur
     * @param piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * getter de l'orientation du mur
     * @return l'orientation du mur
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * setter de l'orientation du mur
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * getter de l'id du mur
     * @return l'id du mur
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id du mur
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Fonction toString du mur
     * @return le toString du mur
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mur{id=").append(id).append(";");
        sb.append("orientation=");
        sb.append(orientation);
        sb.append(";piece=").append(piece.getNom());
        sb.append("}");
        return sb.toString();
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
     * Fonction permettant d'ecrire le mur pour Parcelable
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(orientation);
        dest.writeInt(id);
    }

    /**
     * Fonction qui genere le JSON du mur
     * @return le JSON du mur
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Orientation", orientation);
            jsonObject.put("Id", id);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    /**
     * Fonction equals du mur
     * @param o
     * @return true si egaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Mur) o).getId() && Objects.equals(piece, ((Mur) o).getPiece()) && orientation == ((Mur) o).getOrientation();
    }

    /**
     * Retourne le hashCode de mur
     * @return le hashCode de mur
     */
    @Override
    public int hashCode() {
        return Objects.hash(piece, orientation, id);
    }
}
