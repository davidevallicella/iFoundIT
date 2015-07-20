package com.cellasoft.ifoundit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.models.People;

public class PeopleAdapter extends BaseListAdapter<People> {

    public PeopleAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    protected void populateDataForRow(ViewHolder viewHolder, People item,
                                      int position) {
        if (viewHolder instanceof Holder) {
            Holder holder = (Holder) viewHolder;
            holder.name.setText(item.getFullName());
            holder.domain.setText(item.getDomain());
        }

        imageLoader(viewHolder, item.getThumbnail());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        RelativeLayout view;
        People item = items.get(position);

        if (convertView == null) {
            view = (RelativeLayout) View.inflate(getContext(), resource,
                    null);
            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.people_name);
            holder.domain = (TextView) view.findViewById(R.id.people_domain);
            holder.thumbnail = (ImageView) view.findViewById(R.id.people_image);

            view.setTag(holder);
        } else {
            view = (RelativeLayout) convertView;
            holder = (Holder) view.getTag();
        }

        populateDataForRow(holder, item, position);

        return view;
    }

    class Holder extends ViewHolder {
        TextView name;
        TextView domain;
    }
}
