package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;

public class PeternakSearchDropDownAdapter extends ArrayAdapter<PeternakModel.Peternak> {

    private Context mContext;
    private List<PeternakModel.Peternak> PeternakList;

    private LayoutInflater layoutInflater;

    public PeternakSearchDropDownAdapter(@NonNull Context context,  ArrayList<PeternakModel.Peternak> list) {
        super(context, 0 , list);
        mContext = context;
        PeternakList = new ArrayList<PeternakModel.Peternak>(list.size());
        PeternakList.addAll(list);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.peternakwarung_dropdown,parent,false);

        PeternakModel.Peternak currentPeternak = getItem(position);

        TextView name = (TextView) listItem.findViewById(R.id.namaWarga);
        name.setText(currentPeternak.getFull_name());

        TextView email = (TextView) listItem.findViewById(R.id.emailWarga);
        email.setText(currentPeternak.getEmail());

        return listItem;
    }
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((PeternakModel.Peternak)resultValue).getFull_name();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<PeternakModel.Peternak> suggestions = new ArrayList<PeternakModel.Peternak>();
                for (PeternakModel.Peternak peternak : PeternakList) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (peternak.getFull_name().toLowerCase().contains(constraint.toString().toLowerCase()) || peternak.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(peternak);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<PeternakModel.Peternak>) results.values);
            }
            notifyDataSetChanged();
        }
    };



    @Override
    public Filter getFilter() {
        return mFilter;
    }


}