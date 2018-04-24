package onepunman.remembermethis;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardFragmentBack extends Fragment {

    private TextView mTxtTitle;
    private TextView mTxtDescription;

    private Definition _def;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_item_back, null);

        mTxtTitle = v.findViewById(R.id.cardTitleBack);
        mTxtDescription = v.findViewById(R.id.cardDescriptionBack);

        if (_def != null) {
            mTxtTitle.setText(_def.getName());
            mTxtDescription.setText(_def.getDescription());
        }
        return v;
    }

    public void setInfo(Definition def) {
        _def = def;
    }
}
