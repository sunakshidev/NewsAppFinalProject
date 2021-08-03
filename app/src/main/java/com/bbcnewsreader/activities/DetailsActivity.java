package com.bbcnewsreader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbcnewsreader.app.App;
import com.bbcnewsreader.R;

public class DetailsActivity extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    Button retry;
    LinearLayout noData;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        webView = findViewById(R.id.webView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        noData = findViewById(R.id.noData);
        retry = findViewById(R.id.retry_bt);

        url = getIntent().getStringExtra("url");

        webClientService();

        if (App.mContext.isNetworkConnected()) {
            webClientService();
            webView.loadUrl(url);
        }
        else setInternetPage();


        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (App.mContext.isNetworkConnected())
            {
                webClientService();
                webView.loadUrl(url);
                webView.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
                webView.reload();
            }
            else setInternetPage();
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.black);

        retry.setOnClickListener(v -> {
            if (App.mContext.isNetworkConnected())
            {
                webView.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
            else setInternetPage();
        });


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else super.onBackPressed();
    }

    public void setInternetPage() {
        webView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Please Connect the Network!", Toast.LENGTH_SHORT).show();
    }

    public void webClientService(){
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                swipeRefreshLayout.setRefreshing(true);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                swipeRefreshLayout.setRefreshing(false);

            }
        });
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

}