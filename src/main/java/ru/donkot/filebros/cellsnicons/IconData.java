package ru.donkot.filebros.cellsnicons;

import javax.swing.*;

/**
 * Created by Я on 15.09.2017.
 */
public class IconData { // открыта, закрыта, и объект // stores information about object's icons
    //FIELDS
    private Icon nodeIcon;
    private Icon nodeExpanded;
    private Object nodeData;

    //CONSTRUCTORS
    public IconData(Icon nodeIcon, Icon nodeExpanded, Object nodeData) {
        this.nodeIcon = nodeIcon;
        this.nodeExpanded = nodeExpanded;
        this.nodeData = nodeData;
    }

    public IconData(Icon nodeIcon, Object nodeData) {
        this.nodeIcon = nodeIcon;
        this.nodeData = nodeData;
        this.nodeExpanded = null;
    }

    //FUNCTIONS'N'GETTERS
    public Icon getIcon() {
        return nodeIcon;
    }

    public Icon getExpanded() {
        return nodeExpanded !=null ? nodeExpanded : nodeIcon;
    }

    public Object getData() {
        return nodeData;
    }

    public String toString() {
        return nodeData.toString();
    }
}
