package com.cellasoft.ifoundit.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.adapter.PeopleAdapter;
import com.cellasoft.ifoundit.models.People;

public class PeopleListView extends BaseListView<People> {

    public PeopleListView(Context context) {
        this(context, null, 0);
    }

    public PeopleListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PeopleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        adapter = new PeopleAdapter(context, R.layout.list_view_item);
        this.setAdapter(adapter);
    }
}