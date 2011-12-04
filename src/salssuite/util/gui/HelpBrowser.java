/*
 * HelpBrowser.java
 *
 * Created on 05.06.2011, 00:06:30
 */

/*
 *  SalSSuite - Suite of programmes for managing a SalS project
 *  Copyright (C) 2011  Jannis Limperg <jannis[dot]limperg[at]arcor[dot]de>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package salssuite.util.gui;


import com.petebevin.markdown.MarkdownProcessor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;

/**
 * Enormously simple 'web browser' to display
 * <a href='https://github.com/JLimperg/SalSSuite/wiki'>wiki pages</a> from the
 * Github Wiki. These pages are formatted as Markdown documents and accessible via
 * standardised URLs. This frame displays HTML content (using a
 * <code>JEditorPane</code>) which it generates 'on the fly', parsing the Markdown
 * files. It supports relative hyperlinks and a very basic page history. Links
 * to files that are do not end in ".md" are blocked because the
 * <code>JEditorPane</code> will likely not be able to render them correctly.
 * <p>
 * This browser of course also works with local resources, not only remote
 * ones. The Wiki pages <a href='https://github.com/JLimperg/SalSSuite/wiki/_access'>
 * are available as a Git repository</a>, so it would be an option to include
 * them in the jar files (using the ant script) and access them as local
 * resources via <code>getClass().getResource()</code>.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class HelpBrowser extends javax.swing.JFrame {

    //Serialization not supported!
    private static final long serialVersionUID = 1;

    /**
     * Constructor for non-standard URLs.
     * @param document The Markdown file to load initially when creating the
     * browser. May not be <code>null</code>.</code>.
     */
    public HelpBrowser(URL document) {
        initComponents();
        setLocationRelativeTo(null);

        if(document != null)
            currentURL = document;
        else {
            JOptionPane.showMessageDialog(this, "Entschuldigung, der Hilfe-Browser"
                    + " konnte nicht initialisiert werden.", "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        
        startURL = currentURL;
        loadURL(currentURL);
    }

    /**
     * Constructor for standard Wiki URLs. Guesses the URL to load initially
     * based on the given <code>classname</code>.
     * @param classname The name of the class from which the user is calling
     * for help, usually a client. The <code>HelpBrowser</code> will determine
     * the URL to load initially by replacing all occurrences of
     * code>&lt;class&gt;</code> in {@link #DEFAULT_URL_TEMPLATE} with
     * <code>classname</code>. Obviously you should make sure that URL exists
     * and is accessible.
     */
    public HelpBrowser(String classname) {
        initComponents();
        setLocationRelativeTo(null);
        
        try {
            currentURL = new URL(DEFAULT_URL_TEMPLATE.replaceAll("<class>",
                    classname));
            if(currentURL == null)
                throw new MalformedURLException();
        }
        catch(MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Entschuldigung, der Hilfe-Browser"
                    + " konnte nicht initialisiert werden.", "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        startURL = currentURL;
        loadURL(currentURL);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        main = new javax.swing.JEditorPane();
        backButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SalSSuite - Hilfe-Browser");

        main.setContentType("text/html");
        main.setEditable(false);
        main.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                followHyperlink(evt);
            }
        });
        jScrollPane1.setViewportView(main);

        backButton.setText("Zurück");
        backButton.setToolTipText("Gehe eine Seite zurück.");
        backButton.setEnabled(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backInHistory(evt);
            }
        });

        jButton3.setText("Zum Anfang");
        jButton3.setToolTipText("Gehe zum Anfang.");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoStartPage(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 521, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void followHyperlink(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_followHyperlink
        if(evt.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
            return;

        URL newURL;

        //if it's an absolute link: check if it links to some non-markdown file
        if(evt.getURL() != null) {
            if(!evt.getURL().toString().endsWith(".md")) {
                JOptionPane.showMessageDialog(this, "<html>Links auf externe Seiten "
                        + "können<br/>leider nicht angezeigt werden.</html>",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newURL = evt.getURL();
        }
        else { //if not, it's a relative link to another wiki file
            try {
                //strip current filename from current url
                String[] urlParts = currentURL.toString().split("/");
                String newURLString = "";
                for(int ct = 0; ct < urlParts.length; ct++)
                    if(ct != urlParts.length-1)
                        newURLString += urlParts[ct] + "/";

                /*
                 * Strip "wiki/" part from the relative link if we operate
                 * on "Home.md". This is necessary because the Github Wiki system
                 * redirects https://github.com/.../wiki/Home to
                 * https://github.com/.../wiki, therefore relative links on
                 * the "Home" page have to look like
                 * "wiki/thepageyouactuallywanttoload". But the file Home.md is
                 * _not_ redirected, so the relative link with the "wiki" part
                 * points to nowhere.
                 */
                String relativeLink = evt.getDescription() + ".md";
                String[] currentURLParts = currentURL.toString().split("/");
                if(currentURLParts[currentURLParts.length-1].equals("Home.md"))
                    relativeLink = relativeLink.replace("wiki/", "");

                //append new filename to stripped url -> relative link
                newURLString += relativeLink;
                newURL = new URL(newURLString);
            }
            catch(MalformedURLException e) {
                JOptionPane.showMessageDialog(this, "Kann Hyperlink nicht folgen.",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        //update the history
        backwardHistory.add(currentURL);
        backButton.setEnabled(true);

        //load the URL
        loadURL(newURL);
    }//GEN-LAST:event_followHyperlink

    private void gotoStartPage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoStartPage
        loadURL(startURL);
    }//GEN-LAST:event_gotoStartPage

    private void backInHistory(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backInHistory
        URL newURL = backwardHistory.getLast();
        backwardHistory.remove(newURL);
        if(backwardHistory.isEmpty())
            backButton.setEnabled(false);
        loadURL(newURL);
    }//GEN-LAST:event_backInHistory

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane main;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    /**
     *
     */
    public static final String DEFAULT_URL_TEMPLATE =
            "https://github.com/JLimperg/SalSSuite/wiki/Overview_<class>.md";

    //===============================FIELDS===================================//

    URL currentURL;
    URL startURL;

    LinkedList<URL> backwardHistory = new LinkedList<URL>();

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Loads the specified URL as the JEditorPane's content. The URL is parsed
     * using the markdown parser if necessary.
     * @param url The URL to load.
     */
    private void loadURL(URL url) {
        //get contents from the file
        String text = "";

        try {
            URLConnection con = url.openConnection();
            Scanner in = new Scanner(new InputStreamReader(con.getInputStream()));
            while(in.hasNext()) {
                text += in.nextLine() + "\n";
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Kann URL nicht laden.",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        //process them using markdown if necessary
        if(url.toString().endsWith(".md")) {
            text = new MarkdownProcessor().markdown(text);

            /*
             * Enormously quick and dirty fix for "<" and ">" escaping. The
             * Markdown version of Github's wiki extends Markdown by providing
             * a "<SomeText>" syntax, therefore "<" and ">" need to be escaped
             * using backslashes.
             */
            text = text.replaceAll("\\\\&lt;", "&lt;");
            text = text.replaceAll("\\\\>", "&gt;");
        }

        //set EditorPane's text
        main.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
        main.setText(text);
        main.setCaretPosition(0);

        currentURL = url;
    }

    //============================INNER CLASSES===============================//
}