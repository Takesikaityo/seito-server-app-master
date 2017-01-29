package jp.takesi.seito_server;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by takec on 2017/01/15.
 */

public class CardRecyclerAdapter<S> extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{
    private String[] list = new String[]{
            "Lobiグループの人数が100名を突破！",
            "生徒サーバーが日本マインクラフトサーバー連盟を設立！",
            "遂にサッカーコートが完成しました！！",
            "[生徒鯖に行こう！]",
            "その他"
    };
    private String[] images = new String[]{
            "R.mipmap.news_01",
            "R.mipmap.ic_launcher",
            "R.mipmap.news_02",
            "R.mipmap.ic_launcher",
            "R.mipmap.ic_launcher"
    };
    private String[] info;
    private Context context;

    public CardRecyclerAdapter(Context context, int simple_list_item_1, String[] stringArray) {
        super();
        this.info = stringArray;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return info.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        vh.textView_main.setText(list[position]);
        if(position != list.length){
            vh.textView_sub.setText(info[position]);
        }else {
            vh.textView_sub.setText(info[position]);
        }
        vh.imageView.setImageResource(R.mipmap.ic_launcher);
        vh.textView_sub.setText(info[position]);
        vh.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, info[position] + "\n#生徒サーバー");
                context.startActivity(intent);
            }
        });

        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri uri = Uri.parse("seito://simple/news/" + info[position]);
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("text_data", info[position]);
                intent.putExtra("image_data",R.mipmap.news_02);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public CardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_main;
        TextView textView_sub;
        LinearLayout layout;
        ImageView imageView;
        Button button3;
        public ViewHolder(View v) {
            super(v);
            textView_main = (TextView)v.findViewById(R.id.textView_main);
            textView_sub = (TextView)v.findViewById(R.id.textView_sub);
            layout = (LinearLayout)v.findViewById(R.id.layout);
            imageView = (ImageView)v.findViewById(R.id.imageView);
            button3 = (Button)v.findViewById(R.id.button3);
        }
    }
}