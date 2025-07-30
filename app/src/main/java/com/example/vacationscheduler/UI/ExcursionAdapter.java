package com.example.vacationscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> excursions;
    private final Context context;
    private final LayoutInflater inflater;
    public ExcursionAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView excursionItemView = itemView.findViewById(R.id.excursionTitle);
            TextView excursionItemView2 = itemView.findViewById(R.id.excursionDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Excursion excursion = excursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", excursion.getId());
                    intent.putExtra("title", excursion.getTitle());
                    intent.putExtra("date", excursion.getDate());
                    intent.putExtra("vacationId", excursion.getVacationId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView = inflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        TextView excursionItemView = holder.itemView.findViewById(R.id.excursionTitle);
        TextView excursionItemView2 = holder.itemView.findViewById(R.id.excursionDate);
        if (excursions != null) {
            Excursion current = excursions.get(position);
            excursionItemView.setText(current.getTitle());
            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);            excursionItemView2.setText(sdf.format(current.getDate()));
        } else {
            excursionItemView.setText("No excursions");
        }
    }

    @Override
    public int getItemCount() {
        if (excursions != null) {
            return excursions.size();
        } else return 0;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }
}
