/*
 * HelpBox.java
 *
 * Created on 17 juli 2009, 21:28
 */

package org.schilduil.procinbreeding;

/**
 *
 * @author  bert
 */
public class HelpBox extends javax.swing.JDialog {
   
   /** Creates new form HelpBox */
   public HelpBox(java.awt.Frame parent) {
      super(parent, false);
      initComponents();
   }
   
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jScrollPane1 = new javax.swing.JScrollPane();
      jTextPane1 = new javax.swing.JTextPane();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.schilduil.procinbreeding.SuProcInbreedingApp.class).getContext().getResourceMap(HelpBox.class);
      setTitle(resourceMap.getString("HelpBox.title")); // NOI18N
      setName("HelpBox"); // NOI18N

      jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      jScrollPane1.setName("jScrollPane1"); // NOI18N

      jTextPane1.setContentType("text/html");
      jTextPane1.setEditable(false);
      java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/schilduil/procinbreeding/resources/HelpBox"); // NOI18N
      jTextPane1.setText(bundle.getString("helptext")); // NOI18N
      jTextPane1.setName("jTextPane1"); // NOI18N
      jTextPane1.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            HelpBox.this.keyPressed(evt);
         }
      });
      jScrollPane1.setViewportView(jTextPane1);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void keyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyPressed
      setVisible(false);
   }//GEN-LAST:event_keyPressed
   
   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            HelpBox dialog = new HelpBox(new javax.swing.JFrame());
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
               @Override
               public void windowClosing(java.awt.event.WindowEvent e) {
                  System.exit(0);
               }
            });
            dialog.setVisible(true);
         }
      });
   }
   
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JTextPane jTextPane1;
   // End of variables declaration//GEN-END:variables
   
}
