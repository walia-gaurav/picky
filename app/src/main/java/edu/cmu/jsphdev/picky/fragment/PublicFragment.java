package edu.cmu.jsphdev.picky.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import edu.cmu.jsphdev.picky.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicFragment extends Fragment {

    Button i1;
    Button i2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public, container, false);


        i1 = (Button) view.findViewById(R.id.choice1);
        i2 = (Button) view.findViewById(R.id.choice2);

        customTouchListener(i1);
        customTouchListener(i2);

        return view;
    }

    private void customTouchListener(final Button x) {
        x.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        x.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        x.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x.setText("picked !");
                        ((LinearLayout) getActivity().findViewById(R.id.frame1)).setClickable
                                (true);
                        i1.setEnabled(false);
                        i2.setEnabled(false);
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        x.getBackground().clearColorFilter();
                        x.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

}
