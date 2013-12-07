package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassSelectionActivity extends Activity {
    private String selectedSchoolName;
    //the JSON object representing the selected school. Contains the instructors and queues
    private JSONObject _jsonSchoolObject;
    private ExpandableListView _classQueueListView;
    private ClassExpandableListViewAdapter _classListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_selection_menu);

        _classQueueListView = (ExpandableListView)findViewById(R.id.class_expandablelistview);

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
        HashMap<String, List<String>> classNameItems = new HashMap<String, List<String>>();
        ArrayList<String> instructorNames = new ArrayList<String>();
        JSONArray jsonInstructorArray = null;
        try
        {
            //get the JSON array of the instructors
            jsonInstructorArray = _jsonSchoolObject.getJSONArray("instructors");
            for (int i = 0; i < jsonInstructorArray.length(); i++)
            {
                //names of all the classes this instructor has queues for
                ArrayList<String> classNamesList = new ArrayList<String>();

                //get the instructor JSON object
                JSONObject jsonInstructorObject = jsonInstructorArray.getJSONObject(i);

                //get the name of the instructor from the JSON object
                String instructorName = jsonInstructorObject.getString("name");
                instructorNames.add(instructorName);

                //get the class queues for the current professor
                JSONArray jsonClassesArray = jsonInstructorObject.getJSONArray("queues");
                for(int k = 0; k < jsonClassesArray.length(); k++)
                {
                    JSONObject jsonClassObject = new JSONObject(jsonClassesArray.getString(k));
                    classNamesList.add(jsonClassObject.getString("title"));
                }

                //now map the instructor with the list of classes
                classNameItems.put(instructorName, classNamesList);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        _classListViewAdapter = new ClassExpandableListViewAdapter(this, instructorNames, classNameItems);
        _classQueueListView.setAdapter(_classListViewAdapter);
    }


}
