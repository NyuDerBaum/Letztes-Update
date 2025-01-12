package de.gruppe16.stundenplaner.ui.planer;

public class ModulePreset {
    private String title;  // Course name
    private String prof;   // Professor's name
    private int color;     // Course color

    public ModulePreset(String title, String prof, int color) {
        this.title = title;
        this.prof = prof;
        this.color = color;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getProf() { return prof; }
    public void setProf(String prof) { this.prof = prof; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
