package com.utbm.georace.ui;

/**
 * Created by Franck on 11/11/2014.
 */
public interface NavDrawerItem {
    public int getId();
    public String getLabel();
    public int getType();
    public boolean isEnabled();
    public boolean updateActionBarTitle();
}