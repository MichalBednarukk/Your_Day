package com.example.your_day;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private final ArrayList<Integer> dayList;
    Context context;

    public interface ItemClicked {
        void OnItemClicked(int index, LocalDate event);

    }

    public DayAdapter(Context context, ArrayList<Integer> dayList) {

        this.dayList = dayList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvDayOfMonth, tvDayOfWeek, tvYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvDayOfMonth = itemView.findViewById(R.id.tvDayOfMonth);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvYear = itemView.findViewById(R.id.tvYear);
        }
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(dayList.get(position));
        DayAdapter.ItemClicked itemClicked = (DayAdapter.ItemClicked) context;
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DAY_OF_YEAR, -position);
        Date newDate = calendar.getTime();
        LocalDate localDate = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month month = localDate.getMonth();
        int dayOfMonth = localDate.getDayOfMonth();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int year = localDate.getYear();
        viewHolder.tvDayOfMonth.setText(String.valueOf(dayOfMonth));
        viewHolder.tvMonth.setText(String.valueOf(month));
        viewHolder.tvDayOfWeek.setText(String.valueOf(dayOfWeek));
        viewHolder.tvYear.setText(String.valueOf(year));
        viewHolder.itemView.setOnClickListener(v -> itemClicked.OnItemClicked(position, localDate));
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }
}
