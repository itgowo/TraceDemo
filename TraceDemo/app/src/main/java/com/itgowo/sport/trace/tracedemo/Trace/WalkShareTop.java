package com.itgowo.sport.trace.tracedemo.Trace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgowo.sport.trace.tracedemo.R;


/**
 * Created by lujianchao on 2016/12/26.
 *
 * @author lujianchao
 */

public class WalkShareTop extends RelativeLayout {
    public TextView sharelength, sharename;
    public ImageView sharehead;

    public WalkShareTop(final Context context, final AttributeSet attrs) {
        super (context, attrs);
        View.inflate (context, R.layout.walksharetopview, this);
        initView ();
    }

    private void initView () {
        sharelength = (TextView) findViewById (R.id.sharelength);
        sharename = (TextView) findViewById (R.id.sharename);
        sharehead = (ImageView) findViewById (R.id.sharehead);
    }

    public WalkShareTop(final Context context) {
        super (context);
        View.inflate (context, R.layout.walksharetopview, this);
        initView ();
    }

}
