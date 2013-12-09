package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class TAQueueActivity extends Activity
{
    private String _id;
    private String _token;
    private QueueClient _client;
    private ListView _studentsInQueue;
    private ListView _tasInQueue;
    private boolean _isFrozen = true;
    private boolean _isActive = false;
    private EditText _queueMessageEditText;
    private RelativeLayout _queueScreen;
    private Button _signOutButton;
    private Button _freezeButton;
    private Button _deactivateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ta_queue_menu);

        _client = QueueClientFactory.getInstance();
        //TODO get the info about the current state of the queue

        Intent intent = getIntent();

        _id = intent.getStringExtra("id");
        _token = intent.getStringExtra("token");

        _queueMessageEditText = (EditText) findViewById(R.id.queue_message_edittext);
        //TODO broadcast the message set to the server to be picked up by all clients
    }

    private void setUpSignOutButton()
    {
        _signOutButton = (Button) findViewById(R.id.signout_button);
        _signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO set a destroy request to the server
                //TODO switch views to the login screen

            }
        });
    }

    private void setUpFreezeButton()
    {
        _freezeButton = (Button) findViewById(R.id.freeze_exit_button);
        if (_isFrozen)
        {
            _freezeButton.setText("Unfreeze");
        } else
        {
            _freezeButton.setText("Freeze");
        }
        _freezeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO freeze/unfreeze the queue; send request to server to update queue state
                if (_isActive)
                {
                    if (_isFrozen)
                    {
                        //TODO unfreeze the queue
                        _isFrozen = false;
                        _queueScreen.setBackgroundColor(getResources().getColor(R.color.freezeblue));
                    } else
                    {
                        //TODO freeze the queue
                        _isFrozen = true;
                        _queueScreen.setBackgroundColor(getResources().getColor(R.color.activewhite));
                    }
                } else
                {
                    _queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
                }
            }
        });
    }

    private void setUpDeactivateButton()
    {
        _deactivateButton = (Button) findViewById(R.id.enter_deactivate_button);

        if (_isActive)
        {
            _deactivateButton.setText("Deactivate");
        } else
        {
            _deactivateButton.setText("Activate");
            _queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
        }

        _deactivateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_isActive)
                {
                    //TODO deactivate the queue
                    _isActive = false;
                    _queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
                } else
                {
                    //TODO activate the queue
                    _isActive = true;
                    _queueScreen.setBackgroundColor(getResources().getColor(R.color.activewhite));
                }

            }
        });
    }

    private void getStudents()
    {
        _studentsInQueue = (ListView) findViewById(R.id.students_in_queue_listview);
        //TODO generate data to pass into the adapter
        _studentsInQueue.setAdapter(new StudentListViewAdapter(this, R.id.students_in_queue_listview, null));
        //if the user is a TA, then allow deletions to be performed on student items.

        //TODO set swipe dismiss listener to remove/put back items in the listview
    }

    private void getTas()
    {
        _tasInQueue = (ListView) findViewById(R.id.tas_in_queue_listview);
        //TODO generate data to pass into the adapter
        _tasInQueue.setAdapter(new TAListViewAdapter(this, R.id.tas_in_queue_listview, null));
    }




}
