package com.mobileedu33.tutorme.ui.HelperClasses.TutorDisplayClass;

public class TutorDisplayHelper {

    // Declaring class members
    int tutorImage;
    String tutorName;
    String tutorRadius;
    String numOfStudents;

    public TutorDisplayHelper(int tutorImage, String tutorName, String tutorRadius, String numOfStudents) {
        this.tutorImage = tutorImage;
        this.tutorName = tutorName;
        this.tutorRadius = tutorRadius;
        this.numOfStudents = numOfStudents;
    }

    public int getTutorImage() {
        return tutorImage;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getTutorRadius() {
        return tutorRadius;
    }

    public String getNumOfStudents() {
        return numOfStudents;
    }
}
