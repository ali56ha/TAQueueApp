package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by shong on 12/5/13.
 */
public class SchoolListViewAdapter extends ArrayAdapter<String>
{
    private Context _context;
    private int _resourceId;
    private ArrayList<String> _data;


    public SchoolListViewAdapter(Context context, int resourceID, ArrayList<String> data)
    {
        super(context, resourceID, data);

        _context = context;
        _resourceId = resourceID;
        _data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);


        }
    }
}
