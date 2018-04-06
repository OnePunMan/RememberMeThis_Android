package onepunman.remembermethis;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class CreateNewCourse extends AppCompatActivity {

    private EditText course_name_text;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RememberMeThis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        course_name_text = findViewById(R.id.txt_course_name);

        final Button create_button = findViewById(R.id.btn_create);
        create_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ShowAlert("test");
                //Toast.makeText(CreateNewCourse.this,"Error save file!!!",Toast.LENGTH_SHORT).show();
                FileIO.writeToFile("Testing123", CreateNewCourse.this);
                ShowAlert(FileIO.readFromFile(CreateNewCourse.this));
                ShowAlert(getFilesDir().toString());







            }

        });

        final Button cancel_button = findViewById(R.id.btn_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void ShowAlert(String message) {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(message)
                .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle(path)
                .setIcon(R.drawable.brain)
                .create();
        msg.show();

    }
}
