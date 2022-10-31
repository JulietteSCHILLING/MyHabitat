package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
                            textView.setText(habitat.toString());

                        }

                    }
                }
        );

        habitat = new Habitat();

        textView = findViewById(R.id.textTest);
        textView.setText(habitat.toString());

    }

    public void AffichePieces(View view) {
        Intent intent = new Intent(this, PiecesActivity.class);
        intent.putExtra("Habitat", habitat);
        //startActivity(intent);
        if (intent.resolveActivity(getPackageManager()) != null){
            launcher.launch(intent);
        }
    }

    public void enregistrement(){
        JSONObject enregistrement = new JSONObject();
        JSONArray pieces = new JSONArray();
        JSONArray murs = new JSONArray();

        for(Piece piece : habitat.getPieces()){
            for(Mur mur : piece.getMurs()){
                JSONObject Jmur = new JSONObject();
                try {
                    Jmur.put("Orientation", mur.getOrientation());
                    Jmur.put("Id", mur.getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                murs.put(Jmur);
            }
            pieces.put(piece.getNom());
            pieces.put(murs);
        }
        try {
            enregistrement.put("Pieces", pieces);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if(enregistrement != null){
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("enregistrement.json", MODE_PRIVATE);
                PrintStream ps = new PrintStream(fos);
                ps.print(enregistrement);
                ps.close();
                fos.flush();
                Log.i("testEnregistrement", "enregistrement.json a bien été enregistré");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.i("testJSON", enregistrement.toString());

            FileInputStream fis = null;
            try {
                fis = openFileInput("enregistrement.json");
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
            }
            if (fis != null) {
                String json = getFileContent(fis);

                Log.i("testJSON", json);
            }


        }else{
            Log.i("testJSON", "pbm");
        }
    }

    public String getFileContent( FileInputStream fis ) {
        StringBuilder sb = new StringBuilder();
        Reader r = null;  //or whatever encoding
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
    protected void onPause() {
        Log.i("testOn", "on y est !");
        enregistrement();
        super.onPause();
    }
}