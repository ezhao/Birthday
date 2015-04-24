package com.herokuapp.ezhao.birthday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CountdownFragment extends Fragment {
    @InjectView(R.id.tvDaysRemaining) TextView tvDaysRemaining;
    long daysRemaining;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        ButterKnife.inject(this, view);
        tvDaysRemaining.setText(String.valueOf(daysRemaining));
        return view;
    }

    public void setDaysRemaining(long newDaysRemaining) {
        daysRemaining = newDaysRemaining;
        if (tvDaysRemaining != null) {
            tvDaysRemaining.setText(String.valueOf(daysRemaining));
        }
    }
}
