package onepunman.remembermethis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    final static String TAG = "Debug";
    LinearLayout ll;
    TextView lblCourseTitle;
    TextView lblDescription;

    Button btnAddDefinition;
    Button btnReview;
    Button btnEditCourse;
    Button backButton;

    private File _currentCourseFile;
    private Course _currentCourse;

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
                                String name = txtDefName.getText().toString().trim();
                                if (name.isEmpty()) {
                                    Toast.makeText(CourseActivity.this,"Definition name cannot be empty",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else if (_currentCourse.containsDefinition(name)) {
                                    Toast.makeText(CourseActivity.this,"This definition already exists",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Definition newDef = new Definition(
                                        name,
                                        txtDefDesc.getText().toString().trim(),
                                        txtDefLevel.getText().toString().trim());
                                _currentCourse.addDefinition(newDef);
                                addDefButton(newDef);
                                updateReviewButton();
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

        btnEditCourse = findViewById(R.id.btnEditCourse);
        btnEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

                final EditText txtCourseName = dialogView.findViewById(R.id.txtDefName);
                final EditText txtCourseDesc = dialogView.findViewById(R.id.txtDefDesc);

                dialogView.findViewById(R.id.txtDefLevel).setVisibility(View.GONE);

                // Set existing fields
                txtCourseName.setText(_currentCourse.getName());
                txtCourseDesc.setText(_currentCourse.getDescription());

                builder.setView(dialogView)
                        .setTitle("Edit Course")
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Make sure new name is not empty
                                String name = txtCourseName.getText().toString().trim();
                                if (name.isEmpty()) {
                                    Toast.makeText(CourseActivity.this,"Course name cannot be empty",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (!_currentCourse.getName().equals(name) && FileIO.isAlreadyExist(name)) {
                                    Toast.makeText(CourseActivity.this,"This course already exists",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                _currentCourse.setName(name);
                                _currentCourse.setDescription(txtCourseDesc.getText().toString());

                                updateCourseHeader();
                            }
                        })
                        .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDeleteConfirmation();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.RED);
                    }
                });
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
        updateReviewButton();
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
                Button btnAddWin = coursePopup.findViewById(R.id.btnAddWin);
                Button btnAddLoss = coursePopup.findViewById(R.id.btnAddLose);
                Button btnToggleIgnore = coursePopup.findViewById(R.id.btnToggleIgnore);
                Button btnToggleDifficult = coursePopup.findViewById(R.id.btnToggleDifficult);
                Button btnEdit = coursePopup.findViewById(R.id.btnEdit);
                Button btnSave = coursePopup.findViewById(R.id.btnSave);
                Button btnReset = coursePopup.findViewById(R.id.btnReset);
                Button btnDelete = coursePopup.findViewById(R.id.btnDelete);

                updateContent(defText, def.toString());

                btnAddWin.setTextColor(Color.GREEN);
                btnAddWin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateReviewed(true, true);
                        updateContent(defText, def.toString());
                    }
                });

                btnAddLoss.setTextColor(Color.YELLOW);
                btnAddLoss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        def.updateReviewed(false, true);
                        updateContent(defText, def.toString());
                    }
                });

                btnToggleIgnore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        def.toggleIgnore();
                        updateContent(defText, def.toString());
                    }
                });

                btnToggleDifficult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        def.toggleDifficult();
                        updateContent(defText, def.toString());
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                        LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

                        final EditText txtDefName = dialogView.findViewById(R.id.txtDefName);
                        final EditText txtDefDesc = dialogView.findViewById(R.id.txtDefDesc);
                        final EditText txtDefLevel = dialogView.findViewById(R.id.txtDefLevel);

                        // Set existing fields
                        txtDefName.setText(def.getName());
                        txtDefDesc.setText(def.getDescription());
                        txtDefLevel.setText(def.getLevel());

                        builder.setView(dialogView)
                                .setTitle("Edit Definition")
                                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Add new def
                                        String name = txtDefName.getText().toString().trim();
                                        if (name.isEmpty()) {
                                            Toast.makeText(CourseActivity.this,"Definition name cannot be empty",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else if (!def.getName().equals(name) && _currentCourse.containsDefinition(name)) {
                                            Toast.makeText(CourseActivity.this,"Another definition with this name already exists",Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        def.setName(name);
                                        def.setDescription(txtDefDesc.getText().toString());
                                        def.setLevel(txtDefLevel.getText().toString());

                                        updateReviewButton();
                                        updateContent(defText, def.toString());
                                        btn.setText(def.getName() + ": " + def.getDescription());
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


                btnDelete.setTextColor(Color.RED);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIManager.createConfirmationPopup(CourseActivity.this,"Delete Definition", String.format("Delete \"%1$s\" ? This cannot be undone.", def.getName()), R.drawable.brain,
                                "YES", "NO", null,
                                () -> {
                                    _currentCourse.removeDefinition(def);
                                    ll.removeView(btn);
                                    coursePopup.dismiss();
                        }, null, null);
                    }
                });

                coursePopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateReviewButton();
                    }
                });
                coursePopup.show();
            }
        });
        ll.addView(btn);
    }


    private void init(File courseFile) {
        _currentCourse = new Course();
        _currentCourseFile = courseFile;
        _currentCourse.loadFromFile(courseFile);
        updateCourseHeader();
    }

    private void updateContent(TextView text, String content) {
        text.setText(content);
    }

    private void updateReviewButton() {
        btnReview.setText(String.format("Review (%1$d)", _currentCourse.getReviewList().size()));
    }

    private void updateCourseHeader() {
        lblCourseTitle.setText(_currentCourse.getName());
        lblDescription.setText(_currentCourse.getDescription());
    }

    private void showDeleteConfirmation () {
        UIManager.createConfirmationPopup(this,"Delete course", "Delete this course? This cannot be undone.", R.drawable.brain,
                "YES", "NO", null,
                () -> {_currentCourseFile.delete(); finish(); }, null, null);
    }
}
