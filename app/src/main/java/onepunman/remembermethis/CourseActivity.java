package onepunman.remembermethis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        final Button btnAddDefinition = findViewById(R.id.btnAddDef);
        btnAddDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

                final EditText txtDefName = dialogView.findViewById(R.id.txtDefName);
                final EditText txtDefDesc = dialogView.findViewById(R.id.txtDefDesc);
                final EditText txtDefLevel = dialogView.findViewById(R.id.txtDefLevel);

                builder.setView(dialogView)
                        .setTitle("Add New Definition")
                        .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Add new def
                                Definition newDef = new Definition(
                                        txtDefName.getText().toString(),
                                        txtDefDesc.getText().toString(),
                                        txtDefLevel.getText().toString());
                                _currentCourse.addDefinition(newDef);
                                addDefButton(newDef);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        File f = (File) getIntent().getSerializableExtra("courseFile");
        Init(f);

        ArrayList<Definition> definitions = _currentCourse.getAll(-1);

        for (final Definition def : definitions) {
            addDefButton(def);
        }
    }

    private void addDefButton (final Definition def) {
        Button btn = new Button(this);
        btn.setText(def.getName() + ": " + def.getDescription());
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog coursePopup = new Dialog(CourseActivity.this);

                coursePopup.setContentView(R.layout.activity_popup);

                final TextView defText = coursePopup.findViewById(R.id.lblDefText);
                Button btnUpdateTime = coursePopup.findViewById(R.id.btnUpdateTime);
                Button btnAddWin = coursePopup.findViewById(R.id.btnAddWin);
                Button btnSave = coursePopup.findViewById(R.id.btnSave);

                updateContent(defText, def.toString());

                btnUpdateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateTime();
                        updateContent(defText, def.toString());
                    }
                });

                btnAddWin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateReviewed(true, false);
                        updateContent(defText, def.toString());
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        coursePopup.dismiss();
                    }
                });

                coursePopup.show();
            }
        });
        ll.addView(btn);
    }

    private void updateContent(TextView text, String content) {
        text.setText(content);
    }

    private void Init(File courseFile) {
        _currentCourse = new Course();
        _currentCourseFile = courseFile;
        _currentCourse.loadFromFile(courseFile);
        lblCourseTitle.setText(_currentCourse.getName());
        lblDescription.setText(_currentCourse.getDescription());
    }
}
