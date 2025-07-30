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
import com.example.vacationscheduler.entities.Vacation;

import java.net.CookieHandler;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> vacations;
    private final Context context;
    private final LayoutInflater inflater;

    public VacationAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView vacationItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Vacation vacation = vacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", vacation.getId());
                    intent.putExtra("title", vacation.getTitle());
                    intent.putExtra("lodging", vacation.getLodging());
                    intent.putExtra("startDate", vacation.getStartDate());
                    intent.putExtra("endDate", vacation.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView = inflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        TextView vacationItemView = holder.itemView.findViewById(R.id.textView);
        if (vacations != null) {
            Vacation current = vacations.get(position);
            vacationItemView.setText(current.getTitle());
        } else {
            vacationItemView.setText("No vacations");
        }
    }

    @Override
    public int getItemCount() {
        if (vacations != null) {
            return vacations.size();
        } else return 0;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }
}
