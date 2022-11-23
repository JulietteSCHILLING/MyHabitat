package habitat;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Ouverture implements Parcelable {

    /**
     * Attribut stockant le mur de depart de l'ouverture
     */
    private Mur murDepart;
    /**
     * Attribut stockant le mur d'arrivee de l'ouverture
     */
    private Mur murArrivee;
    /**
     * Attribut stockant le rectangle de depart de l'ouverture
     */
    private Rect rectDepart;
    /**
     * Attribut stockant le rectangle d'arrivee de l'ouverture
     */
    private Rect rectArrivee;

    /**
     * Constructeur de Ouverture a partir du JSON
     * @param jsonObject
     */
    public Ouverture(JSONObject jsonObject) {
        try {
            murDepart = new Mur((JSONObject) jsonObject.get("MurDepart"));
            murArrivee = new Mur((JSONObject) jsonObject.get("MurArrivee"));
            JSONArray JrectDepart = (JSONArray) jsonObject.get("RectDepart");
            rectDepart = new Rect((Integer) JrectDepart.get(2), (Integer) JrectDepart.get(0), (Integer) JrectDepart.get(3), (Integer) JrectDepart.get(1));
            JSONArray JrectArrivee = (JSONArray) jsonObject.get("RectArrivee");
            rectArrivee = new Rect((Integer) JrectArrivee.get(2), (Integer) JrectArrivee.get(0), (Integer) JrectArrivee.get(3), (Integer) JrectArrivee.get(1));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructeur d'Ouverture avec les 2 murs et les 2 rectangles associes
     * @param murDepart
     * @param murArrivee
     * @param rectDepart
     * @param rectArrivee
     */
    public Ouverture(Mur murDepart, Mur murArrivee, Rect rectDepart, Rect rectArrivee){
        this.murDepart = murDepart;
        this.murArrivee = murArrivee;
        this.rectDepart = rectDepart;
        this.rectArrivee = rectArrivee;
    }

    /**
     * Constructeur d'Ouverture pour Parcelable
     * @param in
     */
    protected Ouverture(Parcel in){
        murDepart = in.readParcelable(Mur.class.getClassLoader());
        murArrivee = in.readParcelable(Mur.class.getClassLoader());
        rectDepart = in.readParcelable(Rect.class.getClassLoader());
        rectArrivee = in.readParcelable(Rect.class.getClassLoader());
    }

    /**
     * Creator d'Ouverture pour Parcelable
     */
    public static final Creator<Ouverture> CREATOR = new Creator<Ouverture>() {
        @Override
        public Ouverture createFromParcel(Parcel in) {
            return new Ouverture(in);
        }

        @Override
        public Ouverture[] newArray(int size) {
            return new Ouverture[0];
        }
    };

    /**
     * Fonction qui genere le JSON d'Ouverture
     * @return le JSON d'Ouverture
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArrayD = new JSONArray();
        JSONArray jsonArrayA = new JSONArray();

        try {
            jsonObject.put("MurDepart", murDepart.toJSON());
            jsonObject.put("MurArrivee", murArrivee.toJSON());
            jsonArrayD.put(rectDepart.top);
            jsonArrayD.put(rectDepart.bottom);
            jsonArrayD.put(rectDepart.left);
            jsonArrayD.put(rectDepart.right);
            jsonObject.put("RectDepart", jsonArrayD);
            jsonArrayA.put(rectArrivee.top);
            jsonArrayA.put(rectArrivee.bottom);
            jsonArrayA.put(rectArrivee.left);
            jsonArrayA.put(rectArrivee.right);
            jsonObject.put("RectDepart", jsonArrayD);
            jsonObject.put("RectArrivee", jsonArrayA);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
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
     * Fonction qui ecrit Ouverture pour Parcelable
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(murDepart, flags);
        dest.writeParcelable(murArrivee, flags);
        dest.writeParcelable(rectDepart, flags);
        dest.writeParcelable(rectArrivee, flags);
    }

    /**
     * getter du mur de depart
     * @return le mur de depart de l'ouverture
     */
    public Mur getMurDepart() {
        return murDepart;
    }

    /**
     * setter du mur de depart
     * @param murDepart
     */
    public void setMurDepart(Mur murDepart) {
        this.murDepart = murDepart;
    }

    /**
     * getter du mur d'arrivee
     * @return le mur d'arrivee de l'ouverture
     */
    public Mur getMurArrivee() {
        return murArrivee;
    }

    /**
     * setter du mur d'arrivee
     * @param murArrivee
     */
    public void setMurArrivee(Mur murArrivee) {
        this.murArrivee = murArrivee;
    }

    /**
     * getter du rectangle de depart
     * @return le rectangle de depart de l'ouverture
     */
    public Rect getRectDepart() {
        return rectDepart;
    }

    /**
     * setter du rectangle de depart
     * @param rectDepart
     */
    public void setRectDepart(Rect rectDepart) {
        this.rectDepart = rectDepart;
    }

    /**
     * getter du rectangle d'arrivee
     * @return le rectangle d'arrivee
     */
    public Rect getRectArrivee() {
        return rectArrivee;
    }

    /**
     * setter du rectangle d'arrivee
     * @param rectArrivee
     */
    public void setRectArrivee(Rect rectArrivee) {
        this.rectArrivee = rectArrivee;
    }

    /**
     * Fonction equals de Ouverture
     * @param o
     * @return true si egaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(murDepart, ((Ouverture) o).getMurDepart()) && Objects.equals(murArrivee, ((Ouverture) o).getMurArrivee()) && Objects.equals(rectDepart, ((Ouverture) o).getRectDepart()) && Objects.equals(rectArrivee, ((Ouverture) o).getRectArrivee());
    }

    /**
     * Retourne le hashCode de Ouverture
     * @return le hashCode de Ouverure
     */
    @Override
    public int hashCode() {
        return Objects.hash(murDepart, murArrivee, rectDepart, rectArrivee);
    }

    /**
     * Fonction toString d'ouverture
     * @return le toString d'Ouverture
     */
    @Override
    public String toString() {
        return "Ouverture{" +
                "murDepart=" + murDepart +
                ", murArrivee=" + murArrivee +
                ", rectDepart=" + rectDepart +
                ", rectArrivee=" + rectArrivee +
                '}';
    }
}
