package com.example.goalguru;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<PricePackedModel> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<PricePackedModel> expandableListDetail) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(listPosition).getId()
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.packed_list_child, null);
        }
        TextView expandedListTextView = convertView.findViewById(R.id.tvChildPlanText);
        Button startButton = convertView.findViewById(R.id.btStartTrial); // "Ücretsiz Denemeye Başla" butonu

        String childText = (String) getChild(listPosition, expandedListPosition);
        expandedListTextView.setText(childText);

        // Deneme paketi ise butonu görünür yap, diğer durumlarda gizle
        if (childText.equalsIgnoreCase("ücretsiz")) {
            startButton.setVisibility(View.VISIBLE);
        } else {
            startButton.setVisibility(View.GONE);
        }

        return convertView;
    }


    @Override
    public int getChildrenCount(int listPosition) {
        if(expandableListDetail.get(listPosition).getId()!=null)
            return expandableListDetail.get(listPosition).getId().size();
        else
            return 0;
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableListDetail.get(listPosition).getPackedName();
    }

    @Override
    public int getGroupCount() {
        return this.expandableListDetail.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.packed_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.tvPackedName);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
