/*
 * Copyright (C) 2008, The Schilduil Software Team. All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Schilduil Software Team (http://www.schilduil.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "Schilduil Software Team" or any of its contributors must
 * not be used to endorse or promote products derived from this
 * software without prior written permission. For written permission,
 * please contact license@schilduil.org.
 * 
 * 5. Products derived from this software may not be called "Schilduil",
 * nor may "Schilduil" appear in their name, without prior written
 * permission of the Schilduil Software Team.
 * 
 * 
 * DISCLAIMER
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCHILDUIL DEVELOPMENT TEAM OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE. 
 */

package org.schilduil.procinbreeding;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 * The application's main frame.
 */
public class SuProcInbreedingView extends FrameView {

   private double factor = 2.0;
   private MatrixTableModel matrixData = new MatrixTableModel(factor);
   private File openedFile = null;
   private String pattern = "0.##%"; // "0.000";

   public SuProcInbreedingView(SingleFrameApplication app) {
      super(app);

      initComponents();

      // status bar initialization - message timeout, idle icon and busy animation, etc
      ResourceMap resourceMap = getResourceMap();
      int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
      messageTimer = new Timer(messageTimeout, new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            statusMessageLabel.setText("");
         }
      });
      messageTimer.setRepeats(false);
      int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
      for (int i = 0; i < busyIcons.length; i++) {
         busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
      }
      busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
         }
      });
      idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
      statusAnimationLabel.setIcon(idleIcon);
      progressBar.setVisible(false);

      // connecting action tasks to status bar via TaskMonitor
      TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
      taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

         public void propertyChange(java.beans.PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if ("started".equals(propertyName)) {
               if (!busyIconTimer.isRunning()) {
                  statusAnimationLabel.setIcon(busyIcons[0]);
                  busyIconIndex = 0;
                  busyIconTimer.start();
               }
               progressBar.setVisible(true);
               progressBar.setIndeterminate(true);
            } else if ("done".equals(propertyName)) {
               busyIconTimer.stop();
               statusAnimationLabel.setIcon(idleIcon);
               progressBar.setVisible(false);
               progressBar.setValue(0);
            } else if ("message".equals(propertyName)) {
               String text = (String) (evt.getNewValue());
               statusMessageLabel.setText((text == null) ? "" : text);
               messageTimer.restart();
            } else if ("progress".equals(propertyName)) {
               int value = (Integer) (evt.getNewValue());
               progressBar.setVisible(true);
               progressBar.setIndeterminate(false);
               progressBar.setValue(value);
            }
         }
      });
   }

   @Action
   public void showAboutBox() {
      if (aboutBox == null) {
         JFrame mainFrame = SuProcInbreedingApp.getApplication().getMainFrame();
         aboutBox = new AboutBox(mainFrame);
         aboutBox.setLocationRelativeTo(mainFrame);
      }
      SuProcInbreedingApp.getApplication().show(aboutBox);
   }

   @Action()
   public void showHelpBox() {
      if (helpBox == null) {
         JFrame mainFrame = SuProcInbreedingApp.getApplication().getMainFrame();
         helpBox = new HelpBox(mainFrame);
         helpBox.setLocationRelativeTo(mainFrame);
      }
      SuProcInbreedingApp.getApplication().show(helpBox);
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mainPanel = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      matrix = new javax.swing.JTable();
      menuBar = new javax.swing.JMenuBar();
      javax.swing.JMenu fileMenu = new javax.swing.JMenu();
      openMenuItem = new javax.swing.JMenuItem();
      openUrlMenuItem = new javax.swing.JMenuItem();
      exportCsvMenuItem = new javax.swing.JMenuItem();
      jMenuItem1 = new javax.swing.JMenuItem();
      javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenu helpMenu = new javax.swing.JMenu();
      helpMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
      statusPanel = new javax.swing.JPanel();
      javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
      statusMessageLabel = new javax.swing.JLabel();
      statusAnimationLabel = new javax.swing.JLabel();
      progressBar = new javax.swing.JProgressBar();

      mainPanel.setName("mainPanel"); // NOI18N

      jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      jScrollPane1.setName("jScrollPane1"); // NOI18N

      matrix.setModel(matrixData);
      matrix.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
      matrix.setName("matrix"); // NOI18N
      // Set the default renderer
      matrix.setDefaultRenderer(Double.class, new MatrixCellDoubleRenderer(this.pattern, this.factor));
      jScrollPane1.setViewportView(matrix);

      javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
      mainPanel.setLayout(mainPanelLayout);
      mainPanelLayout.setHorizontalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 560, Short.MAX_VALUE)
         .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
               .addGap(0, 0, 0)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
               .addGap(0, 0, 0)))
      );
      mainPanelLayout.setVerticalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 388, Short.MAX_VALUE)
         .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
               .addGap(0, 0, 0)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
               .addGap(0, 0, 0)))
      );

      menuBar.setName("menuBar"); // NOI18N

      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.schilduil.procinbreeding.SuProcInbreedingApp.class).getContext().getResourceMap(SuProcInbreedingView.class);
      fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
      fileMenu.setName("fileMenu"); // NOI18N

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.schilduil.procinbreeding.SuProcInbreedingApp.class).getContext().getActionMap(SuProcInbreedingView.class, this);
      openMenuItem.setAction(actionMap.get("openFile")); // NOI18N
      openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
      openMenuItem.setMnemonic('O');
      openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
      openMenuItem.setToolTipText(resourceMap.getString("openMenuItem.toolTipText")); // NOI18N
      openMenuItem.setName("openMenuItem"); // NOI18N
      fileMenu.add(openMenuItem);

      openUrlMenuItem.setAction(actionMap.get("openUrl")); // NOI18N
      openUrlMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
      openUrlMenuItem.setText(resourceMap.getString("openUrlMenuItem.text")); // NOI18N
      openUrlMenuItem.setToolTipText(resourceMap.getString("openUrlMenuItem.toolTipText")); // NOI18N
      openUrlMenuItem.setName("openUrlMenuItem"); // NOI18N
      fileMenu.add(openUrlMenuItem);

      exportCsvMenuItem.setAction(actionMap.get("exportToFile")); // NOI18N
      exportCsvMenuItem.setEnabled(false);
      exportCsvMenuItem.setName("exportCsvMenuItem"); // NOI18N
      fileMenu.add(exportCsvMenuItem);

      jMenuItem1.setAction(actionMap.get("openCvsFile")); // NOI18N
      jMenuItem1.setEnabled(false);
      jMenuItem1.setName("jMenuItem1"); // NOI18N
      fileMenu.add(jMenuItem1);

      exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
      exitMenuItem.setName("exitMenuItem"); // NOI18N
      fileMenu.add(exitMenuItem);

      menuBar.add(fileMenu);

      helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
      helpMenu.setName("helpMenu"); // NOI18N

      helpMenuItem.setAction(actionMap.get("showHelpBox")); // NOI18N
      helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
      helpMenuItem.setText(resourceMap.getString("helpMenuItem.text")); // NOI18N
      helpMenuItem.setToolTipText(resourceMap.getString("helpMenuItem.toolTipText")); // NOI18N
      helpMenuItem.setName("helpMenuItem"); // NOI18N
      helpMenu.add(helpMenuItem);

      aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
      aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
      aboutMenuItem.setName("aboutMenuItem"); // NOI18N
      helpMenu.add(aboutMenuItem);

      menuBar.add(helpMenu);

      statusPanel.setName("statusPanel"); // NOI18N

      statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

      statusMessageLabel.setName("statusMessageLabel"); // NOI18N

      statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
      statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

      progressBar.setName("progressBar"); // NOI18N

      javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
      statusPanel.setLayout(statusPanelLayout);
      statusPanelLayout.setHorizontalGroup(
         statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
         .addGroup(statusPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(statusMessageLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 386, Short.MAX_VALUE)
            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(statusAnimationLabel)
            .addContainerGap())
      );
      statusPanelLayout.setVerticalGroup(
         statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(statusPanelLayout.createSequentialGroup()
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(statusMessageLabel)
               .addComponent(statusAnimationLabel)
               .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(3, 3, 3))
      );

      setComponent(mainPanel);
      setMenuBar(menuBar);
      setStatusBar(statusPanel);
   }// </editor-fold>//GEN-END:initComponents
   @Action
   public void openFile() {

      JFileChooser chooser = new JFileChooser();
      if (this.openedFile != null) {
         chooser.setCurrentDirectory(this.openedFile.getParentFile());
      }
      int returnVal = chooser.showOpenDialog(this.mainPanel);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         // chooser.getSelectedFile().getName()
         this.openedFile = chooser.getSelectedFile();
         processFile(chooser.getSelectedFile());
      }

   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JMenuItem exportCsvMenuItem;
   private javax.swing.JMenuItem helpMenuItem;
   private javax.swing.JMenuItem jMenuItem1;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JTable matrix;
   private javax.swing.JMenuBar menuBar;
   private javax.swing.JMenuItem openMenuItem;
   private javax.swing.JMenuItem openUrlMenuItem;
   private javax.swing.JProgressBar progressBar;
   private javax.swing.JLabel statusAnimationLabel;
   private javax.swing.JLabel statusMessageLabel;
   private javax.swing.JPanel statusPanel;
   // End of variables declaration//GEN-END:variables
   private final Timer messageTimer;
   private final Timer busyIconTimer;
   private final Icon idleIcon;
   private final Icon[] busyIcons = new Icon[15];
   private int busyIconIndex = 0;
   private JDialog aboutBox;
   private JDialog helpBox;
   // private JDialog openBox;
   private OpenUrlDialog urlDialog;
   private InbreedingMatrix kinshipMatrix;
   private InbreedingMatrix purifiedKinshipMatrix;

   private void processFile(File selectedFile) {
      try {
         statusMessageLabel.setText(selectedFile.toString());
         progressBar.setValue(0);
         kinshipMatrix = new InbreedingMatrix(selectedFile);
         this.exportCsvMenuItem.setEnabled(true);
         if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
               jMenuItem1.setEnabled(true);
            }
         }
         matrixData.setMatrix(kinshipMatrix);
      } catch (IOException ex) {
         Logger.getLogger(SuProcInbreedingView.class.getName()).log(Level.SEVERE, null, ex);
         statusMessageLabel.setText(ex.getLocalizedMessage());
      } catch (PIPopulationException ex) {
         Logger.getLogger(SuProcInbreedingView.class.getName()).log(Level.SEVERE, null, ex);
         statusMessageLabel.setText(ex.getLocalizedMessage());
      }
   }

   private void processFile(URL selectedFile) {
      try {
         statusMessageLabel.setText(selectedFile.toString());
         progressBar.setValue(0);
         kinshipMatrix = new InbreedingMatrix(selectedFile);
         this.exportCsvMenuItem.setEnabled(true);
         if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
               jMenuItem1.setEnabled(true);
            }
         }
         matrixData.setMatrix(kinshipMatrix);
      } catch (IOException ex) {
         Logger.getLogger(SuProcInbreedingView.class.getName()).log(Level.SEVERE, null, ex);
         statusMessageLabel.setText(ex.getLocalizedMessage());
      } catch (PIPopulationException ex) {
         Logger.getLogger(SuProcInbreedingView.class.getName()).log(Level.SEVERE, null, ex);
         statusMessageLabel.setText(ex.getLocalizedMessage());
      }
   }

   @Action
   public void openUrl() {
      if (urlDialog == null) {
         JFrame mainFrame = SuProcInbreedingApp.getApplication().getMainFrame();
         urlDialog = new OpenUrlDialog(mainFrame);
         urlDialog.setLocationRelativeTo(mainFrame);
      }
      SuProcInbreedingApp.getApplication().show(urlDialog);
      this.processFile(this.urlDialog.getUrl());

   }

   @Action
   public void openCvsFile() {

      if (this.matrix != null) {

         File file = getExportFile();
         if (file != null) {
            this.exportToFile(file);
         }

         if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
               try {
                  desktop.open(file);
               } catch (IOException ex) {
               // Logger.getLogger(SuProcInbreedingView.class.getName()).log(Level.SEVERE, null, ex);
               }
            }

         }

      }

   }

   @Action
   public void exportToFile() {

      if (this.matrix != null) {

         File file = getExportFile();
         if (file != null) {
            this.exportToFile(file);
         }

      }

   }

   public File getExportFile() {

      JFileChooser chooser = new JFileChooser();
      if (this.openedFile != null) {
         chooser.setCurrentDirectory(this.openedFile.getParentFile());
      }

      chooser.setFileFilter(new FileFilter() {

         @Override
         public boolean accept(File arg0) {
            return true;
         }

         @Override
         public String getDescription() {
            return "CSV files";
         }
      });
      int returnVal = chooser.showOpenDialog(this.mainPanel);
      if (returnVal == JFileChooser.APPROVE_OPTION) {

         return chooser.getSelectedFile();

      }

      return null;

   }

   public void exportToFile(File file) {

      try {

         char decimal = '.';
         char separator = ',';
         NumberFormat nf = NumberFormat.getNumberInstance();
         String parsing = nf.format(8.9);
         if (parsing.length() > 1) {
            if (parsing.charAt(1) == ',') {
               decimal = ',';
               separator = ';';
            }
         }
         this.kinshipMatrix.saveToCsv(file, decimal, separator, true);
         
      } catch (IOException ex) {
         this.statusMessageLabel.setText("Couldn't save to " + file + ". (" + ex.getLocalizedMessage() + ")");
      } catch (PIPopulationException ex) {
         this.statusMessageLabel.setText("Couldn't save to " + file + ". (" + ex.getLocalizedMessage() + ")");
      }

      this.statusMessageLabel.setText("Exported to " + file + " saved.");

   }
}
