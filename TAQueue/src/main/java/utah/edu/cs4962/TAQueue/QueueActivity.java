package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class QueueActivity extends Activity
{
    private boolean _isTA;
    private String _id;
    private String _token;
    private QueueClient _client;
    private ListView _studentsInQueue;
    private ListView _tasInQueue;
    private boolean _isFrozen = true;
    private boolean _isActive = false;
    private boolean _inQueue = false;
    private EditText _queueMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final RelativeLayout queueScreen = (RelativeLayout)findViewById(R.layout.queue_screen);
        setContentView(queueScreen);

        _client = QueueClientFactory.getInstance();
        //TODO get the info about the current state of the queue

        Intent intent = getIntent();

        _isTA = intent.getBooleanExtra("isTA", false);
        _id = intent.getStringExtra("id");
        _token = intent.getStringExtra("token");
        //TODO grab the freeze state

        Button signOutButton = (Button)findViewById(R.id.signout_button);
        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO set a destroy request to the server
                //TODO switch views to the login screen

            }
        });

        Button exitOrFreezeButton = (Button)findViewById(R.id.freeze_exit_button);
        if(_isTA)
        {
            if(_isFrozen)
            {
                exitOrFreezeButton.setText("Unfreeze");
            }
            else
            {
                exitOrFreezeButton.setText("Freeze");
            }
        }
        else
        {
            exitOrFreezeButton.setText("Exit Queue");
        }
        exitOrFreezeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO freeze/unfreeze the queue; send request to server to update queue state
                if(_isTA)
                {
                    if(_isActive)
                    {
                        if(_isFrozen)
                        {
                            //TODO unfreeze the queue
                            _isFrozen = false;
                            queueScreen.setBackgroundColor(getResources().getColor(R.color.freezeblue));
                        }
                        else
                        {
                            //TODO freeze the queue
                            _isFrozen = true;
                            queueScreen.setBackgroundColor(getResources().getColor(R.color.activewhite));
                        }
                    }
                    else
                    {
                        queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
                    }
                }
                //TODO exit the queue; send a PUT request to remove the student from the queue
                else
                {
                    if(_inQueue)
                    {
                        //TODO allow the student to exit
                    }
                }
            }
        });

        Button enterOrDeactivateButton = (Button)findViewById(R.id.enter_deactivate_button);
        if(_isTA)
        {
            if(_isActive)
            {
                enterOrDeactivateButton.setText("Deactivate");
            }
            else
            {
                enterOrDeactivateButton.setText("Activate");
                queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
            }
        }
        else
        {
            enterOrDeactivateButton.setText("Enter Queue");
        }
        enterOrDeactivateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(_isTA)
                {
                    if(_isActive)
                    {
                        //TODO deactivate the queue
                        _isActive = false;
                        queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
                    }
                    else
                    {
                        //TODO activate the queue
                        _isActive = true;
                        queueScreen.setBackgroundColor(getResources().getColor(R.color.activewhite));
                    }
                }
                else
                {
                    if(_isActive)
                    {
                        //TODO enter the queue
                    }
                }
            }
        });

        _studentsInQueue = (ListView)findViewById(R.id.students_in_queue_listview);
        //TODO generate data to pass into the adapter
        _studentsInQueue.setAdapter(new StudentListViewAdapter(this, R.id.students_in_queue_listview, null));
        //if the user is a TA, then allow deletions to be performed on student items.
        if(_isTA)
        {
            //TODO set swipe dismiss listener to remove/put back items in the listview
        }

        _tasInQueue = (ListView)findViewById(R.id.tas_in_queue_listview);
        //TODO generate data to pass into the adapter
        _tasInQueue.setAdapter(new TAListViewAdapter(this, R.id.tas_in_queue_listview, null));

        _queueMessageEditText = (EditText)findViewById(R.id.queue_message_edittext);
        if(_isTA)
        {
            //TODO broadcast the message set to the server to be picked up by all clients
        }
    }

    private void getQueueState()
    {

    }


}
