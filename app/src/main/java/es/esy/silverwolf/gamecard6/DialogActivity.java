package es.esy.silverwolf.gamecard6;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        context = this;
        announcedTheResultsDialog();
    }

    //宣佈成績
    private void announcedTheResultsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("宣佈成績");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("難度：幼幼班\n剩餘時間：17秒\n分數：9477");
        builder.setPositiveButton("確定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                winDialog();
            }
        });
        builder.create().show();
    }

    //勝利
    private void winDialog() {
        String option[] = {"繼續遊戲", "回首頁", "前往圖鑑"};

        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("勝利");
        builder.setIcon(android.R.drawable.btn_star_big_on);
        // 列表選項（注意：不可以與builder.setMessage()同時調用）
        builder.setItems(option, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        announcedTheResultsDialog();
                        break;
                    case 1:
                        announcedTheResultsDialog();
                        break;
                    case 2:
                        announcedTheResultsDialog();
                        break;
                }
            }
        });
        builder.create().show();
    }

    //失敗
    private void loseDialog() {
        String option[] = {"繼續遊戲", "回首頁", "前往圖鑑"};

        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("失敗");
        builder.setIcon(android.R.drawable.btn_star_big_off);
        // 列表選項（注意：不可以與builder.setMessage()同時調用）
        builder.setItems(option, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        announcedTheResultsDialog();
                        break;
                    case 1:
                        announcedTheResultsDialog();
                        break;
                    case 2:
                        announcedTheResultsDialog();
                        break;
                }
            }
        });
        builder.create().show();
    }

}
