package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private SchoolListViewAdapter schoolListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_menu);

        schoolListView = (ListView) findViewById(R.id.school_list_view);

        setupSchoolsList();
    }

    /**
     * Populates the list view in the welcome screen with buttons of each school.
     */
    private void setupSchoolsList()
    {
        _client.get("schools.json", null, new JsonHttpResponseHandler()
        {
            /**
             * If the GET request was successful then set up the school list
             * @param response - list of schools w/ instructors w/ queues
             */
            @Override
            public void onSuccess(JSONArray response)
            {
                //arraylist to send to the adapter to populate the views
                ArrayList<String> schoolNamesArray = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        //the school JSON object
                        JSONObject jsonSchoolObject = response.getJSONObject(i);
                        //the name of the school from the JSON object
                        schoolNamesArray.add(jsonSchoolObject.get("name").toString());
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                //create the adapter and send the JSONArray of the response as well so that we can
                //grab out the classes for the selected school later.
                schoolListViewAdapter = new SchoolListViewAdapter(context, R.layout.school_row, schoolNamesArray, response);
                schoolListView.setAdapter(schoolListViewAdapter);

            }
        });


    }

    public QueueClient getClient()
    {
        return _client;
    }

}
