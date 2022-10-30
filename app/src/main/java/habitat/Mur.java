package habitat;

import android.os.Parcel;
import android.os.Parcelable;

public class Mur implements Parcelable {

    private Habitat habitat;
    private Piece piece;
    private Orientation orientation;

    public Mur(Piece piece, Habitat habitat) {
        this.habitat = habitat;
        this.piece = piece;
        this.orientation = Orientation.SUD;  //Par défaut
    }

    public Mur(Piece piece, Orientation orientation, Habitat habitat) {
        this.habitat = habitat;
        this.piece = piece;
        this.orientation = orientation;
    }

    protected Mur(Parcel in) {
        orientation = (Orientation) in.readSerializable();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mur{");
        sb.append("orientation=");
        sb.append(orientation);
        sb.append("}");
        return sb.toString();
        /*
        return "Mur{" +
                "piece=" + piece +
                ", orientation=" + orientation +
                '}';

         */


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(orientation);
    }
}
