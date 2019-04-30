package in.careerdost.quiznew.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import in.careerdost.quiznew.R;
import in.careerdost.quiznew.database.Question_easy;
import in.careerdost.quiznew.recycler_activity.recycler_archived;
import in.careerdost.quiznew.recycler_activity.recycler_examples;
import in.careerdost.quiznew.recycler_activity.recycler_formulae;

public class start_quiz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = "LRTest";
    public static final String EASY_CATEGORY = "easyCategory";
    private Spinner spinnerCatEasy;
    Button btnStartQuizEasy;
    ImageView solExamplesIcon, archivedIcon, impFormulaeIcon;
    InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_quiz);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        btnStartQuizEasy = findViewById(R.id.btnStartQuiz);
        btnStartQuizNormal = findViewById(R.id.btnStartQuizNormal);
        spinnerCatEasy = findViewById(R.id.spinner_category);
        spinnerCatNormal = findViewById(R.id.spinner_category_normal);
        solExamplesIcon = findViewById(R.id.solExamplesIcon);
        archivedIcon = findViewById(R.id.archivedIcon);
        impFormulaeIcon = findViewById(R.id.impFormulaeIcon);

        String[] categoryLevelEasy = Question_easy.getAllCategoryLevels();
        ArrayAdapter<String> adapterCategoryEasy = new ArrayAdapter<>(start_quiz.this,
                android.R.layout.simple_spinner_item, categoryLevelEasy);
        adapterCategoryEasy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCatEasy.setAdapter(adapterCategoryEasy);

        btnStartQuizEasy.setOnClickListener((View v) -> {
            startQuizEasy();
        });

        solExamplesIcon.setOnClickListener((View v) -> {
            Intent intent = new Intent(start_quiz.this, recycler_examples.class);
            startActivity(intent);
        });

        archivedIcon.setOnClickListener((View v) -> {
            Intent intent = new Intent(start_quiz.this, recycler_archived.class);
            startActivity(intent);
        });

        impFormulaeIcon.setOnClickListener((View v) -> {
            Intent intent = new Intent(start_quiz.this, recycler_formulae.class);
            startActivity(intent);
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private void startQuizEasy() {
        String easyCategory = spinnerCatEasy.getSelectedItem().toString();
        Intent levelOne = new Intent(start_quiz.this, quiz_category.class);
        levelOne.putExtra(EASY_CATEGORY, easyCategory);
        startActivity(levelOne);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_aboutUs) {
            Intent aboutus = new Intent(this, Aboutus.class);
            startActivity(aboutus);
        } else if (id == R.id.nav_privacypolicy) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://careerdost.in/privacy-policy"));
                startActivity(intent);
        } if (id == R.id.nav_help) {
            Intent help = new Intent(this, Help.class);
            startActivity(help);
        } else if (id == R.id.nav_youtubePage) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/c/careerdost?sub_confirmation=1"));
                startActivity(intent);
        } else if (id == R.id.nav_fbPage) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/careerdost.in"));
                startActivity(intent);
        } else if (id == R.id.nav_twPage) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/careerdost"));
                startActivity(intent);
        } else if (id == R.id.nav_shareApp) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "What's your Aptitude/Reasoning IQ Score?" +
                    "\n\n- via @careerdost #test #quiz - FREE #Android #app Download now!" +
                    "\nhttps://bit.ly/2G0Z78s";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "What's your Aptitude Score?");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_update) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://bit.ly/2G0Z78s"));
                startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
