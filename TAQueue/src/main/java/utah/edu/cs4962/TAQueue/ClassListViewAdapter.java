package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shong on 12/5/13.
 */
public class ClassListViewAdapter extends ArrayAdapter<String>
{
    private ArrayList<String> _data;
    private Context _context;
    private int _resourceId;
    private JSONArray _jsonInstructorsArray;


    public ClassListViewAdapter(Context context, int resource, ArrayList<String> data)
    {
        super(context, resource, data);

        _context = context;
        _resourceId = resource;
        _data = data;
        _jsonInstructorsArray = new JSONArray(_data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        TextView textView;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            textView = (TextView)row.findViewById(R.id.class_list_textview);
            row.setTag(textView);
        }
        else
        {
            textView = (TextView) row.getTag();
        }

        try
        {
            JSONObject jsonInstructorObject = _jsonInstructorsArray.getJSONObject(position);
            textView.setText(jsonInstructorObject.getString("name"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

//        textView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //launch the class list activity and pass in the name of the selected university
//                Button button = (Button) v;
//                Intent classListActivityIntent = new Intent(_context, ClassSelectionActivity.class);
//                classListActivityIntent.putExtra("selected_school", button.getText());
//                String jsonArrayString = _jsonSchoolObject.toString();
//                classListActivityIntent.putExtra("schools_json_object", jsonArrayString);
//                ((Activity) _context).startActivityForResult(classListActivityIntent, 1);
//            }
//        });

        return row;

    }
}
