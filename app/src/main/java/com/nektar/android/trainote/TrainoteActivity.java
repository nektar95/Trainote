package com.nektar.android.trainote;

import android.support.v4.app.Fragment;

public class TrainoteActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TrainoteFragmentList.newInstance();
    }

}
