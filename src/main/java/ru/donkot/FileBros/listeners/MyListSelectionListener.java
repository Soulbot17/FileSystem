package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.panels.InfoPanel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.text.SimpleDateFormat;

// Информация на панельке при  выделении файлов

public class MyListSelectionListener implements ListSelectionListener {
    //FUNCTIONS
    private FileBros fileBros;
    private InfoPanel infoPanel;

    //CONSTRUCTOR
    public MyListSelectionListener(FileBros fileBros, InfoPanel infoPanel) {
        this.fileBros = fileBros;
        this.infoPanel = infoPanel;
    }

    //FUNCTIONS
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (fileBros.getMyFileList().getSelectedValue() != null) {
            fileBros.setCurrentFile(fileBros.getMyFileList().getSelectedValue().toString());
            File file = new File(fileBros.getMyFileList().getSelectedValue().toString());
            String myselectedfileinfo = getFileName(fileBros.getMyFileList().getSelectedValue().toString());
            String bytes = humanReadableByteCount(file.length(), true);
            infoPanel.setMyInfoNameText(String.format("File name: %s", myselectedfileinfo));
            infoPanel.setMyInfoSizeText(String.format("File size: %s", bytes));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            infoPanel.setMyInfoEditedText(String.format("File last edited: %s", sdf.format(file.lastModified())));
            infoPanel.setMyInfoPathText("File path: " + file.getAbsolutePath());
            infoPanel.setMyInfoIsHidden(String.format("File is hidden: %s", file.isHidden() ? "yes" : "no"));
        }
    }

    //          stolen awesome method to display readable byte
    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private String getFileName(String filepath) {
        return filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length());
    }
}
