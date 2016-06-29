package pl.marchuck.wikiaapi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lukasz Marczak
 * @since 08.05.16.
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionAdapterViewHolder> {

    List<String> dataSet;

    public void updateDataset(List<String> dataSet) {
        this.dataSet = dataSet;
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }


    public SuggestionAdapter(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    public SuggestionAdapter() {
        this(new ArrayList<String>());
    }

    @Override
    public SuggestionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
        return new SuggestionAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SuggestionAdapterViewHolder holder, int position) {
        final String item = dataSet.get(position);
        holder.textView.setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class SuggestionAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SuggestionAdapterViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
        }
    }
}