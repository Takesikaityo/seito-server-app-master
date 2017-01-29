package jp.takesi.seito_server;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



public class NewsActivity extends AppCompatActivity {
    int text = 0;
    //int image = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text_data");
        String url = intent.getStringExtra("url_data");
        //text = intent.getIntExtra("text_data", 0);
        //image = intent.getIntExtra("image_data",0);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(text);
        //ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        //imageView.setImageResource(image);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);

        /*Intent intent1 = getIntent();
        String action = intent1.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            android.net.Uri uri = intent1.getData();
            String str = uri.toString();
            try {
                String encodeStr = URLDecoder.decode(str, "utf-8");
                String string = encodeStr;
                //String string = encodeStr.substring(23);
                //TextView textView1 = (TextView) findViewById(R.id.textView);
                //textView1.setText(string);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("生徒サーバー公式News");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}