package de.gruppe16.stundenplaner.ui.planer;
import android.content.Context;


import java.util.Calendar;// Klasse, die eine Zelle im Stundenplan repräsentiert

import de.gruppe16.stundenplaner.notification.Notification;
public class TimetableCell {
    private String title; // Titel des Moduls (z.B. Kursname,Header)
    private String room; // Raum, in dem der Kurs stattfindet
    private String prof; // Name des Professors
    private int color; // Farbe der Zelle
    private  String startTime; // Startzeit des Moduls
    private int day; // Tag an dem das Modul stattfindet
    private  boolean notification; // Benachrichtigung für das Modul
    public TimetableCell(String title) {
        this.title = title;
    }
    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom() { return room; }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public int getColor() { return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public boolean getNotification() { return notification;}

    public void setNotification(boolean notification) { this.notification = notification; }

    public Calendar parseTime(String[] timeParts) {
        // Parse startTime to set the Calendar instance
        Calendar calendar = Calendar.getInstance();

        if (timeParts.length == 2) {
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0); // Ensure milliseconds are reset
        }

        return calendar;
    }

    public void manageNotification(Context context) {
        int timeBeforeStart = -15;
        Notification notificationHelper = new Notification(context);

        if (!notification) {
            // Cancel the notification if it's disabled
            notificationHelper.cancelNotification();
            return;
        }

        // Parse startTime to set the Calendar instance
        Calendar classStartTime = parseTime(startTime.split(":"));

        // Get the current date
        Calendar today = Calendar.getInstance();

        // Adjust to the next occurrence of the specified day
        int currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextOccurrence = (day - currentDayOfWeek + 7) % 7;

        // Set classStartTime to the correct day
        classStartTime.add(Calendar.DAY_OF_YEAR, daysUntilNextOccurrence);

        // Subtract timeBeforeStart to set the notification time
        classStartTime.add(Calendar.MINUTE, timeBeforeStart);

        // Schedule the notification
        notificationHelper.scheduleNotification(
                title,
                "Class in room " + room + " with " + prof,
                classStartTime
        );
    }
}
