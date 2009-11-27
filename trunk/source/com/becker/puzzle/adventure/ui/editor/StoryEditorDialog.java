package com.becker.puzzle.adventure.ui.editor;

import com.becker.ui.GUIUtil;
import com.becker.ui.components.GradientButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.becker.puzzle.adventure.Story;
import com.becker.puzzle.adventure.Scene;
import com.becker.ui.dialogs.AbstractDialog;
import com.becker.ui.table.TableButtonListener;

import java.util.List;

/**
 * Allows editing of a story in a separate dialog.
 * You can add/remove/reorder/change scenes in the story.
 * @author Barry Becker
 */
public class StoryEditorDialog extends AbstractDialog
                               implements ActionListener,
                                          TableButtonListener,
                                          ListSelectionListener {
    /** The story to edit */
    private Story story_;

    private SceneEditorPanel sceneEditor;

    private static final Font INSTRUCTION_FONT = new Font("Sans Serif", Font.PLAIN, 10);

    private List<Scene>  parentScenes_;
    private ChildTable  childTable_;

    /** click this when done editing the scene. */
    private GradientButton okButton_ = new GradientButton();

    // for adding/removing/reordering scene choice destinations
    private GradientButton addButton_ = new GradientButton();
    private GradientButton removeButton_ = new GradientButton();
    private GradientButton moveUpButton_ = new GradientButton();
    private GradientButton moveDownButton_ = new GradientButton();

    /** location for images. */
    private static final String IMAGE_PATH =  "com/becker/puzzle/adventure/ui/images/";

    private int selectedChildRow_ = -1;

    /**
     * Constructor
     * @param story creates a copy of this in case we cancel.
     */
    public StoryEditorDialog(Story story) {

        story_ = new Story(story);

        this.setResizable(true);
        setTitle("Story Editor");
        this.setModal( true );
        showContent();
    }


    @Override
    protected JComponent createDialogContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(SceneEditorPanel.EDITOR_WIDTH, 700));
        JPanel editingPane = createEditingPane();
        JLabel title = new JLabel("Navigate through the scene heirarchy and change values for scenes.");
        title.setBorder(BorderFactory.createEmptyBorder(5, 4, 20, 4));
        title.setFont(INSTRUCTION_FONT);
    
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(editingPane, BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
      
        return mainPanel;
    }

    /**
     * Parent table on top.
     * Scene editor in the middle.
     * Child options on the bottom.
     * @return the panel that holds the story editor controls
     */
    private JPanel createEditingPane() {
        JPanel editingPane = new JPanel(new BorderLayout());

        editingPane.add(createParentTable(), BorderLayout.NORTH);
        editingPane.add(createSceneEditingPanel(), BorderLayout.CENTER);
        editingPane.add(createButtonsPanel(), BorderLayout.SOUTH);

        return editingPane;
    }

    /**
     * @return  table holding list of scenes that lead to the currently edited scene.
     */
    private JComponent createParentTable() {
        JPanel parentContainer = new JPanel(new BorderLayout());
        parentScenes_ = story_.getParentScenes();
        ParentTable parentTable = new ParentTable(parentScenes_, this);

        JPanel tableHolder = new JPanel();
        tableHolder.setMaximumSize(new Dimension(500, 300));
        parentContainer.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Parent Scenes" ));

        parentContainer.add(new JScrollPane(parentTable.getTable()), BorderLayout.WEST);

        parentContainer.setPreferredSize(new Dimension(SceneEditorPanel.WIDTH, 120));
        return parentContainer;
    }

    private JPanel createSceneEditingPanel() {
        JPanel container = new JPanel(new BorderLayout());

        sceneEditor = new SceneEditorPanel(story_.getCurrentScene());

        container.add(sceneEditor, BorderLayout.CENTER);
        container.add(createChildTable(), BorderLayout.SOUTH);

        return container;
    }

    /**
     * @return  table of child scene choices.
     */
    private JComponent createChildTable() {
        JPanel childContainer = new JPanel(new BorderLayout());

        childTable_ = new ChildTable(story_.getCurrentScene().getChoices(), this);
        childTable_.addListSelectionListener(this);

        childContainer.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),"Choices (to navigate to child scenes)" ) );

        childContainer.add(new JScrollPane(childTable_.getTable()), BorderLayout.CENTER);

        childContainer.setPreferredSize(new Dimension(SceneEditorPanel.WIDTH, 200));
        return childContainer;
    }

    /**
     * Create the buttons that go at the botton ( eg row editing buttons and OK, Cancel, ...)
     * @return ok cancel panel.
     */
    JPanel createButtonsPanel() {
        JPanel outerPanel = new JPanel( new BorderLayout() );
        JPanel leftButtonsPanel = createLeftButtons();
        JPanel rightButtonsPanel = createRightButtons();

        outerPanel.add(leftButtonsPanel, BorderLayout.WEST);
        outerPanel.add(rightButtonsPanel, BorderLayout.EAST);
        return outerPanel;
    }

    JPanel createLeftButtons() {
        JPanel leftButtonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( addButton_, "Add",
                "Add a new child scene choice to the current scene before the selected position.");
        initBottomButton( removeButton_, "Remove",
                "Remove the child scene at the selected position.");
        initBottomButton( moveUpButton_, "Up",
                "Move the current scene up one row.");
        initBottomButton( moveDownButton_, "Down",
                "Move the current scene down one row.");

        addButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "plus.gif"));
        removeButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "minus.gif"));
        moveUpButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "up_arrow.png"));
        moveDownButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "down_arrow.png"));

        addButton_.setEnabled(false);
        removeButton_.setEnabled(false);
        moveUpButton_.setEnabled(false);
        moveDownButton_.setEnabled(false);

        leftButtonsPanel.add( addButton_ );
        leftButtonsPanel.add( removeButton_ );
        leftButtonsPanel.add( moveUpButton_ );
        leftButtonsPanel.add( moveDownButton_ );
        return leftButtonsPanel;
    }

    JPanel createRightButtons() {
        JPanel rightButtonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( okButton_, "OK",
                "Save your edits and see the changes in the story. " );
        initBottomButton( cancelButton_, "Cancel",
                "Go back to the story without saving your edits." );

        rightButtonsPanel.add( okButton_ );
        rightButtonsPanel.add( cancelButton_ );
        return rightButtonsPanel;
    }


    /**
     * Called when one of the add/remove/move/ok/cancel buttons are clicked for editing choices.
     */
    @Override
    public void actionPerformed( ActionEvent e )
    {
        super.actionPerformed(e);
        Object source = e.getSource();
        int row = selectedChildRow_;
        ChildTableModel childModel = childTable_.getChildTableModel();

        if ( source == okButton_ ) {      
            ok();
        }
        else if (source == addButton_) {
            if (row < childModel.getRowCount())
                addNewChoice(row);
        }
        else if (source == removeButton_) {
            System.out.println("remove row");
            int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete choice "
                    + childModel.getValueAt(row, ChildTable.NAVIGATE_INDEX) +"?");
            if (answer == JOptionPane.YES_OPTION) {
                childModel.removeRow(row);
                story_.getCurrentScene().deleteChoice(row);
            }
        }
        else if (source == moveUpButton_) {
            selectedChildRow_ = childTable_.moveRow(row, row - 1);
            updateMoveButtons();
        }
        else if (source == moveDownButton_) { 
            selectedChildRow_ = childTable_.moveRow(row, row + 1);
            updateMoveButtons();
        }
        e.setSource(null);
    }

    /**
     * @param row  table row
     * @param col  table column
     * @param buttonId id of buttonEditor clicked.
     */
    public void tableButtonClicked(int row, int col, String buttonId) {

        if (ChildTable.NAVIGATE_TO_CHILD_BUTTON_ID.equals(buttonId)) {
            commitSceneChanges();
            story_.advanceScene(row);
        }
        else if (ParentTable.NAVIGATE_TO_PARENT_BUTTON_ID.equals(buttonId)) {
            commitSceneChanges();
            story_.advanceToScene(parentScenes_.get(row).getName());
        }
        else {
            assert false : "unexpected id =" + buttonId;
        }
        showContent();
    }

    /**
     * Show a dialog that allows selecting the new child scene destination.
     * This will be either an exisiting scene or a new one.
     * A new row is automatically added to the table.
     * @param row row of the new choice in the child table.
     */
    private void addNewChoice(int row) {
        NewChoiceDialog newChoiceDlg = new NewChoiceDialog(story_.getCandidateDestinationSceneNames());
        ChildTableModel childModel = childTable_.getChildTableModel();

        boolean canceled = newChoiceDlg.showDialog();
        if (!canceled) {
            String addedSceneName = newChoiceDlg.getSelectedDestinationScene();
            String choiceDescription = childModel.getChoiceDescription(row);
            story_.addChoiceToCurrentScene(addedSceneName, choiceDescription);
            childModel.addNewChildChoice(row, addedSceneName);
            newChoiceDlg.close();
        }
    }

    /**
     * @return our edited copy of the story we were passed at construction.
     */
    public Story getEditedStory() {
        return story_;
    }

    void commitSceneChanges() {
        sceneEditor.doSave();
        if (sceneEditor.isSceneNameChanged()) {
             story_.sceneNameChanged(sceneEditor.getOldSceneName(), sceneEditor.getEditedScene().getName());
        }
        // also save the choice text (it may have been modified)
        childTable_.getChildTableModel().updateSceneChoices(story_.getCurrentScene());
    }

    void ok()
    {
        commitSceneChanges();
        this.setVisible( false );
    }

    /**
     * A row in the child table has been selected or changed selected.
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        selectedChildRow_ = e.getFirstIndex();
        System.out.println("selected row now "+ selectedChildRow_);
        removeButton_.setEnabled(true);
        addButton_.setEnabled(true);
        updateMoveButtons();
    }

    private void updateMoveButtons() {
        moveUpButton_.setEnabled(selectedChildRow_ > 0);
        moveDownButton_.setEnabled(selectedChildRow_ < childTable_.getNumRows() - 2);
    }
}
