package utah.edu.cs4962.TAQueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shong on 12/5/13.
 */
public class ClassExpandableListViewAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    //header for each section w/ instructor name
    private List<String> _sectionHeaders;
    // maps the header to the child section items
    private HashMap<String, List<String>> _sectionItemMap;
    //JSON object representing the chosen school with instructors and queues
    private JSONObject _jsonSchoolObject;

    public ClassExpandableListViewAdapter(
            Context context,
            List<String> sectionHeader,
            HashMap<String, List<String>> sectionItemMap,
            JSONObject jsonObject)
    {
        _context = context;
        _sectionHeaders = sectionHeader;
        _sectionItemMap = sectionItemMap;
        _jsonSchoolObject = jsonObject;
    }

    /**
     * Creates the section header with the instructor's name
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String instructorName = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_section_header, null);
        }

        TextView sectionHeaderTextView = (TextView) convertView.findViewById(R.id.section_header_item);
        sectionHeaderTextView.setText(instructorName);

        return convertView;
    }

    /**
     * Populates the section with classes associated with the instructor
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        final String className = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.class_row, null);
        }

        TextView sectionItemTextView = (TextView) convertView.findViewById(R.id.section_list_item);
        sectionItemTextView.setText(className);

        //add a listenter to the class item
        sectionItemTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView selectedClass = (TextView) v;

            }
        });

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._sectionItemMap.get(this._sectionHeaders.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._sectionItemMap.get(this._sectionHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._sectionHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._sectionHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
