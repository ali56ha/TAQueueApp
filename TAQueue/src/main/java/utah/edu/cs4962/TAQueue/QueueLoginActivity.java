package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
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
    private boolean isTA = false;
    private String _id;
    private String _token;
    private TextView _errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_login);

        _errorTextView = (TextView)findViewById(R.id.error_banner);

        _client = QueueClientFactory.getInstance();

        Intent intent = getIntent();

        _schoolAbbrev = intent.getStringExtra("school_abbrev");
        _classNumber = intent.getStringExtra("class_number");
        _className = intent.getStringExtra("class_name");
        _instructorUsername = intent.getStringExtra("instructor_username");

        TextView selectedQueueNameTextView = (TextView) findViewById(R.id.selected_queue_name);
        selectedQueueNameTextView.setText(_className);

        final EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
        final EditText pwdOrLocEditText = (EditText) findViewById(R.id.pwd_or_loc_edittext);

        final TextView pwdOrLocTextView = (TextView)findViewById(R.id.pwd_or_loc_textview);

        Button studentButton = (Button)findViewById(R.id.student_button);
        studentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pwdOrLocTextView.setText("Location");
                isTA = false;
            }
        });

        Button taButton = (Button) findViewById(R.id.ta_button);
        taButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pwdOrLocTextView.setText("Password");
                isTA = true;
            }
        });


        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(usernameEditText.getText().length() != 0 && pwdOrLocEditText.getText().length() != 0)
                {
                    loginUser(usernameEditText.getText().toString(), pwdOrLocEditText.getText().toString());
                }
            }
        });

    }

    private void loginUser(String username, String pwdOrLoc)
    {
        String studentOrTA;
        RequestParams params = new RequestParams();
        JSONObject jsonCredentialsObject = new JSONObject();
        JSONObject jsonUserObject = new JSONObject();
        try
        {
            jsonCredentialsObject.put("username", username);

            if(isTA)
            {
                studentOrTA = "/tas";
                jsonCredentialsObject.put("password", pwdOrLoc);
                jsonUserObject.put("ta", jsonCredentialsObject);
                params.add("Authentication:", pwdOrLoc);
            }
            else
            {
                studentOrTA = "/students";
                jsonCredentialsObject.put("location", pwdOrLoc);
                jsonUserObject.put("student", jsonCredentialsObject);
                params.add("Authentication:", "None");
            }

            String url = "schools/" + _schoolAbbrev + "/" + _instructorUsername + "/" + _classNumber + studentOrTA;

//            params
            params.add("Parameters:", jsonUserObject.toString());

            _client.post(url, params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONObject response)
                {
                    try
                    {
                        _id = response.getString("id");
                        _token = response.getString("token");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    Log.d("logged in successfully", "suceessfull");
                }

                @Override
                public  void onFailure(int statusCode, Throwable e, JSONObject errorResponse)
                {
                    String errorMsg = null;
                    try
                    {
                        errorMsg = errorResponse.getString("errors");
                    } catch (JSONException e1)
                    {
                        e1.printStackTrace();
                    }
                    _errorTextView.setText(errorMsg);
                }
            });

            if(_id != null && _token != null)
            {
                Intent queueActivity = new Intent(this, QueueActivity.class);
                queueActivity.putExtra("id", _id);
                queueActivity.putExtra("token", _token);
                queueActivity.putExtra("isTA", isTA);
                this.startActivity(queueActivity);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }


}
