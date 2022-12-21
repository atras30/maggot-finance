package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.TrashManagerModel;

public class SpinnerRegisterAdapter extends BaseAdapter {
    Context context;
    int[] flags;
    List<TrashManagerModel.TrashManagers> trashManagers;
    LayoutInflater inflater;

    public SpinnerRegisterAdapter(Context applicationContext, List<TrashManagerModel.TrashManagers> trashManagers) {
        this.context = applicationContext;
        this.flags = flags;
        this.trashManagers = trashManagers;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return trashManagers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.register_spinner_layout, null);
        TextView nama_tempat = view.findViewById(R.id.nama_tempat);
        TextView nama_pengelola = view.findViewById(R.id.nama_pengelola);
        nama_tempat.setText(trashManagers.get(i).getTempat());
        nama_pengelola.setText(trashManagers.get(i).getNama_pengelola());
        return view;
    }
}
