package com.example.myhabitat;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Ouverture;
import habitat.Piece;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import outils.GrapheHabitat;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    private Habitat habitat;
    private TextView textView;

    /**
     * onCreate de MainActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ouvrirJSON();

        //Graphe graphe = new Graphe(habitat);
        GrapheHabitat grapheHabitat = new GrapheHabitat(habitat);


        Button b = findViewById(R.id.buttonImmersion);
        if(habitat.getPieces().size() == 0) {
            b.setEnabled(false);
        }else{
            b.setEnabled(true);
        }

    }

    /**
     * Permet de lancer l'activite de conception
     * @param view
     */
    public void conception(View view) {
        Intent intent = new Intent(this, ModeConceptionActivity.class);
        intent.putExtra("Habitat", habitat);
        startActivity(intent);
    }

    /**
     * Permet de lancer l'activite d'immersion
     * @param view
     */
    public void immersion(View view) {
        Intent intent = new Intent(this, ModeImmersionActivity.class);
        intent.putExtra("Habitat", habitat);
        startActivity(intent);
    }

    /**
     * Permet de recuperer l'habitat enregistre
     */
    public void ouvrirJSON(){
        habitat = new Habitat();
        FileInputStream fis = null;
        try {
            fis = openFileInput("enregistrement.json");
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
        }
        if (fis != null) {
            String json = getFileContent(fis);

            try {
                JSONObject enregistrement = new JSONObject(json);
                JSONArray pieces = enregistrement.getJSONArray("Pieces");
                for(int i=0; i<pieces.length(); i++){
                    JSONObject Jpiece = (JSONObject) pieces.get(i);
                    Piece piece = new Piece(Jpiece);
                    habitat.addPiece(piece);
                }
                JSONArray ouvertures = enregistrement.getJSONArray("Ouvertures");
                for(int i=0; i<ouvertures.length(); i++){
                    JSONObject Jouverture = (JSONObject) ouvertures.get(i);
                    Ouverture ouverture = new Ouverture(Jouverture);
                    habitat.addOuverture(ouverture);
                }
                Log.i("testJSONouverture", habitat.toString());

            } catch (JSONException e) {
                //throw new RuntimeException(e);
            }

            Log.i("testJSON", json);
        }else{
            Log.i("testJSON", "pbm ouverture");
        }
        //textView.setText(habitat.toString());
    }

    /**
     * Fonction permettant de recuperer le texte dans un texte
     * @param fis
     * @return
     */
    public String getFileContent(FileInputStream fis) {
        StringBuilder sb = new StringBuilder();
        Reader r = null;
        try {
            r = new InputStreamReader(fis, "UTF-8");
            int ch = r.read();
            while(ch >= 0) {
                sb.append((char)ch);
                ch = r.read();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * Fonction de 1er demarrage de l'activite
     */
    @Override
    protected void onPostResume() {
        ouvrirJSON();
        Button b = findViewById(R.id.buttonImmersion);
        if(habitat.getPieces().size() == 0) {
            b.setEnabled(false);
        }else{
            b.setEnabled(true);
        }
        super.onPostResume();
    }
}