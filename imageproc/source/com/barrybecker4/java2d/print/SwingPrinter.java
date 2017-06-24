package com.barrybecker4.java2d.print;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class SwingPrinter extends JFrame {
    public static void main( String[] args ) {
        new SwingPrinter();
    }

    private PageFormat mPageFormat;

    public SwingPrinter() {
        super( "SwingPrinter v1.0" );
        createUI();
        PrinterJob pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();
        setVisible( true );
    }

    private void createUI() {
        setSize( 300, 300 );
        center();

        // Add the menu bar.
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu( "File", true );
        file.add( new FilePrintAction() ).setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK ) );
        file.add( new FilePageSetupAction() ).setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_P,
                        InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ) );
        file.addSeparator();
        file.add( new FileQuitAction() ).setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_Q, InputEvent.CTRL_MASK ) );
        mb.add( file );
        setJMenuBar( mb );

        // Add the contents of the window.
        getContentPane().add( new PatchworkComponent() );

        // Exit the application when the window is closed.
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e ) {
                System.exit( 0 );
            }
        } );
    }

    protected void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension us = getSize();
        int x = (screen.width - us.width) / 2;
        int y = (screen.height - us.height) / 2;
        setLocation( x, y );
    }

    public class FilePrintAction
            extends AbstractAction {
        public FilePrintAction() {
            super( "Print" );
        }

        @Override
        public void actionPerformed( ActionEvent ae ) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            ComponentPrintable cp = new ComponentPrintable( getContentPane() );
            pj.setPrintable( cp, mPageFormat );
            if ( pj.printDialog() ) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.out.println( e );
                }
            }
        }
    }

    public class FilePageSetupAction
            extends AbstractAction {
        public FilePageSetupAction() {
            super( "Page setup..." );
        }

        @Override
        public void actionPerformed( ActionEvent ae ) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            mPageFormat = pj.pageDialog( mPageFormat );
        }
    }

    public static class FileQuitAction extends AbstractAction {
        public FileQuitAction() {
            super( "Quit" );
        }

        @Override
        public void actionPerformed( ActionEvent ae ) {
            System.exit( 0 );
        }
    }
}