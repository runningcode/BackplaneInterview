package com.nelson.backplaneinterview;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.nelson.backplaneinterview.models.ReceiptItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReceiptLoader extends AsyncTaskLoader<List<ReceiptItem>> {

    private int number;

    public ReceiptLoader(Context context, int number) {
        super(context);
        this.number = number;
    }

    @Override
    public List<ReceiptItem> loadInBackground() {
        BackplaneHttpClient httpClient = new BackplaneHttpClient(BackplaneHttpClient.UrlType.RECEIPT, number);
        String data = httpClient.getData();
        if (data != null && !"".equals(data)) {
            try {
                List<ReceiptItem> loadedObjects = new ArrayList<ReceiptItem>();
                JSONArray receiptArrayJson = new JSONArray(data);
                for (int i = 0; i < receiptArrayJson.length(); i++) {
                    loadedObjects.add(new ReceiptItem(receiptArrayJson.getJSONObject(i)));
                }
                return loadedObjects;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
