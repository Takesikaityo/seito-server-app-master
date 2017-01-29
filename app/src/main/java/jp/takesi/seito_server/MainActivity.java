package jp.takesi.seito_server;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    final MainActivity context = this;
    private PEQuery cs = new PEQuery("101.141.82.225", 19132);//101.143.28.97
    protected int open = 0;

    private static final int DESTINATION_EXTERNAL_PUBLICDIR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("生徒サーバー公式アプリホーム");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory(), "/games/com.mojang/minecraftpe/external_servers.txt");
                try {
                    if (alreadyAddedInList()) {
                        Toast.makeText(MainActivity.this, "すでに追加されています", Toast.LENGTH_SHORT).show();
                    } else if (!file.exists()) {
                        Toast.makeText(MainActivity.this, "マインクラフトPEがインストールされていません\nPlayストアに移動します...", Toast.LENGTH_SHORT).show();
                    } else {
                        FileWriter out = new FileWriter(file, true);
                        out.append("1000:seito-server:101.141.82.225:19132\n");
                        out.close();
                        Toast.makeText(MainActivity.this, "正常に追加しました", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setAction("android.intent.category.LAUNCHER");
                intent.setClassName("com.mojang.minecraftpe", "com.mojang.minecraftpe.MainActivity");
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void ver() {
        try {
            URL url = new URL("https://docs.google.com/uc?authuser=0&id=0Bxp5wIuQibuSMENiUlRxazljU0E&export=download");
            // HTTP接続開始
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            // SDカードの設定
            String PATH = Environment.getExternalStorageDirectory() + "/seito-server/";
            File file = new File(PATH);
            file.mkdirs();

            // テンポラリファイルの設定
            File outputFile = new File(file, "ver.xml");
            FileOutputStream fos = new FileOutputStream(outputFile);
            // ダウンロード開始
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
            String listXmlPath = Environment.getExternalStorageDirectory() + "/seito-server/ver.xml";
            String content = new Scanner(
                    new File(listXmlPath)).useDelimiter("\\z").next();
            //XMLファイルをまとめて読み込み
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(new StringReader(content));
            //解析するXMLファイルの中身を渡す
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        Log.i("MainActivity", "テキスト = " + xpp.getText());
                        break;
                }
                eventType = xpp.next();
                //次のトークンに進む
            }
            PackageManager pm = context.getPackageManager();
            String versionName = "";
            try {
                PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Log.i("MainActivity", "ドキュメント終了");
        } catch (XmlPullParserException e) {
            Log.e("MainActivity", "XMLの解析失敗.");
        } catch (IOException e) {
            Log.e("MainActivity", "XMLファイルの読み込みに失敗.");
        }
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
                if (s.endsWith(":101.141.82.225:19132")) return true;
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

    public void download() {
        try {
            // URL設定
            URL url = new URL("https://docs.google.com/uc?authuser=0&id=0Bxp5wIuQibuSaXg0WTUwZFBIdGc&export=download");
            // HTTP接続開始
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            // SDカードの設定
            String PATH = Environment.getExternalStorageDirectory() + "/seito-server/";
            File file = new File(PATH);
            file.mkdirs();

            // テンポラリファイルの設定
            File outputFile = new File(file, "seito-server.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);
            // ダウンロード開始
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
            // Intent生成
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // MIME type設定
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/seito-server/" + "seito-server.apk")), "application/vnd.android.package-archive");
            // Intent発行
            startActivity(intent);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void app_bata() {
        try {
            // URL設定
            URL url = new URL("https://docs.google.com/uc?authuser=0&id=0Bxp5wIuQibuSZklRcnRtQURqejQ&export=download");
            // HTTP接続開始
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            // SDカードの設定
            String PATH = Environment.getExternalStorageDirectory() + "/seito-server/";
            File file = new File(PATH);
            file.mkdirs();

            // テンポラリファイルの設定
            File outputFile = new File(file, "seito-server-bata.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);
            // ダウンロード開始
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
            // Intent生成
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // MIME type設定
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/seito-server/" + "seito-server-bata.apk")), "application/vnd.android.package-archive");
            // Intent発行
            startActivity(intent);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public void ping() {
        if (cs.hand("101.141.82.225")) { //101.143.28.97
            open = 1;
        } else {
            open = 0;
        }
    }


    public void setOpened() {
        if (open == 1) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setTitle("生徒サーバー　稼働中");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setColor(ContextCompat.getColor(this,R.color.colorAccent))
                            .setContentTitle("生徒サーバー稼働中！！")
                            .setContentText("タップするとMinecraft PEが起動します。");
             Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1000, mBuilder.build());
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setTitle("生徒サーバー　停止中");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
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
        if (id == R.id.action_sync) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setTitle("生徒サーバー　読み込み中...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ping();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setOpened();
                        }
                    });
                }
            }).start();
        }
        if (id == R.id.action_feedback) {
            startActivity(new Intent(this, FeedBackActivity.class));
        }
        if (id == R.id.action_app) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download();
                    }
                }).start();
            }
        if (id == R.id.action_app_bata) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    app_bata();
                    ver();
                }
            }).start();
        }if (id == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_ServerInfo) {
            startActivity(new Intent(this, serverInfoActivity.class));
        } else if (id == R.id.nav_rule) {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("url_data", "https://sites.google.com/view/seito-server/%E5%88%A9%E7%94%A8%E8%A6%8F%E7%B4%84");
            context.startActivity(intent);
        } else if (id == R.id.nav_we) {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("text_data", "      生徒サーバーの同盟サーバー\n\nSironeko Next サーバー\nhttps://lobi.co/sp/button?group_uid=f61c55192d0cf389bf1ed05dd60a3e737abea737&type=group&via_invite=1" +
            "\n生活 inko サーバー\nhttps://web.lobi.co/game/minecraftpe/group/9555d384478c80a5047b1a333718846bd31894b6");
            context.startActivity(intent);
        } else if (id == R.id.nav_developers) {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("text_data", "      生徒サーバー公式アプリでは、外部Intentの受け取りに対応しております。\n" +
                    "ブラウザーでURLのところに\n" +
                    "seito://simple/news/コンテンツ\n" +
                    " と打つとNewsActivityが起動して、コンテンツと表示されます。");
            context.startActivity(intent);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_homepage) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://sites.google.com/view/seito-server/ホーム")));
        } else if (id == R.id.nav_google) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/communities/106844935708543397045")));
        } else if (id == R.id.nav_google_space) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://spaces.google.com/space/5457659639632943499")));
        } else if (id == R.id.nav_lobi) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://lobi.co/sp/button?group_uid=d5312f8f5700be43a3e2bca43c167505855a99cd&type=group&via_invite=1")));
        } else if (id == R.id.nav_line) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://line.me/R/ti/p/%40jzy0132y")));
        } else if (id == R.id.nav_shere) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "#生徒サーバー\n生徒サーバーで遊ぼう！！\nIPアドレス: 101.141.82.225\nポート: 19132\n生徒サーバー公式アプリを使って共有");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
