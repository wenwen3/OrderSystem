package com.yalkansoft.utils;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestUtils {

    public static RequestUtils instance;

    public static RequestUtils getInstance(){
        if(instance == null){
            instance = new RequestUtils();
        }

        return instance;
    }
    private int timeout;
    private int bufferSize;
    private String encode;
    private HttpParams httpParams;
    public void init(){
        this.timeout = 20000;
        this.bufferSize = 8192;
        this.encode = "UTF-8";
        HttpConnectionParams.setConnectionTimeout(this.httpParams = (HttpParams)new BasicHttpParams(), this.timeout);
        HttpConnectionParams.setSoTimeout(this.httpParams, this.timeout);
        HttpConnectionParams.setSocketBufferSize(this.httpParams, this.bufferSize);
        HttpClientParams.setRedirecting(this.httpParams, true);
        this.httpClient = (HttpClient)new DefaultHttpClient(this.httpParams);
    }

    public void request(Map<String,String> requestParamsMap, String requestUrl, RequestCallBack<String> requestCallBack){
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
//        requestParams.setHeader();
        if(requestParamsMap != null ){
            for (Map.Entry<String,String> mEntries: requestParamsMap.entrySet()) {
                requestParams.addBodyParameter(mEntries.getKey(),mEntries.getValue());
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST,requestUrl,requestParams,requestCallBack);
    }
    private HttpClient httpClient;
    public String doGet(String string, final Map<String, String> map){
        String string2 = "";
        while (true) {
            Label_0170: {
                Label_0065: {
                    if (map == null) {
                        break Label_0065;
                    }
                    final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                    if (iterator.hasNext()) {
                        break Label_0170;
                    }
                    if (string2.length() > 0) {
                        string2.replaceFirst("&", "?");
                    }
                    string = String.valueOf(string) + string2;
                }
                final HttpGet httpGet = new HttpGet(string);
                try {
                    final HttpResponse execute = this.httpClient.execute((HttpUriRequest)httpGet);
                    if (execute.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(execute.getEntity());
                    }
                    continue;

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    httpGet.abort();
                }
            }
            break;
        }
        return "";
    }

    public String doPost(final String s, final Map<String, String> map) throws Exception {
        final ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        while (true) {
            Label_0212: {
                Label_0035: {
                    if (map == null) {
                        break Label_0035;
                    }
                    final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                    if (iterator.hasNext()) {
                        break Label_0212;
                    }
                }
                final HttpPost httpPost = new HttpPost(s);
                Label_0064: {
                    if (map == null) {
                        break Label_0064;
                    }
                    try {
                        httpPost.setEntity((HttpEntity)new UrlEncodedFormEntity((List)list, "UTF-8"));
                        Log.v("WebClient", "doPost: " + s);
                        Log.v("WebClient", "HttpPost: " + httpPost);
                        Log.v("WebClient", "data: " + list.toString());
                        final HttpResponse execute = this.httpClient.execute((HttpUriRequest)httpPost);
                        if (execute.getStatusLine().getStatusCode() == 200) {
                            final String string = EntityUtils.toString(execute.getEntity());
                            Log.v("WebClient", "strResp: " + string);
                            return string;
                        }
                        continue;
                    }
                    finally {
                        httpPost.abort();
                    }
                }
            }
            break;
        }
        return "";
    }
}
