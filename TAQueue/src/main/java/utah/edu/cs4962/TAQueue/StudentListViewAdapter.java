package utah.edu.cs4962.TAQueue;

import android.app.Activity;
import android.content.Context;
import android.view.DragEvent;
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
public class StudentListViewAdapter extends ArrayAdapter<String>
{
    private ArrayList<String> _studentsInQueue;
    private Context _context;
    private int _resourceId;
    private ArrayList<Integer> _colors = new ArrayList<Integer>();

    public StudentListViewAdapter(Context context, int resource, ArrayList<String> students)
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        TextView studentTextView;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) _context).getLayoutInflater();
            row = inflater.inflate(_resourceId, parent, false);
            studentTextView = (TextView)row.findViewById(R.id.queue_row_textview);
            row.setTag(studentTextView);
        }
        else
        {
            studentTextView = (TextView) row.getTag();
        }

        studentTextView.setText(_studentsInQueue.get(position));
        studentTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO add the student to the TA who is viewing this student
            }
        });

        //TODO set dismiss swipe listener

        studentTextView.setBackgroundColor(_colors.get(position % 6));

        return row;
    }
}
