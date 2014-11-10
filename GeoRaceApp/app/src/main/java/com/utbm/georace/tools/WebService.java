package com.utbm.georace.tools;

import android.util.Log;

import com.utbm.georace.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojo on 03/11/2014.
 */
public class WebService  {

    private static WebService instance= null;

    private DefaultHttpClient httpClient;
    private HttpParams httpParams;
    private HttpPost httpPost;


    private WebService() {

        httpClient = new DefaultHttpClient();
        httpParams = new BasicHttpParams();
        httpPost = new HttpPost();

        HttpConnectionParams.setConnectionTimeout(httpParams, Config.Http.connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, Config.Http.socketTimeout);
        httpClient.setParams(httpParams);

    }

    public static WebService getInstance(){

        if(instance==null)instance=new WebService();

        return instance;

    }

    private boolean isResponseOk(HttpResponse resp) {

        HttpEntity httpEntity = resp.getEntity();
        StatusLine statusLine = resp.getStatusLine();
        Log.d("STATUS LINE", Integer.toString(statusLine.getStatusCode()));


        if (statusLine.getStatusCode() != HttpStatus.SC_OK)
            return false;

        String content_type = resp.getFirstHeader("content-type").getValue();

        if (!content_type.contains("application/json"))
            return false;


        return true;
    }


    //Interroge le webservice,
    // Si l'utilisateur est autorisé la fonction retourne l'obj User autorisé,
    // Sinon null
    public User getLogin(String name, String password) {

        try {

            httpPost.setURI(new URI(Config.Service.service_login));
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userLogin", name));
            param.add(new BasicNameValuePair("userPassword", password));

            Log.d("Login attempt :", name);

            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();

            if (isResponseOk(response)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpEntity.writeTo(out);
                out.close();

                Log.d("REQUEST RESULTS ", out.toString());
                JSONObject jsonObject = new JSONObject(out.toString());
                if (jsonObject.isNull("Status")) {
                    User user = new User(jsonObject);
                    Log.d("JSON TEXT", jsonObject.toString());
                    Log.d("JSON to JAVA object", user.getLoginName());

                    return user;

                } else {

                    Log.e("FAILED LOGIN ATTEMPT", "Accès non autorisé");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
