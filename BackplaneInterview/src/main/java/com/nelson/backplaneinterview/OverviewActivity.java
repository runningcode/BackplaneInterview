package com.nelson.backplaneinterview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class OverviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ReceiptsListFragment())
                    .commit();
        }
    }
}
