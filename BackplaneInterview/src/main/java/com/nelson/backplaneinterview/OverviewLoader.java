package com.nelson.backplaneinterview;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.nelson.backplaneinterview.models.Receipt;
import com.nelson.backplaneinterview.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class OverviewLoader extends AsyncTaskLoader<List<Object>> {

    public OverviewLoader(Context context) {
        super(context);
    }

    @Override
    public List<Object> loadInBackground() {
        BackplaneHttpClient httpClient = new BackplaneHttpClient(BackplaneHttpClient.UrlType.OVERVIEW, 0);
        String data = httpClient.getData();
        if (data != null && !"".equals(data)) {
            try {
                List<Object> loadedObjects = new ArrayList<Object>();
                JSONObject dataJsonObject = new JSONObject(data);
                JSONObject userJson = dataJsonObject.getJSONObject("user");
                loadedObjects.add(new User(userJson));

                JSONArray receiptsJson = dataJsonObject.getJSONArray("receipts");
                for (int i = 0; i < receiptsJson.length(); i++) {
                    loadedObjects.add(new Receipt(receiptsJson.getJSONObject(i)));
                }
                return loadedObjects;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
