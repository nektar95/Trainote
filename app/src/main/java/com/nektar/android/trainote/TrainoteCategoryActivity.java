package com.nektar.android.trainote;

import android.support.v4.app.Fragment;

/**
 * Created by olo35 on 17.05.2016.
 */
public class TrainoteCategoryActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return TrainoteCategoryFragment.newInstance();
    }
}
