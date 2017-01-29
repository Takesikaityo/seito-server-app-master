package jp.takesi.seito_server;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by takec on 2017/01/15.
 */
public class CardRecyclerView extends RecyclerView {

    private String[] info = new String[] {
            "先ほど、生徒サーバーLobiグループの人数が100名を突破しました！\nこれを記念して、今週の日曜日に記念イベントを開く予定です！",
            "生徒サーバーが日本マインクラフトサーバー連盟を設立しました！興味のある方はぜひとも入ってください。\n鯖主の方へ\n詳しくはtakesiのこちゃでお気軽にご相談ください。",
            "遂にサッカーコートが完成しました！皆様、是非ともご来場ください！！",
            "[生徒鯖の全てがここにある！]\n" + "(2016/11/4更新)\n" + "[生徒鯖に行こう！]\n" + "ここを参考にしていざ出発だ！\n" + "[ステータス]\n" + "設立日時: 2016年11月3日\n" + "IPアドレス: 101.143.28.97\n" + "ポート番号: 19132\n" + "設立者: +たけしかいちょー\n" + "(サブアカウント +takesi たけしかいちょー)\n" + "[生徒鯖の権限人]\n" + "+takesi_0611\n" + "siitake\n" + "getup\n" + "[生徒鯖の管理者]\n" + "紹介までもう少し待ってね\n" + "君の目の前には楽しいサーバー生活が待っている！\n" + "これからも生徒鯖をよろしくね！\n" + "------\n" + "生徒鯖管理人一同\n" + "#生徒鯖",
            "その他"
    };

    public CardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRecyclerAdapter(context);
    }

    public void setRecyclerAdapter(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter<String>(context, android.R.layout.simple_list_item_1, info));
    }
    }