package ru.donkot.FileBros;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Vector;

/**
 * file nodes to tree
 */
class FileNode {

    File my_file;

    public FileNode(File my_file) {
        this.my_file = my_file;
    }

    File getFile() {
        return my_file;
    }

    @Override
    public String toString() {
        return my_file.getName().length() > 0 ? my_file.getName() : my_file.getPath();
    }

    boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild(); // смотрим, можно ли развернуть папку
        if (flag==null) { //no flag
            return false;
        }
        Object obj = flag.getUserObject(); // присваиваем первого потомка к объекту
        if (!(obj instanceof Boolean)) { //already expanded
            return false;
        }
        parent.removeAllChildren(); //remove flag

        File[]files = listFiles(); // массив объектов
        if (files==null) return true; // есои пустой - вернуть ИСТИНУ

        Vector v = new Vector();
        for (File f : files) { // пока в массиве файлов есть файлы
            if (!(f.isDirectory())) continue; // если не дерриктория - продолжаем

            FileNode fnode = new FileNode(f); // делаем файл нод из файла
            boolean isAdded = false; // не добавлено
            for (int i = 0; i < v.size(); i++) { // от 0 до конца вектора
                FileNode nd = (FileNode) v.elementAt(i); // беру i обект
                if (fnode.compareTo(nd) < 0) {  // если имя меньше
                    v.insertElementAt(fnode, i); // добавить filenode на место i
                    isAdded = true; // флаг добавлено
                    break;
                }
            }
            if (!isAdded) { // если не добавилось - ДОБАВИТЬ
                v.addElement(fnode);
            }
        }
        for (int i = 0; i<v.size();i++) { // от 0 до конца вектора
            FileNode nd = (FileNode) v.elementAt(i);
            IconData data = new IconData(MyIconSet.getFolderIcon(), MyIconSet.getExpendedIcon(),nd); // делаем иконочку папки для дерева из файл нод
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(data); // создаём node девера
            parent.add(node); // добавляем эту иконку к корневому каталогу

            if (nd.hasSubDirs()) { // если у нода есть поддерриктории
                node.add(new DefaultMutableTreeNode(Boolean.TRUE)); // может иметь "детей"
            }
        }
        return true;
    }

    boolean hasSubDirs() {
        File[] files = listFiles();
        if (files==null) {
            return false;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    int compareTo(FileNode toCompare) { //сравнение длины имени
        return my_file.getName().compareToIgnoreCase(toCompare.my_file.getName());
    }

    File[] listFiles() { // если это папка - нахуй, null, иначе массив с файлами
        if (!my_file.isDirectory()) {
            return null;
        }
        return my_file.listFiles();
    }
}
