package com.example.project_one_2340;

public class GenericItem {
    private final String title;
    private final String subtitle1;
    private final String subtitle2;

    public GenericItem(String title, String subtitle1, String subtitle2) {
        this.title = title;
        this.subtitle1 = subtitle1;
        this.subtitle2 = subtitle2;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle1() {
        return subtitle1;
    }

    public String getSubtitle2() {
        return subtitle2;
    }
}
