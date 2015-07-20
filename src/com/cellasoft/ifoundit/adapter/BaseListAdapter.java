package com.cellasoft.ifoundit.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.cellasoft.ifoundit.utils.Lists;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BaseListAdapter<T> extends ArrayAdapter<T> {

    private static final int REFRESH_MESSAGE = 1;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected int resource; // store the resource layout id for 1 row
    protected List<T> items;
    protected IncomingHandler handler = new IncomingHandler(this);

    public BaseListAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.items = Lists.newArrayList();
    }

    public BaseListAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract void populateDataForRow(ViewHolder holder, T item, int position);

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    protected void imageLoader(ViewHolder holder, String imageUrl) {
        if (!holder.isSameView(imageUrl)) {
            holder.thumbnail.setTag(imageUrl);
            imageLoader.displayImage(imageUrl, holder.thumbnail, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    view.setTag(failReason.getType().name());
                }
            });
        }
    }

    public synchronized void addItems(List<T> items) {
        if (this.items != null) {
            this.items.addAll(items);
        } else {
            this.items = Lists.newArrayList();
            this.items.addAll(items);
        }
        this.refresh();
    }

    public synchronized void refresh() {
        this.handler.sendEmptyMessage(REFRESH_MESSAGE);
    }

    public synchronized List<T> getItems() {
        if (this.items == null) {
            this.items = Lists.newArrayList();
        }

        return this.items;
    }

    public synchronized void setItems(List<T> items) {
        this.items = items;
        this.notifyDataSetInvalidated();
    }

    @Override
    public void clear() {
        this.items.clear();
        this.refresh();
    }

    static class IncomingHandler extends Handler {
        private final WeakReference<BaseListAdapter<?>> mAdapter;

        IncomingHandler(BaseListAdapter<?> adapter) {
            this.mAdapter = new WeakReference<BaseListAdapter<?>>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BaseListAdapter<?> adapter = mAdapter.get();
            if (adapter != null && msg.what == REFRESH_MESSAGE) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    class ViewHolder {
        ImageView thumbnail;

        public boolean isSameView(String tag) {
            if (tag == null) {
                return thumbnail.getTag() == null;
            }
            return tag.equals(thumbnail.getTag());
        }
    }
}
