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

package org.sc205.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.sc205.model.Game;
import org.sc205.model.GameSet;
import org.sc205.model.Partnership;
import org.sc205.model.Partnership.Affiliation;
import org.sc205.view.ScoreBox.Renderer;

/**
 *
 * @author mre
 */
public class ScoreboardFrame extends JFrame {

   public ScoreboardFrame() throws HeadlessException {
      super( "Isle Sur la Sorgue Trophy" );
   }

   public ScoreboardFrame( ScoreboardFrame sf )
        throws HeadlessException {
      super( "Isle Sur la Sorgue Trophy" );
   }

   public Observable getObservable() {
      return obs;
   }

   public void update( Game game ) {
      throw new UnsupportedOperationException( "Not supported yet." );
   }

   final public ScoreboardFrame layout( JFrame frame ) {
      setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
      GameSet gameSet = GameSet.instance();
      ArrayList<Partnership> europeans =
           gameSet.getPartnershipLists().get( Affiliation.EUROPEAN );
      ArrayList<Partnership> americans =
           gameSet.getPartnershipLists().get( Affiliation.AMERICAN );
      int rows = 1 + americans.size();
      int cols = 1 + europeans.size();
      scoreBox = new ScoreBox[rows][cols];
      JPanel panel = new JPanel();
      panel.setLayout( new GridLayout( rows, cols ) );
      for (int row = 0; row < rows; row++)
         for (int col = 0; col < cols; col++) {
            ScoreBox box = new ScoreBox();
            scoreBox[row][col] = box;
            box.setRenderer( new UnusedCell( box ) );
            panel.add( box );
         }
      scoreBox[0][0].setRenderer( new NumericScore( scoreBox[0][0]) );
      for (int col = 1; col < cols; col++) {
         Partnership ps = europeans.get( col - 1 );
         final Renderer renderer = new TeamRenderer( scoreBox[0][col], ps, col );
         scoreBox[0][col].setRenderer( renderer);
      }
      for (int row = 1; row < rows; row++) {
         Partnership ps = americans.get( row - 1 );
         final Renderer renderer = new TeamRenderer( scoreBox[row][0], ps, row );
         scoreBox[row][0].setRenderer(renderer);
      }
      if (frame == null) {
         this.setLocation( 50, 50 );
         this.setPreferredSize( new Dimension( 800, 600 ) );
      }
      else {
         this.setLocation( frame.getLocationOnScreen() );
         Rectangle bounds = frame.getBounds();
         this.setPreferredSize( new Dimension( bounds.width, bounds.height ) );
         frame.setVisible( false );
         frame.dispose();
      }
      for (int row = 1; row < rows; row++)
         for (int col = 1; col < cols; col++) {
            final Partnership 
                 euro = europeans.get( col - 1 ),
                 amcan = americans.get( row - 1 );
            final Game game = GameSet.instance().get( euro, amcan );
            Renderer renderer = new GameRenderer( scoreBox[row][col], game );
            scoreBox[row][col].setRenderer( renderer );
            game.setScoreBox( scoreBox[row][col]);
         }
      this.add( panel );
      this.pack();
      this.setVisible( true );
      gameSet.setAllScoreBoxes( scoreBox );
      return this;
   }

   private ScoreBox[][] scoreBox;
   private Observable obs = new Observable() {

      @Override
      public void notifyObservers( Object o ) {
         setChanged();
         super.notifyObservers( o );
      }
   };
}
