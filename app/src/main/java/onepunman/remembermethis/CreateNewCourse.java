package onepunman.remembermethis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewCourse extends AppCompatActivity {

    private EditText course_name_text;
    private EditText course_description_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        course_name_text = findViewById(R.id.txt_course_name);
        course_description_text = findViewById(R.id.txt_course_description);

        final Button create_button = findViewById(R.id.btn_create);
        create_button.setOnClickListener(v -> {
            String courseName = course_name_text.getText().toString().trim();
            String courseDescription = course_description_text.getText().toString().trim();

            if (FileIO.isAlreadyExist(courseName)) {
                Toast.makeText(CreateNewCourse.this,"Course already exists, please use another name",Toast.LENGTH_SHORT).show();
                return;
            }

            Course newCourse = new Course();
            if (newCourse.createNew(courseName, courseDescription)) {
                if (newCourse.save()) {
                    Toast.makeText(CreateNewCourse.this,"Course Created", Toast.LENGTH_SHORT).show();
                    UIManager.createConfirmationPopup(CreateNewCourse.this,
                            "Course Created", "\"" + courseName + "\"" + " has been created", R.drawable.brain,
                            "OK", null, null,
                            () -> { finish(); }, null, null);
                }
                else {
                    Toast.makeText(CreateNewCourse.this,"Failed to save course", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(CreateNewCourse.this,"Course name cannot be empty",Toast.LENGTH_SHORT).show();
            }
        });

        final Button cancel_button = findViewById(R.id.btn_cancel);
        cancel_button.setOnClickListener(v -> finish());
    }
}
