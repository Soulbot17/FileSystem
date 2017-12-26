package ru.donkot.FileBros.panels;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.Localizable;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class InfoPanel extends JPanel implements Localizable{
    //FIELDS
    private JLabel myInfoNameText;
    private JLabel myInfoSizeText;
    private JLabel myInfoEditedText;
    private JLabel myInfoPathText;
    private JLabel myInfoIsHidden;

    private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    //CONSTRUCTOR
    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());

        prepareTextLabels();
        updateLocale(FileBros.resourceBundle);
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
    private void prepareTextLabels() {
        myInfoNameText = new JLabel();
        myInfoNameText.setFont(font);
        add(myInfoNameText);

        myInfoSizeText = new JLabel();
        myInfoSizeText.setFont(font);
        add(myInfoSizeText);

        myInfoEditedText = new JLabel();
        myInfoEditedText.setFont(font);
        add(myInfoEditedText);

        myInfoPathText = new JLabel();
        myInfoPathText.setFont(font);
        add(myInfoPathText);

        myInfoIsHidden = new JLabel();
        myInfoIsHidden.setFont(font);
        add(myInfoIsHidden);
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        myInfoNameText.setText(bundle.getString("myInfoNameText"));
        myInfoSizeText.setText(bundle.getString("myInfoSizeText"));
        myInfoEditedText.setText(bundle.getString("myInfoEditedText"));
        myInfoPathText.setText(bundle.getString("myInfoPathText"));
        myInfoIsHidden.setText(bundle.getString("myInfoIsHidden"));
    }
}
