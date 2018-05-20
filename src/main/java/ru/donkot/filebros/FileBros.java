package ru.donkot.filebros;

import ru.donkot.filebros.cellsnicons.IconData;
import ru.donkot.filebros.cellsnicons.MyIconSet;
import ru.donkot.filebros.cellsnicons.*;
import ru.donkot.filebros.listeners.*;
import ru.donkot.filebros.panels.InfoPanel;
import ru.donkot.filebros.panels.TopMenu;
import ru.donkot.filebros.panels.SorthPanel;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Soulbot17
 */

public class FileBros extends JFrame implements Localizable{

    //FIELDS
    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("lang");

    private JTree myFolderTree;
    private JTextField myDisplay;
    private DefaultTreeModel myTreeModel;
    private JList myFileList = new JList();

    private InfoPanel myInfoPanel;
    private String currentFile = null;
    private String currentFolder = null;
    private DefaultMutableTreeNode currentNode = null;

    private JPanel panelLeft = new JPanel(new BorderLayout());
    private JPanel panelRight = new JPanel(new BorderLayout());
    private SorthPanel panelSouth;
    private TopMenu topBar;

    private transient MyListSelectionListener listSelectionListener;
    private transient DirSelectionListener dirSelectionListener;
    //GETTERS AND SETTERS


    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public InfoPanel getInfoPanel() {
        return myInfoPanel;
    }

    public JPanel getLeftPanel() {
        return panelLeft;
    }

    public JPanel getRightPanel() {
        return panelRight;
    }

    public SorthPanel getSouthPanel() {
        return panelSouth;
    }

    public DefaultTreeModel getMyTreeModel() {
        return myTreeModel;
    }

    public void setCurrentNode(DefaultMutableTreeNode currentNode) {
        this.currentNode = currentNode;
    }

    public JList getMyFileList() {
        return myFileList;
    }

    public void setMyFileListData(Vector vector) {
        myFileList.setListData(vector);
    }

    public DefaultMutableTreeNode getCurrentNode() {
        return currentNode;
    }

    public JTree getMyFolderTree() {
        return myFolderTree;
    }

    public JTextField getMyDisplay() {
        return myDisplay;
    }

    public void setMydisplayText(String text) {
        myDisplay.setText(text);
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    //CONSTRUCTOR
    public FileBros() {
        updateLocale(resourceBundle);
        myInfoPanel = new InfoPanel(this);
        panelSouth = new SorthPanel(this);
        initializeGUI();
    }

    //FUNCTIONS
    public void start() {
        setVisible(true);
    }

    private void initializeGUI() {
        setIconImage(MyIconSet.getTitleIcon().getImage());
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        setLocationRelativeTo(null);

        prepareTreeNodes();

        myDisplay = new JTextField(32);
        myDisplay.setEditable(false);


        JScrollPane paneTree = new JScrollPane(myFolderTree);
        paneTree.setWheelScrollingEnabled(true);
        paneTree.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneTree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        panelLeft.add(paneTree, BorderLayout.CENTER);
        panelLeft.add(myDisplay, BorderLayout.SOUTH);


        JScrollPane panelList = new JScrollPane(myFileList);
        panelList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        myFileList.setCellRenderer(new MyListCellRenderer());
        myFileList.addMouseListener(new MyListMouseAdapter(this));

        listSelectionListener = new MyListSelectionListener(this);
        myFileList.addListSelectionListener(listSelectionListener);
        panelRight.add(panelList, BorderLayout.CENTER);
        panelRight.add(myInfoPanel, BorderLayout.SOUTH);

        topBar = new TopMenu(this);
        getContentPane().add(panelSouth, BorderLayout.SOUTH);
        getContentPane().add(panelLeft, BorderLayout.WEST);
        getContentPane().add(panelRight, BorderLayout.CENTER);
        getContentPane().add(topBar, BorderLayout.NORTH);
    }

    private void prepareTreeNodes() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(MyIconSet.getComputerIcon(), null, "PC")); // главная иконка
        DefaultMutableTreeNode node;
        File[] list = File.listRoots(); // диски
        for (File aList : list) {
            node = new DefaultMutableTreeNode(new IconData(MyIconSet.getDiskIcon(), null, new FileNode(aList)));
            top.add(node); // добавляю к иконке компа диски
            node.add(new DefaultMutableTreeNode(Boolean.TRUE)); // может иметь "детей"
        }
        myTreeModel = new DefaultTreeModel(top); // закладываю основу модели
        myFolderTree = new JTree(myTreeModel); // создаю дерево по модели
        myFolderTree.setIgnoreRepaint(true); // улучшение фул скрин мода, хуй знает
        myFolderTree.putClientProperty("JTree.lineStyle", "Aligned"); // стырил из шпаргалки по jtree
        TreeCellRenderer renderer = new IconCellRenderer(); // иконочки для дерева
        myFolderTree.setCellRenderer(renderer); // кастомные, чтоб веселее было
        myFolderTree.addTreeExpansionListener(new DirExpansionListener(this));
        myFolderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        myFolderTree.setShowsRootHandles(true);
        myFolderTree.setEditable(false);

        dirSelectionListener = new DirSelectionListener(this, myInfoPanel);
        myFolderTree.addTreeSelectionListener(dirSelectionListener);
    }


    public DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    //          get FileNode from TreeNode
    public FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node == null) return null;
        Object obj = node.getUserObject(); // get selected object
        if (obj instanceof IconData) { //если объект икон дата, вернуть икондату
            obj = ((IconData) obj).getData();
        }
        if (obj instanceof FileNode) { // если ФН - то фн
            return (FileNode) obj;
        } else return null;
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        resourceBundle = bundle;
        setTitle(bundle.getString("title"));
        if (topBar != null) {
            topBar.updateLocale(bundle);
        }
        if (listSelectionListener != null) {
            listSelectionListener.updateLocale(bundle);
        }
        if (panelSouth != null) {
            panelSouth.updateLocale(bundle);
        }
        if (dirSelectionListener != null) {
            dirSelectionListener.updateLocale(bundle);
        }
    }
}
