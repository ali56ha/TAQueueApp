package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Controls the login for students and TAs
 */
public class QueueLoginActivity extends Activity
{
    private QueueClient _client;
    private String _schoolAbbrev;
    private String _classNumber;
    private String _instructorUsername;
    private String _className;
    private boolean _isTA = false;
    private String _id;
    private String _token;
    private TextView _errorTextView;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_login);

        _context = this;

        _errorTextView = (TextView) findViewById(R.id.error_banner);

        _client = QueueClientFactory.getInstance();

        Intent intent = getIntent();

        //grab the info from the previous activity so we can build the url from it
        _schoolAbbrev = intent.getStringExtra("school_abbrev");
        _classNumber = intent.getStringExtra("class_number");
        _className = intent.getStringExtra("class_name");
        _instructorUsername = intent.getStringExtra("instructor_username");

        TextView selectedQueueNameTextView = (TextView) findViewById(R.id.selected_queue_name);
        selectedQueueNameTextView.setText(_className);

        final EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
        final EditText pwdOrLocEditText = (EditText) findViewById(R.id.pwd_or_loc_edittext);
        final TextView pwdOrLocTextView = (TextView) findViewById(R.id.pwd_or_loc_textview);

        Button studentButton = (Button) findViewById(R.id.student_button);
        studentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pwdOrLocTextView.setText("Location");
                //show the location text
                pwdOrLocEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                _isTA = false;
            }
        });

        Button taButton = (Button) findViewById(R.id.ta_button);
        taButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pwdOrLocTextView.setText("Password");
                //hide the password
                pwdOrLocEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                _isTA = true;
            }
        });


        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //only let the user login if they have specified both the username and password fields.
                if (usernameEditText.getText().length() != 0 && pwdOrLocEditText.getText().length() != 0)
                {
                    loginUser(usernameEditText.getText().toString(), pwdOrLocEditText.getText().toString());
                }
                else
                {
                    if(_isTA)
                    {
                        _errorTextView.setText("Fill in username and password");
                    }
                    else
                    {
                        _errorTextView.setText("Fill in username and location");
                    }
                }
            }
        });

    }

    /**
     * Logs in the user student/TA into the selected queue
     * @param username
     * @param pwdOrLoc
     */
    private void loginUser(String username, String pwdOrLoc)
    {
        //the end part of the url which determines the type of user signing in.
        String studentOrTA;
        //params to send with the HTTP request
        RequestParams params = new RequestParams();

        //keep copies of the method params to use in the response handler
        final String _username = username;
        final String _location = pwdOrLoc;

        //format the request params based on type of user
        if (_isTA)
        {
            studentOrTA = "/tas.json";
            params.add("ta[username]", username);
            params.add("ta[password]", pwdOrLoc);
        } else
        {
            studentOrTA = "/students.json";
            params.add("student[username]", username);
            params.add("student[location]", pwdOrLoc);
        }

        //build the url for the POST request that creates the new user and logs them into the queue.
        String url = "schools/" + _schoolAbbrev + "/" + _instructorUsername + "/" + _classNumber + studentOrTA;

        _client.post(url, params, new JsonHttpResponseHandler()
        {
            /**
             * Successful user login request
             * @param response
             */
            @Override
            public void onSuccess(JSONObject response)
            {
                try
                {
                    //get the id and token returned
                    _id = response.getString("id");
                    _token = response.getString("token");

                    //go to the TA queue activity
                    if(_isTA)
                    {
                        Intent queueActivity = new Intent(_context, TAQueueActivity.class);
                        queueActivity.putExtra("id", _id);
                        queueActivity.putExtra("token", _token);
                        queueActivity.putExtra("class_number", _classNumber);
                        queueActivity.putExtra("instructor_username", _instructorUsername);
                        queueActivity.putExtra("school_abbrev", _schoolAbbrev);
                        queueActivity.putExtra("username", _username);
                        ((Activity)_context).startActivityForResult(queueActivity, 1);
                    }
                    else //go to the student activity
                    {
                        Intent queueActivity = new Intent(_context, StudentQueueActivity.class);
                        queueActivity.putExtra("id", _id);
                        queueActivity.putExtra("token", _token);
                        queueActivity.putExtra("class_number", _classNumber);
                        queueActivity.putExtra("instructor_username", _instructorUsername);
                        queueActivity.putExtra("school_abbrev", _schoolAbbrev);
                        queueActivity.putExtra("username", _username);
                        queueActivity.putExtra("location", _location);
                        ((Activity)_context).startActivityForResult(queueActivity, 1);
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            /**
             * What is displayed upon failure of the user login request
             * @param statusCode
             * @param e
             * @param errorResponse
             */
            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
            {
                try
                {
                    JSONArray jsonArray = errorResponse.getJSONArray("errors");
                    String msg = "";
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        msg += jsonArray.getString(i) + " ";
                    }
                    _errorTextView.setText(msg);
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        _client = QueueClientFactory.getInstance();
        //clear any error messages that were set
        _errorTextView.setText("");
    }


}
