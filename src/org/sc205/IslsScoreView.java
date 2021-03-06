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
 * IslsScoreView.java
 */
package org.sc205;

import java.awt.Color;
import org.sc205.controller.AddPartnershipDialog;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.sc205.model.GameSet;
import org.sc205.view.ScoreboardFrame;

/**
 * The application's main frame.
 */
final public class IslsScoreView extends FrameView {

   public static IslsScoreView INSTANCE;

   private TimerTask tt = new TimerTask() {

      @Override
      public void run() {
         if (!GameSet.instance().allGames().isEmpty())
            GameSet.instance().updateUnplayable();
      }
   };

   private java.util.Timer timer = new java.util.Timer();

   public IslsScoreView( SingleFrameApplication app ) {
      super( app );

      initComponents();
      INSTANCE = this;
      timer.schedule( tt, 2000, 1000 );
      
      // status bar initialization - message timeout, idle icon and busy animation, etc
      ResourceMap resourceMap = getResourceMap();
      int messageTimeout = resourceMap.getInteger( "StatusBar.messageTimeout" );
      messageTimer = new Timer( messageTimeout, new ActionListener() {

         public void actionPerformed( ActionEvent e ) {
            statusMessageLabel.setText( "" );
         }
      } );
      messageTimer.setRepeats( false );
      int busyAnimationRate = resourceMap.getInteger( "StatusBar.busyAnimationRate" );
      for (int i = 0; i < busyIcons.length; i++)
         busyIcons[i] = resourceMap.getIcon( "StatusBar.busyIcons[" + i + "]" );
      busyIconTimer = new Timer( busyAnimationRate, new ActionListener() {

         public void actionPerformed( ActionEvent e ) {
            busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            statusAnimationLabel.setIcon( busyIcons[busyIconIndex] );
         }
      } );
      idleIcon = resourceMap.getIcon( "StatusBar.idleIcon" );
      statusAnimationLabel.setIcon( idleIcon );
      progressBar.setVisible( false );

      // connecting action tasks to status bar via TaskMonitor
      TaskMonitor taskMonitor = new TaskMonitor( getApplication().getContext() );
      taskMonitor.addPropertyChangeListener( new java.beans.PropertyChangeListener() {

         public void propertyChange( java.beans.PropertyChangeEvent evt ) {
            String propertyName = evt.getPropertyName();
            if ("started".equals( propertyName )) {
               if (!busyIconTimer.isRunning()) {
                  statusAnimationLabel.setIcon( busyIcons[0] );
                  busyIconIndex = 0;
                  busyIconTimer.start();
               }
               progressBar.setVisible( true );
               progressBar.setIndeterminate( true );
            }
            else if ("done".equals( propertyName )) {
               busyIconTimer.stop();
               statusAnimationLabel.setIcon( idleIcon );
               progressBar.setVisible( false );
               progressBar.setValue( 0 );
            }
            else if ("message".equals( propertyName )) {
               String text = (String)(evt.getNewValue());
               statusMessageLabel.setText( (text == null) ? "" : text );
               messageTimer.restart();
            }
            else if ("progress".equals( propertyName )) {
               int value = (Integer)(evt.getNewValue());
               progressBar.setVisible( true );
               progressBar.setIndeterminate( false );
               progressBar.setValue( value );
            }
         }
      } );
   }

   @Action
   public void showAboutBox() {
      if (aboutBox == null) {
         JFrame mainFrame = ISLS.getApplication().getMainFrame();
         aboutBox = new IslsScoreAboutBox( mainFrame );
         aboutBox.setLocationRelativeTo( mainFrame );
      }
      ISLS.getApplication().show( aboutBox );
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mainPanel = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      maxGames = new javax.swing.JTextField();
      jPanel1 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      leftText = new javax.swing.JTextPane();
      jScrollPane2 = new javax.swing.JScrollPane();
      rightText = new javax.swing.JTextPane();
      menuBar = new javax.swing.JMenuBar();
      javax.swing.JMenu fileMenu = new javax.swing.JMenu();
      addPartnershipMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenu helpMenu = new javax.swing.JMenu();
      javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
      objectives = new javax.swing.JMenuItem();
      guidance = new javax.swing.JMenuItem();
      guidelines = new javax.swing.JMenuItem();
      jSeparator1 = new javax.swing.JPopupMenu.Separator();
      jMenuItem1 = new javax.swing.JMenuItem();
      statusPanel = new javax.swing.JPanel();
      javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
      statusMessageLabel = new javax.swing.JLabel();
      statusAnimationLabel = new javax.swing.JLabel();
      progressBar = new javax.swing.JProgressBar();

      mainPanel.setName("mainPanel"); // NOI18N

      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.sc205.ISLS.class).getContext().getResourceMap(IslsScoreView.class);
      jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
      jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
      jLabel1.setName("jLabel1"); // NOI18N

