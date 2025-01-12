package de.gruppe16.stundenplaner.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.ui.planer.TimetableCell;
import de.gruppe16.stundenplaner.ui.planer.PlanerFragment;


/**
 * Implementation of App Widget functionality.
 */
public class NormalWidget extends AppWidgetProvider {

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get the next class information
        TimetableCell nextClass = getNextClass();

        // Create a RemoteViews instance
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.normal_widget);

        // Set the next class information on the views
        views.setTextViewText(R.id.class_name, nextClass.getTitle());
        views.setTextViewText(R.id.class_time, nextClass.getStartTime());
        views.setTextViewText(R.id.class_room, nextClass.getRoom());

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private TimetableCell getNextClass() {
        List<TimetableCell> timetableCells = PlanerFragment.getDataList();

        // Extra time to show the "next" class even if it has already started
        long timeBeforeStart = (5) * 60 * 1000;
        // Note that the widget might take up to 30 minutes after that to update.
        // 30 minutes is the minimum widget update time.

        // Get the current time and day
        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Monday = 2, ..., Saturday = 7
        long currentTimeMillis = now.getTimeInMillis();

        // Initialize variables to track the next class
        TimetableCell nextClass = null;
        long nextClassStartTimeMillis = Long.MAX_VALUE;

        for (TimetableCell cell : timetableCells) {
            // Validate startTime before processing
            String startTime = cell.getStartTime();
            String title = cell.getTitle();
            if (startTime == null || startTime.isEmpty() || title.isEmpty()) {
                // Skip this cell if startTime is invalid
                continue;
            }

            // Get the class's start time as a Calendar object
            String[] timeParts = cell.getStartTime().split(":");
            Calendar classStartTime = cell.parseTime(timeParts);

            // Adjust the class start time to match the correct day
            int daysUntilClass = (cell.getDay() - currentDay + 7) % 7;
            classStartTime.add(Calendar.DAY_OF_YEAR, daysUntilClass);

            long classStartTimeMillis = classStartTime.getTimeInMillis();

            // Check if the class is in the future and is the earliest upcoming class
            if (classStartTimeMillis > currentTimeMillis - timeBeforeStart && classStartTimeMillis < nextClassStartTimeMillis) {
                nextClass = cell;
                nextClassStartTimeMillis = classStartTimeMillis;
            }
        }

        // In case no Class is coming up the rest of the day
        if (nextClass == null || nextClass.getTitle().isEmpty()) {
            nextClass = new TimetableCell("Kein Kurs");
            nextClass.setRoom(" - ");
            nextClass.setStartTime(" - ");
        }

        return nextClass;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}