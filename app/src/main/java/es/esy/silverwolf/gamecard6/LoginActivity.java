package es.esy.silverwolf.gamecard6;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //取得此Button的實體
        Button button = (Button) findViewById(R.id.Benter);

        //實做OnClickListener界面

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取得前端輸入的名字字串
                EditText editText = (EditText)findViewById(R.id.inputName);
                String name = editText.getText().toString();

                //new一個intent物件
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(LoginActivity.this, HomeActivity.class);

                //new一個Bundle物件，並將要傳遞的資料(使用者輸入的名字)傳入
                Bundle bundle = new Bundle();
                bundle.putString("name",name );

                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                //開啟Activity
                startActivity(intent);


            }
        });
    }

}
