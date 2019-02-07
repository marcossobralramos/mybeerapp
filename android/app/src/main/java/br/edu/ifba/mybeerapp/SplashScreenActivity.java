package br.edu.ifba.mybeerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 4000;

    private Handler handler = new Handler();
    private Runnable runnableCodigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        handler.post(runnableCodigo);
    }
}
