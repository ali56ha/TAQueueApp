package utah.edu.cs4962.TAQueue;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private String _schoolAbbrev;
    private HashMap<String, Pair<String, String>> _classInfoMap;
    private QueueClient _client;

    public ClassExpandableListViewAdapter(
            Context context,
            List<String> sectionHeader,
            HashMap<String, List<String>> sectionItemMap,
            String schoolAbbrev,
            HashMap<String, Pair<String, String>> classInfoMap)
    {
        _context = context;
        _sectionHeaders = sectionHeader;
        _sectionItemMap = sectionItemMap;
        _schoolAbbrev = schoolAbbrev;
        _client = QueueClientFactory.getInstance();
        _classInfoMap = classInfoMap;
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

        final TextView sectionItemTextView = (TextView) convertView.findViewById(R.id.section_list_item);
        sectionItemTextView.setText(className);

        //add a listener to the class item
        sectionItemTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView selectedClass = (TextView) v;
                Pair<String, String> classInfoPair = _classInfoMap.get(selectedClass.getText());
                String classNumber = classInfoPair.second;
                String instructorUsername = classInfoPair.first;

                Intent queueLoginActivity = new Intent(_context, QueueLoginActivity.class);
                queueLoginActivity.putExtra("school_abbrev", _schoolAbbrev);
                queueLoginActivity.putExtra("class_number", classNumber);
                queueLoginActivity.putExtra("instructor_username", instructorUsername);
                queueLoginActivity.putExtra("class_name", selectedClass.getText());
                _context.startActivity(queueLoginActivity);
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
