package com.example.firebase.custom.authentication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.firebase.custom.authentication.fragment.MainFragment;
import com.example.firebase.custom.authentication.R;
import com.example.firebase.custom.authentication.fragment.SignInFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showFragment(SignInFragment.newInstance());
    }

    public void showMainFragment() {
        showFragment(MainFragment.newInstance());
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentPlaceHolder, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}