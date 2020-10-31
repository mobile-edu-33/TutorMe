package com.mobileedu33.tutorme.ui.activities.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.mobileedu33.tutorme.data.models.UserProfile;
import com.mobileedu33.tutorme.data.models.UserType;
import com.mobileedu33.tutorme.ui.HelperClasses.SubjectClasses.SubjectHelperClass;
import com.mobileedu33.tutorme.ui.HelperClasses.SubjectClasses.SubjectListAdapter;
import com.mobileedu33.tutorme.ui.HelperClasses.TutorDisplayClass.TutorDisplayAdapter;
import com.mobileedu33.tutorme.ui.HelperClasses.TutorDisplayClass.TutorDisplayHelper;
import com.mobileedu33.tutorme.ui.HelperClasses.ViewLessonClasses.ViewLessonAdapter;
import com.mobileedu33.tutorme.ui.HelperClasses.ViewLessonClasses.ViewLessonsHelper;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.activities.LoginActivity;
import com.mobileedu33.tutorme.ui.fragments.AssignmentFragment;
import com.mobileedu33.tutorme.ui.fragments.DashboardFragment;
import com.mobileedu33.tutorme.ui.fragments.LessonsFragment;
import com.mobileedu33.tutorme.ui.fragments.ProfileFragment;
import com.mobileedu33.tutorme.ui.viewmodels.DashboardActivityViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;

