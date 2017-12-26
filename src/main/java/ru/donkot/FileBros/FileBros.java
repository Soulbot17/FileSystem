package ru.donkot.FileBros;

import ru.donkot.FileBros.cellsnicons.IconData;
import ru.donkot.FileBros.cellsnicons.MyIconSet;
import ru.donkot.FileBros.cellsnicons.*;
import ru.donkot.FileBros.listeners.*;
import ru.donkot.FileBros.panels.InfoPanel;
import ru.donkot.FileBros.panels.NorthPanel;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Soulbot17
 */
/*
@TODO NEW
-refactor this shit
@TODO BUGS
*/

public class FileBros extends JFrame implements Localizable {

    //FIELDS
    public static ResourceBundle resourceBundle;

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
    private NorthPanel panelNorth;
    private MyListSelectionListener listSelectionListener;
    //GETTERS AND SETTERS
    public InfoPanel getInfoPanel() {
        return myInfoPanel;
    }

    public JPanel getLeftPanel() {
        return panelLeft;
    }

    public JPanel getRightPanel() {
        return panelRight;
    }

    public NorthPanel getNorthPanel() {
        return panelNorth;
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
        resourceBundle = ResourceBundle.getBundle("lang");
        updateLocale(resourceBundle);
        myInfoPanel = new InfoPanel();
        panelNorth = new NorthPanel(this);
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
        myFolderTree.addTreeSelectionListener(new DirSelectionListener(this, myInfoPanel));

        myDisplay = new JTextField(32);
        myDisplay.setEditable(false);

        getContentPane().add(panelNorth, BorderLayout.NORTH);
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
        getContentPane().add(panelLeft, BorderLayout.WEST);

        JScrollPane panelList = new JScrollPane(myFileList);
        panelList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        myFileList.setCellRenderer(new MyListCellRenderer());
        myFileList.addMouseListener(new MyListMouseAdapter(this));



        listSelectionListener = new MyListSelectionListener(this);
        myFileList.addListSelectionListener(listSelectionListener);
        panelRight.add(panelList, BorderLayout.CENTER);
        panelRight.add(myInfoPanel, BorderLayout.SOUTH);
        getContentPane().add(panelRight, BorderLayout.CENTER);
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
        if (listSelectionListener!=null) {
            listSelectionListener.updateLocale(bundle);
        }
    }
}
