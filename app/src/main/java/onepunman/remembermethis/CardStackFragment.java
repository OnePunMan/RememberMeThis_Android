package onepunman.remembermethis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CardStackFragment extends Fragment {

    private Definition _definition;

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
        mTxtDesc.setText(_definition.getDescription());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_definition != null)
                {
                    Log.e("Debug", _definition.getName());
                    ReviewActivity parentActivity = (ReviewActivity) getActivity();
                    parentActivity.removeCard(CardStackFragment.this);
                }
            }
        });
        return v;
    }

    public void setDefinition(Definition def) {
        _definition = def;
    }
}
