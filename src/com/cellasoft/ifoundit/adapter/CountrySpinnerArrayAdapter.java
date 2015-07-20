package com.cellasoft.ifoundit.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import com.cellasoft.ifoundit.models.Country;
import com.cellasoft.ifoundit.utils.Lists;
import com.cellasoft.ifoundit.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netsysco on 26/07/2014.
 */
public class CountrySpinnerArrayAdapter extends BaseListAdapter<Country> {

    private List<Country> mOriginalValues;
    private ArrayFilter mFilter;

    public CountrySpinnerArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CountrySpinnerArrayAdapter(Context context, int resource, List<Country> items) {
        super(context, resource, items);
        mOriginalValues = new ArrayList<Country>(items);
    }

    @Override
    protected void populateDataForRow(ViewHolder viewHolder, Country country, int position) {
        if (viewHolder instanceof Holder) {
            Holder holder = (Holder) viewHolder;
            holder.name.setText(country.key.toUpperCase());
            int flag = UIUtils.getDrawable(this.getContext(), "ic_" + country.key);
            try {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(flag), null, null, null);
                int dp5 = (int) (5 * getContext().getResources().getDisplayMetrics().density + 0.5f);
                holder.name.setCompoundDrawablePadding(dp5);
            } catch (Resources.NotFoundException e) {
                //do nothing
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        Country item = items.get(position);

        if (convertView == null) {
            holder = new Holder();
            holder.name = (TextView) View.inflate(getContext(), resource,
                    null);
        } else {
            holder.name = (TextView) convertView;
        }

        populateDataForRow(holder, item, position);

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
                    mOriginalValues = new ArrayList<Country>(items);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    List<Country> list = new ArrayList<Country>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String query = prefix.toString().toLowerCase();

                List<Country> countries = mOriginalValues;
                List<Country> filter = new ArrayList<Country>(countries.size());

                for (Country country : countries) {
                    if (country.key.startsWith(query)) {
                        filter.add(country);
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
                items = (ArrayList<Country>) results.values;
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