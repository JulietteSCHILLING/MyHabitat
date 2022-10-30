package habitat;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class Piece implements Parcelable {
    private Habitat habitat;
    private ArrayList<Mur> murs;

    public Piece(Habitat habitat) {
        this.habitat = habitat;
        murs = new ArrayList<>(4);
    }

    protected Piece(Parcel in) {
        habitat = new Habitat();
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

    @Override
    public String toString() {
        return "Piece{" +
                "murs=" + murs.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(murs);
    }

    public void setCorrectly() {
        for(Mur mur : murs){
            mur.setPiece(this);

        }
    }
}
