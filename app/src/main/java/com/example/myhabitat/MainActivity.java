package com.example.myhabitat;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Piece;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    private Habitat habitat;
    private TextView textView;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        launcher = registerForActivityResult(
                // Contrat qui détermine le type de l'interaction
                new ActivityResultContracts.StartActivityForResult(),
                // Callback appelé lorsque le résultat sera disponible
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //On récupère les données de habitat
                        Intent intent = result.getData();
                        if(intent != null) {
                            Bundle extras = intent.getExtras();
                            habitat = (Habitat) extras.get("Habitat");
                            habitat.setCorrectly();
                            ouvrirJSON();
                            textView.setText(habitat.toJSON().toString());

                        }

                    }
                }
        );

         */

        textView = findViewById(R.id.textTest);

        ouvrirJSON();

        textView.setText(habitat.toString());

    }

    public void conception(View view) {
        Intent intent = new Intent(this, ModeConceptionActivity.class);
        intent.putExtra("Habitat", habitat);
        startActivity(intent);
        /*
        if (intent.resolveActivity(getPackageManager()) != null){
            launcher.launch(intent);
        }

         */
    }

    public void immersion(View view) {
        Intent intent = new Intent(this, ModeImmersionActivity.class);
        intent.putExtra("Habitat", habitat);
        startActivity(intent);
        /*
        if (intent.resolveActivity(getPackageManager()) != null){
            launcher.launch(intent);
        }

         */
    }

    public void ouvrirJSON(){
        //habitat.reset();
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
                Log.i("testJSONouverture", habitat.toString());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("testJSON", json);
        }else{
            Log.i("testJSON", "pbm ouverture");
        }
        textView.setText(habitat.toString());
    }

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

    @Override
    protected void onPostResume() {
        ouvrirJSON();
        super.onPostResume();
    }
}