package com.mobileedu33.tutorme.ui.HelperClasses.ViewLessonClasses;

public class ViewLessonsHelper {

    // Images is in the resource folder it can be referenced by assigning an int variable
    int image;
    String title, description;
    // ViewLessonHelper class constructor
    public ViewLessonsHelper(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    /* Getters */
    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
