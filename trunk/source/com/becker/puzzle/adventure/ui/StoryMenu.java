package com.becker.puzzle.adventure.ui;

import com.becker.puzzle.adventure.Story;
import com.becker.ui.util.GUIUtil;
import com.becker.ui.file.ExtensionFileFilter;
import com.becker.ui.file.FileChooserUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * File menu for story application.
 * You can open, save, or edit a story file.
 *
 * @author Barry Becker
 */
class StoryMenu extends JMenu implements ActionListener  {

    //private JFrame frame_;
    private GraphicalAdventure storyApp_;

    private JMenuItem openItem_;
    private JMenuItem saveItem_;
    private JMenuItem editItem_;
    private JMenuItem exitItem_;

    private static final String EXT = "xml";

    /**
     * Game application constructor
     * @param storyApp the initially selected game.
     */
    public StoryMenu(GraphicalAdventure storyApp)
    {
        super("Story");

        this.setBorder(BorderFactory.createEtchedBorder());

        storyApp_ = storyApp;
        setBorder(BorderFactory.createEtchedBorder());

        openItem_ =  createMenuItem("Open");
        saveItem_ =  createMenuItem("Save");
        editItem_ =  createMenuItem("Edit");
        exitItem_ = createMenuItem("Exit");
        add(openItem_);
        add(saveItem_);
        add(editItem_);
        add(exitItem_);
    }

    /**
     * called when the user has selected a different story file option.
     * @param e
     */
    public void actionPerformed( ActionEvent e )
    {
         JMenuItem item = (JMenuItem) e.getSource();
         if (item == openItem_)  {
            openStory();
        }
        else if (item == saveItem_) {
            saveStory();
        }
        else if (item == editItem_) {
            storyApp_.editStory();
        }
        else if (item == exitItem_) {
            if (confirmExit())  {
                System.exit(0);
            }
        }
        else {
            assert false : "unexpected menuItem = "+ item.getName();
        }
    }

    /**
     * If there are modifications, confirm before exiting.
     * @return true if exiting was confirm or if no edit was made so confirm not needed.
     */
    private boolean confirmExit() {

        if (storyApp_.isStoryEdited())  {
            int choice = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Are you sure you want to exit?",
                    "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * View the story that the user opens from the file chooser.
     */
    private void openStory() {
        File file = FileChooserUtil.getSelectedFileToOpen(EXT, getDefaultDir());
        if ( file != null)  {
            storyApp_.loadStory(file);
        }
    }

    /**
     * Save the current story to a file.
     */
    private void saveStory() {
        File file = FileChooserUtil.getSelectedFileToSave(EXT, getDefaultDir());
        if ( file != null) {
            // if it does not have the .sgf extension already then add it
            String fPath = file.getAbsolutePath();
            fPath = ExtensionFileFilter.addExtIfNeeded(fPath, EXT);
            storyApp_.saveStory(fPath);
        }
    }

    private File getDefaultDir() {
        String defaultDir = GUIUtil.RESOURCE_ROOT + Story.STORIES_ROOT;
        return new File(defaultDir);
    }

    /**
     * Create a menu item.
     * @param name name of the menu item. The label.
     * @return the menu item to add.
     */
    JMenuItem createMenuItem(String name)
    {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        return item;
    }

}
