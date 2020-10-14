package com.mobileedu33.tutorme.data;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class SharedPreferencesRepository {
   public static final String IS_FIRST_RUN = "isFirstRun";
   private final SharedPreferences sharedPreferences;

   @Inject
   public SharedPreferencesRepository(SharedPreferences sharedPreferences) {
      this.sharedPreferences = sharedPreferences;
   }

   public boolean isFirstRun() {
      return sharedPreferences.getBoolean(IS_FIRST_RUN, true);
   }

   public void setIsFirstRun(boolean value) {
      getEditor().putBoolean(IS_FIRST_RUN, value).apply();
   }

   private SharedPreferences.Editor getEditor() {
      return sharedPreferences.edit();
   }
}
