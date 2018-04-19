package onepunman.remembermethis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

public class SelectCourse extends AppCompatActivity {
    final static String TAG = "Debug";
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);

        ll = findViewById(R.id.courseLayout);

        final Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        final Button btnCreateNewCourse = findViewById(R.id.btnCreateNewCourse);
        btnCreateNewCourse.setOnClickListener(view -> {
            Intent i = new Intent(SelectCourse.this, CreateNewCourse.class);
            startActivity(i);
        });

        updateContent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateContent();
    }

    private void updateContent() {
        ll.removeAllViews();
        final File [] courseFiles = FileIO.getCourseFiles();
        if (courseFiles == null || courseFiles.length <= 0) {
            Log.d(TAG, "No Courses");
            return;
        }

        Button btn;
        int count = 1;
        for (int i = 0; i < courseFiles.length; i++) {
            final String fileName = courseFiles[i].getName();
            if (fileName.endsWith(".course")) {
                btn = new Button(this);
                btn.setText(count + ". " + (fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName));
                btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll.addView(btn);
                count += 1;
                final int fileNum = i;

                btn.setOnClickListener(view -> {
                    Intent j = new Intent(SelectCourse.this, CourseActivity.class);
                    j.putExtra("courseFile", courseFiles[fileNum]);
                    startActivity(j);
                });
            }
        }
    }
}
