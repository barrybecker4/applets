package com.becker.ui.dialogs;

import com.becker.java2d.ui.ImageListPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import com.becker.ui.dialogs.AbstractDialog;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * Show an image in a preview dialog.
 * @author Barry Becker
 */
public class ImagePreviewDialog extends AbstractDialog
                                                  implements ActionListener {

    private BufferedImage image_;
    

    public ImagePreviewDialog(BufferedImage img) {

        image_ = img;
        this.setResizable(true);
        setTitle("Image Preview");
        this.setModal( true );
        showContent();
    }


    protected JComponent createDialogContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(createImagePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private ImageListPanel createImagePanel() {
        ImageListPanel imagePanel = new ImageListPanel();
        imagePanel.setMaxNumSelections(1);
        imagePanel.setPreferredSize(new Dimension(700, 400));
        imagePanel.setSingleImage(image_);
        return imagePanel;
    }

    /**
     *  create the buttons that go at the botton ( eg OK, Cancel, ...)
     */
    protected  JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( cancelButton_, "Cancel", "Cancel image prview" );
        buttonsPanel.add( cancelButton_ );

        return buttonsPanel;
    }

}
