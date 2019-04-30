package in.careerdost.quiznew.activities;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import in.careerdost.quiznew.R;

public class formulae_act extends AppCompatActivity {

    WebView formulaeUrl;
    TextView formulaeTitle, formulaeCat;
    ImageView formulae_icon;

    private final String TAG = InterstitialAdActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(in.careerdost.quiznew.R.layout.formulae_act);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 500);

        String name = getIntent().getExtras().getString("name");
        String category = getIntent().getExtras().getString("category");
        String img = getIntent().getExtras().getString("img");
        String url = getIntent().getExtras().getString("url");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(in.careerdost.quiznew.R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitleEnabled(true);

        formulaeTitle = findViewById(in.careerdost.quiznew.R.id.formulaeTitle);
        formulaeCat = findViewById(in.careerdost.quiznew.R.id.formulaeCat);
        formulae_icon = findViewById(in.careerdost.quiznew.R.id.formulae_icon);
        formulaeUrl = findViewById(in.careerdost.quiznew.R.id.formulaeUrl);

        formulaeTitle.setText(name);
        formulaeCat.setText(category);
        formulaeUrl.setWebViewClient(new WebViewClient());
        formulaeUrl.loadUrl(url);

        formulaeUrl.getSettings().setLoadsImagesAutomatically(true);
        formulaeUrl.getSettings().setJavaScriptEnabled(true);
        formulaeUrl.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        formulaeUrl.getSettings().setUseWideViewPort(true);
        formulaeUrl.getSettings().setLoadWithOverviewMode(true);
        formulaeUrl.getSettings().setSupportZoom(true);
        formulaeUrl.getSettings().setBuiltInZoomControls(true);
        formulaeUrl.getSettings().setDisplayZoomControls(false);

        collapsingToolbarLayout.setTitle(name);

        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .placeholder(in.careerdost.quiznew.R.drawable.loading_shape)
                .error(in.careerdost.quiznew.R.drawable.loading_shape);

        Glide.with(this).load(img).apply(requestOptions).into(formulae_icon);

        interstitialAd = new InterstitialAd(this, getString(in.careerdost.quiznew.R.string.fb_interstitial));
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
        interstitialAd.loadAd();
        showAdWithDelay();

        AdView adView = new AdView(this, getString(in.careerdost.quiznew.R.string.fb_banner), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();
    }

    private void showAdWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
                return;
            }
            if (interstitialAd.isAdInvalidated()) {
                return;
            }
            interstitialAd.show();
        }, 1000 * 60 * 5);
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
}