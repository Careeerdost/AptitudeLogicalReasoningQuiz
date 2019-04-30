package in.careerdost.quiznew.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import in.careerdost.quiznew.R;
import in.careerdost.quiznew.database.CustomPercentFormatter;
import in.careerdost.quiznew.database.Question_easy;

public class results_quiz_category extends AppCompatActivity {
    ArrayList<Question_easy> questionList = new ArrayList<>();
    TextView mGrade, mFinalScore;
    Button btnRetake, btnShareResult;
    ImageView resultsImage;
    InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_quiz_category);

        mGrade = findViewById(R.id.congrats);
        mFinalScore = findViewById(R.id.youScored);

        btnRetake = findViewById(R.id.btnRetake);
        btnShareResult = findViewById(R.id.btnShareResult);
        resultsImage = findViewById(R.id.resultsImage);

        Bundle b = getIntent().getExtras();
        final int score = b.getInt("score");
        final int totalQues = b.getInt("totalQuestion");
        final int skipped = b.getInt("skipped");
        final int correctAnswer = b.getInt("correctAnswer");
        final int incorrectAnswer = b.getInt("incorrectAnswer");
        int eachCorrectAnswerScore = 5;
        int totalQuesScore = totalQues * eachCorrectAnswerScore;
        String mFinalScoreText = "You Scored " + score + " out of " + totalQuesScore;
        mFinalScore.setText(mFinalScoreText);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        AdRequest interAdRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(interAdRequest);

        btnRetake.setOnClickListener((View v) -> {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        finish();
                    }
                });
            } else {
                startActivity(new Intent(results_quiz_category.this, start_quiz.class));
                results_quiz_category.this.finish();
            }
        });
        btnShareResult.setOnClickListener((View v) -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I Scored " + score + " out of " + totalQuesScore +
                    "\n\n- via @careerdost #test #quiz - FREE #Android #app Download now!" +
                    "\nhttps://bit.ly/2G0Z78s";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "What's your Aptitude Score?");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
    }
}
