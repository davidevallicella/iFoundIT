package com.cellasoft.ifoundit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import com.cellasoft.ifoundit.models.State;
import com.cellasoft.ifoundit.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netsysco on 29/07/2014.
 */
public class StateSpinnerAdapter extends BaseListAdapter<State> {

    private ArrayFilter mFilter;
    private List<State> mOriginalValues;

    public StateSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public StateSpinnerAdapter(Context context, int resource, List<State> items) {
        super(context, resource, items);
        mOriginalValues = new ArrayList<State>(items);
    }

    @Override
    protected void populateDataForRow(ViewHolder viewHolder, State state, int position) {
        if (viewHolder instanceof Holder) {
            Holder holder = (Holder) viewHolder;
            holder.name.setText(state.name);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        State state = items.get(position);

        if (convertView == null) {
            holder = new Holder();
            holder.name = (TextView) View.inflate(getContext(), resource,
                    null);
        } else {
            holder.name = (TextView) convertView;
        }

        populateDataForRow(holder, state, position);

        return holder.name;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    class Holder extends ViewHolder {
        TextView name;
    }

    private class ArrayFilter extends Filter {
        private final Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<State>(items);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    List<State> list = new ArrayList<State>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String query = prefix.toString().toLowerCase();

                List<State> states = mOriginalValues;
                List<State> filter = new ArrayList<State>(states.size());

                for (State state : states) {
                    if (state.name.toLowerCase().startsWith(query)) {
                        filter.add(state);
                    }
                }

                results.values = filter;
                results.count = filter.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                items = (ArrayList<State>) results.values;
            } else {
                items = Lists.newArrayList();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
