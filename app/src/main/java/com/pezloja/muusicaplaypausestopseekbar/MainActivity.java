package com.pezloja.muusicaplaypausestopseekbar;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView str_lleva, str_queda;
    Button btn_play, btn_pause, btn_stop;
    SeekBar sk_musica;
    MediaPlayer mediaPlayer;
    Runnable handlerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        str_lleva = findViewById(R.id.str_lleva);
        str_queda = findViewById(R.id.str_queda);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_stop = findViewById(R.id.btn_stop);
        sk_musica = findViewById(R.id.sk_musica);

        mediaPlayer = MediaPlayer.create(this, R.raw.vals_ruso);

        sk_musica.setMax(mediaPlayer.getDuration());

        setSupportActionBar(findViewById(R.id.menu));

        btn_play.setOnClickListener(view -> empezarMusica());
        btn_pause.setOnClickListener(view -> pausarMusica());
        btn_stop.setOnClickListener(view -> pararMusica());

        sk_musica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(position);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void empezarMusica() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        startTimer();
    }

    private void pausarMusica() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void pararMusica() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.vals_ruso);
        sk_musica.setProgress(0);
        mostrarTiempos();
    }

    public void mostrarTiempos() {
        mostarTimeMMSS(mediaPlayer.getCurrentPosition(), str_lleva);
        mostarTimeMMSS(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition(), str_queda);
    }

    private void mostarTimeMMSS(long valor, TextView txtView) {
        String formatTime = String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(valor)
                , TimeUnit.MILLISECONDS.toSeconds( valor) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes( valor)));
        txtView.setText(formatTime);
    }

    public void startTimer() {
        handlerTask = () -> {
            sk_musica.setProgress(mediaPlayer.getCurrentPosition());
            mostrarTiempos();
            new Handler().postDelayed(handlerTask, 1000);
        };
        handlerTask.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_salir) {
            crearDialogoSalir();
        }
        return false;
    }

    private void crearDialogoSalir() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Seguro que deseas salir?")
                .setPositiveButton("Si", (dialogInterface, i) -> {
                    mediaPlayer.stop();
                    finish();
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}