package ru.donkot.filebros.listeners;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.Localizable;
import ru.donkot.filebros.panels.InfoPanel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

// Информация на панельке при  выделении файлов

public class MyListSelectionListener implements ListSelectionListener, Localizable {
    //FUNCTIONS
    private FileBros fileBros;
    private InfoPanel infoPanel;
    private File currentFile;
    private String mySelectedFileInfo;
    private String bytes;

    //CONSTRUCTOR
    public MyListSelectionListener(FileBros fileBros) {
        this.fileBros = fileBros;
        infoPanel = fileBros.getInfoPanel();
    }

    //FUNCTIONS
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (fileBros.getMyFileList().getSelectedValue() != null) {
            fileBros.setCurrentFile(fileBros.getMyFileList().getSelectedValue().toString());
            currentFile = new File(fileBros.getMyFileList().getSelectedValue().toString());
            mySelectedFileInfo = getFileName(fileBros.getMyFileList().getSelectedValue().toString());
            bytes = humanReadableByteCount(currentFile.length(), true);
            updateLocale(fileBros.getResourceBundle());
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
        return filepath.substring(filepath.lastIndexOf('\\') + 1, filepath.length());
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        if (mySelectedFileInfo!=null) {
            infoPanel.setMyInfoNameText(String.format(bundle.getString("myInfoNameText2"), mySelectedFileInfo));
            infoPanel.setMyInfoSizeText(String.format(bundle.getString("myInfoSizeText2"), bytes));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", fileBros.getResourceBundle().getLocale());
            infoPanel.setMyInfoEditedText(String.format(bundle.getString("myInfoEditedText2"), sdf.format(currentFile.lastModified())));
            infoPanel.setMyInfoPathText(bundle.getString("myInfoPathText2") + currentFile.getAbsolutePath());
            infoPanel.setMyInfoIsHidden(String.format(bundle.getString("myInfoIsHidden2"), currentFile.isHidden() ? "+" : "-"));
        } else infoPanel.updateLocale(bundle);
    }
}
