package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TAQueueActivity extends Activity
{
    private QueueClient _client;
    private Timer _timer;
    private Context _context;

    private String _id;
    private String _token;
    private String _classNumber;
    private String _username;
    private String _instructorUsername;

    private boolean _isFrozen = false;
    private boolean _isActive = false;

    private ListView _studentsInQueueListView;
    private ListView _tasInQueueListView;
    private EditText _queueMessageEditText;
    private TextView _selectedClassNumberTextView;
    //use this to change the background to indicate if the queue is active.
    private RelativeLayout _queueScreen;
    private Button _deactivateQueueButton;
    private Button _freezeQueueButton;
    private Button _signOutButton;
    private GestureDetector _gestureDetector;

    private int _deactivatedColor;
    private int _activeColor;
    private int _frozenColor;
    private int _inactivieTextColor;
    private int _activeTextColor;
    private int _newStudentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ta_queue_menu);



        _queueScreen = (RelativeLayout)findViewById(R.id.ta_q_relativelayout);
        _freezeQueueButton = (Button) findViewById(R.id.ta_q_freeze_button);
        _queueMessageEditText= (EditText)findViewById(R.id.ta_q_msg_banner);
        _deactivateQueueButton = (Button) findViewById(R.id.ta_q_deactivate_button);
        _studentsInQueueListView = (ListView) findViewById(R.id.ta_q_students_listview);
        _tasInQueueListView = (ListView) findViewById(R.id.ta_q_tas_listview);

        _deactivatedColor = getResources().getColor(R.color.deactivatered);
        _frozenColor = getResources().getColor(R.color.freezeblue);
        _activeColor = getResources().getColor(R.color.activewhite);
        _activeTextColor = getResources().getColor(R.color.activetextgrey);
        _inactivieTextColor = getResources().getColor(R.color.inactivetextgrey);

        _client = QueueClientFactory.getInstance();

        Intent intent = getIntent();

        _id = intent.getStringExtra("id");
        _token = intent.getStringExtra("token");
        _username = intent.getStringExtra("username");
        _instructorUsername = intent.getStringExtra("instructor_username");
        _classNumber = intent.getStringExtra("class_number");

        _gestureDetector = new GestureDetector(new GestureListener(_id, _token));

        _client.setBasicAuth(_id, _token);

        _selectedClassNumberTextView = (TextView)findViewById(R.id.ta_q_class_name_banner);
        _selectedClassNumberTextView.setText(_classNumber);

        setUpSignOutButton();
        setUpFreezeButton();
        setUpDeactivateQueueButton();

        _context = this;

        _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity)_context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.d("ran timer", "ran timer");
                        getQueueJSON();
                    }
                });
            }

        }, 0, 10000);
    }

    private void setupMessageBanner(String msg)
    {
        //if there is a message display it, otherwise the default one will be displayed
        if(msg != null && msg.trim().length() != 0)
        {
            _queueMessageEditText.setText(msg);
            _queueMessageEditText.setTextColor(_activeTextColor);
            RequestParams params = new RequestParams();
            params.put("queue[status]", msg);
            _client.addAuthHeader(_id, _token);
            _client.put("queue.json", params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONObject response)
                {
                    getQueueJSON();
                }

                @Override
                public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                {
                    try
                    {
                        Log.d("banner msg update failed 1", errorResponse.getString("errors"));
                    } catch (JSONException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
                {
                    Log.d("banner msg update failed 1", responseBody);
                }
                @Override
                public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
                {
                    Log.d("banner msg update failed 1", errorResponse.toString());
                }
                @Override
                public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
                {
                    try
                    {
                        Log.d("banner msg update failed 1", errorResponse.getString("errors"));
                    } catch (JSONException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                @Override
                public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
                {
                    Log.d("banner msg update failed 1", errorResponse.toString());
                }
                @Override
                public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
                {
                    try
                    {
                        Log.d("banner msg update failed 1", errorResponse.getString("errors"));
                    } catch (JSONException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            });
        }
        else
        {
            _queueMessageEditText.setTextColor(_inactivieTextColor);
        }
    }

    private void getQueueJSON()
    {
        String url = "queue.json";
        _client.removeAuthHeader();
        _client.authGet(_id, _token, url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                try
                {
                    _isFrozen = response.getBoolean("frozen");
                    _isActive = response.getBoolean("active");
                    setupMessageBanner(response.getString("status"));

                    //set up the color of the queue based on the queue state
                    if (_isActive)
                    {
                        if (_isFrozen)
                        {
                            _queueScreen.setBackgroundColor(_frozenColor);
                            _freezeQueueButton.setText("Unfreeze");
                        } else
                        {
                            _queueScreen.setBackgroundColor(_activeColor);
                            _freezeQueueButton.setText("Freeze");
                            _deactivateQueueButton.setText("Deactivate");
                        }
                    } else
                    {
                        _queueScreen.setBackgroundColor(_deactivatedColor);
                        _deactivateQueueButton.setText("Activate");
                    }

                    //get the ta info
                    JSONArray tasInQueue = response.getJSONArray("tas");
                    setupTasListView(tasInQueue);

                    //get the student info
                    JSONArray studentsInQueue = response.getJSONArray("students");
                    setupStudentsListView(studentsInQueue, tasInQueue);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
            {
                try
                {
                    Log.d("failed to get queue", errorResponse.getString("errors"));
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
            {
                Log.d("failed to get queue 2 ", responseBody);
            }
            @Override
            public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
            {
                Log.d("failed to get queue 3 ", errorResponse.toString());
            }
            @Override
            public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
            {
                try
                {
                    Log.d("failed to get queue 4 ", errorResponse.getString("errors"));
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
            @Override
            public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
            {
                Log.d("failed to get queue 5 ", errorResponse.toString());
            }
            @Override
            public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
            {
                try
                {
                    Log.d("failed to get queue 6 ", errorResponse.getString("errors"));
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setUpSignOutButton()
    {
        _signOutButton = (Button) findViewById(R.id.ta_q_signout_button);
        _signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO set a destroy request to the server
                String url = "tas/" + _id + ".json";
                _client.removeAuthHeader();
                _client.authDelete(_id, _token, url, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject result)
                    {
                        //TODO switch views to the login screen
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("ta signout failed 1 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e)
                    {
                            Log.d("ta signout failed 2 ", responseBody);
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                            Log.d("ta signout failed 3 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("ta signout failed 4 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONArray errorResponse)
                    {
                        Log.d("ta signout failed 5 ", errorResponse.toString());
                    }
                    @Override
                    public void	onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("ta signout failed 6 ", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                });
            }
        });
    }

    private void setUpFreezeButton()
    {
        _freezeQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RequestParams params = new RequestParams();
                params.put("queue[frozen]", Boolean.toString(!_isFrozen));
                _client.addAuthHeader(_id, _token);
                _client.put("queue.json", params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        //TODO see if it is possible to get the updated state of the queue
                        _isFrozen = !_isFrozen;
                        getQueueJSON();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("ta freeze queue failed", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setUpDeactivateQueueButton()
    {
        _deactivateQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RequestParams params = new RequestParams();
                params.put("queue[active]", Boolean.toString(!_isActive));
                _client.addAuthHeader(_id, _token);
                _client.put("queue.json", params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        //TODO see if the response gives the updated state so we don't have to manually do it ourselves
                        //get the current queue state
                        getQueueJSON();
                        _isActive = !_isActive;
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                    {
                        try
                        {
                            Log.d("ta deactivate failed", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setupStudentsListView(JSONArray jsonArray, JSONArray jsonTaArray)
    {
        ArrayList<String> studentsInQueueData = new ArrayList<String>();
        HashMap<String, String> studentToTAMap = new HashMap<String, String>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                JSONObject jsonStudentObject = jsonArray.getJSONObject(i);
                String student = jsonStudentObject.getString("username") + "@" + jsonStudentObject.getString("location");
                boolean inQueue = jsonStudentObject.getBoolean("in_queue");
                if(inQueue)
                {
                    studentsInQueueData.add(student);
                    studentToTAMap.put(jsonStudentObject.getString("ta_id"), student);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        studentToTAMap = getStudentToTaMap(jsonTaArray);
        StudentListViewAdapter adapter = new StudentListViewAdapter(
                this,
                R.layout.ta_queue_row,
                studentsInQueueData,
                studentToTAMap,
                _tasInQueueListView,
                true);
        _studentsInQueueListView.setAdapter(adapter);
    }

    private void setupTasListView(JSONArray jsonArray)
    {
        //TODO generate data to pass into the adapter
        ArrayList<String> tasInQueueData = new ArrayList<String>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                JSONObject jsonTaObject = jsonArray.getJSONObject(i);
                String ta = jsonTaObject.getString("username");
                tasInQueueData.add(ta);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        TAListViewAdapter adapter = new TAListViewAdapter(this, R.layout.queue_row, tasInQueueData);
        _tasInQueueListView.setAdapter(adapter);
    }

    private HashMap<String, String> getStudentToTaMap(JSONArray jsonArray)
    {
        HashMap<String, String> studentToTAMap = new HashMap<String, String>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                JSONObject jsonTaObject = jsonArray.getJSONObject(i);
                String ta = jsonTaObject.getString("username");
                JSONObject jsonStudentObject = jsonTaObject.getJSONObject("student");
                String student = jsonStudentObject.getString("username") + "@" + jsonStudentObject.getString("location");
                studentToTAMap.put(student, ta);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return studentToTAMap;
    }




}
