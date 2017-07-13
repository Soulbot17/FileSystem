import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Soulbot17
 */
/*
@TODO:
-icons for files
-short names for files
-upper bar with delete folder / create folder

@BUGS:

*/

public class MyTree extends JFrame {
    public static ArrayList<String> c_textExtensions;
    public static ArrayList<String> c_graphicExtensions;
    public static final ImageIcon DISK_ICON = new ImageIcon("src/iconset/disk24.png");
    public static final ImageIcon FOLDER_ICON = new ImageIcon("src/iconset/folder24.png");
    public static final ImageIcon EXPENDED_ICON = new ImageIcon("src/iconset/expfolder24.png");
    public static final ImageIcon COMPUTER_ICON = new ImageIcon("src/iconset/computer24.png");
    public static final ImageIcon TEXT_ICON = new ImageIcon("src/iconset/text24.png");
    public static final ImageIcon GRAPHIC_ICON = new ImageIcon("src/iconset/pic24.png");
    public static final ImageIcon NOEX_ICON = new ImageIcon("src/iconset/noex24.png");

    protected JPanel myinfopanel;
    protected JTree mytree;
    protected JTextField mydisplay;
    protected DefaultTreeModel mytreemodel;
    protected JPanel panelLeft = new JPanel(new BorderLayout());
    protected JPanel panelRight = new JPanel(new BorderLayout());
    protected JList jList = new JList();
    protected Vector nullvector = new Vector();
    protected String myselectedfileinfo;
    protected JLabel my_infoNameText = new JLabel("File name: no file selected yet.");
    protected JLabel my_infoSizeText = new JLabel("File size: ...");
    protected JLabel my_infoEditedText = new JLabel("File last edited: ...");
    protected JScrollPane paneTree;

    public static void main(String[] args) {
        new MyTree();
    }

    public MyTree() throws HeadlessException {
        super("FileDude");
        listInit();
        myselectedfileinfo = "File name: no file selected yet";
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
        setLocationRelativeTo(null);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(COMPUTER_ICON,null,"PC"));

        DefaultMutableTreeNode node;
        File[] list = File.listRoots();
        for (int i = 0; i<list.length;i++) {
            node = new DefaultMutableTreeNode(new IconData(DISK_ICON,null,new FileNode(list[i])));
            top.add(node);
            node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }
        mytreemodel = new DefaultTreeModel(top);
        mytree = new JTree(mytreemodel);
        //WTF
        mytree.putClientProperty("JTree.lineStyle","Aligned");

        TreeCellRenderer renderer = new IconCellRenderer();
        mytree.setCellRenderer(renderer);

        mytree.addTreeSelectionListener(new DirSelectionListener());
        mytree.addTreeExpansionListener(new DirExpansionListener());

        mytree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        mytree.setShowsRootHandles(true);
        mytree.setEditable(false);
        mydisplay = new JTextField(32);
        mydisplay.setEditable(false);
        //WTF ENDS

        paneTree = new JScrollPane(mytree);
        paneTree.setWheelScrollingEnabled(true);
        paneTree.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneTree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(paneTree,BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        myinfopanel = creteInfoPanel();
//        setResizable(false);
        panelLeft.add(paneTree,BorderLayout.CENTER);
        panelLeft.add(mydisplay,BorderLayout.SOUTH);
        getContentPane().add(panelLeft,BorderLayout.WEST);

        JScrollPane panelList = new JScrollPane(jList);
        panelList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList.setCellRenderer(new MyListCellRenderer());
        jList.addListSelectionListener(new MyListSelectionListener());
        panelRight.add(panelList,BorderLayout.CENTER);
        panelRight.add(myinfopanel,BorderLayout.SOUTH);
        getContentPane().add(panelRight,BorderLayout.CENTER);
        setVisible(true);
    }

