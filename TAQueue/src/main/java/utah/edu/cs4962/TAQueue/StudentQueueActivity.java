package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class StudentQueueActivity extends Activity
{
    private QueueClient _client;

    private String _id;
    private String _token;
    private String _classNumber;
    private String _username;
    private String _location;
    private String _instructorUsername;

    private boolean _isFrozen = false;
    private boolean _isActive = false;
//    private boolean _isQuestionBased = false;
    //whether or not the user is in the queue
    private boolean _inQueue = false;

    private ListView _studentsInQueueListView;
    private ListView _tasInQueueListView;
    private TextView _queueMessageTextView;
    private TextView _selectedClassNumberTextView;
    //use this to change the background to indicate if the queue is active.
    private RelativeLayout _queueScreen;
    private Button _enterQueueButton;
    private Button _exitQueueButton;
    private Button _signOutButton;

    private int _deactivatedColor;
    private int _activeColor;
    private int _frozenColor;
    private int _inactivieTextColor;
    private int _activeTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_queue_menu);

        _queueScreen = (RelativeLayout)findViewById(R.id.student_q_relativelayout);
        _exitQueueButton = (Button) findViewById(R.id.student_q_exit_button);
        _queueMessageTextView = (TextView)findViewById(R.id.student_q_msg_banner);
        _enterQueueButton = (Button) findViewById(R.id.student_q_enter_button);
        _studentsInQueueListView = (ListView) findViewById(R.id.student_q_students_listview);
        _tasInQueueListView = (ListView) findViewById(R.id.student_q_tas_listview);

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
        _location = intent.getStringExtra("location");
        _instructorUsername = intent.getStringExtra("instructor_username");
        _classNumber = intent.getStringExtra("class_number");

        _selectedClassNumberTextView = (TextView)findViewById(R.id.student_q_class_name_banner);
        _selectedClassNumberTextView.setText(_classNumber);

        setUpSignOutButton();
        setUpExitButton();
        setUpEnterQueueButton();

        //now get the state of the queue
        getQueueJSON();

    }

    private void getQueueJSON()
    {
        String url = "/queue.json";
        _client.setBasicAuth(_id, _token);
        _client.authGet(_id, _token, url, null, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                try
                {
                    _isFrozen = response.getBoolean("frozen");
                    _isActive = response.getBoolean("active");
//                    _isQuestionBased = response.getBoolean("is_question_based");
                    setupMessageBanner(response.getString("status"));

                    //set up the color of the queue based on the queue state
                    if (_isActive)
                    {
                        if (_isFrozen)
                        {
                            _queueScreen.setBackgroundColor(_frozenColor);
                        } else
                        {
                            _queueScreen.setBackgroundColor(_activeColor);
                        }
                    } else
                    {
                        _queueScreen.setBackgroundColor(_deactivatedColor);
                    }

                    //get the ta info
                    JSONArray tasInQueue = response.getJSONArray("tas");
                    setupTasListView(tasInQueue);

                    //get the student info
                    JSONArray studentsInQueue = response.getJSONArray("students");
                    setupStudentsListView(studentsInQueue);

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
        });
    }

    private void setupMessageBanner(String msg)
    {
        //if there is a message display it, otherwise the default one will be displayed
        if(msg != null)
        {
            _queueMessageTextView.setText(msg);
            _queueMessageTextView.setTextColor(_activeTextColor);
        }
        else
        {
            _queueMessageTextView.setTextColor(_inactivieTextColor);
        }
    }

    private void setUpSignOutButton()
    {
        _signOutButton = (Button) findViewById(R.id.student_q_signout_button);
        _signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO set a destroy request to the server
                String url = "/students/" + _id + ".json";
                _client.setBasicAuth(_id, _token);
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
                            Log.d("student signout failed", errorResponse.getString("errors"));
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setUpExitButton()
    {
        _exitQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO exit the queue
                if(_inQueue)
                {
                    String url = "/queue/exit_queue.json";
                    _client.authGet(_id, _token, url, null, new JsonHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(JSONObject response)
                        {
                            //TODO see if it is possible to get the updated state of the queue
                            _inQueue = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                        {
                            try
                            {
                                Log.d("student exit queue failed", errorResponse.getString("errors"));
                            } catch (JSONException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }

    private void setUpEnterQueueButton()
    {
        _enterQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //only let the student enter the queue if the queue is active and not frozen
                if (_isActive && !_isFrozen && !_inQueue)
                {
                    String url = "/queue/enter_queue.json";

                    _client.authGet(_id, _token, url, null, new JsonHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(JSONObject response)
                        {
                            //TODO see if the response gives the updated state so we don't have to manually do it ourselves
                            //get the current queue state
                            getQueueJSON();
                            _inQueue = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                        {
                            try
                            {
                                Log.d("student enter queue failed", errorResponse.getString("errors"));
                            } catch (JSONException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void setupStudentsListView(JSONArray jsonArray)
    {
        ArrayList<String> studentsInQueueData = new ArrayList<String>();
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
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        StudentListViewAdapter adapter = new StudentListViewAdapter(this, R.id.student_q_students_listview, studentsInQueueData);
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
        _tasInQueueListView.setAdapter(new TAListViewAdapter(this, R.id.student_q_tas_listview, tasInQueueData));
    }


}
