package com.mobileedu33.tutorme.ui.HelperClasses.SubjectClasses;


public class SubjectHelperClass {

    int subjectImage;
    String  subjectTitle, subjectDescription;


    public SubjectHelperClass(int subjectImage, String subjectTitle, String subjectDescription) {
        this.subjectImage = subjectImage;
        this.subjectTitle = subjectTitle;
        this.subjectDescription = subjectDescription;
    }

    public int getSubjectImage() {
        return subjectImage;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }
}
