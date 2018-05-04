package onepunman.remembermethis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ReviewActivity extends FragmentActivity {
    final String TAG = "Debug";
    FragmentManager fm;

    private FrameLayout mCardFrame;
    private TextView mTxtCounter;
    private TextView mTxtTime;

    private Course _course;
    private ArrayList<Definition> _reviewList;
    private ArrayList<Definition> _laterList;
    private boolean _reviewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.createConfirmationPopup(ReviewActivity.this, "Exit Session", "Do you want to exist this review session? Your progress will be saved.",
                        R.drawable.brain, "YES", "NO", null, () -> { saveCourseIfReal(); finish(); }, null, null);
            }
        });

        fm = getFragmentManager();

        File f = (File) getIntent().getSerializableExtra("courseFile");
        if (f != null) {
            _course = new Course();
            _course.loadFromFile(f);
        }
        else {
            Log.e("Debug", "Failed to start review session");
        }


        _reviewAll = getIntent().getExtras().getBoolean("isAll");

        mCardFrame = findViewById(R.id.viewPager);
        mTxtCounter = findViewById(R.id.txtRemaining);
        mTxtTime = findViewById(R.id.txtTimer);

        mTxtTime.setText(String.format("Course: %s", _course.getName()));

        _reviewList = _reviewAll ? _course.getAll() : _course.getReviewList();
        Collections.shuffle(_reviewList);

        _laterList = new ArrayList<>();
        updateDefCount();

        FragmentTransaction fs = fm.beginTransaction();

        for (Definition def : _reviewList) {
            addCard(def);
        }

        fs.commit();
    }

    private void addCard(Definition def) {
        FragmentTransaction fs = fm.beginTransaction();

        CardStackFragment newCard = new CardStackFragment();
        newCard.setDefinition(def, _reviewAll);

        fs.add(mCardFrame.getId(), newCard, def.getName());
        fs.commit();
    }

    public void removeCard(CardStackFragment card, Definition def, boolean checkFinish) {
        fm.beginTransaction().remove(card).commit();
        _reviewList.remove(def);

        if (checkFinish) checkFinish();

        updateDefCount();
    }

    public void removeCard(CardStackFragment card, Definition def) {
        removeCard(card, def, true);
    }

    public void putAtEnd(CardStackFragment card, Definition def) {
        removeCard(card, def, false);
        _laterList.add(def);

        checkFinish();

        updateDefCount();
    }

    private void checkFinish() {
        if (_reviewList.size() <= 0) {
            if (_laterList.size() > 0) {
                for (Definition item : _laterList) {
                    addCard(item);
                }

                swapLists();
            }
            else
            {
                saveCourseIfReal();
                finish();
            }
        }
    }

    public int updateDefCount() {
        int size = _reviewList.size() + _laterList.size();
        mTxtCounter.setText(String.format("Remaining: %d", size));
        return size;
    }

    public void makeToast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }

    private void swapLists() {
        ArrayList<Definition> temp = _reviewList;
        _reviewList = _laterList;
        _laterList = temp;

        Collections.shuffle(_reviewList);
    }

    private void saveCourseIfReal() {
        if (!_reviewAll) {
            _course.save();
        }
    }
}
