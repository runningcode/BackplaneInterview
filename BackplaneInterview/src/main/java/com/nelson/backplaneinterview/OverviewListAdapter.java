package com.nelson.backplaneinterview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nelson.backplaneinterview.models.Receipt;
import com.nelson.backplaneinterview.models.ReceiptItem;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OverviewListAdapter extends BaseExpandableListAdapter
        implements LoaderManager.LoaderCallbacks<List<ReceiptItem>> {

    private LayoutInflater layoutInflater;
    private ArrayList<Receipt> receipts;
    private FragmentActivity context;
    private SimpleDateFormat simpleDateFormat;
    private NumberFormat numberFormat;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;

    public OverviewListAdapter(FragmentActivity context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        receipts = new ArrayList<Receipt>();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    public void setData(List<Object> objects) {
        if (objects != null) {
            receipts.clear();
            for (Object object : objects) {
                Receipt receipt = (Receipt) object;
                receipts.add(receipt);
                context.getSupportLoaderManager().initLoader(receipt.getId(), null, this).forceLoad();
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return receipts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // +2 for the header and footer
        return receipts.get(groupPosition).getReceiptItems().size() + 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return receipts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // -1 because we added a header
        return receipts.get(groupPosition).getReceiptItems().get(childPosition - 1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ReceiptViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.receipt_view, parent, false);
            viewHolder = new ReceiptViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.total = (TextView) convertView.findViewById(R.id.total);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReceiptViewHolder) convertView.getTag();
        }


        Receipt receipt = (Receipt) getGroup(groupPosition);

        if (receipt != null) {
            String date = simpleDateFormat.format(receipt.getDate());
            viewHolder.date.setText(date);
            viewHolder.total.setText(numberFormat.format(receipt.getTotal()));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        switch (getChildType(groupPosition, childPosition)) {
            case TYPE_HEADER:
                // this is our header
                ReceiptHeaderViewHolder receiptHeaderViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.gray_dark));
                    receiptHeaderViewHolder = new ReceiptHeaderViewHolder();
                    receiptHeaderViewHolder.header = (TextView) convertView.findViewById(android.R.id.text1);
                    convertView.setTag(receiptHeaderViewHolder);
                } else {
                    receiptHeaderViewHolder = (ReceiptHeaderViewHolder) convertView.getTag();
                }
                Receipt receipt = (Receipt) getGroup(groupPosition);
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm");
                String date = simpleDateFormat.format(receipt.getDate());
                String time = simpleTimeFormat.format(receipt.getDate());
                String text = receipt.getRestraurantName() + " " + date + " at " + time;
                receiptHeaderViewHolder.header.setText(text);
                break;
            case TYPE_FOOTER:
                // this is the footer
                ReceiptHeaderViewHolder receiptFooterViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.receipt_item_footer, parent, false);
                    receiptFooterViewHolder = new ReceiptHeaderViewHolder();
                    receiptFooterViewHolder.header = (TextView) convertView.findViewById(R.id.total);
                    convertView.setTag(receiptFooterViewHolder);
                } else {
                    receiptFooterViewHolder = (ReceiptHeaderViewHolder) convertView.getTag();
                }
                Receipt receipt1 = (Receipt) getGroup(groupPosition);
                receiptFooterViewHolder.header.setText(numberFormat.format(receipt1.getTotal()));
                break;
            case TYPE_NORMAL:
                ReceiptItemViewHolder viewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.receipt_item, parent, false);
                    viewHolder = new ReceiptItemViewHolder();
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
                    viewHolder.cost = (TextView) convertView.findViewById(R.id.cost);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ReceiptItemViewHolder) convertView.getTag();
                }

                ReceiptItem receiptItem = (ReceiptItem) getChild(groupPosition, childPosition);
                if (receiptItem != null) {
                    viewHolder.name.setText(receiptItem.getName());
                    viewHolder.quantity.setText("x" + receiptItem.getCount());
                    viewHolder.cost.setText(numberFormat.format(receiptItem.getTotalPrice()));
                }
                break;
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getChildTypeCount() {
        return 3;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (childPosition == 0) {
            return TYPE_HEADER;
        } else if (childPosition == getChildrenCount(groupPosition) - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public Loader<List<ReceiptItem>> onCreateLoader(int id, Bundle args) {
        return new ReceiptLoader(context, id);
    }

    @Override
    public void onLoadFinished(Loader<List<ReceiptItem>> loader, List<ReceiptItem> data) {
        if (data != null) {
            Receipt receipt = (Receipt) getGroup(loader.getId() - 1);
            receipt.setReceiptItems(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ReceiptItem>> loader) {
    }

    static class ReceiptViewHolder {
        public TextView name;
        public TextView date;
        public TextView total;
    }

    static class ReceiptHeaderViewHolder {
        public TextView header;
    }

    static class ReceiptItemViewHolder {
        public TextView name;
        public TextView quantity;
        public TextView cost;

    }
}
