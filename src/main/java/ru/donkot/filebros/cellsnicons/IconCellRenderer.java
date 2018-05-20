package ru.donkot.filebros.cellsnicons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
//          nice icons for my JTree
public class IconCellRenderer extends JLabel implements TreeCellRenderer {
    //FIELDS
    private Color myTextSelectionColor;
    private Color myTextNonSelectedColor;
    private Color myBkSelectedColor;
    private Color myBkNonSelectedColor;
    private Color myBorderSelectedColor;

    private boolean isSelected;

    //CONSTRUCTOR
    public IconCellRenderer() { //  созданным цветам присваиваем цвета системы
        myTextSelectionColor = UIManager.getColor("Tree.selectionForeground");
        myTextNonSelectedColor = UIManager.getColor("Tree.textForeground");
        myBkSelectedColor = UIManager.getColor("Tree.selectionBackground");
        myBkNonSelectedColor = UIManager.getColor("Tree.textBackground");
        myBorderSelectedColor = UIManager.getColor("Tree.selectionBorderColor");
        setOpaque(false);
    }

    //FUNCTIONS
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) { //создание иконок jtree
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value; // пиздим значение
        Object obj = node.getUserObject(); // зачем то копирую в отдельный объект
        setText(obj.toString()); // меняем текст на текст объекта (название файла)

        if (obj instanceof Boolean) setText("Waiting for data..."); // если false - ждём
        if (obj instanceof IconData) { // если икондата
            IconData idata = (IconData) obj;
            if (expanded) {
                setIcon(idata.getExpanded());
            } else setIcon(idata.getIcon());
        } else setIcon(null);
        setFont(tree.getFont());
        setForeground(selected ? myTextSelectionColor : myTextNonSelectedColor);
        setBackground(selected ? myBkSelectedColor : myBkNonSelectedColor);
        isSelected = selected;
        return this;
    }

    //          selection animation
    @Override
    protected void paintComponent(Graphics g) {
        Color bColor = getBackground();
        Icon icon = getIcon();

        g.setColor(bColor);
        int offset = 0;
        if (icon != null && getText() != null) {
            offset = (icon.getIconWidth() + getIconTextGap());
        }
        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        if (isSelected) {
            g.setColor(myBorderSelectedColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }
        super.paintComponent(g);
    }
}
