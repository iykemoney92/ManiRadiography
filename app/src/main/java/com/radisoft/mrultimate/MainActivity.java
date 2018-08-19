package com.radisoft.mrultimate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Tracker mTracker;
    private final String TAG = "com.MM";
    private final String name ="MM";
    private static GoogleAnalytics sAnalytics;
    private AdView mAdView;
    private WebView wb;
    private Activity _this;
    private String asset_path = "file:///android_asset/";
    private String blog_url = "http://maniradiography.wordpress.com";
    private String youtube_link = "https://www.youtube.com/playlist?list=PLfLZ5PyfyffHCWF523Pw1KSbyE6hiScNY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _this = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       sAnalytics = GoogleAnalytics.getInstance(this);
        if (mTracker == null) {
            mTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        if(mTracker==null)
        {
            Toast.makeText(getApplicationContext(),"Tracker is null",Toast.LENGTH_LONG).show();
        }
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            //    Toast.makeText(getApplicationContext(),"Ad loaded",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                String failedString="";
                switch (errorCode)
                {
                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        failedString="Internal Error";
                        break;
                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
                        failedString="Invalid Request Error";
                        break;
                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
                        failedString="Network Error";
                        break;
                    case AdRequest.ERROR_CODE_NO_FILL:
                        failedString="No Fill Error";
                        break;

                }
               // Toast.makeText(getApplicationContext(),"Ad failed "+failedString,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
              //  Toast.makeText(getApplicationContext(),"Ad opened",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            //    Toast.makeText(getApplicationContext(),"Ad left",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }

        });
        wb = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb.setVerticalScrollBarEnabled(true);
        wb.setHorizontalScrollBarEnabled(true);
        wb.setWebChromeClient(new WebChromeClient() {
            private ProgressDialog mProgress;

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (mProgress == null) {
                    mProgress = new ProgressDialog(_this);
                    mProgress.show();
                }
                mProgress.setMessage("Loading " + String.valueOf(progress) + "%");
                if (progress == 100) {
                    mProgress.dismiss();
                    mProgress = null;
                }
            }
        });
        wb.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("facebook")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return false;
                }
                else {

                    view.loadUrl(url);
                    return true;
                }
            }
        });
        wb.loadUrl(asset_path+"xray/1/8/index.html");

        RateThisApp.init(new RateThisApp.Config(3, 5));

        // Set callback (optional)
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
            //    Toast.makeText(MainActivity.this, "Yes event", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoClicked() {
            //    Toast.makeText(MainActivity.this, "No event", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClicked() {
            //    Toast.makeText(MainActivity.this, "Cancel event", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + name);
        /*mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (mAdView != null) {
            mAdView.resume();
        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (wb.canGoBack()) {
                wb.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_ratings)
        {
            RateThisApp.showRateDialog(MainActivity.this);
        }
        if(id == R.id.action_share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String appPackage = getApplicationContext().getPackageName();
            String shareBody = "Hey! Check out this awesome radiological app." +
                    " It helps you to check radiological examination procedures, " +
                    "video demonstrations and other details. You can download it from playstore via " + " https://play.google.com/store/apps/details?id=" + appPackage;;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mani's Radiography App Download");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        //noinspection SimplifiableIfStatement
        if(id == R.id.action_req_edit)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Request Content Edit");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, we appreciate you wish to contribute to the development and updates of Mani's Radiography. " +
                    "Please do attach the zip file containing the contents you wished edited");
            intent.setData(Uri.parse("mailto:maniradiography@gmail.com")); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Obtain the shared Tracker instance.
        int id = item.getItemId();
        if (id == R.id.history) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("History")
                    .build());
            wb.loadUrl(asset_path+"xray/1/8/index.html");
        }if (id == R.id.pob) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Planes Of The Body")
                    .build());
            wb.loadUrl(asset_path+"xray/1/0/index.html");

        }if (id == R.id.position) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Positioning")
                    .build());
            wb.loadUrl(asset_path+"xray/1/1/index.html");

        }if (id == R.id.project) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Projections")
                    .build());
            wb.loadUrl(asset_path+"xray/1/2/index.html");

        }if (id == R.id.cassystem) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Cassette Systems")
                    .build());
            wb.loadUrl(asset_path+"xray/1/3/index.html");

        }if (id == R.id.expfactor) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Exposure Factors")
                    .build());
            wb.loadUrl(asset_path+"xray/1/4/index.html");

        }if (id == R.id.pmeasure) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Protection Measures")
                    .build());
            wb.loadUrl(asset_path+"xray/1/5/index.html");

        }if (id == R.id.blog) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Blog")
                    .build());
            wb.loadUrl(blog_url);

        }if (id == R.id.abbr) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Abbr")
                    .build());
            wb.loadUrl(asset_path+"xray/1/15/index.html");

        }if (id == R.id.units) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Intro")
                    .setAction("Units")
                    .build());
            wb.loadUrl(asset_path+"xray/1/16/index.html");

        }
        if (id == R.id.ref) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Reference")
                    .build());
            wb.loadUrl(asset_path+"reference.html");
        }
        if (id == R.id.about) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("About")
                    .build());
            wb.loadUrl(asset_path+"about.html");
        }
        if (id == R.id.headneck) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Head & Neck")
                    .build());
            wb.loadUrl(asset_path+"xray/2/index.html");
        }
        if (id == R.id.thorax) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Thorax")
                    .build());
            wb.loadUrl(asset_path+"xray/3/index.html");
        }if (id == R.id.ulimb) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Upper Limb")
                    .build());
            wb.loadUrl(asset_path+"xray/4/index.html");
        }if (id == R.id.abdomen) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Abdomen")
                    .build());
            wb.loadUrl(asset_path+"xray/5/index.html");
        }if (id == R.id.pelvis) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Pelvis")
                    .build());
            wb.loadUrl(asset_path+"xray/6/index.html");
        }if (id == R.id.llimb) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Lower Limb")
                    .build());
            wb.loadUrl(asset_path+"xray/7/index.html");
        }if (id == R.id.vcolumn) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Vertebral Column")
                    .build());
            wb.loadUrl(asset_path+"xray/8/index.html");
        }
        if (id == R.id.contrast) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Contrast Examinations")
                    .build());
            wb.loadUrl(asset_path+"contrast/index.html");
        }if (id == R.id.mri) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("MRI")
                    .build());
            wb.loadUrl(asset_path+"mri/index.html");
        }
        if (id == R.id.ct) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("CT")
                    .build());
            wb.loadUrl(asset_path+"ct/index.html");
        }
        if (id == R.id.ultrasound) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Ultrasound")
                    .build());
            wb.loadUrl(asset_path+"uss/index.html");
        }
        if (id == R.id.mammo) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Mammography")
                    .build());
            wb.loadUrl(asset_path+"mammo/index.html");
        }if (id == R.id.endo) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Endoscopy")
                    .build());
            wb.loadUrl(asset_path+"endo/index.html");
        }
        if (id == R.id.tracto) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Tractography")
                    .build());
            wb.loadUrl(asset_path+"tracto/index.html");
        }if (id == R.id.videos) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Others")
                    .setAction("Videos")
                    .build());
            wb.loadUrl(youtube_link);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static long getFileSize(final File file)
    {
        if(file==null||!file.exists())
            return 0;
        if(!file.isDirectory())
            return file.length();
        final List<File> dirs=new LinkedList<File>();
        dirs.add(file);
        long result=0;
        while(!dirs.isEmpty())
        {
            final File dir=dirs.remove(0);
            if(!dir.exists())
                continue;
            final File[] listFiles=dir.listFiles();
            if(listFiles==null||listFiles.length==0)
                continue;
            for(final File child : listFiles)
            {
                result+=child.length();
                if(child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }



    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
