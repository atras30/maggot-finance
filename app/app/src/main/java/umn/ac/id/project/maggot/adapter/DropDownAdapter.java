package umn.ac.id.project.maggot.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;

public class DropDownAdapter extends ArrayAdapter<PeternakModel.Peternak> {
    LayoutInflater flater;

    public DropDownAdapter(Activity context,int resourceId, int textviewId, List<PeternakModel.Peternak> list){

        super(context,resourceId,textviewId, list);
        flater = context.getLayoutInflater();

}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position){
        PeternakModel.Peternak peternak = getItem(position);
        viewHolder holder;
        View rowview = convertView;
        if(rowview == null) {
            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.peternakwarung_dropdown, null, false);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.PeternakName.setText(peternak.getFull_name());
        return rowview;
    }

    private class viewHolder{
        TextView PeternakName;
    }
}
