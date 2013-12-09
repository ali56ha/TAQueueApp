package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shong on 12/8/13.
 */
public class TAListViewAdapter extends ArrayAdapter
{
    private ArrayList<String> _tasInQueue;
    private Context _context;
    private int _resourceId;
    private ArrayList<Integer> _colors = new ArrayList<Integer>();

    public TAListViewAdapter(Context context, int resource, ArrayList<String> tasInQueue)
    {
        super(context, resource);
        _context = context;
        _resourceId = resource;
        _tasInQueue = tasInQueue;

        _colors.add(R.color.blue);
        _colors.add(R.color.green);
        _colors.add(R.color.orange);
        _colors.add(R.color.purple);
        _colors.add(R.color.teal);
        _colors.add(R.color.pink);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        TextView button;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            button = (TextView)row.findViewById(R.id.queue_row_textview);
            row.setTag(button);
        }
        else
        {
            button = (TextView) row.getTag();
        }

        button.setText(_tasInQueue.get(position));
        //set the color of the button. It will wrap around for a TA list greater than 6, but that is acceptable
        button.setBackgroundColor(_colors.get(position%6));

        return row;
    }
}
