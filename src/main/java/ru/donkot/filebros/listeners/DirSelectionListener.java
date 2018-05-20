package ru.donkot.filebros.listeners;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.FileNode;
import ru.donkot.filebros.Localizable;
import ru.donkot.filebros.panels.InfoPanel;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

//          то, что происходит при выборе node в fileTree

public class DirSelectionListener implements TreeSelectionListener, Localizable {
    //FIELDS
    private FileBros fileBros;
    private InfoPanel infoPanel;
    private int filesCount = 0;

    //CONSTRUCTOR
    public DirSelectionListener(FileBros fileBros, InfoPanel infoPanel) {
        this.fileBros = fileBros;
        this.infoPanel = infoPanel;
    }

    //FUNCTIONS
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        infoPanel.updateLocale(fileBros.getResourceBundle());
        fileBros.setCurrentFolder(e.getPath().toString());
        DefaultMutableTreeNode node = fileBros.getTreeNode(e.getPath());
        fileBros.setCurrentNode(fileBros.getTreeNode(e.getPath()));
        FileNode fileNode = fileBros.getFileNode(node);
        displayNumberOfFiles(fileNode);
    }

    private void displayNumberOfFiles(FileNode fileNode) {
        if (fileNode == null || fileNode.getFile().listFiles() == null) {
            fileBros.setMydisplayText("");
            return;
        }

        Vector<File> filesVector = new Vector<>();

        fileBros.setCurrentFolder(fileNode.getFile().getAbsolutePath());
        filesCount = Objects.requireNonNull(fileNode.getFile().listFiles()).length;
        fileBros.setMydisplayText(fileBros.getResourceBundle().getString("selectionFiles") + String.valueOf(filesCount));

        File file = new File(fileBros.getCurrentFolder());
        File[] files = file.listFiles();
        if (files == null || files.length <= 0) {
            fileBros.setMyFileListData(new Vector());
            return;
        }
        Arrays.stream(files).filter(f -> !f.isDirectory()).forEach(filesVector::add);
        fileBros.setMyFileListData(filesVector);

    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        fileBros.setMydisplayText(fileBros.getResourceBundle().getString("selectionFiles") + String.valueOf(filesCount));
    }
}
