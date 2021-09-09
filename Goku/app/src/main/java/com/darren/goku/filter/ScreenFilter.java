package com.darren.goku.filter;

import android.content.Context;

import com.darren.goku.R;

public class ScreenFilter extends AbstractFilter {

    public ScreenFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }

}
