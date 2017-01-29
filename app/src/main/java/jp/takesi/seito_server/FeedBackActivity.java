package jp.takesi.seito_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FeedBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("フィードバック");
        }
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    TextView txt = (TextView) findViewById(R.id.hatena);
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    if (radioGroup != null) {
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.radioButton:
                                txt.setText("どの画面で、何が起きたかをお聞かせください");
                                break;
                            case R.id.radioButton2:
                                txt.setText("どの文章が、どうなっているのかをお聞かせください");
                                break;
                            case R.id.radioButton3:
                                txt.setText("どの画像が、どうなっているのかをお聞かせください");
                                break;
                            case R.id.radioButton4:
                                txt.setText("どのような状況でアプリが落ちるかをお聞かせください");
                                break;
                            case R.id.radioButton5:
                                txt.setText("どの画面で、どのような状況で、何が起きたかをお聞かせください");
                                break;
                        }
                    }

                }
            });
        }
        Button btn = (Button) findViewById(R.id.button);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText edit = (EditText) findViewById(R.id.editText);
                    SpannableStringBuilder sp = (SpannableStringBuilder) edit.getText();
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    String sub = "other";
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButton:
                            sub = "layout";
                            break;
                        case R.id.radioButton2:
                            sub = "text";
                            break;
                        case R.id.radioButton3:
                            sub = "image";
                            break;
                        case R.id.radioButton4:
                            sub = "crash";
                            break;
                        case R.id.radioButton5:
                            sub = "other";
                            break;
                    }
                    // インテントのインスタンス生成
                    Intent intent = new Intent();
                    // インテントにアクション及び送信情報をセット
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:piggtomop@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback_" + sub);
                    intent.putExtra(Intent.EXTRA_TEXT, sp.toString());
                    // メール起動
                    startActivity(intent);
                }
            });
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