package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shong on 12/8/13.
 */
public class StudentListViewAdapter extends ArrayAdapter<String>
{
    private ArrayList<String> _studentsInQueue;
    private HashMap<String, String> _studentToTaMap;
    private HashMap<String, Integer> _taColorMap;
    private TAListViewAdapter _adapter;
    private ListView _tasInQueue;
    private Context _context;
    private int _resourceId;
    private ArrayList<Integer> _colors = new ArrayList<Integer>();
    private int _defaultColor;

    public StudentListViewAdapter(Context context, int resource, ArrayList<String> students, HashMap<String, String> studentToTaMap)
    {
        super(context, resource, students);
        _context = context;
        _studentsInQueue = students;
        _resourceId = resource;

        _colors.add(R.color.blue);
        _colors.add(R.color.green);
        _colors.add(R.color.orange);
        _colors.add(R.color.purple);
        _colors.add(R.color.teal);
        _colors.add(R.color.pink);

        _tasInQueue = (ListView)((Activity) _context).findViewById(R.id.student_q_tas_listview);
        _adapter = (TAListViewAdapter)_tasInQueue.getAdapter();
        _taColorMap = _adapter.getColors();
        _studentToTaMap = studentToTaMap;
        _defaultColor = ((Activity) _context).getResources().getColor(R.color.activewhite);
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

        String student = _studentsInQueue.get(position);
        String ta = _studentToTaMap.get(student);
        int color = _defaultColor;
        if(ta != null)
            color = _taColorMap.get(ta);


        button.setText(_studentsInQueue.get(position));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, Color.BLACK);
        drawable.setColor(color);
        button.setBackground(drawable);

        //TODO set dismiss swipe listener

//        studentTextView.setBackgroundColor(_colors.get(position % 6));

        return row;
    }
}
