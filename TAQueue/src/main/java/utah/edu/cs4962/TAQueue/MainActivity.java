package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private QueueClient _client = new QueueClient();
    private ListView schoolListView = null;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_menu);

        schoolListView = (ListView) findViewById(R.id.school_list_view);

        setupSchoolsList();
    }

    public void setupSchoolsList()
    {
        _client.get("schools.json", null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONArray response)
            {
                ArrayList<String> schoolNamesArray = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++)
                {
                    TextView text = new TextView(context);
                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        schoolNamesArray.add(jsonObject.get("name").toString());
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout., schoolNamesArray);
                schoolListView.setAdapter(adapter);

            }
        });


    }

}
