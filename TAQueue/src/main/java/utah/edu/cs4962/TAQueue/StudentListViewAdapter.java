package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shong on 12/8/13.
 */
public class StudentListViewAdapter extends ArrayAdapter<String>
{
    private ArrayList<String> _studentsInQueue;
    private HashMap<String, String> _studentToTaMap;
    private HashMap<String, Integer> _taColorMap;
    private HashMap<String, String> _studentToIdMap;
    private TAListViewAdapter _adapter;
    private ListView _tasInQueue;
    private Context _context;
    private int _resourceId;
    private ArrayList<Integer> _colors = new ArrayList<Integer>();
    private int _defaultColor;
    private boolean _isTa;
    private QueueClient _client;
    private String _taId;
    private String _taToken;

    //TODO add a hashmap of students to their ids
    //todo add the info for the TA in order to make requests
    public StudentListViewAdapter(
            Context context,
            int resource,
            ArrayList<String> students,
            HashMap<String, String> studentToTaMap,
            ListView tasInQueue,
            boolean isTA,
            String taId,
            String taToken,
            HashMap<String, String> studentToIdMap)
    {
        super(context, resource, students);
        _context = context;
        _studentsInQueue = students;
        _resourceId = resource;
        _isTa = isTA;
        _taId = taId;
        _taToken = taToken;
        _studentToIdMap = studentToIdMap;
        if(_isTa)
        {
            _client = QueueClientFactory.getInstance();
        }

        _colors.add(R.color.blue);
        _colors.add(R.color.green);
        _colors.add(R.color.orange);
        _colors.add(R.color.purple);
        _colors.add(R.color.teal);
        _colors.add(R.color.pink);

        _tasInQueue = tasInQueue;
        _adapter = (TAListViewAdapter)_tasInQueue.getAdapter();
        _taColorMap = _adapter.getColors();
        _studentToTaMap = studentToTaMap;
        _defaultColor = ((Activity) _context).getResources().getColor(R.color.activewhite);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Button studentName = null;
        //only used in the TA case
        TAQueueRow taQueueRow = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            if(_isTa)
            {
                taQueueRow = new TAQueueRow();
                taQueueRow.studentButton = (Button)row.findViewById(R.id.ta_queue_studentname_button);
                taQueueRow.removeButton = (Button)row.findViewById(R.id.ta_queue_remove_button);
                taQueueRow.putbackButton = (Button)row.findViewById(R.id.ta_queue_putback_button);
                row.setTag(taQueueRow);
            }
            else
            {
                studentName = (Button)row.findViewById(R.id.queue_row_button);
                row.setTag(studentName);
            }
        }
        else
        {
            if(_isTa)
            {
                taQueueRow = (TAQueueRow)row.getTag();
            }
            else
            {
                studentName = (Button) row.getTag();
            }

        }

        String student = _studentsInQueue.get(position);
        String ta = _studentToTaMap.get(student);
        int color = _defaultColor;
        if(ta != null)
            color = _taColorMap.get(ta);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, Color.BLACK);
        drawable.setColor(color);

        if(_isTa && taQueueRow != null)
        {
            taQueueRow.studentButton.setText(_studentsInQueue.get(position));
            taQueueRow.studentButton.setBackground(drawable);
            setupStudentButton(taQueueRow);
            setupRemoveButton(taQueueRow);
            setupPutbackButton(taQueueRow);
        }
        else
        {
            studentName.setText(_studentsInQueue.get(position));
            studentName.setBackground(drawable);
        }

        return row;
    }

    private void setupStudentButton(TAQueueRow taQueueRow)
    {
        taQueueRow.studentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button studentButton = (Button) v;
                String studentUsernameLocation = studentButton.getText().toString();
                //if the student is being helped by a TA already then don't accept the student
                if(_studentToTaMap.get(studentUsernameLocation) != null)
                    return;
                int index = studentUsernameLocation.indexOf("@");
                String studentUsername = studentUsernameLocation.substring(0, index);
                String studentId = _studentToIdMap.get(studentUsername);

                //TODO put together the GET request for accepting a student for this ta
                String url = "students/" + studentId + "/ta_accept.json";
                _client.removeAuthHeader();
                _client.authGet(_taId, _taToken, url, null, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        ((TAQueueActivity)_context).getQueueJSON();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to accept student 1", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
                    {
                        Log.d("failed to accept student 2 ", responseBody);
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to accept student 3 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to accept student 4 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to accept student 5 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to accept student 6 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                });

            }
        });

    }

    private void setupRemoveButton(TAQueueRow taQueueRow)
    {
        final TAQueueRow copyRow = taQueueRow;
        taQueueRow.removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                //TODO put together the GET request to remove teh student from teh TA's help
                String studentUsernameLocation = copyRow.studentButton.getText().toString();
                int index = studentUsernameLocation.indexOf("@");
                String studentUsername = studentUsernameLocation.substring(0, index);
                String studentId = _studentToIdMap.get(studentUsername);
                String url = "students/" + studentId + "/ta_remove.json";
                _client.removeAuthHeader();
                _client.authGet(_taId, _taToken, url, null, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        ((TAQueueActivity)_context).getQueueJSON();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to remove student 1", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
                    {
                        Log.d("failed to remove student 2 ", responseBody);
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to remove student 3 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to remove student 4 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to remove student 5 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to remove student 6 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                });
            }
        }

        );


    }

    private void setupPutbackButton(TAQueueRow taQueueRow)
    {
        final TAQueueRow copyRow = taQueueRow;
        taQueueRow.putbackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                //todo put together the GET request to put back the student in the queue
                String studentUsernameLocation = copyRow.studentButton.getText().toString();
                int index = studentUsernameLocation.indexOf("@");
                String studentUsername = studentUsernameLocation.substring(0, index);
                String studentId = _studentToIdMap.get(studentUsername);
                String url = "students/" + studentId + "/ta_putback.json";
                _client.removeAuthHeader();
                _client.authGet(_taId, _taToken, url, null, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        ((TAQueueActivity)_context).getQueueJSON();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to putback student 1", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
                    {
                        Log.d("failed to putback student 2 ", responseBody);
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to putback student 3 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to putback student 4 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("failed to putback student 5 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("failed to putback student 6 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                });
            }
        }

        );
    }

}
