package com.cellasoft.ifoundit.activity;

import com.actionbarsherlock.app.SherlockListActivity;

import static com.cellasoft.ifoundit.utils.LogUtils.makeLogTag;

public abstract class BaseListActivity extends SherlockListActivity {
    protected boolean refresh = false;

    protected abstract void loadData();

    protected abstract void initListView();

    protected abstract void initActionBar();
}
