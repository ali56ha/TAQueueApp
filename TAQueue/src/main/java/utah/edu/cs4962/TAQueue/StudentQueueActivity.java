package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class StudentQueueActivity extends Activity
{
    private String _id;
    private String _token;
    private QueueClient _client;
    private ListView _studentsInQueue;
    private ListView _tasInQueue;
    private boolean _isFrozen = true;
    private boolean _isActive = false;
    private boolean _inQueue = false;
    private EditText _queueMessageEditText;
    private RelativeLayout _queueScreen;
    private Button _enterQueueButton;
    private Button _exitQueueButton;
    private Button _signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_queue_menu);

        _client = QueueClientFactory.getInstance();
        //TODO get the info about the current state of the queue

        Intent intent = getIntent();

        _id = intent.getStringExtra("id");
        _token = intent.getStringExtra("token");
        //TODO grab the freeze state

    }

    private void getQueueJSON()
    {

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

    private void setUpExitButton()
    {
        _exitQueueButton = (Button) findViewById(R.id.freeze_exit_button);
        if (_isFrozen)
        {
            //TODO display the correct color for the queue
        } else
        {
            //TODO display teh correct color for the queue
        }
        _exitQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO exit the queue
            }
        });
    }

    private void setUpEnterQueueButton()
    {
        _enterQueueButton = (Button) findViewById(R.id.enter_deactivate_button);

        if (_isActive)
        {
            _enterQueueButton.setText("Deactivate");
        } else
        {
            _enterQueueButton.setText("Activate");
            _queueScreen.setBackgroundColor(getResources().getColor(R.color.deactivatered));
        }

        _enterQueueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_isActive)
                {
                        //TODO enter the queue
                    }

            }
        });
    }

    private void getStudents()
    {
        _studentsInQueue = (ListView) findViewById(R.id.students_in_queue_listview);
        //TODO generate data to pass into the adapter
        _studentsInQueue.setAdapter(new StudentListViewAdapter(this, R.id.students_in_queue_listview, null));
    }

    private void getTas()
    {
        _tasInQueue = (ListView) findViewById(R.id.tas_in_queue_listview);
        //TODO generate data to pass into the adapter
        _tasInQueue.setAdapter(new TAListViewAdapter(this, R.id.tas_in_queue_listview, null));
    }


}
