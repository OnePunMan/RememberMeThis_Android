package onepunman.remembermethis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
    Button btnReviewAll;
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

        refreshList();

        btnAddDefinition = findViewById(R.id.btnAddDef);
        btnAddDefinition.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
            LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

            final EditText txtDefName = dialogView.findViewById(R.id.txtDefName);
            final EditText txtDefDesc = dialogView.findViewById(R.id.txtDefDesc);
            final EditText txtDefLevel = dialogView.findViewById(R.id.txtDefLevel);

            builder.setView(dialogView)
                    .setTitle("Add New Definition")
                    .setPositiveButton(R.string.create, (dialogInterface, i) -> {
                        // Add new def
                        String name = txtDefName.getText().toString().trim();
                        String desc = txtDefDesc.getText().toString().trim();
                        String level =  txtDefLevel.getText().toString().trim();

                        if (name.isEmpty()) {
                            Toast.makeText(CourseActivity.this, "Definition name cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (name.contains(Course.DELIMITER) ||
                                desc.contains(Course.DELIMITER) ||
                                level.contains(Course.DELIMITER)) {
                            Toast.makeText(CourseActivity.this, "Definition contains illegal characters", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (_currentCourse.containsDefinition(name)) {
                            Toast.makeText(CourseActivity.this, "Definition already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Definition newDef = new Definition(name, desc, level);
                        _currentCourse.addDefinition(newDef);
                        addDefButton(newDef);
                        updateReviewButton();
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        btnEditCourse = findViewById(R.id.btnEditCourse);
        btnEditCourse.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
            LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

            final EditText txtCourseName = dialogView.findViewById(R.id.txtDefName);
            final EditText txtCourseDesc = dialogView.findViewById(R.id.txtDefDesc);

            dialogView.findViewById(R.id.txtDefLevel).setVisibility(View.GONE);

            // Set existing fields
            txtCourseName.setText(_currentCourse.getName());

            String desc = _currentCourse.getDescription();
            txtCourseDesc.setText(desc.equals(Course.EMPTY_COURSE_DESC_PLACEHOLDER) ? null : desc);

            builder.setView(dialogView)
                    .setTitle("Edit Course")
                    .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                        // Make sure new name is not empty
                        String name = txtCourseName.getText().toString().trim();
                        if (name.isEmpty()) {
                            Toast.makeText(CourseActivity.this, "Course name cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!_currentCourse.getName().toLowerCase().equals(name.toLowerCase()) && FileIO.isAlreadyExist(name)) {
                            Toast.makeText(CourseActivity.this, "This course already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        _currentCourse.setName(name);
                        _currentCourse.setDescription(txtCourseDesc.getText().toString());

                        updateCourseHeader();
                    })
                    .setNeutralButton(R.string.delete, (dialog, which) -> showDeleteConfirmation())
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.RED));
            dialog.show();
        });

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> {
            _currentCourse.save();
            finish();
        });

        btnReview = findViewById(R.id.btnReview);
        updateReviewButton();
        btnReview.setOnClickListener(v -> {
            // Review Stuff
            ArrayList<Definition> reviewList = _currentCourse.getReviewList();
            if (reviewList.isEmpty()) {
                UIManager.createConfirmationPopup(CourseActivity.this,
                        "Nothing to Review", "You currently have nothing to review.", R.drawable.brain,
                        "OK", null, null, null, null, null);
            }
            else {
                // Start review
                _currentCourse.save();
                Intent i = new Intent(CourseActivity.this, ReviewActivity.class);
                i.putExtra("courseFile", _currentCourseFile);
                startActivity(i);
            }
        });

        btnReviewAll = findViewById(R.id.btnReviewAll);
        btnReviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_currentCourse.getAll().isEmpty()) {
                    UIManager.createConfirmationPopup(CourseActivity.this,
                            "Nothing to Review", "This course is empty.", R.drawable.brain,
                            "OK", null, null, null, null, null);
                }
                else {
                    // Practice review
                    _currentCourse.save();
                    Intent i = new Intent(CourseActivity.this, ReviewActivity.class);
                    i.putExtra("courseFile", _currentCourseFile);
                    i.putExtra("isAll", true);
                    startActivity(i);
                }
            }
        });
    }

    private void refreshList() {
        ll.removeAllViews();
        ArrayList<Definition> definitions = _currentCourse.getAll();
        for (final Definition def : definitions) {
            addDefButton(def);
        }
    }

    private void addDefButton (final Definition def) {
        final Button btn = new Button(this);
        btn.setText(def.getName() + " : " + def.getDescription());
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(v -> {
            final Dialog coursePopup = new Dialog(CourseActivity.this);

            coursePopup.setContentView(R.layout.activity_popup);

            final TextView defText = coursePopup.findViewById(R.id.lblDefText);
            Button btnAddWin = coursePopup.findViewById(R.id.btnAddWin);
            Button btnAddLoss = coursePopup.findViewById(R.id.btnAddLose);
            Button btnToggleIgnore = coursePopup.findViewById(R.id.btnToggleIgnore);
            Button btnToggleDifficult = coursePopup.findViewById(R.id.btnToggleDifficult);
            Button btnEdit = coursePopup.findViewById(R.id.btnEdit);
            Button btnBack = coursePopup.findViewById(R.id.btnBack);
            Button btnReset = coursePopup.findViewById(R.id.btnReset);
            Button btnDelete = coursePopup.findViewById(R.id.btnDelete);

            updateContent(defText, def.toString());

            btnAddWin.setTextColor(Color.GREEN);
            btnAddWin.setOnClickListener(view -> {
                def.updateReviewed(true, true);
                updateContent(defText, def.toString());
            });

            btnAddLoss.setTextColor(Color.YELLOW);
            btnAddLoss.setOnClickListener(view -> {
                def.updateReviewed(false, true);
                updateContent(defText, def.toString());
            });

            btnToggleIgnore.setOnClickListener(v14 -> {
                def.toggleIgnore();
                updateContent(defText, def.toString());
            });

            btnToggleDifficult.setOnClickListener(v13 -> {
                def.toggleDifficult();
                updateContent(defText, def.toString());
            });

            btnEdit.setOnClickListener(v12 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                LayoutInflater inflater = CourseActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_new_definition, null);

                final EditText txtDefName = dialogView.findViewById(R.id.txtDefName);
                final EditText txtDefDesc = dialogView.findViewById(R.id.txtDefDesc);
                final EditText txtDefLevel = dialogView.findViewById(R.id.txtDefLevel);

                // Set existing fields
                txtDefName.setText(def.getName());

                String desc = def.getDescription();
                txtDefDesc.setText(desc.equals(Definition.EMPTY_DESC_PLACEHOLDER) ? null : desc);

                String lvl = def.getLevel();
                txtDefLevel.setText(lvl.equals(Definition.EMPTY_LEVEL_PLACEHOLDER) ? null : lvl);

                builder.setView(dialogView)
                        .setTitle("Edit Definition")
                        .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                            // Add new def
                            String name = txtDefName.getText().toString().trim();
                            String des = txtDefDesc.getText().toString().trim();
                            String level = txtDefLevel.getText().toString().trim();

                            if (name.isEmpty()) {
                                Toast.makeText(CourseActivity.this, "Definition name cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if (name.contains(Course.DELIMITER) ||
                                    des.contains(Course.DELIMITER) ||
                                    level.contains(Course.DELIMITER)) {
                                Toast.makeText(CourseActivity.this, "Definition contains illegal characters", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            else if (!def.getName().equals(name) && _currentCourse.containsDefinition(name)) {
                                Toast.makeText(CourseActivity.this, "Another definition with this name already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            def.setName(name);
                            def.setDescription(des);
                            def.setLevel(level);

                            updateReviewButton();
                            updateContent(defText, def.toString());
                            btn.setText(def.getName() + " : " + def.getDescription());
                        })
                        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            btnBack.setOnClickListener(view -> coursePopup.dismiss());

            btnReset.setOnClickListener(view -> UIManager.createConfirmationPopup(CourseActivity.this, "Reset Definition",
                    String.format("Reset all data for \"%1$s\" ?", def.getName()), R.drawable.brain,
                    "YES", "CANCEL", null,
                    () -> {
                        def.reset();
                        updateContent(defText, def.toString());
                    },
                    null, null));


            btnDelete.setTextColor(Color.RED);
            btnDelete.setOnClickListener(v1 -> UIManager.createConfirmationPopup(CourseActivity.this, "Delete Definition", String.format("Delete \"%1$s\" ? This cannot be undone.", def.getName()), R.drawable.brain,
                    "YES", "NO", null,
                    () -> {
                        _currentCourse.removeDefinition(def);
                        ll.removeView(btn);
                        coursePopup.dismiss();
                    }, null, null));

            coursePopup.setOnDismissListener(dialog -> updateReviewButton());
            coursePopup.show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        _currentCourse.reload();
        refreshList();
        updateReviewButton();
    }
}
