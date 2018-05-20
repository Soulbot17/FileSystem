package ru.donkot.filebros.listeners;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.FileNode;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;

//          развёртывание папки

public class DirExpansionListener implements TreeExpansionListener {
    //FIELDS
    private FileBros fileBros;

    //CONSTRUCTOR
    public DirExpansionListener(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    //FUNCTIONS
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        final DefaultMutableTreeNode node = fileBros.getTreeNode(event.getPath());
        final FileNode fileNode = fileBros.getFileNode(node);

        new Thread(() -> {
            if (fileNode != null && fileNode.expand(node)) {
                Runnable runnable = () -> fileBros.getMyTreeModel().reload(node);
                SwingUtilities.invokeLater(runnable);
            }
        }).start();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        //do nothing
    }
}
