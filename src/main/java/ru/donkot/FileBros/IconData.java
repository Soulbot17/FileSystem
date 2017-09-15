package ru.donkot.FileBros;

import javax.swing.*;

/**
 * Created by Я on 15.09.2017.
 */
class IconData { // открыта, закрыта, и объект // stores information about object's icons
    Icon n_icon;
    Icon n_expanded;
    Object n_data;

    public IconData(Icon n_icon, Icon n_expanded, Object n_data) {
        this.n_icon = n_icon;
        this.n_expanded = n_expanded;
        this.n_data = n_data;
    }

    public IconData(Icon n_icon, Object n_data) {
        this.n_icon = n_icon;
        this.n_data = n_data;
        this.n_expanded = null;
    }

    public Icon getIcon() {
        return n_icon;
    }

    public Icon getExpanded() {
        return n_expanded!=null ? n_expanded : n_icon;
    }

    public Object getData() {
        return n_data;
    }

    public String toString() {
        return n_data.toString();
    }
}
