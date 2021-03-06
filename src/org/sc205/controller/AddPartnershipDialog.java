/*
 *  Copyright 2011 Michael R. Elliott <mre@m79.net>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * AddPartnershipDialog.java
 *
 * Created on Dec 4, 2010, 3:46:51 PM
 */
package org.sc205.controller;

import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.sc205.IslsScoreView;
import org.sc205.model.GameSet;
import org.sc205.model.Partnership;
import org.sc205.model.Partnership.Affiliation;
import org.sc205.view.ScoreboardFrame;

/**
 *
 * @author mre
 */
public class AddPartnershipDialog extends javax.swing.JDialog
     implements Observer {

   /** Creates new form AddPartnershipDialog */
   public AddPartnershipDialog( java.awt.Frame parent, boolean modal ) {
      super( parent, modal );
      initComponents();
      affiliationSelector.setBorder( affiliationBoxBorder() );
      affiliationSelector.getObservable().addObserver( this );
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      affiliationGroup = new javax.swing.ButtonGroup();
      buttonGroup1 = new javax.swing.ButtonGroup();
      cancel = new javax.swing.JButton();
      ok = new javax.swing.JButton();
      title = new javax.swing.JLabel();
      names = new javax.swing.JPanel();
      name0 = new javax.swing.JTextField();
      name1 = new javax.swing.JTextField();
      affiliationSelector = new org.sc205.view.FlagRadioButtonForm();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setName("Form"); // NOI18N

      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.sc205.ISLS.class).getContext().getResourceMap(AddPartnershipDialog.class);
      cancel.setText(resourceMap.getString("cancel.text")); // NOI18N
      cancel.setName("cancel"); // NOI18N
      cancel.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelActionPerformed(evt);
         }
      });

      ok.setText(resourceMap.getString("ok.text")); // NOI18N
      ok.setName("ok"); // NOI18N
      ok.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            okActionPerformed(evt);
         }
      });

      title.setBackground(resourceMap.getColor("title.background")); // NOI18N
      title.setFont(resourceMap.getFont("title.font")); // NOI18N
      title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      title.setText(resourceMap.getString("title.text")); // NOI18N
      title.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      title.setName("title"); // NOI18N
      title.setOpaque(true);

      names.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("names.border.title"))); // NOI18N
      names.setName("names"); // NOI18N

      name0.setText(resourceMap.getString("name0.text")); // NOI18N
      name0.setName("name0"); // NOI18N

      name1.setText(resourceMap.getString("name1.text")); // NOI18N
      name1.setName("name1"); // NOI18N

      javax.swing.GroupLayout namesLayout = new javax.swing.GroupLayout(names);
      names.setLayout(namesLayout);
      namesLayout.setHorizontalGroup(
         namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(namesLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(name0, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
               .addComponent(name1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
            .addContainerGap())
      );
      namesLayout.setVerticalGroup(
         namesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(namesLayout.createSequentialGroup()
            .addComponent(name0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(name1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      affiliationSelector.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("affiliationSelector.border.title"))); // NOI18N
      affiliationSelector.setName("affiliationSelector"); // NOI18N

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(affiliationSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(11, 11, 11)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(names, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                     .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 267, Short.MAX_VALUE)
                        .addComponent(ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel)))))
            .addContainerGap())
      );

      layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancel, ok});

      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(names, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(cancel)
                     .addComponent(ok)))
               .addComponent(affiliationSelector, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
            .addContainerGap())
      );

      layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancel, ok});

      pack();
   }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
       this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
       Partnership.Affiliation affiliation = affiliationSelector.selection();
       String player1 = this.name0.getText();
       String player2 = this.name1.getText();
       final boolean looksOK = affiliation != null
            && player1.trim().length() > 0 && player2.trim().length() > 0;
       if (looksOK) {
          Partnership ps = new Partnership( affiliation );
          ps.setBaseName( 0, player1 );
          ps.setBaseName( 1, player2 );
          GameSet.instance().add( ps );
          final ScoreboardFrame sb = IslsScoreView.scoreboard;
          IslsScoreView.scoreboard = new ScoreboardFrame( sb ).layout( sb );
          dispose();
       }
    }//GEN-LAST:event_okActionPerformed

   /**
    * @param args the command line arguments
    */
   public static void main( String args[] ) {
      java.awt.EventQueue.invokeLater( new Runnable() {

         public void run() {
            AddPartnershipDialog dialog = new AddPartnershipDialog( new javax.swing.JFrame(), true );
            dialog.addWindowListener( new java.awt.event.WindowAdapter() {

               public void windowClosing( java.awt.event.WindowEvent e ) {
                  System.exit( 0 );
               }
            } );
            dialog.setVisible( true );
         }
      } );
   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.ButtonGroup affiliationGroup;
   private org.sc205.view.FlagRadioButtonForm affiliationSelector;
   private javax.swing.ButtonGroup buttonGroup1;
   private javax.swing.JButton cancel;
   private javax.swing.JTextField name0;
   private javax.swing.JTextField name1;
   private javax.swing.JPanel names;
   private javax.swing.JButton ok;
   private javax.swing.JLabel title;
   // End of variables declaration//GEN-END:variables

   private Border affiliationBoxBorder() {
      String borderTitle = "Select Affiliation";
      final Affiliation selection = affiliationSelector.selection();
      if (selection != null)
         switch (selection) {
            case AMERICAN:
               borderTitle = "American/Canadian";
               break;
            case EUROPEAN:
               borderTitle = "European";
               break;
         }
      return BorderFactory.createTitledBorder( borderTitle );
   }

   public void update( Observable o, Object o1 ) {
      affiliationSelector.setBorder( affiliationBoxBorder() );
   }
}
