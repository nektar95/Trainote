package com.nektar.android.trainote;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by olo35 on 14.04.2016.
 */

public  class TrainSpinnerListener implements AdapterView.OnItemSelectedListener
{
    private String mCategory;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        mCategory=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mCategory=parent.getItemAtPosition(1).toString();

    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
