package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shong on 12/8/13.
 */
public class TAListViewAdapter extends ArrayAdapter
{
    private ArrayList<String> _tasInQueue;
    private Context _context;
    private int _resourceId;

    private HashMap<String, Integer> _colorMap = new HashMap<String, Integer>();

    public TAListViewAdapter(Context context, int resource,
                             ArrayList<String> tasInQueue,
                             HashMap<String, Integer> colorMap)
    {
        super(context, resource, tasInQueue);
        _context = context;
        _resourceId = resource;
        _tasInQueue = tasInQueue;
        _colorMap = colorMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Button button;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            button = (Button)row.findViewById(R.id.queue_row_button);
            row.setTag(button);
        }
        else
        {
            button = (Button) row.getTag();
        }

        button.setText(_tasInQueue.get(position));
        //set the color of the button. It will wrap around for a TA list greater than 6, but that is acceptable
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, Color.BLACK);
        int color = _colorMap.get(_tasInQueue.get(position));
        drawable.setColor(color);
        button.setBackground(drawable);

        return row;
    }

    public void refill(ArrayList<String> tas, HashMap<String, Integer> colorMap) {
        _tasInQueue.clear();
        _tasInQueue.addAll(tas);
        _colorMap = colorMap;
        notifyDataSetChanged();
    }
}
