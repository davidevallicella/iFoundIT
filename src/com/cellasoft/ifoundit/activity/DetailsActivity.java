package com.cellasoft.ifoundit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.models.People;
import com.cellasoft.ifoundit.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailsActivity extends SherlockActivity {
    private People people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_detail);

        people = (People) getIntent().getSerializableExtra("People");
        init();
    }

    private void init() {
        initActionBar();
 
         ImageLoader.getInstance().displayImage(people.getImage(), (ImageView) findViewById(R.id.image_detail));
        ((TextView)findViewById(R.id.mobile_detail)).setText(people.getPhone());

        findViewById(R.id.image_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPeople();
            }
        });
    }

    protected void initActionBar() {
        getSupportActionBar().setTitle(people.getFullName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showPeople() {
        UIUtils.safeOpenBrowser(getBaseContext(), people.getUrl());
    }
}
