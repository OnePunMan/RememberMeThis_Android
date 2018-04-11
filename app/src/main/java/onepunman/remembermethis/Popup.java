package onepunman.remembermethis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class Popup extends AppCompatActivity {
    final static String TAG = "Debug";
    public static double SCALE_HEIGHT = 0.6;
    public static double SCALE_WIDTH = 0.8;
    TextView defText;
    Definition _currentDef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        defText = findViewById(R.id.lblDefText);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * SCALE_WIDTH), (int) (height * SCALE_HEIGHT));

        _currentDef = (Definition) getIntent().getSerializableExtra("definition");
        if (_currentDef != null) {
            defText.setText(_currentDef.toString());
        }
    }
}
