package com.gc.jmihelp.model;

public class ChildModel {
    String title;
    int resource = -1;

    public ChildModel(String title, int resource) {
        this.title = title;
        this.resource = resource;
    }

    boolean isSelected;

    public ChildModel(String title){
        this.title = title;
    }

    public ChildModel(String title, boolean isSelected){
        this.title = title;
        this.isSelected = isSelected;
    }

    public ChildModel(String title, int resource, boolean isSelected) {
        this.title = title;
        this.resource = resource;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public int getResource() {
        return resource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
