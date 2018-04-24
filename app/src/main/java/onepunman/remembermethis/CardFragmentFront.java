package onepunman.remembermethis;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardFragmentFront extends Fragment {

    private TextView mTxtTitle;
    private Definition _def;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("Debug", "ON CREATE VIEW");
        View v = inflater.inflate(R.layout.card_item_front, null);
        mTxtTitle = v.findViewById(R.id.cardTitleFront);

        if (_def != null) {
            mTxtTitle.setText(_def.getName());
        }
        return v;
    }

    public void setInfo(Definition def) {
        Log.e("Debug", "SET INFO!");
        if (def == null) Log.e("Debug", "setInfo: NULL");
        _def = def;
    }
}
