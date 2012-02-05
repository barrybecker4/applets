// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.comparison.ui.grid;

import com.becker.game.twoplayer.comparison.model.*;
import com.becker.game.twoplayer.comparison.ui.grid.cellrenderers.FirstColumnCellRenderer;
import com.becker.game.twoplayer.comparison.ui.grid.cellrenderers.ResultGridCellRenderer;
import com.becker.game.twoplayer.comparison.ui.grid.cellrenderers.ResultHeaderCellRenderer;
import com.becker.ui.table.BasicTableModel;
import com.becker.ui.table.TableBase;
import com.becker.ui.table.TableColumnMeta;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
 * Turns the list of search option configurations into a NxN grid
 * to compare the performance of all the search configurations in the list.
 * 
 * @author Barry Becker
 */
class ComparisonGrid extends TableBase {

    private static final int HEADER_HEIGHT = 40;
    private String[] colNames;


    /**
     * Constructor
     * @param optionsList to initialize the rows in the table with. May be null.
     */
    public static ComparisonGrid createInstance(SearchOptionsConfigList optionsList)  {
        return new ComparisonGrid(optionsList, createColumnNames(optionsList));
    }
    
    /** Set the height of the rows */
    public void updateRowHeight(int height) {
        getTable().setRowHeight((height-HEADER_HEIGHT)/getNumRows());
    }

    /**
     * Constructor
     * @param optionsList to initialize the rows in the table with. May be null.
     */
    private ComparisonGrid(SearchOptionsConfigList optionsList, String[] colNames)  {
        super(optionsList, colNames);
        this.colNames = colNames;
        this.initializeTable(optionsList);
        this.setRowHeight(60);

        for (int i = 1; i < getNumColumns(); i++) {

            TableColumn col = table_.getColumnModel().getColumn(i);
            col.setHeaderRenderer(new ResultHeaderCellRenderer());
        }
    }
    
    private static String[] createColumnNames(SearchOptionsConfigList optionsList) {
        String[] names = new String[optionsList.size() + 1];
        names[0] = "---";
        int i=1;
        for (SearchOptionsConfig config : optionsList) {
             names[i++] = config.getName();
        }
        return names;
    }

    @Override
    protected void updateColumnMeta(TableColumnMeta[] columnMeta) {

        columnMeta[0].setPreferredWidth(210);
        columnMeta[0].setMaxWidth(310);
        columnMeta[0].setCellRenderer(new FirstColumnCellRenderer());

        for (int i = 1; i < getNumColumns(); i++) {
            columnMeta[i].setCellRenderer(new ResultGridCellRenderer());

            TableColumn col = table_.getColumnModel().getColumn(i);
            col.setHeaderRenderer(new ResultHeaderCellRenderer());
        }
    }


    @Override
    protected TableModel createTableModel(String[] columnNames) {
        return new BasicTableModel(columnNames, 0, false);
    }

    @Override
    public BasicTableModel getModel() {
        return (BasicTableModel)table_.getModel();
    }

    /**
     * add a row based on a player object
     * @param opts to add
     */
    @Override
    public void addRow(Object opts) {

        SearchOptionsConfig optionsConfig = (SearchOptionsConfig) opts;

        if (colNames == null)   return;

        Object[] d = new Object[colNames.length + 1];

        d[0] = optionsConfig.getName();
        for (int i = 1; i <= colNames.length; i++) {
            d[i] = new PerformanceResultsPair();
        }
        getModel().addRow(d);
    }

    public void updateWithResults(ResultsModel model) {
        int size = colNames.length-1;
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                getModel().setValueAt(model.getResults(i,j), i, j + 1);
            }
        }
    }
}
