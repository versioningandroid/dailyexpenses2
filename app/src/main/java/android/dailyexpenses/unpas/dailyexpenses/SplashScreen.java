package android.dailyexpenses.unpas.dailyexpenses;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;



public class SplashScreen extends Activity {
    private static int timer = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,HomeActivity.class);
                startActivity(i);

                finish();
            }
        },timer);
    }
}
