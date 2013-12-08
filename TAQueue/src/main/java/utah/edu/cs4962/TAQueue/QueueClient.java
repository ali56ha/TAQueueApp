package utah.edu.cs4962.TAQueue;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.*;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by shong on 12/4/13.
 */
public class QueueClient
{
    private static final String BASE_URL = "http://nine.eng.utah.edu/";
    private static JSONArray _response = null;

    private AsyncHttpClient client = new AsyncHttpClient();

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);

    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
