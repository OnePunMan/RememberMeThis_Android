package onepunman.remembermethis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tekle.oss.android.animation.*;

public class CardStackFragment extends Fragment {
    final String TAG = "Debug";
    private Definition _definition;
    private boolean _isPractice;

    private ReviewActivity parentActivity;
    private TextView mTxtName;
    private TextView mTxtDesc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_item, null);

        // Init data here
        mTxtName = v.findViewById(R.id.cardTitle);
        mTxtDesc = v.findViewById(R.id.cardDescription);

        mTxtName.setText(_definition.getName());
        mTxtDesc.setText(null);

        parentActivity = (ReviewActivity) getActivity();


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_definition != null)
                {
                    mTxtDesc.setText(_definition.getDescription());
                }
            }
        });

        v.setOnTouchListener(new OnSwipeTouchListener(parentActivity) {
            @Override
            public void onSwipeRight() {
                AnimationFactory.fadeOut(v);
                if (!_isPractice) _definition.updateReviewed(true, true);
                parentActivity.makeToast("Nice!");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeLeft() {
                AnimationFactory.fadeOut(v);
                if (!_isPractice) _definition.updateReviewed(false, true);
                parentActivity.makeToast("You'll get it next time!");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeTop() {
                AnimationFactory.fadeOut(v);
                parentActivity.makeToast("Come back later");
                parentActivity.putAtEnd(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeBottom() {
                AnimationFactory.fadeOut(v);
                parentActivity.makeToast("Skipped");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }
        });

        return v;
    }

    public void setDefinition(Definition def, boolean advance) {
        _definition = def;
        _isPractice = advance;
    }
}
