package com.pgleska.sklepallegro.connector.api.controller;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApiRequest<T> extends JsonRequest<T> {

    private static final String TAG = ApiRequest.class.getName();
    private static final String PROTOCOL_CHARSET = "utf-8";

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    private final Class classToParse;
    private final Response.Listener<T> listener;
    private String requestBody;

    private Map<String, String> headers;

    public ApiRequest(int method, String url, String requestBody, Class classToParse, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.classToParse = classToParse;
        this.listener = listener;
        if (requestBody != null) {
            this.requestBody = requestBody;
        }
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e(TAG, "Unsupported Encoding while trying to get the bytes of " + requestBody +
                    "using " + PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> headers = new HashMap<>();

            headers = response.headers;
            headers.remove("Content-Type");
            headers.put("Content-Type", "application/json; charset=utf-8");

            String json = new String(response.data, HttpHeaderParser.parseCharset(headers));

            return Response.success(gson.fromJson(json, classToParse), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            VolleyLog.e(TAG, e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null || headers.isEmpty()) {
            return super.getHeaders();
        } else {
            return headers;
        }
    }
}
