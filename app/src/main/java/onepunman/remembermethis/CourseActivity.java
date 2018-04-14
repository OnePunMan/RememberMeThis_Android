package onepunman.remembermethis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    final static String TAG = "Debug";
    LinearLayout ll;
    static TextView lblCourseTitle;
    static TextView lblDescription;

    private static File _currentCourseFile;
    private static Course _currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ll = findViewById(R.id.courseContent);
        lblCourseTitle = findViewById(R.id.courseTitle);
        lblDescription = findViewById(R.id.courseDescription);

        final Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _currentCourse.save();
                finish();
            }
        });

        File f = (File) getIntent().getSerializableExtra("courseFile");
        Init(f);

        Button btn;
        ArrayList<Definition> definitions = _currentCourse.getAll(-1);

        for (final Definition def : definitions) {
            btn = new Button(this);
            btn.setText(def.getName() + ": " + def.getDescription());
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.addView(btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CourseActivity.this, Popup.class);
                    i.putExtra("definition", def);
                    startActivity(i);
                }
            });
        }

    }

    public static void Init(File courseFile) {
        _currentCourse = new Course();
        _currentCourseFile = courseFile;
        _currentCourse.loadFromFile(courseFile);

        lblCourseTitle.setText(_currentCourse.getName());
        lblDescription.setText(_currentCourse.getDescription());
    }
}
