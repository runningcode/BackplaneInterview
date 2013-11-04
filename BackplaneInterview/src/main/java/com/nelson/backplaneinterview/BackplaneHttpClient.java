package com.nelson.backplaneinterview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class BackplaneHttpClient {
    private UrlType urlType;
    private int number;

    public BackplaneHttpClient(UrlType urlType, int number) {
        this.urlType = urlType;
        this.number = number;
    }

    private static final String OVERVIEW_URL = "http://sampleapi.bplane.com/overview/1/";
    private static final String RECEIPT_URL = "http://sampleapi.bplane.com/receipt/";

    public enum UrlType {
        OVERVIEW,
        RECEIPT
    }

    public String getData() {
        InputStream inputStream = null;
        try {
            switch (urlType) {
                case OVERVIEW:
                    inputStream = new URL(OVERVIEW_URL).openStream();
                    break;
                case RECEIPT:
                    inputStream = new URL(RECEIPT_URL + number).openStream();
                    break;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder stringBuilder = new StringBuilder();
            int cp;
            while ((cp = reader.read()) != -1) {
                stringBuilder.append((char) cp);
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
