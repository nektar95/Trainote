package com.nektar.android.trainote;

import android.support.v4.app.Fragment;

/**
 * Created by olo35 on 15.05.2016.
 */
public class TrainoteInfoActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return TrainoteInfoFragment.newInstance();
    }
}
