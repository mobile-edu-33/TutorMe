package com.mobileedu33.tutorme.ui.HelperClasses.ViewLessonClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileedu33.tutorme.R;

import java.util.ArrayList;

/* The Adapter class holds the value and puts them in the design*/
public class ViewLessonAdapter extends RecyclerView.Adapter<ViewLessonAdapter.ViewLessonViewHolder> {

    // Values
    ArrayList<ViewLessonsHelper> viewLessonArrayList;

    // Constructor of the ViewLessonAdapter class which takes as input an arraylist
    public ViewLessonAdapter(ArrayList<ViewLessonsHelper> viewLessonArrayList) {
        this.viewLessonArrayList = viewLessonArrayList;
    }

    @NonNull
    @Override
    // Creates the view lesson card xml design dynamically
    public ViewLessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_lessons_card, parent, false);
        ViewLessonViewHolder lessonViewHolder = new ViewLessonViewHolder(view);
        return lessonViewHolder;
    }

    @Override
    /* This methods binds the design and the code which is in Helper class*/
    public void onBindViewHolder(@NonNull ViewLessonViewHolder holder, int position) {

        // Gets the ViewLessonsHelper class and assign the position of each item
        ViewLessonsHelper viewLessonsHelper = viewLessonArrayList.get(position);

        // Gets the image at the current position which is at 0 index
        holder.image.setImageResource(viewLessonsHelper.getImage());
        holder.title.setText(viewLessonsHelper.getTitle());
        holder.desc.setText(viewLessonsHelper.getDescription());
    }

    @Override
    public int getItemCount() {
        return viewLessonArrayList.size();
    }

    /* This sub-class will old the views or the design of the recycler(card view) view*/
    public static class ViewLessonViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, desc;

        public ViewLessonViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.view_lesson_image);
            title = itemView.findViewById(R.id.view_lesson_title);
            desc = itemView.findViewById(R.id.view_lesson_desc);
        }
    }
}
