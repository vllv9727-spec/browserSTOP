package com.stoplien.browser;
import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private WebView webView;
    private EditText urlBar;
    private static final String HTML =
        "<html><body style='background:#121216;color:#fff;font-family:sans-serif;text-align:center;padding-top:16%'>" +
        "<div style='display:inline-block;background:#dc1414;border:4px solid #fff;padding:15px 30px;font-size:32px;font-weight:bold;border-radius:12px'>STOP</div>" +
        "<div style='margin-top:12px'><span style='background:#f5c211;color:#121216;padding:4px 12px;border-radius:6px;font-weight:bold;font-size:13px'>BETA 1.1 • UPDATED</span></div>" +
        "<h2 style='color:#f5c211;margin-top:12px'>STOPlien Browser Mobile</h2>" +
        "<form action='https://duckduckgo.com' method='get'>" +
        "<input name='q' placeholder='Поиск или адрес...' style='padding:12px;width:70%;border-radius:8px;border:2px solid #dc1414;background:#1e1e24;color:#fff'>" +
        "<button style='padding:12px 18px;background:#dc1414;color:#fff;border:none;border-radius:8px;font-weight:bold'>Поиск</button>" +
        "</form></body></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        urlBar = new EditText(this);
        urlBar.setHint("Введите адрес или запрос...");
        urlBar.setSingleLine(true);
        urlBar.setImeOptions(EditorInfo.IME_ACTION_GO);
        urlBar.setOnEditorActionListener((v, actionId, event) -> {
            String q = urlBar.getText().toString().trim();
            if (!q.isEmpty()) {
                if (q.contains(".") && !q.contains(" ")) {
                    if (!q.startsWith("http")) q = "https://" + q;
                    webView.loadUrl(q);
                } else {
                    webView.loadUrl("https://duckduckgo.com?q=" + q);
                }
            }
            return true;
        });

        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.startsWith("https://stoplien")) urlBar.setText(url);
            }
        });
        webView.loadDataWithBaseURL("https://stoplien.local", HTML, "text/html", "UTF-8", null);

        root.addView(urlBar);
        root.addView(webView, new LinearLayout.LayoutParams(-1, -1));
        setContentView(root);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
