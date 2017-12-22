package ru.donkot.FileBros.cellsnicons;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

//          system view for my file list
public class MyListCellRenderer extends DefaultListCellRenderer {
    //FUNCTIONS
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        FileSystemView view = FileSystemView.getFileSystemView();
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setText(view.getSystemDisplayName((File) value));
        label.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        label.setIcon(view.getSystemIcon((File) value));
        label.setHorizontalTextPosition(RIGHT);
        return label;
    }
}
