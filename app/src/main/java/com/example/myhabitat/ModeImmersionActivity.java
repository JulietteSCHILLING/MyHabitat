package com.example.myhabitat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Orientation;
import habitat.Piece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;

public class ModeImmersionActivity extends AppCompatActivity implements SensorEventListener {
    Habitat habitat;
    Mur murEnCours;
    Piece pieceEnCours;
    private SensorManager sensorManager;
    private ImageView imageViewBoussole;
    private float debut = 0;
    private ImageView imageViewMur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
            }
        }

        setContentView(R.layout.activity_mode_immersion);

        pieceEnCours = habitat.getPieces().get(0);
        affichePiece(pieceEnCours);
        imageViewBoussole = findViewById(R.id.imageViewBoussole);
        imageViewMur = findViewById(R.id.imageViewMur);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_immersion, menu);

        int i =0;
        for(Piece piece : habitat.getPieces()){
            menu.add(piece.getNom());
            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.i("testImmersion", "J'ouvre " + item.getTitle());
                    Toast.makeText(getBaseContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    affichePiece(piece);
                    return false;
                }
            });
            i++;
        }

        return true;
    }

    public void affichePiece(Piece piece){
        pieceEnCours = piece;
        ImageView imageViewMur = findViewById(R.id.imageViewMur);
        murEnCours = piece.getMurs().get(0);

        //On récupère la photo
        FileInputStream fis = null;
        try {
            fis = openFileInput(murEnCours.getId()+".data");
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
        }
        if (fis != null) {
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageViewMur.setImageBitmap(bm);
        }else{
            Log.i("testDrawable", "pas de photo");
            imageViewMur.setImageDrawable(getDrawable(R.drawable.imagemur));
        }
    }

    public void afficheMur(){
        //On récupère la photo
        FileInputStream fis = null;
        try {
            fis = openFileInput(murEnCours.getId()+".data");
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
        }
        if (fis != null) {
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageViewMur.setImageBitmap(bm);
        }else{
            Log.i("testDrawable", "pas de photo");
            imageViewMur.setImageDrawable(getDrawable(R.drawable.imagemur));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // On récupère l'angle
        float angle = -(Math.round(event.values[0]));

        //On créé l'animation de rotation
        RotateAnimation rotateAnimation = new RotateAnimation(debut, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(200);

        //Et on la lance
        if(imageViewBoussole == null) {
            imageViewBoussole = findViewById(R.id.imageViewBoussole);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageViewBoussole.startAnimation(rotateAnimation);


                if(angle<(-45) && angle>=(-135)){
                    murEnCours = pieceEnCours.getMurOrientation(Orientation.EST);
                } else if (angle<(-135) && angle>=(-225)) {
                    murEnCours = pieceEnCours.getMurOrientation(Orientation.SUD);
                } else if (angle<(-225) && angle>=(-315)) {
                    murEnCours = pieceEnCours.getMurOrientation(Orientation.OUEST);
                }else{
                    murEnCours = pieceEnCours.getMurOrientation(Orientation.NORD);
                }
                afficheMur();
            }
        });

        //Maj de l'angle de depart
        debut = angle;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }
}