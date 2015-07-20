package com.cellasoft.ifoundit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import com.cellasoft.ifoundit.adapter.BaseListAdapter;
import com.cellasoft.ifoundit.utils.Lists;

import java.util.List;

public class BaseListView<T> extends ListView {

    protected BaseListAdapter<T> adapter;
    protected LayoutInflater mInflater;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;

    public BaseListView(Context context) {
        super(context);
        init(context);
    }

    public BaseListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setDivider(getResources().getDrawable(
                android.R.drawable.divider_horizontal_bright));
        setDrawSelectorOnTop(true);
    }

    public void refresh() {
        adapter.refresh();
    }

    public int size() {
        return adapter.getCount();
    }

    public boolean isEmpty() {
        return adapter.getCount() == 0;
    }

    public void clean() {
        adapter.clear();
    }

    public void setItems(List<T> items) {
        if (items == null) {
            items = Lists.newArrayList();
        }
        adapter.setItems(items);
        this.setSelection(0);
    }

    public void addItems(List<T> items) {
        adapter.addAll(items);
    }

    /**
     * Sets empty layout resource
     *
     * @param res the resource of the layout to be shown when no items are available to load in the list
     */
    public void setEmptyViewRes(int res) {
        this.mEmptyView = (ViewGroup) mInflater.inflate(res, null);
        setEmptyView(mEmptyView);
    }

    /**
     * Sets loading layout resource
     *
     * @param res the resource of the layout to be shown when the list is loading
     */
    public void setLoadingViewRes(int res) {
        this.mLoadingView = (ViewGroup) mInflater.inflate(res, null);
        setEmptyView(mLoadingView);
    }
}
