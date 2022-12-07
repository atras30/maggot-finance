package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.WarungModel;

public class WarungSearchDropDownAdapter extends ArrayAdapter<WarungModel.Warung> {

    private Context mContext;
    private List<WarungModel.Warung> WarungList;

    private LayoutInflater layoutInflater;

    public WarungSearchDropDownAdapter(@NonNull Context context,  ArrayList<WarungModel.Warung> list) {
        super(context, 0 , list);
        mContext = context;
        try {
            WarungList = new ArrayList<WarungModel.Warung>(list.size());
            WarungList.addAll(list);
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e) {
            Toast.makeText(context, "Belum ada warga/warung yang terdaftar.", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.peternakwarung_dropdown,parent,false);

        WarungModel.Warung currentWarung = getItem(position);

        TextView name = (TextView) listItem.findViewById(R.id.namaWarga);
        name.setText(currentWarung.getFull_name());

        TextView email = (TextView) listItem.findViewById(R.id.emailWarga);
        email.setText(currentWarung.getEmail());

        return listItem;
    }
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((WarungModel.Warung)resultValue).getFull_name();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<WarungModel.Warung> suggestions = new ArrayList<WarungModel.Warung>();
                for (WarungModel.Warung warung : WarungList) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (warung.getFull_name().toLowerCase().contains(constraint.toString().toLowerCase()) || warung.getEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(warung);
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
                addAll((ArrayList<WarungModel.Warung>) results.values);
            }
            notifyDataSetChanged();
        }
    };



    @Override
    public Filter getFilter() {
        return mFilter;
    }


}