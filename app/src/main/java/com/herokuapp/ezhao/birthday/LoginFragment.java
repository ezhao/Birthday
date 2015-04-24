package com.herokuapp.ezhao.birthday;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements DatePickerDialogFragment.DatePickerDialogHandler {
    @InjectView(R.id.etUsername) EditText etUsername;
    @InjectView(R.id.etPassword) EditText etPassword;
    @InjectView(R.id.etBirthday) TextView etBirthday;
    private GregorianCalendar birthday;
    private DaysRemainingListener daysRemainingListener;

    public interface DaysRemainingListener {
        public void showDaysRemaining(long daysRemaining);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        daysRemainingListener = (DaysRemainingListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.etBirthday)
    public void onClickBirthday(View view) {
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getChildFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setTargetFragment(LoginFragment.this);
        dpb.show();
    }

    @OnClick(R.id.btnLogin)
    public void onClickLogin(View view) {
        ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("EMILY", "user login success");
                    birthday = new GregorianCalendar();
                    birthday.setTime(user.getDate("birthday"));
                    daysRemainingListener.showDaysRemaining(getDaysRemaining());
                } else {
                    Log.i("EMILY", "user login FAILED - " + e.toString());
                }
            }
        });
    }

    @OnClick(R.id.btnCreateAccount)
    public void onClickCreateAccount(View view) {
        if (birthday == null) {
            Log.i("EMILY", "no birthday");
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("birthday", birthday.getTime());
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("EMILY", "user created");
                    daysRemainingListener.showDaysRemaining(getDaysRemaining());
                } else {
                    Log.i("EMILY", "user NOT created - " + e.toString());
                }
            }
        });
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        birthday = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        etBirthday.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(birthday.getTime()));
    }

    public long getDaysRemaining() {
        GregorianCalendar birthdayThisYear = new GregorianCalendar();
        birthdayThisYear.set(GregorianCalendar.MONTH, birthday.get(GregorianCalendar.MONTH));
        birthdayThisYear.set(GregorianCalendar.DAY_OF_MONTH, birthday.get(GregorianCalendar.DAY_OF_MONTH));

        long diffInMillis = birthdayThisYear.getTimeInMillis() - (new GregorianCalendar()).getTimeInMillis();
        long daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        return daysRemaining;
    }
}
