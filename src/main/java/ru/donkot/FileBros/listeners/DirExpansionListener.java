package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.FileNode;

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
        final FileNode fnode = fileBros.getFileNode(node);
        Thread runner = new Thread() {
            public void run() {
                if (fnode != null && fnode.expand(node)) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            fileBros.getMyTreeModel().reload(node);
                        }
                    };
                    SwingUtilities.invokeLater(runnable);
                }
            }
        };
        runner.start();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
    }
}
