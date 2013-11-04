package com.nelson.backplaneinterview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.nelson.backplaneinterview.models.User;
import com.nelson.backplaneinterview.support.ExpandableListFragment;

import java.text.NumberFormat;
import java.util.List;

public class ReceiptsListFragment extends ExpandableListFragment
        implements LoaderManager.LoaderCallbacks<List<Object>> {

    private OverviewListAdapter overviewListAdapter;
    private TextView nameText;
    private TextView dataText;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = layoutInflater.inflate(R.layout.header_view, null);
        nameText = (TextView) header.findViewById(R.id.name);
        dataText = (TextView) header.findViewById(R.id.data);
        ExpandableListView listView = getListView();
        listView.addHeaderView(header);
        listView.setDivider(new ColorDrawable(getResources().getColor(android.R.color.black)));
        listView.setDividerHeight(1);
        listView.setChildDivider(new ColorDrawable(getResources().getColor(R.color.gray_dark)));

        overviewListAdapter = new OverviewListAdapter(getActivity());
        setListAdapter(overviewListAdapter);
        setEmptyText("No Data");
        setListShown(false);

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<Object>> onCreateLoader(int i, Bundle bundle) {
        return new OverviewLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Object>> listLoader, List<Object> objects) {
        if (objects != null && !objects.isEmpty()) {
            if (objects.get(0) instanceof User) {
                User user = (User) objects.get(0);
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                nameText.setText(user.getName());
                dataText.setText(numberFormat.format(user.getBalance()));
                objects.remove(0);
            }
            overviewListAdapter.setData(objects);
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        } else {
            setEmptyText("An error occured");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Object>> listLoader) {
    }
}
