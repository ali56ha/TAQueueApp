package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Populates the list view in the welcome screen with every school.
 *
 * Created by shong on 12/5/13.
 */
public class SchoolListViewAdapter extends ArrayAdapter<String>
{
    private Context _context;
    private int _resourceId;
    private ArrayList<String> _data;
    private JSONArray _jsonArray;
    private JSONObject _jsonSchoolObject;

    /**
     *
     * @param context
     * @param resourceID
     * @param data - names of the schools
     * @param jsonArray - the complete response of schools, instructors, and queues
     */
    public SchoolListViewAdapter(
            Context context,
            int resourceID,
            ArrayList<String> data,
            JSONArray jsonArray)
    {
        super(context, resourceID, data);

        _context = context;
        _resourceId = resourceID;
        _data = data;
        _jsonArray = jsonArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Button button;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            button = (Button)row.findViewById(R.id.schoolbutton);
            row.setTag(button);
        }
        else
        {
            button = (Button) row.getTag();
        }

        button.setText(_data.get(position));
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //launch the class list activity and pass in the name of the selected university
                Button button = (Button) v;
                //need to get the parent of the button which is the listview item which is a relative layout
                RelativeLayout buttonParent = (RelativeLayout) button.getParent();
                //need to get the grandparent of the button in order to get the index of the button's parent
                //which is the currently selected item.
                ListView buttonGrandParent = (ListView) buttonParent.getParent();

                //create the intent and store the name of the selected university
                Intent classListActivityIntent = new Intent(_context, ClassSelectionActivity.class);
                classListActivityIntent.putExtra("selected_school", button.getText());

                //now get the school object for the selected school; ex: if U of U button is clicked,
                //then grab the instructors and queues for the U of U
                try
                {
                    _jsonSchoolObject = _jsonArray.getJSONObject(buttonGrandParent.indexOfChild(buttonParent));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                //convert the selected school into a string that we store for the class selection activity
                String jsonArrayString = _jsonSchoolObject.toString();
                classListActivityIntent.putExtra("schools_json_object", jsonArrayString);
                _context.startActivity(classListActivityIntent);
            }
        });

        return row;

    }
}
