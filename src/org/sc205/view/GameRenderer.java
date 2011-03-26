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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.sc205.controller.DeclareWinner;
import org.sc205.model.Game;
import org.sc205.model.GameSet;
import org.sc205.model.Partnership;
import org.sc205.model.Partnership.Affiliation;

/**
 *
 * @author mre
 */
public class GameRenderer extends ScoreBox.Renderer {

   public GameRenderer( ScoreBox scoreBox, Game game ) {
      super( scoreBox );
      this.game = game;
   }

   @Override
   public void mouseClicked( MouseEvent me ) {
      super.mouseClicked( me );
      switch (game.getStatus()) {
         case AVAILABLE:
            startGame();
            break;
         case IN_PROGRESS:
         case PLAYED:
            setWinner();
            break;
         default:
            break;
      }
   }

   private void setPlayedConflict( Game.Status from, Game.Status to ) {
      final Partnership amCan = game.getTeam( Affiliation.AMERICAN );
      final Partnership euro = game.getTeam( Affiliation.EUROPEAN );
      for (Game iteratedGame: GameSet.instance().allGames())
         if (iteratedGame.getStatus() == from
              && (iteratedGame.getTeam( Affiliation.EUROPEAN ) == euro
              || iteratedGame.getTeam( Affiliation.AMERICAN ) == amCan)) {
            iteratedGame.setStatus( to );
            iteratedGame.getScoreBox().repaint();
         }
   }

   private void startGame() {
      game.start();
      setPlayedConflict( Game.Status.AVAILABLE, Game.Status.SCHEDULE_CONFLICT );
      GameSet.instance().scheduleNextGame();
      scoreBox.repaint();
   }

   private void setWinner() {
      DeclareWinner dw = new DeclareWinner( null, true );
      dw.setGame( game );
      dw.setVisible( true );
   }

   private class Repainter extends TimerTask {

      @Override
      public void run() {
         scoreBox.repaint();
      }
   }

   @Override
   public void paint( ScoreBox scoreBox, Graphics2D gg ) {
      super.paint( scoreBox, gg );
      bounds = scoreBox.getBounds();
      bounds.x = bounds.y = 0;
      switch (game.getStatus()) {
         case SCHEDULE_CONFLICT:
            gg.setColor( ScoreBox.SCHEDULE_CONFLICT );
            gg.fill( bounds );
            break;
         case AVAILABLE:
            gg.setColor( game.isNext()
                 ? ScoreBox.ON_DECK : ScoreBox.UNSCHEDULED );
            gg.fill( bounds );
            break;
         case IN_PROGRESS:
            gg.setColor( ScoreBox.IN_PROGRESS );
            gg.fill( bounds );
            final long startSec = game.getStart().getTime() / 1000;
            final long elapsed = System.currentTimeMillis() / 1000 - startSec;
            final long seconds = elapsed % 60;
            final long minutes = elapsed / 60;
            String timeString = String.format( "%d:%02d", minutes, seconds );
            final float glyphHeight = 0.25f * w;
            Font font =
                 new Font( "Sans-serif", Font.BOLD, Math.round( glyphHeight ) );
            TextLayout tl = new TextLayout(
                 timeString, font, gg.getFontRenderContext() );
            gg.setColor( Color.WHITE );
            final Rectangle2D textBounds = tl.getBounds();
            int x,
             y;
            x = (int)Math.round( w / 2f - textBounds.getWidth() / 2f );
            y = (int)Math.round( h / 2f + textBounds.getHeight() / 2f );
            tl.draw( gg, x, y );
            if (repainter == null) {
               repainter = new Repainter();
               TIMER.schedule( repainter, 300L, 300L );
            }
            break;
         case PLAYED:
            paintWinner( scoreBox, gg );
            break;
         case UNAVAILABLE:
            paintTooManyGames( scoreBox, gg );
            break;
      }
      paintBoundary( scoreBox, gg );
   }

   private void paintTooManyGames( ScoreBox scoreBox, Graphics2D gg ) {
      gg.setColor( Color.BLACK );
      gg.fill( bounds );
      return;
   }

   private void paintWinner( ScoreBox scoreBox, Graphics2D gg ) {
      final Partnership part = game.getWinner();
      final Affiliation winner = part.affiliation;
      gg.drawImage( IP.get( winner, w, h ), null, 0, 0 );
   }
   private TimerTask repainter = null;
   private Rectangle bounds;
   private Game game;
   final private static Timer TIMER = new Timer();
   final private static ImageProvider IP = ImageProvider.instance();
}
