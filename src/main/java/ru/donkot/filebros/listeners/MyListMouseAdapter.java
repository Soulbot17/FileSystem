package ru.donkot.filebros.listeners;

import lombok.extern.log4j.Log4j2;
import ru.donkot.filebros.FileBros;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

// Двойной клик для открытия файла

@Log4j2
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
                log.error("MyListMouseAdapter in mouseClicked: ", e1);
            }
        }
    }
}
