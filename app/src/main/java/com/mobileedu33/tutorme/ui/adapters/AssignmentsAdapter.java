package com.mobileedu33.tutorme.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {

    private List<Assignment> list;
    private OnItemClickListener onItemClickListener;

    public AssignmentsAdapter(OnItemClickListener onItemClickListener) {
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void addData(List<Assignment> items) {
        list = items;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_lesson_image)
        ImageView viewLessonImage;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDescription)
        TextView txtDescription;
        @BindView(R.id.txtDueDate)
        TextView txtDueDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Assignment model, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(model));
            txtTitle.setText(model.getTitle());
            txtDescription.setText(model.getDescription());
            txtDueDate.setText(model.getDateDue());

            if (model.getImageUrl() != null) {
                Glide.with(itemView)
                        .load(model.getImageUrl())
                        .placeholder(R.drawable.grey_background)
                        .into(viewLessonImage);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Assignment item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Assignment assignment);
    }

}