      maxGames.setText(resourceMap.getString("maxGames.text")); // NOI18N
      maxGames.setName("maxGames"); // NOI18N
      maxGames.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            maxGamesActionPerformed(evt);
         }
      });

      jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      jPanel1.setName("jPanel1"); // NOI18N

      jScrollPane1.setName("jScrollPane1"); // NOI18N

      leftText.setBackground(resourceMap.getColor("jTextPane1.background")); // NOI18N
      leftText.setEditable(false);
      leftText.setFont(resourceMap.getFont("leftText.font")); // NOI18N
      leftText.setText(resourceMap.getString("leftText.text")); // NOI18N
      leftText.setName("leftText"); // NOI18N
      jScrollPane1.setViewportView(leftText);

      jScrollPane2.setName("jScrollPane2"); // NOI18N

      rightText.setBackground(resourceMap.getColor("jTextPane1.background")); // NOI18N
      rightText.setFont(resourceMap.getFont("jTextPane1.font")); // NOI18N
      rightText.setText(resourceMap.getString("rightText.text")); // NOI18N
      rightText.setName("rightText"); // NOI18N
      jScrollPane2.setViewportView(rightText);

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
            .addContainerGap())
      );

      javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
      mainPanel.setLayout(mainPanelLayout);
      mainPanelLayout.setHorizontalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(mainPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(mainPanelLayout.createSequentialGroup()
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(maxGames, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      mainPanelLayout.setVerticalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
               .addComponent(maxGames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
      );

      menuBar.setName("menuBar"); // NOI18N

      fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
      fileMenu.setName("fileMenu"); // NOI18N

      addPartnershipMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
      addPartnershipMenuItem.setText(resourceMap.getString("addPartnershipMenuItem.text")); // NOI18N
      addPartnershipMenuItem.setName("addPartnershipMenuItem"); // NOI18N
      addPartnershipMenuItem.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            addPartnershipMenuItemActionPerformed(evt);
         }
      });
      fileMenu.add(addPartnershipMenuItem);

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.sc205.ISLS.class).getContext().getActionMap(IslsScoreView.class, this);
      exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
      exitMenuItem.setName("exitMenuItem"); // NOI18N
      fileMenu.add(exitMenuItem);

      menuBar.add(fileMenu);

      helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
      helpMenu.setName("helpMenu"); // NOI18N

      aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
      aboutMenuItem.setName("aboutMenuItem"); // NOI18N
      helpMenu.add(aboutMenuItem);

      objectives.setMnemonic('O');
      objectives.setText(resourceMap.getString("objectives.text")); // NOI18N
      objectives.setName("objectives"); // NOI18N
      objectives.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            objectivesActionPerformed(evt);
         }
      });
      helpMenu.add(objectives);

      guidance.setMnemonic('G');
      guidance.setText(resourceMap.getString("guidance.text")); // NOI18N
      guidance.setName("guidance"); // NOI18N
      guidance.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            guidanceActionPerformed(evt);
         }
      });
      helpMenu.add(guidance);

      guidelines.setMnemonic('l');
      guidelines.setText(resourceMap.getString("guidelines.text")); // NOI18N
      guidelines.setName("guidelines"); // NOI18N
      guidelines.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            guidelinesActionPerformed(evt);
         }
      });
      helpMenu.add(guidelines);

      jSeparator1.setName("jSeparator1"); // NOI18N
      helpMenu.add(jSeparator1);

      jMenuItem1.setMnemonic('L');
      jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
      jMenuItem1.setName("jMenuItem1"); // NOI18N
      jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem1ActionPerformed(evt);
         }
      });
      helpMenu.add(jMenuItem1);

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
         .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
         .addGroup(statusPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(statusMessageLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 358, Short.MAX_VALUE)
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

    private void addPartnershipMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPartnershipMenuItemActionPerformed
       new AddPartnershipDialog( null, true ).setVisible( true );
    }//GEN-LAST:event_addPartnershipMenuItemActionPerformed
   Color[] colors = new Color[]{Color.ORANGE, Color.YELLOW};
   int ndx = 0;

    private void maxGamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxGamesActionPerformed
       try {
          lastMaxGames = Integer.parseInt( maxGames.getText() );
          setLastMaxGames();
       }
       catch (Exception ex) {
          return;
       }
    }//GEN-LAST:event_maxGamesActionPerformed

    private void objectivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectivesActionPerformed
       StringBuilder sb = new StringBuilder();
       sb.append( "Maintain the score in the Isle Sur la Sorgue 8 ball tournament" );
       sb.append( "\nin a manner which maximizes the number of games played per" );
       sb.append( "\npartnership." );
       sb.append( "\n\nIllustrate the extent to which a trivial need can be");
       sb.append( "\naddressed by a preposterously over-engineered solution." );
       JOptionPane.showMessageDialog( null, sb.toString() );
    }//GEN-LAST:event_objectivesActionPerformed

    private void guidanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guidanceActionPerformed
       StringBuilder sb = new StringBuilder();
       sb.append( "1) Using the 'File' menu selection, add partnerships as needed" );
       sb.append( "\ninsuring that both the names and the affiliation (European" );
       sb.append( "\nor American/Canadian) are specified." );
       sb.append( "\n\n2) Click on an available game to signify its start." );
       sb.append( "\n\n3) Click on an in-progress game at its completion in" );
       sb.append( "\norder to record the result." );
       JOptionPane.showMessageDialog( null, sb.toString() );
    }//GEN-LAST:event_guidanceActionPerformed

    // Guidelines action performed
    //
    private void guidelinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guidelinesActionPerformed
       StringBuilder sb = new StringBuilder();
       sb.append( "Additional partnerships can be added at any time by clicking" );
       sb.append( "\non the score box at the upper left." );
       sb.append( "\n\nThe maximum number of games per partnership (which might" );
       sb.append( "\nbe important if time is short or there is a small number" );
       sb.append( "\nof tables) can be adjusted on the start window.  Otherwise" );
       sb.append( "\nit will allow all possible unique games." );
       sb.append( "\n\nThere is a scheduler which 'suggests' the next game to" );
       sb.append( "\nbe played, but doesn't insist on it.  This is shown by a" );
       sb.append( "\ndarker yellow rectangle.  The start window displays the" );
       sb.append( "\npartnerships, their number of games played and the time since" );
       sb.append( "\ntheir last game ended.  Select the next game as you see fit." );
       sb.append( "\n\nParnerships can be adjusted by clicking on the partnership" );
       sb.append( "\n(top row or leftmost column) and changing the information." );
       sb.append( "\n\nSince there is likely to be beer present, the score window" );
       sb.append( "\nhas deliberately had its close button disabled in order to" );
       sb.append( "\nlessen the probability of the program's being terminated" );
       sb.append( "\ninadvertently." );
       JOptionPane.showMessageDialog( null, sb.toString() );
    }//GEN-LAST:event_guidelinesActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       String[] licenseBlurb = new String[]{
          "Copyright 2011 Michael R. Elliott <mre@m79.net>.\n",
          "\n",
          "Licensed under the Apache License, Version 2.0 (the \"License\");\n",
          "you may not use this file except in compliance with the License.\n",
          "You may obtain a copy of the License at\n",
          "\n",
          "     http://www.apache.org/licenses/LICENSE-2.0\n",
          "\n",
          "Unless required by applicable law or agreed to in writing, software\n",
          "distributed under the License is distributed on an \"AS IS\" BASIS,\n",
          "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n",
          "See the License for the specific language governing permissions and\n",
          "limitations under the License.\n",};
       JOptionPane.showMessageDialog( null, licenseBlurb );
    }//GEN-LAST:event_jMenuItem1ActionPerformed
   private int lastMaxGames = 9;
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JMenuItem addPartnershipMenuItem;
   private javax.swing.JMenuItem guidance;
   private javax.swing.JMenuItem guidelines;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JMenuItem jMenuItem1;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JPopupMenu.Separator jSeparator1;
   private javax.swing.JTextPane leftText;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JTextField maxGames;
   private javax.swing.JMenuBar menuBar;
   private javax.swing.JMenuItem objectives;
   private javax.swing.JProgressBar progressBar;
   private javax.swing.JTextPane rightText;
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

   public enum Col {

      LEFT, RIGHT
   }

   public int getMaxGames() {
      return lastMaxGames;
   }

   public void setMaxGames( int lastMaxGames ) {
      this.lastMaxGames = lastMaxGames;
      setLastMaxGames();
   }

   public void setText( Col col, String text ) {
      if (col == Col.LEFT)
         leftText.setText( text );
      else if (col == Col.RIGHT)
         rightText.setText( text );
   }

   private void setLastMaxGames() {
      maxGames.setBackground( colors[ndx++ % 2] );
      GameSet.instance().updateUnplayable();
      maxGames.setText( String.valueOf( lastMaxGames ) );
   }

    public static ScoreboardFrame scoreboard = null;
}
