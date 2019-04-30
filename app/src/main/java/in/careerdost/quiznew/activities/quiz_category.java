package in.careerdost.quiznew.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import in.careerdost.quiznew.R;
import in.careerdost.quiznew.database.Aptitude_cal_db;
import in.careerdost.quiznew.database.Question_easy;
import in.careerdost.quiznew.database.QuizDbHelper_easy;

public class quiz_category extends AppCompatActivity {
    private static final long COUNTDOWN_IN_MILLIS = 420000;
    ArrayList<Question_easy> questionList = new ArrayList<>();
    private int score = 0;
    private int qid = 0;
    private int questionNum = 0;
    private int totalQuestion;
    int attempt_counter = 7;
    int correctAnswer = 0;

    InterstitialAd interstitialAd = null;

    private Question_easy currentQ;
    private TextView question, timer, overallScore, txtTotalQuestion,
            txtShowAnswer, overallCorrectAnswer;
    private Button btnConfirm;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3, rb4;

    private int skipped = 0;
    private ColorStateList txtColorDefaultRb;
    private ColorStateList txtColorDefaultCd;
    private long timeLeftInMillis;
    private boolean answered;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_category);

        question = findViewById(R.id.question);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);

        btnConfirm = findViewById(R.id.btn_confirm);
        Button btnSkip = findViewById(R.id.btn_skip);
        txtShowAnswer = findViewById(R.id.txt_show_Answer);
        TextView catLevel = findViewById(R.id.ques_category);
        overallCorrectAnswer = findViewById(R.id.overallCorrectAnswer);
        overallScore = findViewById(R.id.overallScore);
        timer = findViewById(R.id.timer);
        txtTotalQuestion = findViewById(R.id.txtTotalQuestion);

        Intent intent = getIntent();
        String easyCategory = intent.getStringExtra(start_quiz.EASY_CATEGORY);
        catLevel.setText(easyCategory);

        overallScore.setText(getResources().getString(R.string.score_zero));
        overallCorrectAnswer.setText(getResources().getString(R.string.correct_zero));
        String quesNumText = (Integer.toString(questionNum) + "/" + totalQuestion);
        txtTotalQuestion.setText(quesNumText);

        QuizDbHelper_easy dbHelper = new QuizDbHelper_easy(this);
        questionList = dbHelper.getQuestions(easyCategory);
        totalQuestion = questionList.size();
        Collections.shuffle(questionList);
        currentQ = questionList.get(qid);
        setNextQues();

        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

        btnConfirm.setOnClickListener((View v) -> {
            if (!answered) {
                if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                    checkAnswer();
                } else {
                    Toast.makeText(getBaseContext(), "Please select a Option", Toast.LENGTH_SHORT).show();
                }
            } else {
                setNextQues();
            }
        });
        btnSkip.setOnClickListener((View v) -> {
            skipped = skipped + 1;
            setNextQues();
        });

        AdView mAdView = findViewById(R.id.quiz_banner_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        AdRequest interAdRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(interAdRequest);
    }

    private void setNextQues() {
        rbGroup.clearCheck();
        txtShowAnswer.setText("");
        if (qid < totalQuestion) {
            currentQ = questionList.get(qid);
            question.setText(currentQ.getQUESTION());
            rb1.setText(currentQ.getOPTA());
            rb2.setText(currentQ.getOPTB());
            rb3.setText(currentQ.getOPTC());
            rb4.setText(currentQ.getOPTD());
            qid++;
            questionNum++;
            String quesNumText = (Integer.toString(questionNum) + "/" + totalQuestion);
            txtTotalQuestion.setText(quesNumText);
            answered = false;
            btnConfirm.setText(getResources().getString(R.string.confirm));

        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        answered = true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQ.getANSWER()) {
            String overall_score = "Score: " + score;
            overallScore.setText(overall_score);
        }
        if (answerNr == currentQ.getANSWER()) {
            String overall_correct_answer = "Correct: " + correctAnswer;
            overallCorrectAnswer.setText(overall_correct_answer);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQ.getANSWER()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                txtShowAnswer.setText(getResources().getString(R.string.option_a_is_correct));
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                txtShowAnswer.setText(getResources().getString(R.string.option_b_is_correct));
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                txtShowAnswer.setText(getResources().getString(R.string.option_c_is_correct));
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                txtShowAnswer.setText(getResources().getString(R.string.option_d_is_correct));
                break;
        }
        if (qid < totalQuestion) {
            btnConfirm.setText(getResources().getString(R.string.next));
        } else {
            btnConfirm.setText(getResources().getString(R.string.finish));
        }
    }

    private void finishQuiz() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        } else {
            Intent intent = new Intent(quiz_category.this, results_quiz_category.class);
            Bundle b = new Bundle();
            b.putInt("score", score);
            b.putInt("totalQuestion", totalQuestion);
            b.putInt("skipped", skipped);
            b.putInt("correctAnswer", correctAnswer);
            int incorrectAnswer = totalQuestion - correctAnswer;
            b.putInt("incorrectAnswer", incorrectAnswer);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }
}
