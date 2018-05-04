package onepunman.remembermethis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.tekle.oss.android.animation.*;

public class CardStackFragment extends Fragment {
    final String TAG = "Debug";
    private Definition _definition;
    private boolean _isPractice;
    private boolean _isShown;

    private ReviewActivity parentActivity;
    //private ViewAnimator mViewAnimator;

    private TextView mTxtName;
    private TextView mTxtDesc;

    //private CardFragmentFront _cardFront;
    //private CardFragmentBack _cardBack;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_item, null);

       //mViewAnimator = v.findViewById(R.id.viewAnimator);
       parentActivity = (ReviewActivity) getActivity();

       mTxtName = v.findViewById(R.id.cardTitle);
       mTxtDesc = v.findViewById(R.id.cardDescription);

       mTxtName.setText(_definition.getName());
       mTxtDesc.setText(null);

       /*
        _cardFront = new CardFragmentFront();
        _cardBack = new CardFragmentBack();

        _cardFront.setInfo(_definition);
        _cardBack.setInfo(_definition);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fs = fm.beginTransaction();

        fs.add(mViewAnimator.getId(), _cardFront, "cardFront");
        fs.add(mViewAnimator.getId(), _cardBack, "cardBack");

        fs.commit();
        */

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_definition != null)
                {
                    // Flip card
                    mTxtDesc.setText(_definition.getDescription());
                    //AnimationFactory.flipTransition(mViewAnimator, AnimationFactory.FlipDirection.LEFT_RIGHT);
                    if (_isShown) {
                        AnimationFactory.fadeOut(mTxtDesc);
                    }
                    else {
                        AnimationFactory.fadeIn(mTxtDesc);
                    }
                    _isShown = !_isShown;
                }
            }
        });

        v.setOnTouchListener(new OnSwipeTouchListener(parentActivity) {
            @Override
            public void onSwipeRight() {
                Animation a = AnimationFactory.outToRightAnimation(500, null);
                v.startAnimation(a);

                if (!_isPractice) _definition.updateReviewed(true, true);
                parentActivity.makeToast("Nice!");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeLeft() {
                Animation a = AnimationFactory.outToLeftAnimation(500, null);
                v.startAnimation(a);

                if (!_isPractice) _definition.updateReviewed(false, true);
                parentActivity.makeToast("You'll get it next time!");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeTop() {
                Animation a = AnimationFactory.outToTopAnimation(500, null);
                v.startAnimation(a);

                parentActivity.makeToast("Come back later");
                parentActivity.putAtEnd(CardStackFragment.this, _definition);
            }

            @Override
            public void onSwipeBottom() {
                Animation a = AnimationFactory.outToBottomAnimation(500, null);
                v.startAnimation(a);

                parentActivity.makeToast("Skipped");
                parentActivity.removeCard(CardStackFragment.this, _definition);
            }
        });

        return v;
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _cardFront = new CardFragmentFront();
        _cardBack = new CardFragmentBack();

        _cardFront.setInfo(_definition);
        _cardBack.setInfo(_definition);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fs = fm.beginTransaction();

        fs.add(mViewAnimator.getId(), _cardFront, "cardFront");
        fs.add(mViewAnimator.getId(), _cardBack, "cardBack");

        fs.commit();
    }
    */

    public void setDefinition(Definition def, boolean advance) {
        Log.e("Debug", "parent set def");
        _definition = def;
        _isPractice = advance;
        _isShown = false;
    }
}
