package com.becker.ui;

import com.becker.ui.AbstractCellEditor;

import javax.swing.*;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import java.awt.*;

/**
 * ColorCellRenderer renders a cell in a table that reperesents a color
 * @see com.becker.ui.ColorCellRenderer
 *
 * @author Barry Becker
 */
public class ColorCellEditor extends AbstractCellEditor
{

    ColorCellRenderer cellRenderer_ = new ColorCellRenderer();
    String title_;

    public ColorCellEditor(String title)
    {
        title_ = title;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int col)
    {
        // we know the value is a Color
        Color color = (Color)value;
        Color selectedColor = JColorChooser.showDialog(table, title_, color );
        if (selectedColor == null)  // then it was canceled.
           selectedColor = color;

        this.setCellEditorValue(selectedColor);
        table.getModel().setValueAt(selectedColor, row, col);    // shouldn't need this

        return cellRenderer_.getTableCellRendererComponent(table, selectedColor, true, isSelected, row, col);
    }

}
