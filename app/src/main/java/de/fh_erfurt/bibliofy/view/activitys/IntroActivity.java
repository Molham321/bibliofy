package de.fh_erfurt.bibliofy.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.fh_erfurt.bibliofy.R;

public class IntroActivity extends AppCompatActivity {

    private static final String LOG_TAG_EVENTS = "EventCallbacks";

    /**
     * Definition of a new click listener instance as anonymous class
     * Will handle all buttons found in this activity
     */
    private View.OnClickListener buttonClickListener = (v) -> {

        Log.i(LOG_TAG_EVENTS, "Button tapped");

        // Check which button was clicked
        if (v.getId() == R.id.btGoToSecondIntroActivity) {
            Log.i(LOG_TAG_EVENTS, "Go to Second Intro Activity Button tapped");

            goToHomePageActivity() ;
        }
    };

    /**
     * Activity Start Method
     * Helper Methods to Track Life Cycle
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        // Link Button with Click Listener
        Button toSecond = findViewById(R.id.btGoToSecondIntroActivity);
        toSecond.setOnClickListener(this.buttonClickListener);
    }


    /**
     * Helper to start next activity
     */
    private void goToHomePageActivity() {
        Log.i(LOG_TAG_EVENTS, "Switching to Home Page Activity");
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
    }
}