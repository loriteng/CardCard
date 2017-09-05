package es.esy.silverwolf.gamecard6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayGame extends AppCompatActivity {
    private int items;
    private int[][] cards;
    private Context context;
    private Drawable backImage;
    private List<Drawable> images;
    private AnimalCard			firstCard, seconedCard;
    private ButtonListener		buttonListener;
    private static Object	lock	= new Object();
    int	pairCount;
    private TableLayout mainTable;
    private UpdateCardsHandler	handler;
    private TextView timeView;
    private Time timeset;
    public static final String KEY = "DateSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new UpdateCardsHandler();
        setContentView(R.layout.activity_play_game);
//lori
        //lori
        loadImages();//讀取翻牌圖片
        backImage = getResources().getDrawable(R.drawable.empty);//卡牌背面
        buttonListener = new ButtonListener();//點擊監視器
        mainTable = (TableLayout) findViewById(R.id.GameLayout);//翻牌的表格
        context = mainTable.getContext();//翻牌表格的數量
        final Button SetUpStop = (Button) findViewById(R.id.SetUp);

        SetUpStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // /設定遊戲時間
        timeView = (TextView) findViewById(R.id.time);

        GameTime(getDate("SaveTime"));

        initilizeGame(getDate("SaveLsRow"),getDate("SaveLsColume"));
    }

    //離開遊戲畫面時呼叫timeset.pause()暫停時間
    @Override
    protected void onPause() {
        super.onPause();
        timeset.pause();
    }

    //回到遊戲畫面時呼叫timeset.pause()暫停時間
    @Override
    protected void onRestart() {
        super.onRestart();
        timeset.resume();
    }

    //防止玩家按返回鍵時回上頁的Layout, 讓此Layout的返回鍵變成跟home鍵功能一樣
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intentHome = new Intent(Intent.ACTION_MAIN);
            intentHome.addCategory(Intent.CATEGORY_HOME);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initilizeGame(int rowCount,int columeCount) {
        cards = new int[columeCount][rowCount];
        items = (rowCount * columeCount) / 2; // 記錄可配對個數

        mainTable.removeAllViews();

        for (int y = 0; y < rowCount; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard = null;
        loadCards(rowCount,columeCount); // 產生卡片
        pairCount = 0;

    }

    private void loadImages() {
        images = new ArrayList<Drawable>();
        images.add(getResources().getDrawable(R.drawable.item01));
        images.add(getResources().getDrawable(R.drawable.item02));
        images.add(getResources().getDrawable(R.drawable.item03));
        images.add(getResources().getDrawable(R.drawable.item04));
        images.add(getResources().getDrawable(R.drawable.item05));
        images.add(getResources().getDrawable(R.drawable.item06));
        images.add(getResources().getDrawable(R.drawable.item07));
        images.add(getResources().getDrawable(R.drawable.item08));
    }

    //生成遊戲(卡牌)
    private void loadCards(int rowCount,int columeCount) {
        try {
            int size = rowCount * columeCount;
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                list.add(new Integer(i)); // 加入所有卡片編號
            }

            Random r = new Random();

            for (int i = size - 1; i >= 0; i--) {
                int t = 0;

                if (i > 0) {
                    t = r.nextInt(i); // 隨機取得編號
                }

                t = list.remove(t).intValue(); // 從 list 中取出編號
                cards[i % columeCount][i / columeCount] = t % (size / 2); // 將編號放入指定位置
            }

            // 再次洗牌
            for (int i = 0; i < rowCount; i++)
                for (int j = 0; j < columeCount; j++) {
                    int rc = r.nextInt(rowCount);
                    int cc = r.nextInt(columeCount);
                    int temp;

                    temp = cards[i][j];
                    cards[i][j] = cards[rc][cc];
                    cards[rc][cc] = temp;
                }
        }
        catch (Exception e) {
        }

    }
    //將生成的卡牌放入畫面內
    private TableRow createRow(int y) {
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < getDate("SaveLsColume"); x++) {
            row.addView(createImageButton(x, y));
        }
        return row;
    }

    private View createImageButton(int x, int y) {
        Button button = new Button(context);
        button.setBackgroundDrawable(backImage);
        button.setId(100 * x + y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            synchronized (lock) {
                if (firstCard != null && seconedCard != null) {
                    return;
                }
                int id = v.getId();
                int x = id / 100;
                int y = id % 100;
                turnCard((Button) v, x, y);
            }
        }

        private void turnCard(Button button, int x, int y) {
            button.setBackgroundDrawable(images.get(cards[x][y]));

            if (firstCard == null) {
                firstCard = new AnimalCard(button, x, y);
            }
            else {
                if (firstCard.x == x && firstCard.y == y) {
                    return; // 選到相同的卡片則不動作
                }

                seconedCard = new AnimalCard(button, x, y);

                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            synchronized (lock) {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e) {
                        }
                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 500); // 停頓0.5秒
            }
        }
    }

    class UpdateCardsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }

        public void checkCards() {
            if (cards[seconedCard.x][seconedCard.y] == cards[firstCard.x][firstCard.y]) {
                firstCard.button.setEnabled(false);
                seconedCard.button.setEnabled(false);
                Toast.makeText(getApplicationContext(), "配對成功！", Toast.LENGTH_SHORT).show();
                pairCount++;
                if (pairCount >= items) {
                    timeset.cancel();
                    Button button = new Button(PlayGame.this);
                    button.setText("確定");
                    final Dialog dialog = new Dialog(PlayGame.this);
                    dialog.setTitle("恭喜你完成所有配對！");
                    dialog.setContentView(button);
                    dialog.show();
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
            else {
                seconedCard.button.setBackgroundDrawable(backImage);
                firstCard.button.setBackgroundDrawable(backImage);
                Toast.makeText(getApplicationContext(), "配對錯誤...", Toast.LENGTH_SHORT).show();
            }
            firstCard = null;
            seconedCard = null;
        }
    }

    private static final int	MenuGroupID= 0;
    private static final int	MenuItemID1	= 0;
    private static final int	MenuItemID2	= 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(MenuGroupID, MenuItemID1, Menu.NONE, R.string.reset);
        menu.add(MenuGroupID, MenuItemID2, Menu.NONE, R.string.exit);
        return true;
    }

    @Override
    //重新遊戲/關閉程式
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MenuItemID1: // 重新遊戲
                initilizeGame(getDate("SaveLsRow"),getDate("SaveLsColume"));
                return true;

            case MenuItemID2: // 關閉程式
                this.finish();
                return true;
        }
        return false;
    }

    //卡牌遊戲結束
    private void GameFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.GameFinish);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(R.string.exit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
    //遊戲時間計算
    private void GameTime(int time) {
        //設定時間
        timeset = new Time(time*1000,1000) {

            public void onTick(long millisUntilFinished, int percent) {
                long minute;
                minute = (millisUntilFinished / 60000);
                millisUntilFinished = millisUntilFinished - (minute * 60000);
                // 傳回參數指定秒數的「時：分」格式字串
                timeView.setText(
                                "時間：" +
                                String.format("%02d", minute) +
                                ":" +
                                String.format("%02d", millisUntilFinished / 1000)
                                );
            }
            public void onFinish() {
                timeView.setText(
                                "時間：" +
                                String.format("%02d", 0) +
                                ":" +
                                String.format("%02d", 0 / 1000)
                );
                GameFinish();
            }
        }.start();
    }

    //儲存DateSet.xml暫存資料檔
    public void setDate(String key, int value)
    {
        SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor PE = spref.edit();
        PE.putInt(key, value);
        PE.commit();
    }

    //設定DateSet.xml暫存資料檔
    public int getDate(String key)
    {
        SharedPreferences spref = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        int strValue = spref.getInt(key, 0);
        return strValue;
    }

}
