package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private static final int SPLASH_DISPLAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent beerListIntent = new Intent(SplashScreenActivity.this, StartScreenActivity.class);
                SplashScreenActivity.this.startActivity(beerListIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}