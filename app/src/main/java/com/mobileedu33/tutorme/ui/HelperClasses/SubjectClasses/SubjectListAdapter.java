package com.mobileedu33.tutorme.ui.HelperClasses.SubjectClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileedu33.tutorme.R;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ListSubjectViewHolder> {

    ArrayList<SubjectHelperClass> listOfSubjects;

    // SubjectListAdapter class constructor
    public SubjectListAdapter(ArrayList<SubjectHelperClass> mListOfSubjects) {
        this.listOfSubjects = mListOfSubjects;
    }

    @NonNull
    @Override
    public ListSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subjects_card, parent,false);
        return new ListSubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSubjectViewHolder holder, int position) {
        SubjectHelperClass subjectHelper = listOfSubjects.get(position);

        holder.subjectImage.setImageResource(subjectHelper.getSubjectImage());
        holder.subjectTitle.setText(subjectHelper.getSubjectTitle());
        holder.lessonCounter.setText(subjectHelper.getSubjectDescription());
    }

    @Override
    public int getItemCount() {
        return listOfSubjects.size();
    }

    /* This sub-class will old the views or the design of the recycler(card view) view*/
    public static class ListSubjectViewHolder extends RecyclerView.ViewHolder {

        // Create objects of the views
        ImageView subjectImage;
        TextView subjectTitle, lessonCounter;

        // ListSubjectViewHolder constructor
        public ListSubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectImage = itemView.findViewById(R.id.subject_image);
            subjectTitle = itemView.findViewById(R.id.subject_title);
            lessonCounter = itemView.findViewById(R.id.tv_lesson_counter);
        }
    }


}
