package com.nektar.android.trainote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by olo35 on 29.04.2016.
 */
public class NotePagerActivity extends FragmentActivity implements NotePagerInterface {
    private ViewPager mViewPager;
    private List<List<Note>> weeks;
    private int numberOfWeek;
    private static final int REQUEST_NOTE = 0;
    private static final int SAVE_CODE = 11;
    public static final String EXTRA_PERIOD =
            "com.nektar.android.trainote.period";

    public void update()
    {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode == SAVE_CODE)
        {
            weeks = NoteContainer.get(this).getWeeks();
            update();
        }

    }

    private void createPager()
    {
        weeks = NoteContainer.get(this).getWeeks();


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        numberOfWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        //list initialize
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public int getItemPosition(Object object) {

                return POSITION_NONE;
            }
            @Override
            public Fragment getItem(int position) {
                return TrainoteFragmentList.newInstance(position);
            }

            @Override
            public int getCount() {
                return weeks.size();

            }
        });

        mViewPager.setPageTransformer(true,new NotePagerTransformer());

        if(getIntent().hasExtra(EXTRA_PERIOD)) {
            int n = (int) getIntent().getSerializableExtra(EXTRA_PERIOD);
            mViewPager.setCurrentItem(n);
        }
        else {

            if(weeks.get(weeks.size()-1).size()==0)
            {
                mViewPager.setCurrentItem(weeks.size()-1);
            }
            else {
                for (int i = 0; i < weeks.size(); i++) {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(weeks.get(i).get(0).getDate());
                    if (calendar1.get(Calendar.WEEK_OF_YEAR) == numberOfWeek) {

                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_note_pager);

        createPager();

    }

    @Override
    public void updatePager() {
        createPager();
    }

    @Override
    public void setCurrentWeek(int n) {
        mViewPager.setCurrentItem(n);
    }
}
