package com.nektar.android.trainote;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by olo35 on 07.05.2016.
 */
public class NotePagerTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.88f;
        private static final float MIN_ALPHA = 0.6f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1)
            {
                view.setAlpha(0);

            } else if (position <= 1)
            {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);


                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {

                view.setAlpha(0);
            }
        }
    }
