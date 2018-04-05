package onepunman.remembermethis;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateNewCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        final Button create_button = findViewById(R.id.btn_create);
        create_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowAlert();
            }

        });

        final Button cancel_button = findViewById(R.id.btn_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ShowAlert() {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Hello World!")
                .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Testing Title+")
                .setIcon(R.drawable.brain)
                .create();
        msg.show();

    }
}
