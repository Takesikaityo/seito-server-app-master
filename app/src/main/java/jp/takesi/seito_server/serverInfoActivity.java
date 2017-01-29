package jp.takesi.seito_server;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import jp.takesi.seito_server.PEQuery;

import java.io.*;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class serverInfoActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    final serverInfoActivity context = this;
    private PEQuery cs = new PEQuery("101.143.25.2", 19132);
    protected int open = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("サーバー情報");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "/games/com.mojang/minecraftpe/external_servers.txt");
                try {
                    if (alreadyAddedInList()) {
                        Toast.makeText(serverInfoActivity.this, "すでに追加されています", Toast.LENGTH_SHORT).show();
                    } else if (!file.exists()) {
                        Toast.makeText(serverInfoActivity.this, "マインクラフトPEがインストールされていません\nPlayストアに移動します...", Toast.LENGTH_SHORT).show();
                    } else {
                        FileWriter out = new FileWriter(file, true);
                        out.append("1000:seito-Server:101.143.25.2:19132\n");
                        out.close();
                        Toast.makeText(serverInfoActivity.this, "正常に追加しました", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView text = (TextView) findViewById(R.id.t);
        text.setText("読み込み中...");
        text.setTextSize(22);
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setMax(100);
        pb.setProgress(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ping();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setOpened();
                        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
                        pb.setProgress(100);
                        pb.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
        Button btn=(Button)findViewById(R.id.button2);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text = (TextView) findViewById(R.id.t);
                    text.setText("読み込み中...");
                    text.setTextColor(0xffa4b0e9);
                    text.setTextSize(22);
                    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
                    pb.setProgress(0);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ping();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setOpened();
                                    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
                                    pb.setProgress(100);
                                    pb.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean alreadyAddedInList() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/games/com.mojang/minecraftpe/external_servers.txt");
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            while (true) {
                String s = br.readLine();
                if (br == null) break;
                if (s.endsWith(":101.143.25.2:19132")) return true;
            }
            return false;
        } catch (Throwable ex) {
            return false;
        } finally {
            try {
                if (fr != null) fr.close();
                if (br != null) br.close();
            } catch (IOException e) {

            }
        }
    }

    public void ping() {
        if (cs.hand("101.143.25.2")) {
            open = 1;
        } else {
            open = 0;
        }
    }


    public void setOpened() {
        if (open == 1) {
            TextView textView2 = (TextView) findViewById(R.id.t);
            textView2.setText("稼働中");
            textView2.setTextColor(getResources().getColor(R.color.Sucsess));
            textView2.setTextSize(22);
        } else {
            TextView textView2 = (TextView) findViewById(R.id.t);
            textView2.setText("停止中");
            textView2.setTextColor(getResources().getColor(R.color.Filed));
            textView2.setTextSize(22);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}