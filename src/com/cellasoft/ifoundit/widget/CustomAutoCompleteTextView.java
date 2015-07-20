package com.cellasoft.ifoundit.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import com.cellasoft.ifoundit.models.Country;
import com.cellasoft.ifoundit.utils.UIUtils;

/**
 * Customizing AutoCompleteTextView to return Country Name
 * corresponding to the selected item
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView implements TextWatcher {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Returns the country name corresponding to the selected item
     */
    @Override
    protected CharSequence convertSelectionToString(Object item) {
        if (item instanceof Country) {
            Country country = (Country) item;

            int flag = UIUtils.getDrawable(this.getContext(), "ic_" + country.key);
            try {
                setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(flag), null, null, null);
                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                setCompoundDrawablePadding(dp5);
            } catch (Resources.NotFoundException e) {
                //do nothing
            }

            return country.key.toUpperCase();
        }
        return item.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.length() != 2) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}