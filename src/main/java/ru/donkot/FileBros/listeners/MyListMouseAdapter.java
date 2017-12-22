package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

// Двойной клик для открытия файла

public class MyListMouseAdapter extends MouseAdapter {
    //FIELDS
    private FileBros fileBros;
    private Desktop desktop = Desktop.getDesktop();

    //CONSTRUCTOR
    public MyListMouseAdapter(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    //FUNCTIONS
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            try {
                desktop.open(new File(fileBros.getCurrentFile()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