@AndroidEntryPoint
public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Creating a RecyclerView object
    private RecyclerView mViewLessonRecycler, mTutorDisplayRecyclerView;
    private SubjectListAdapter subjectListAdapter;
    private TutorDisplayAdapter tutorDisplayAdapter;
    private ViewLessonAdapter viewLessonAdapter;
    private LinearLayout contentView;
    private ChipNavigationBar chipNavigationBar;
    private ImageView profilePhoto;
    private TextView userName;
    private TextView userEmail;

    static final float END_SCALE = 0.7f;

    // Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private DashboardActivityViewModel dashboardActivityViewModel;
    private LinearLayout teachersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        androidx.appcompat.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.menu_icon);

        // Initialize viewmodel
        dashboardActivityViewModel = new ViewModelProvider(this)
                .get(DashboardActivityViewModel.class);
        getLifecycle().addObserver(dashboardActivityViewModel);

        // Getting a reference to the recycler view and its ID
        mViewLessonRecycler = findViewById(R.id.view_lessons_recycler);
        mTutorDisplayRecyclerView = findViewById(R.id.tutor_display_recycler);
        // Get a reference to the drawer menu icon
        contentView = findViewById(R.id.contentView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        teachersLayout = findViewById(R.id.layoutTeachers);
        profilePhoto = headerView.findViewById(R.id.user_profile_pic);
        userEmail = headerView.findViewById(R.id.user_email);
        userName = headerView.findViewById(R.id.user_username);
        // Menu hooks
        navigationDrawer();

        // Calling the Recycler view functions
        setupLessonsRecyclerView();
        setSubjectsRecyclerView();

        dashboardActivityViewModel.getUserProfile();
        if (dashboardActivityViewModel.getUserType() == UserType.STUDENT) {
            setupStudentsDashboard();
        } else {
            setupTutorDashboard();
        }
        // Gets a reference to the ChipNavigation Bar
        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        // Handles the methods of the ChipNavigation bar
        bottomMenu();
    }

    private void setupTutorDashboard() {
        teachersLayout.setVisibility(View.GONE);
    }

    private void setupStudentsDashboard() {
        tutorDisplayRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserProfile userProfile = dashboardActivityViewModel.getUserProfile();
        if (userProfile.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(userProfile.getPhotoUrl())
                    .placeholder(R.drawable.avtar)
                    .into(profilePhoto);
        }
        userName.setText(userProfile.getDisplayName());
        userEmail.setText(userProfile.getEmail());
    }

    @Override
    protected void onStop() {
        super.onStop();
        getLifecycle().removeObserver(dashboardActivityViewModel);
        dashboardActivityViewModel.removeLiveDataObservers(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        handleDrawerVisibility();
        return super.onSupportNavigateUp();
    }

    private void bottomMenu() {

        // The setOnItemSelectedListener is called when a menu is pressed to display a fragment
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {

                    case R.id.bottom_nav_dashboard:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.bottom_nav_assignment:
                        fragment = new AssignmentFragment();
                        break;
                    case R.id.bottom_nav_lessons:
                        fragment = new LessonsFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }

                // Place the fragments inside the dashboard
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment).commit();
            }
        });
    }

    // Navigation Drawer functions
    private void navigationDrawer() {

        /* Navigation Drawer - allows us to interact with the navigation drawer*/
        navigationView.bringToFront();
        /* Creates a listener which detects when a view or item is clicked in the navigation*/
        navigationView.setNavigationItemSelectedListener(this);
        // Highlights a horizontal bar when a user hovers the items
        navigationView.setCheckedItem(R.id.nav_home);

        /*This code runs when a user clicks on the drawer menu icon*/

        // Calling the animation function
        animateNavigationDrawer();
    }

    private void handleDrawerVisibility() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // This functions creates an animation for the drawer layout when the user opens and closes it
    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Scale the View based on a current slide offset
                final float diffScaledOffSet = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffSet;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffSet / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    /* This function will close the drawer when the back button is pressed and closed the app
    * if the back button is pressed after the drawer is closed*/
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_subjects:
                startActivity(new Intent(getApplicationContext(), AllSubjects.class));
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        return true;
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(aVoid -> {
                    Realm defaultInstance = Realm.getDefaultInstance();
                    defaultInstance.beginTransaction();
                    defaultInstance.delete(TutorProfile.class);
                    defaultInstance.delete(StudentProfile.class);
                    defaultInstance.commitTransaction();
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    intent.setAction("login");
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
            showMessageSnackBar("An error loging out. Try again", null, null);
                });
    }

    // Recycler views functions
    private void setupLessonsRecyclerView() {
        // Loads only the views which are visible to the user
        mViewLessonRecycler.setHasFixedSize(true);
        // Sets the layout of the recycler view and its orientation, by default it is vertical
        mViewLessonRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create an ArrayList
        ArrayList<ViewLessonsHelper> viewLessonsArrayList = new ArrayList<>();

        // Gets the string resource with the ID lesson_description
        String description = getResources().getString(R.string.lesson_description);

        // Add the items(parameters) when the ViewLessonsHelper class is called
        viewLessonsArrayList.add(new ViewLessonsHelper(R.drawable.mathematics_video, "Mathematics", description));
        viewLessonsArrayList.add(new ViewLessonsHelper(R.drawable.english_video, "Julius Ceasar", description));
        viewLessonsArrayList.add(new ViewLessonsHelper(R.drawable.watching_youtube, "Arts & Cultures", description));

        viewLessonAdapter = new ViewLessonAdapter(viewLessonsArrayList);
        mViewLessonRecycler.setAdapter(viewLessonAdapter);
    }

    private void setSubjectsRecyclerView() {
        RecyclerView subjectRecycler = findViewById(R.id.view_subjects_rv);

        subjectRecycler.setHasFixedSize(true);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<SubjectHelperClass> subjectHelper = new ArrayList<>();

        subjectHelper.add(new SubjectHelperClass(R.drawable.english_subject, "English", "240 Lessons"));
        subjectHelper.add(new SubjectHelperClass(R.drawable.maths_subject, "Mathematics", "40 Lessons"));
        subjectHelper.add(new SubjectHelperClass(R.drawable.economy_subject, "Economics", "140 Lessons"));
        subjectHelper.add(new SubjectHelperClass(R.drawable.geo_subject, "Geography", "150 Lessons"));
        subjectHelper.add(new SubjectHelperClass(R.drawable.physics_subject, "Physics", "180 Lessons"));
        subjectHelper.add(new SubjectHelperClass(R.drawable.science_subject, "Science", "190 Lessons"));

        subjectListAdapter = new SubjectListAdapter(subjectHelper);
        subjectRecycler.setAdapter(subjectListAdapter);
    }

    private void tutorDisplayRecycler() {
        mTutorDisplayRecyclerView.setHasFixedSize(true);
        mTutorDisplayRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<TutorDisplayHelper> tutorDisplayArr = new ArrayList<>();

        tutorDisplayArr.add(new TutorDisplayHelper(R.drawable.tutor_image, "Miss Olivia Gonvender", "14 km away", "230 lessons"));
        tutorDisplayArr.add(new TutorDisplayHelper(R.drawable.tutor_image, "Miss Olivia Gonvender", "3 km away", "230 lessons"));
        tutorDisplayArr.add(new TutorDisplayHelper(R.drawable.tutor_image, "Miss Olivia Gonvender", "12 km away", "230 lessons"));
        tutorDisplayArr.add(new TutorDisplayHelper(R.drawable.tutor_image, "Miss Olivia Gonvender", "10 km away", "230 lessons"));

        tutorDisplayAdapter = new TutorDisplayAdapter(tutorDisplayArr);
        mTutorDisplayRecyclerView.setAdapter(tutorDisplayAdapter);
    }
}