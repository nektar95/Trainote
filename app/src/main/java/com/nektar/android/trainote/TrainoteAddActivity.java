package com.nektar.android.trainote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by olo35 on 14.04.2016.
 */
public class TrainoteAddActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_ID = "note_passing_id";
    private static final int SAVE_CODE = 11;



    protected Fragment createFragment() {
        if(getIntent().hasExtra(EXTRA_NOTE_ID))
        {
            UUID id =(UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);

            return TrainoteAddFragment.newInstance(id);
        }

        return TrainoteAddFragment.newInstance();
    }
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode == SAVE_CODE)
        {
            setResult(requestCode);
        }
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
