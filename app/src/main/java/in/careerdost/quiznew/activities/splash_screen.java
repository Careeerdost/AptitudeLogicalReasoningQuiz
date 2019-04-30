package in.careerdost.quiznew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;

import in.careerdost.quiznew.R;
import io.fabric.sdk.android.Fabric;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);

        //Splash Screen

        new Handler().postDelayed(() -> {
                Intent brandIntroSplash = new Intent(splash_screen.this, start_quiz.class);
                startActivity(brandIntroSplash);
                finish();
        }, 3000); // Sleep 3 seconds
    }
}
