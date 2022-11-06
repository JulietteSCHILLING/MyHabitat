package habitat;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import outils.FabriqueId;

public class Mur implements Parcelable {

    private Habitat habitat;
    private Piece piece;
    private Orientation orientation;
    private int id; //Utile pour stocker la photo associée au mur

    public Mur(Piece piece, Habitat habitat) {
        this.habitat = habitat;
        this.piece = piece;
        this.orientation = Orientation.SUD;  //Par défaut
        id = FabriqueId.getInstance().getId();
    }

    public Mur(Piece piece, Orientation orientation, Habitat habitat) {
        this.habitat = habitat;
        this.piece = piece;
        this.orientation = orientation;
        id = FabriqueId.getInstance().getId();
    }

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

    protected Mur(Parcel in) {
        orientation = (Orientation) in.readSerializable();
        id = in.readInt();
    }

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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mur{id=").append(id).append(";");
        sb.append("orientation=");
        sb.append(orientation);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(orientation);
        dest.writeInt(id);
    }

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
}
