package com.cellasoft.ifoundit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.adapter.CountrySpinnerArrayAdapter;
import com.cellasoft.ifoundit.adapter.StateSpinnerAdapter;
import com.cellasoft.ifoundit.models.Country;
import com.cellasoft.ifoundit.models.State;
import com.cellasoft.ifoundit.utils.Lists;
import com.cellasoft.ifoundit.utils.Validation;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pipl.api.data.Utils;
import com.pipl.api.data.containers.Person;
import com.pipl.api.data.fields.Address;
import com.pipl.api.data.fields.Field;
import com.pipl.api.data.fields.Name;

import java.util.ArrayList;
import java.util.List;

import static com.cellasoft.ifoundit.utils.LogUtils.LOGE;
import static com.cellasoft.ifoundit.utils.LogUtils.makeLogTag;

public class MainActivity extends SherlockActivity {

    public static final String TAG = makeLogTag(MainActivity.class);
    public static final String EXTRA_PERSON = "person";

    private EditText tv_first;
    private EditText tv_last;
    private EditText tv_city;
    private AutoCompleteTextView tv_country;
    private AutoCompleteTextView tv_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().destroy();
    }

    private void init() {
        tv_first = (EditText) findViewById(R.id.filter_first_name);
        tv_last = (EditText) findViewById(R.id.filter_last_name);
        tv_state = (AutoCompleteTextView) findViewById(R.id.filter_state);
        tv_city = (EditText) findViewById(R.id.filter_city);
        tv_country = (AutoCompleteTextView) findViewById(R.id.filter_country);

        List<Country> countries = Lists.newArrayList();

        for (String key : Utils.COUNTRIES.keySet()) {
            countries.add(new Country(key, Utils.COUNTRIES.get(key)));
        }

        CountrySpinnerArrayAdapter adapter = new CountrySpinnerArrayAdapter(this, R.layout.spinner_item, countries);
        tv_country.setAdapter(adapter);

        tv_country.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                String country = s.toString().toUpperCase();
                if (Utils.STATES.containsKey(country)) {

                    List<State> states = Lists.newArrayList();

                    for (String key : Utils.STATES.get(country).keySet()) {
                        states.add(new State(key, Utils.STATES.get(country).get(key)));
                    }

                    StateSpinnerAdapter adapter = new StateSpinnerAdapter(MainActivity.this, R.layout.spinner_item, states);
                    tv_state.setAdapter(adapter);
                    tv_state.setEnabled(true);
                } else {
                    tv_state.setText("");
                    tv_state.setEnabled(false);
                }
            }
        });
        tv_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validate();
                }
            }
        });

        tv_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validate();
                }
            }
        });

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_action_android) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_action_android) // resource or drawable
                .showImageOnFail(R.drawable.ic_action_android) // resource or drawable
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(32))
                .build();

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validate() {

        return Validation.isName(tv_first, true)
                && Validation.isName(tv_last, true)
                && Validation.isCountry(tv_country, true)
                && Validation.isState(tv_state, tv_country.getText().toString(), false);
    }

    private void search() {
        if (validate()) {
            try {
                ArrayList<Field> fields = Lists.newArrayList();
                String first = tv_first.getText().toString();
                String last = tv_last.getText().toString();
                String country = tv_country.getText().toString();
                String city = tv_city.getText().toString();
                String state = tv_state.getText().toString();
                fields.add(new Name.Builder().first(first).last(last).build());
                fields.add(new Address.Builder().country(country).city(city).state(state).build());

                startResultActivity(new Person.Builder().fields(fields).build());
            } catch (NullPointerException e) {
                LOGE(TAG, e.getMessage());
            }
        }
    }

    private void startResultActivity(Person person) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra(EXTRA_PERSON, person);
        startActivity(intent);
    }
}
