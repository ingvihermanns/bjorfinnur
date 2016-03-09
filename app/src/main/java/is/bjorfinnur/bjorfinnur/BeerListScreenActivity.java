package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Notandi on 9.3.2016.
 */
public class BeerListScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, StartScreenActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
