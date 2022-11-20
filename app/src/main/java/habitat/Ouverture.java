package habitat;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Ouverture implements Parcelable {

    private Mur murDepart;
    private Mur murArrivee;
    private Rect rectDepart;
    private Rect rectArrivee;

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

    public Ouverture(Mur murDepart, Mur murArrivee, Rect rectDepart, Rect rectArrivee){
        this.murDepart = murDepart;
        this.murArrivee = murArrivee;
        this.rectDepart = rectDepart;
        this.rectArrivee = rectArrivee;
    }

    protected Ouverture(Parcel in){
        murDepart = in.readParcelable(Mur.class.getClassLoader());
        murArrivee = in.readParcelable(Mur.class.getClassLoader());
        rectDepart = in.readParcelable(Rect.class.getClassLoader());
        rectArrivee = in.readParcelable(Rect.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(murDepart, flags);
        dest.writeParcelable(murArrivee, flags);
        dest.writeParcelable(rectDepart, flags);
        dest.writeParcelable(rectArrivee, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(murDepart, ((Ouverture) o).getMurDepart()) && Objects.equals(murArrivee, ((Ouverture) o).getMurArrivee()) && Objects.equals(rectDepart, ((Ouverture) o).getRectDepart()) && Objects.equals(rectArrivee, ((Ouverture) o).getRectArrivee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(murDepart, murArrivee, rectDepart, rectArrivee);
    }

    public Mur getMurDepart() {
        return murDepart;
    }

    public void setMurDepart(Mur murDepart) {
        this.murDepart = murDepart;
    }

    public Mur getMurArrivee() {
        return murArrivee;
    }

    public void setMurArrivee(Mur murArrivee) {
        this.murArrivee = murArrivee;
    }

    public Rect getRectDepart() {
        return rectDepart;
    }

    public void setRectDepart(Rect rectDepart) {
        this.rectDepart = rectDepart;
    }

    public Rect getRectArrivee() {
        return rectArrivee;
    }

    public void setRectArrivee(Rect rectArrivee) {
        this.rectArrivee = rectArrivee;
    }

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
