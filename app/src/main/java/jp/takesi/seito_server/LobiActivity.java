package jp.takesi.seito_server;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LobiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobi);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            String str = uri.toString();
            String another = "group/";
            String string = str.substring(str.indexOf(another) + 6);
            //Uri uri1 = Uri.parse("https://lobi.co/sp/button?group_uid=" + string + "&type=group&via_invite=1");
            //Log.d(string,"これこれ");
            //Intent re_intent = new Intent(Intent.ACTION_VIEW, uri1);
            //startActivity(re_intent);
            WebView myWebView = (WebView) findViewById(R.id.webview);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.loadUrl("https://lobi.co/sp/button?group_uid=" + string + "&type=group&via_invite=1");
        }
    }
}
