package onepunman.remembermethis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends AppCompatActivity {
    final static String TAG = "Debug";
    public final static double SCALE_HEIGHT = 1;
    public final double SCALE_WIDTH = 1;
    TextView defText;
    Definition _currentDef;

    Button btnUpdateTime;
    Button btnAddWin;
    Button btnAddLose;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        defText = findViewById(R.id.lblDefText);
        btnAddLose = findViewById(R.id.btnAddLose);
        btnAddWin = findViewById(R.id.btnAddWin);
        btnSave = findViewById(R.id.btnSave);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * SCALE_WIDTH), (int) (height * SCALE_HEIGHT));

        _currentDef = (Definition) getIntent().getSerializableExtra("definition");

        if (_currentDef != null) {
            defText.setText(_currentDef.toString());
        }


        btnUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentDef.updateTime();
            }
        });

        btnAddWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentDef.updateReviewed(true, false);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
