package com.cellasoft.ifoundit.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.actionbarsherlock.view.MenuInflater;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.models.People;
import com.cellasoft.ifoundit.utils.FixSearchAPIRequest;
import com.cellasoft.ifoundit.utils.Lists;
import com.cellasoft.ifoundit.utils.UIUtils;
import com.cellasoft.ifoundit.widget.PeopleListView;
import com.pipl.api.data.Utils;
import com.pipl.api.data.containers.Person;
import com.pipl.api.data.containers.Record;
import com.pipl.api.search.SearchAPIRequest;
import com.pipl.api.search.SearchAPIResponse;

import java.util.List;

import static com.cellasoft.ifoundit.utils.LogUtils.LOGE;
import static com.cellasoft.ifoundit.utils.LogUtils.makeLogTag;

public class ResultActivity extends BaseListActivity {

    public static final String TAG = makeLogTag(ResultActivity.class);
    private static final int RESULT_SETTINGS = 1;

    private List<People> peoples;
    private List<People> filterList;
    private PeopleListView listView;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list_layout);

        Object objPerson = getIntent().getSerializableExtra(MainActivity.EXTRA_PERSON);
        if (objPerson instanceof Person) {
            person = (Person) objPerson;
        }

        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        listView.refresh();
    }

    @Override
    protected void onDestroy() {
        if (listView != null) {
            listView.clean();
        }

        super.onDestroy();
    }

    private void init() {
        peoples = Lists.newArrayList();
        filterList = Lists.newArrayList();
        initActionBar();
        initListView();
        loadData();
    }

    @Override
    protected void initActionBar() {
        getSupportActionBar().setTitle(person.getNames().get(0).toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListView() {
        listView = (PeopleListView) getListView();
    }


    @SuppressLint("NewApi")
    @Override
    protected void loadData() {
        AsyncTask task = new AsyncTask<Void, Void, Boolean>() {
            LinearLayout progress = (LinearLayout) findViewById(R.id.loading);

            private String error;

            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                progress.setVisibility(View.GONE);
                if (success) {
                    showUserSettings();
                } else {
                    LOGE(TAG, error);
                    UIUtils.showMessage(ResultActivity.this, error);
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (person != null) {
                    try {

                        FixSearchAPIRequest request = new FixSearchAPIRequest.Builder().person(person).build();
                        SearchAPIResponse response = request.send();

                        for (Record record : response.getRecords()) {
                            
                            System.out.println(record.toString());
                            peoples.add(new People(record));
                        }

                    } catch (Exception e) {
                        error = e.getMessage();
                        return false;
                    }
                }

                return true;
            }
        };

        if (UIUtils.hasHoneycomb()) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    (Void[]) null);
        } else {
            task.execute((Void[]) null);
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        People people = (People) l.getItemAtPosition((int) id);
        showDetails(people);
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        new MenuInflater(this).inflate(R.menu.people_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.menu_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;
        }
    }

    private void showPeople(People people) {
        UIUtils.safeOpenBrowser(getBaseContext(), people.getUrl());
    }

    private void showSettings() {
        Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivityForResult(intent, RESULT_SETTINGS);
    }

    private void showDetails(People people) {
        Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
        intent.putExtra("People", people);
        startActivity(intent);
    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        boolean onlyImage = sharedPrefs.getBoolean("prefOnlyImage", false);
        boolean onlySocial = sharedPrefs.getBoolean("prefOnlySocial", false);

        filterList.clear();

        for (People p : peoples) {
            String domain = p.getDomain().toLowerCase();
            if (onlyImage && !Utils.isValidUrl(p.getImage())) {
                continue;
            }
            if (onlySocial && !(domain.contains("facebook") || domain.contains("linkedin")
                    || domain.contains("twitter") || domain.contains("plus.google"))) {
                continue;
            }

            filterList.add(p);
        }

        runOnUiThread(new Runnable() {
            public void run() {
                listView.setItems(filterList);
            }
        });
    }
}
