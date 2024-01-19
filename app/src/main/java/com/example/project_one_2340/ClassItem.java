package com.example.project_one_2340;

public class ClassItem {
    private final String name;
    private final String professor;
    private final String meetingTime;

    public ClassItem(String name, String professor, String meetingTime) {
        this.name = name;
        this.professor = professor;
        this.meetingTime = meetingTime;
    }

    public String getName() {
        return name;
    }

    public String getProf() {
        return professor;
    }

    public String getMeetingTime() {
        return meetingTime;
    }
}