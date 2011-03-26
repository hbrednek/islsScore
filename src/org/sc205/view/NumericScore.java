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
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.EnumMap;
import org.sc205.controller.AddPartnershipDialog;
import org.sc205.model.GameSet;
import org.sc205.model.Partnership.Affiliation;

/**
 *
 * @author mre
 */
public class NumericScore extends ScoreBox.Renderer {

   public NumericScore( ScoreBox scoreBox ) {
      super( scoreBox );
   }

   @Override
   public void mouseClicked( MouseEvent me ) {
      super.mouseClicked( me );
      new AddPartnershipDialog( null, true ).setVisible( true );
   }

   @Override
   public void paint( ScoreBox scoreBox, Graphics2D gg ) {
      super.paint(  scoreBox, gg);
      final Rectangle bg = new Rectangle( 0, 0, w, h );
      gg.setColor( Color.WHITE );
      gg.fill( bg );
      final float hGap = w / 50f;
      final float vGap = hGap;
      final GeneralPath upper = new GeneralPath();
      upper.moveTo( hGap, 0 );
      upper.lineTo( w, 0 );
      upper.lineTo( w, h - vGap );
      upper.lineTo( hGap, 0 );
      gg.setColor( ScoreBox.EURO );
      gg.fill(  upper );
      final GeneralPath lower = new GeneralPath();
      lower.moveTo( 0, vGap );
      lower.lineTo( w - hGap, h );
      lower.lineTo( 0, h );
      lower.lineTo( 0, vGap );
      gg.setColor( ScoreBox.AM_CAN );
      gg.fill(  lower );
      final float glyphHeight = 0.5f * h;
      final float glyphMargin = w / 20f;
      final String eScore = "" + GAME_SET.getScore( Affiliation.EUROPEAN );
      final String aScore = "" + GAME_SET.getScore( Affiliation.AMERICAN );
      Font font = new Font( "sans", Font.BOLD, Math.round( glyphHeight ));
      TextLayout tl = new TextLayout( aScore, font, gg.getFontRenderContext() );
      gg.setColor(  Color.WHITE );
      final Rectangle2D bounds = tl.getBounds();
      int x, y;
      x = (int)((float)w / 2 + -bounds.getWidth() - glyphMargin);
      y = Math.round( h - glyphMargin - strokeWidth );
      tl.draw( gg, x, y );
      tl = new TextLayout( eScore, font, gg.getFontRenderContext() );
      x = (int)((float)w / 2 + glyphMargin);
      y = Math.round(tl.getAscent() * 0.8f + glyphMargin + strokeWidth );
      tl.draw( gg, x, y );
      paintBoundary( scoreBox, gg );
   }

   final private static GameSet GAME_SET = GameSet.instance();
}
