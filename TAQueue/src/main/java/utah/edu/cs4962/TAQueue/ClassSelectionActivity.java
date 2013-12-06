package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClassSelectionActivity extends Activity {
    private String selectedSchoolName;
    private JSONObject _jsonSchoolObject;
    private ListView _classQueueListView;
    private ClassListViewAdapter _classListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_selection_menu);

        _classQueueListView = (ListView)findViewById(R.id.class_list_view);

        //display the name of the selected university
        TextView selectedClassNameTextView = (TextView)findViewById(R.id.selected_class_name);
        Intent intent = getIntent();
        selectedSchoolName = intent.getStringExtra("selected_school");

        //now get the JSON containing info about the queues
        selectedClassNameTextView.setText("Queues for " + selectedSchoolName);
        try
        {
            _jsonSchoolObject = new JSONObject(intent.getStringExtra("schools_json_object"));
            //create the list view of the queues for this university
            setUpClassesList();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Populates the list view with information about the queues for this university.
     */
    private void setUpClassesList()
    {
        //holds JSON strings for each JSON object
        ArrayList<String> classJSONInstructorQueuesArray = new ArrayList<String>();
        JSONArray jsonInstructorArray = null;
        try
        {
            jsonInstructorArray = _jsonSchoolObject.getJSONArray("instructors");
            for (int i = 0; i < jsonInstructorArray.length(); i++)
            {
                classJSONInstructorQueuesArray.add(jsonInstructorArray.getJSONObject(i).toString());
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        _classListViewAdapter = new ClassListViewAdapter(this, R.layout.class_row, classJSONInstructorQueuesArray);
        _classQueueListView.setAdapter(_classListViewAdapter);
    }


}
