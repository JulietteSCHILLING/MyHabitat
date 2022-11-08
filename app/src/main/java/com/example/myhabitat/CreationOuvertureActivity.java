package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Orientation;
import habitat.Piece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;

public class CreationOuvertureActivity extends AppCompatActivity{

    private Habitat habitat;
    private ImageView imageViewDepart;
    private ImageView imageViewArrivee;
    private Piece pieceDepart;
    private Piece pieceArrivee;
    private Orientation orientationPieceDepart;
    private Orientation orientationPieceArrivee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_ouverture);

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
            }
        }

        imageViewDepart = findViewById(R.id.imageViewDepart);
        imageViewArrivee = findViewById(R.id.imageViewArrivee);

        Spinner spinnerD = findViewById(R.id.spinnerDepart);
        Spinner spinnerDOrientation = findViewById(R.id.spinnerDOrientation);
        Spinner spinnerA = findViewById(R.id.spinnerArrivee);
        Spinner spinnerAOrientation = findViewById(R.id.spinnerAOrientation);

        String[] arrayPieces = new String[habitat.getPieces().size()];
        for(int i=0; i<habitat.getPieces().size(); i++){
            arrayPieces[i] = habitat.getPieces().get(i).getNom();
        }

        String[] arrayOrientation = new String[]{"NORD", "SUD", "EST", "OUEST"};

        ArrayAdapter arrayAdapterD = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayPieces);
        spinnerD.setAdapter(arrayAdapterD);
        ArrayAdapter arrayAdapterDOrientation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOrientation);
        spinnerDOrientation.setAdapter(arrayAdapterDOrientation);
        ArrayAdapter arrayAdapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayPieces);
        spinnerA.setAdapter(arrayAdapterA);
        ArrayAdapter arrayAdapterAOrientation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOrientation);
        spinnerAOrientation.setAdapter(arrayAdapterAOrientation);


        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceDepart = habitat.getPieces().get(position);
                affichePieceDepart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        orientationPieceDepart = Orientation.NORD;
                        break;
                    case 1:
                        orientationPieceDepart = Orientation.SUD;
                        break;
                    case 2:
                        orientationPieceDepart = Orientation.EST;
                        break;
                    case 3:
                        orientationPieceDepart = Orientation.OUEST;
                        break;
                    default:
                        orientationPieceDepart = Orientation.SUD;
                        break;
                }
                affichePieceDepart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceArrivee = habitat.getPieces().get(position);
                affichePieceArrivee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        orientationPieceArrivee = Orientation.NORD;
                        break;
                    case 1:
                        orientationPieceArrivee = Orientation.SUD;
                        break;
                    case 2:
                        orientationPieceArrivee = Orientation.EST;
                        break;
                    case 3:
                        orientationPieceArrivee = Orientation.OUEST;
                        break;
                    default:
                        orientationPieceArrivee = Orientation.SUD;
                        break;
                }
                affichePieceArrivee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void affichePieceDepart(){
        if(pieceDepart != null && orientationPieceDepart != null){
            Mur mur = pieceDepart.getMurOrientation(orientationPieceDepart);
            FileInputStream fis = null;
            try {
                fis = openFileInput(mur.getId()+".data");
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
            }
            if (fis != null) {
                Bitmap bm = BitmapFactory.decodeStream(fis);

                imageViewDepart.setImageBitmap(bm);
            }else{
                imageViewDepart.setImageDrawable(getDrawable(R.drawable.imagemur));
            }
        }
    }


    public void affichePieceArrivee(){
        if(pieceArrivee != null && orientationPieceArrivee != null){
            Mur mur = pieceArrivee.getMurOrientation(orientationPieceArrivee);
            FileInputStream fis = null;
            try {
                fis = openFileInput(mur.getId()+".data");
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
            }
            if (fis != null) {
                Bitmap bm = BitmapFactory.decodeStream(fis);

                imageViewArrivee.setImageBitmap(bm);
            }else{
                imageViewArrivee.setImageDrawable(getDrawable(R.drawable.imagemur));
            }
        }
    }

}