package es.esy.silverwolf.gamecard6;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();

        String name = bundle.getString("name");
        TextView textView = (TextView)findViewById(R.id.showName);
        textView.setText("冒險家 "+name+",");

    }
}
