package onepunman.remembermethis;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

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
        create_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String courseName = course_name_text.getText().toString();
                String courseDescription = course_description_text.getText().toString();

                Course newCourse = new Course();
                if (newCourse.createNew(courseName, courseDescription)) {
                    if (newCourse.save()) {
                        Toast.makeText(CreateNewCourse.this,"Course Created",Toast.LENGTH_SHORT).show();
                        showAlert("Course Created", "\"" + courseName + "\"" + " has been created" );
                    } else {
                        Toast.makeText(CreateNewCourse.this,"Failed to save course",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CreateNewCourse.this,"Course name cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button cancel_button = findViewById(R.id.btn_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setTitle(title)
                .setIcon(R.drawable.brain)
                .create();
        msg.show();

    }
}
