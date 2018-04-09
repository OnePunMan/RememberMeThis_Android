package onepunman.remembermethis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class SelectCourse extends AppCompatActivity {

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);


        ll = findViewById(R.id.courseLayout);

        final Button exit_button = findViewById(R.id.btn_back);
        exit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        final File [] courseFiles = FileIO.getFilesInSubDir("RememberMeThis/Courses");

        if (courseFiles == null || courseFiles.length <= 0) {
            Log.d("FileIO", "No Courses");
            return;
        }

        Button btn;
        int count = 1;
        for (int i = 0; i < courseFiles.length; i++){
            final String fileName = courseFiles[i].getName();
            if (fileName.endsWith(".course")) {
                btn = new Button(this);
                btn.setText(count + ". " + fileName);
                btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll.addView(btn);
                count += 1;

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SelectCourse.this, fileName, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }


    }

}
