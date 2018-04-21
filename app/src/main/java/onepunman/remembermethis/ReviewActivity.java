package onepunman.remembermethis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.util.ArrayList;

public class ReviewActivity extends FragmentActivity {
    final String TAG = "Debug";

    private FrameLayout mCardFrame;
    private Course _course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        File f = (File) getIntent().getSerializableExtra("courseFile");
        if (f != null) {
            _course = new Course();
            _course.loadFromFile(f);
        }
        else {
            Log.e("Debug", "Failed to start review session");
        }

        mCardFrame = findViewById(R.id.viewPager);
        ArrayList<Definition> reviewDefs = _course.getReviewList();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fs = fm.beginTransaction();

        for (Definition def : reviewDefs) {
            CardStackFragment newCard = new CardStackFragment();
            newCard.setDefinition(def);
            fs.add(mCardFrame.getId(), newCard, def.getName());
        }

        fs.commit();
    }

    public void removeCard(CardStackFragment card) {
        getFragmentManager().beginTransaction().remove(card).commit();
    }
}
