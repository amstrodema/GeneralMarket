package comgalaxyglotech.confirmexperts.generalmarket.Controller.Payment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comgalaxyglotech.confirmexperts.generalmarket.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayDues extends Fragment {


    public PayDues() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_pay_dues, container, false);
    }

}
