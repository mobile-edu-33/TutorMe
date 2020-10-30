package com.mobileedu33.tutorme.ui.HelperClasses.TutorDisplayClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileedu33.tutorme.R;

import java.util.ArrayList;

public class TutorDisplayAdapter extends RecyclerView.Adapter<TutorDisplayAdapter.TutorDisplayViewHolder> {

    // Create an ArrayList which will display the tutor details inside the Recycler view
    ArrayList<TutorDisplayHelper> tutorDisplayArr;

    /* This is the constructor for the base class TutorDisplayAdapter class which takes as input
    * an array list of tutors when the object is instatiated*/
    public TutorDisplayAdapter(ArrayList<TutorDisplayHelper> tutorDisplayArr) {
        this.tutorDisplayArr = tutorDisplayArr;
    }

    @NonNull
    @Override
    public TutorDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates the Card view and binds it with the Recycler view adapter
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_display_card, parent, false);
        return new TutorDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorDisplayViewHolder holder, int position) {

        TutorDisplayHelper tutorDisplay = tutorDisplayArr.get(position);
        holder.displayImage.setImageResource(tutorDisplay.getTutorImage());
        holder.tutorName.setText(tutorDisplay.getTutorName());
        holder.tutorRadius.setText(tutorDisplay.getTutorRadius());
        holder.numOfStudents.setText(tutorDisplay.getNumOfStudents());
    }

    @Override
    public int getItemCount() {
        return tutorDisplayArr.size();
    }

    /* This class will hold all the Views*/
    public static class TutorDisplayViewHolder extends RecyclerView.ViewHolder {

        // Creating View objects
        ImageView displayImage;
        TextView tutorName, tutorRadius, numOfStudents;

        public TutorDisplayViewHolder(@NonNull View itemView) {
            super(itemView);

            displayImage = itemView.findViewById(R.id.tutor_display_img);
            tutorName = itemView.findViewById(R.id.tutor_display_name);
            tutorRadius = itemView.findViewById(R.id.tutor_distance);
            numOfStudents = itemView.findViewById(R.id.tutor_student_counter);
        }
    }
}
