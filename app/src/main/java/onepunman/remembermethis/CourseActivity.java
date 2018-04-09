package onepunman.remembermethis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class CourseActivity extends AppCompatActivity {
    final static String TAG = CourseActivity.class.getName();
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
                finish();
            }
        });

        FileWrapper w = (FileWrapper) getIntent().getSerializableExtra("courseFile");
        Init(w.getFile());
}

    public static void Init(File courseFile) {
        _currentCourseFile = courseFile;
        lblCourseTitle.setText(courseFile.getName());
        lblDescription.setText(courseFile.getAbsolutePath());

        _currentCourse = new Course();
        _currentCourse.loadFromFile(courseFile);


    }
}
