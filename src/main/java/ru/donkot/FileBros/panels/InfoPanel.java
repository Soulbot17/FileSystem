package ru.donkot.FileBros.panels;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    //FIELDS
    private JLabel myInfoNameText = new JLabel("File name: no file selected yet.");
    private JLabel myInfoSizeText = new JLabel("File size: ...");
    private JLabel myInfoEditedText = new JLabel("File last edited: ...");
    private JLabel myInfoPathText = new JLabel("File path: ...");
    private JLabel myInfoIsHidden = new JLabel("File is hidden: ...");

    //CONSTRUCTOR
    public InfoPanel() {

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        myInfoNameText.setFont(font);
        add(myInfoNameText);
        myInfoSizeText.setFont(font);
        add(myInfoSizeText);
        myInfoEditedText.setFont(font);
        myInfoIsHidden.setFont(font);
        add(myInfoIsHidden);
        add(myInfoEditedText);
        myInfoPathText.setFont(font);
        add(myInfoPathText);
    }

    //GETTERS AND SETTERS
    public void setMyInfoNameText(String text) { myInfoNameText.setText(text); }

    public void setMyInfoSizeText(String text) {
        myInfoSizeText.setText(text);
    }

    public void setMyInfoEditedText(String text) {
        myInfoEditedText.setText(text);
    }

    public void setMyInfoPathText(String text) {
        myInfoPathText.setText(text);
    }

    public void setMyInfoIsHidden(String text) {
        myInfoIsHidden.setText(text);
    }

    //FUNCTIONS
    public void clearInfoPanel() {
        myInfoNameText.setText("File name: no file selected yet.");
        myInfoSizeText.setText("File size: ...");
        myInfoEditedText.setText("File last edited: ...");
        myInfoPathText.setText("File path: ...");
        myInfoIsHidden.setText("File is hidden: ...");
    }
}
