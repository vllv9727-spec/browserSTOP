package com.stoplien.browser;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String HOME = "data:text/html;charset=utf-8," +
        "<html><body style='background:#121216;color:#fff;font-family:sans-serif;text-align:center;padding-top:20%25'>" +
        "<div style='display:inline-block;background:#dc1414;border:4px solid #fff;padding:15px 30px;font-size:32px;font-weight:bold;border-radius:12px'>STOP</div>" +
        "<h2 style='color:#f5c211'>STOPlien Browser Mobile</h2>" +
        "<form action='https://duckduckgo.com' method='get'>" +
        "<input name='q' placeholder='Поиск или адрес...' style='padding:12px;width:70%25;border-radius:8px;border:2px solid #dc1414;background:#1e1e24;color:#fff'>" +
        "<button style='padding:12px 18px;background:#dc1414;color:#fff;border:none;border-radius:8px;font-weight:bold'>Поиск</button>" +
        "</form></body></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(HOME);
        setContentView(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
