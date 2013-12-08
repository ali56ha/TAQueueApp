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

public class QueueActivity extends Activity
{
    private boolean _isTA;
    private String _id;
    private String _token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_screen);

        Intent intent = getIntent();

        _isTA = intent.getBooleanExtra("isTA", false);
        _id = intent.getStringExtra("id");
        _token = intent.getStringExtra("token");
    }

}