    protected class MyListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()){
                File file = new File(jList.getSelectedValue().toString());
                myselectedfileinfo = getFileName(jList.getSelectedValue().toString());
                String bytes = humanReadableByteCount(file.length(),true);
                my_infoNameText.setText(String.format("File name: %s",myselectedfileinfo));
                my_infoSizeText.setText(String.format("File size: %s",bytes));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                my_infoEditedText.setText(String.format("File created: %s",sdf.format(file.lastModified())));
            }
        }

        public String humanReadableByteCount(long bytes, boolean si) { //stolen awesome method to display readable byte
            int unit = si ? 1000 : 1024;
            if (bytes < unit) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
            return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
        }
    }
    void listInit(){
        c_graphicExtensions = new ArrayList<>();
        c_textExtensions = new ArrayList<>();

        c_textExtensions.add(".txt");
        c_textExtensions.add(".doc");

        c_graphicExtensions.add(".jpeg");
        c_graphicExtensions.add(".png");
        c_graphicExtensions.add(".img");
    }

    protected JPanel creteInfoPanel(){
        JPanel panel = new JPanel();
        Font font = new Font(Font.SANS_SERIF,Font.PLAIN,12);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setFocusable(false);
        panel.setBorder(BorderFactory.createEtchedBorder());
        my_infoNameText.setFont(font);
        panel.add(my_infoNameText);
        my_infoSizeText.setFont(font);
        panel.add(my_infoSizeText);
        my_infoEditedText.setFont(font);
        panel.add(my_infoEditedText);
        return panel;
    }

    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node==null) return null;
        Object obj = node.getUserObject();
        if (obj instanceof IconData) {
            obj = ((IconData) obj).getData();
        }
        if (obj instanceof FileNode) {
            return (FileNode) obj;
        } else return null;
    }

    class DirExpansionListener implements TreeExpansionListener{

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            final DefaultMutableTreeNode node = getTreeNode(event.getPath());
            final FileNode fnode = getFileNode(node);
            Thread runner = new Thread(){
              public void run(){
                  if (fnode!=null&&fnode.expand(node)) {
                      Runnable runnable = new Runnable() {
                          @Override
                          public void run() {
                              mytreemodel.reload(node); //??
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

    class DirSelectionListener implements TreeSelectionListener{
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = getTreeNode(e.getPath());
            FileNode fnode = getFileNode(node);
            Vector<File> vfiles = new Vector<>();
            if (fnode!=null) {
                String tempo = fnode.getFile().getAbsolutePath();
                mydisplay.setText(tempo);
                File file = new File(tempo);
                File[] files = file.listFiles();
                if (files!=null&&files.length>0) {
                    for (int i = 0; i<files.length;i++) {
                        if (!files[i].isDirectory()) {
                            vfiles.add(files[i]);
                        } else continue;
                    }
                    jList.setListData(vfiles);
                } else jList.setListData(nullvector);
            } else mydisplay.setText("");
        }
    }

    class IconCellRenderer extends JLabel implements TreeCellRenderer{
        protected Color my_textSelectionColor;
        protected Color my_textNonSelectedColor;
        protected Color my_bkSelectedColor;
        protected Color my_bkNonSelectedColor;
        protected Color my_borderSelectedColor;

        protected boolean my_selected;

        public IconCellRenderer() {
            super();
            my_textSelectionColor = UIManager.getColor("Tree.selectionForeground");
            my_textNonSelectedColor = UIManager.getColor("Tree.textForeground");
            my_bkSelectedColor = UIManager.getColor("Tree.selectionBackground");
            my_bkNonSelectedColor = UIManager.getColor("Tree.textBackground");
            my_borderSelectedColor = UIManager.getColor("Tree.selectionBorderColor");
            setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object obj = node.getUserObject();
            setText(obj.toString());

            if (obj instanceof Boolean) setText("Waiting for data...");
            if (obj instanceof IconData) {
                IconData idata = (IconData) obj;
                if (expanded) {
                    setIcon(idata.getExpanded());
                } else setIcon(idata.getIcon());
            }
            else setIcon(null);
            setFont(tree.getFont());
            setForeground(selected ? my_textSelectionColor : my_textNonSelectedColor);
            setBackground(selected ? my_bkSelectedColor : my_bkNonSelectedColor);
            my_selected = selected;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Color bColor = getBackground();
            Icon icon = getIcon();

            g.setColor(bColor);
            int offset = 0;
            if (icon != null && getText() != null) {
                offset = (icon.getIconWidth() + getIconTextGap());
            }
            g.fillRect(offset,0,getWidth()-1-offset,getHeight()-1);
            if (my_selected) {
                g.setColor(my_borderSelectedColor);
                g.drawRect(offset,0,getWidth()-1-offset,getHeight()-1);
            }
            super.paintComponent(g);
        }
    }

    class FileNode {
        protected File my_file;

        public FileNode(File my_file) {
            this.my_file = my_file;
        }

        public File getFile() {
            return my_file;
        }

        @Override
        public String toString() {
            return my_file.getName().length() > 0 ? my_file.getName() : my_file.getPath();
        }

        public boolean expand (DefaultMutableTreeNode parent) {
            DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild();
            if (flag==null) { //no flag
                return false;
            }
            Object obj = flag.getUserObject();
            if (!(obj instanceof Boolean)) { //already expanded
                return false;
            }
            parent.removeAllChildren(); //remove flag

            File[]files = listFiles();
            if (files==null) return true;

            Vector v = new Vector();
            for (int m = 0; m<files.length;m++) {
                File f = files[m];
                if (!(f.isDirectory())) continue;

                FileNode fnode = new FileNode(f);
                boolean isAdded = false;
                for (int i = 0; i<v.size();i++) {
                    FileNode nd = (FileNode) v.elementAt(i);
                    if (fnode.compareTo(nd)<0) {
                        v.insertElementAt(fnode,i);
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    v.addElement(fnode);
                }
            }
            for (int i = 0; i<v.size();i++) {
                FileNode nd = (FileNode) v.elementAt(i);
                IconData data = new IconData(MyTree.FOLDER_ICON,MyTree.EXPENDED_ICON,nd);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
                parent.add(node);

                if (nd.hasSubDirs()) {
                    node.add(new DefaultMutableTreeNode(new Boolean(true)));
                }
            }
            return true;
        }

        protected boolean hasSubDirs() {
            File[] files = listFiles();
            if (files==null) {
                return false;
            }
            for (int m = 0; m<files.length;m++) {
                if (files[m].isDirectory()) {
                    return true;
                }
            }
            return false;
        }

        protected int compareTo(FileNode toCompare) {
            return my_file.getName().compareToIgnoreCase(toCompare.my_file.getName());
        }

        protected File[] listFiles() {
            if (!my_file.isDirectory()) {
                return null;
            }
            return my_file.listFiles();
        }
    }

    class IconData {
        protected Icon n_icon;
        protected Icon n_expanded;
        protected Object n_data;

        public IconData(Icon n_icon, Icon n_expanded, Object n_data) {
            this.n_icon = n_icon;
            this.n_expanded = n_expanded;
            this.n_data = n_data;
        }

        public IconData(Icon n_icon, Object n_data) {
            this.n_icon = n_icon;
            this.n_data = n_data;
            this.n_expanded = null;
        }

        public Icon getIcon() {
            return n_icon;
        }

        public Icon getExpanded() {
            return n_expanded!=null ? n_expanded : n_icon;
        }

        public Object getData() {
            return n_data;
        }

        public String toString() {
            return n_data.toString();
        }
    }

    protected String getFileName(String filepath){
        String fileName = filepath.substring(filepath.lastIndexOf("\\")+1,filepath.length());
        return fileName;
    }

    class MyListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            FileSystemView view = FileSystemView.getFileSystemView();
            JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setText(view.getSystemDisplayName((File) value));
            label.setIcon(view.getSystemIcon((File) value));
            label.setHorizontalTextPosition(RIGHT);

//            String cellText = getFileName(value.toString());
//            String values = getFileExtension(value.toString());
//            label.setText(cellText);
//            if (values!=null&&c_textExtensions.contains(values)) {
//                label.setIcon(TEXT_ICON);
//                label.setHorizontalTextPosition(RIGHT);
//            } else if (values!=null&&c_graphicExtensions.contains(values)) {
//                label.setIcon(GRAPHIC_ICON);
//                label.setHorizontalTextPosition(RIGHT);
//            } else {
//                label.setIcon(NOEX_ICON);
//                label.setHorizontalTextPosition(RIGHT);
//            }

            return label;
        }

        private String getFileExtension(String mystr) {
            int index = mystr.indexOf('.');
            return index == -1? null : mystr.substring(index);
        }
    }
}
