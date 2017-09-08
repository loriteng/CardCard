package es.esy.silverwolf.gamecard6;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/7.
 */
//玩家資料
public class UserDateDAO {
    private AnimalData animalData;

    // 表格名稱
    public static final String TABLE_NAME = "PCD";

    // 編號表格欄位名稱，固定不變
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "name";

    // 其它需要的表格欄位名稱

    public void NewUser(){
        animalData.getAnimalData();

    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_NAME + " REAL NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public UserDateDAO(Context context) {
        db = GameData.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public UserData insert(UserData userdata) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();
        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(USER_NAME, userdata.getusername());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        userdata.setId(id);
        // 回傳結果
        return userdata;
    }

    // 修改參數指定的物件
    public boolean update(UserData userdata) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(USER_NAME, userdata.getusername());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = USER_ID  + "=" + userdata.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return  db.update(TABLE_NAME, cv, where ,null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = USER_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    // 取得所有資料的Cursor物件
//    public Cursor getAllCursor() {
//        Cursor result = db.query(TABLE_NAME, SHOW_COLUMNS, null, null ,null ,null ,null);
//        return result;
//    }
    // 第一次執行應用程式新增一些範例資料
    public void sampleData(Context context) {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstTime = sp.getBoolean("FIRST_TIME", true);

        if (firstTime) {
            UserData p01 = new UserData(0, "AA");
            UserData p02 = new UserData(1, "BB");
            UserData p03 = new UserData(2, "CC");

            insert(p01);
            insert(p02);
            insert(p03);


            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("FIEST_TIME", false);
            editor.commit();
        }
    }
}
