package onepunman.remembermethis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;

public class CourseActivity extends AppCompatActivity {
    final static String TAG = "Debug";
    LinearLayout ll;
    TextView lblCourseTitle;
    TextView lblDescription;

    Button btnAddDefinition;
    Button btnReview;
    Button backButton;

    private File _currentCourseFile;
    private Course _currentCourse;
    Formatter formatter = new Formatter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ll = findViewById(R.id.courseContent);
        lblCourseTitle = findViewById(R.id.courseTitle);
        lblDescription = findViewById(R.id.courseDescription);

        File f = (File) getIntent().getSerializableExtra("courseFile");
        init(f);

        ArrayList<Definition> definitions = _currentCourse.getAll();
        ArrayList<Definition> reviewList = _currentCourse.getReviewList();

        for (final Definition def : definitions) {
            addDefButton(def);
        }

        btnAddDefinition = findViewById(R.id.btnAddDef);
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
                                refreshView();
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

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _currentCourse.save();
                finish();
            }
        });

        btnReview = findViewById(R.id.btnReview);
        btnReview.setText(formatter.format("Review (%1$1d)", reviewList.size()).toString());
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Review Stuff
            }
        });
    }

    private void addDefButton (final Definition def) {
        final Button btn = new Button(this);
        btn.setText(def.getName() + ": " + def.getDescription());
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog coursePopup = new Dialog(CourseActivity.this);

                coursePopup.setContentView(R.layout.activity_popup);

                final TextView defText = coursePopup.findViewById(R.id.lblDefText);
                Button btnAddLoss = coursePopup.findViewById(R.id.btnAddLose);
                Button btnAddWin = coursePopup.findViewById(R.id.btnAddWin);
                Button btnSave = coursePopup.findViewById(R.id.btnSave);
                Button btnReset = coursePopup.findViewById(R.id.btnReset);
                Button btnDelete = coursePopup.findViewById(R.id.btnDelete);

                updateContent(defText, def.toString());

                btnAddLoss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateReviewed(false, true);
                        updateContent(defText, def.toString());
                    }
                });

                btnAddWin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateReviewed(true, true);
                        updateContent(defText, def.toString());
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        coursePopup.dismiss();
                    }
                });

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.reset();
                        updateContent(defText, def.toString());
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _currentCourse.removeDefinition(def);
                        updateContent(defText, def.toString());
                        ll.removeView(btn);
                        coursePopup.dismiss();
                    }
                });

                coursePopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        refreshView();
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

    private void init(File courseFile) {
        _currentCourse = new Course();
        _currentCourseFile = courseFile;
        _currentCourse.loadFromFile(courseFile);
        lblCourseTitle.setText(_currentCourse.getName());
        lblDescription.setText(_currentCourse.getDescription());
    }

    private void refreshView() {
        btnReview.setText(formatter.format("Review (%1$1d)", _currentCourse.getReviewList().size()).toString());
    }
}
