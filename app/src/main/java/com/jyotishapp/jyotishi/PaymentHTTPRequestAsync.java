package com.jyotishapp.jyotishi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentHTTPRequestAsync extends AsyncTask<String, Void, Boolean> {

    String json="", postResponse="", getResponse="";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            json = jsonData("Ushtun", "Geeshu");
            postResponse = doPostRequest("http://www.roundsapp.com/post", json);
            Log.v("AAAAA", "POST "+ postResponse);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            getResponse = doGetRequest("https://www.vogella.com/");
            Log.v("AAAA", "GET " + getResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    void checkSum(){
        TreeMap<String, String> paytmParams = new TreeMap<>();
        paytmParams.put("MID", "kPXHkA06345429779966");
        paytmParams.put("ORDERID", "jyotish_1");
//        String checkSum = CheckSumServiceHelper.
    }

    private String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request)
                .execute();
        return response.body().string();
    }

    private String jsonData(String p1, String p2){
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + p1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + p2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    private String doPostRequest(String url, String json) throws IOException{
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }
}
