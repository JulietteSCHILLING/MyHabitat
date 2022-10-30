package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public Habitat getHabitat(){
        return this.habitat;
    }
}