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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import org.sc205.controller.EditPartnershipDialog;
import org.sc205.model.Partnership;
import org.sc205.model.Partnership.Affiliation;

/**
 *
 * @author mre
 */
public class TeamRenderer extends ScoreBox.Renderer {

   public TeamRenderer( ScoreBox scoreBox, Partnership partnership, int ndx ) {
      super( scoreBox );
      this.partnership = partnership;
      if (partnership.affiliation == Affiliation.EUROPEAN) {
         bg = ScoreBox.EURO;
         teamName = "E" + ndx;
      }
      else {
         bg = ScoreBox.AM_CAN;
         teamName = "A" + ndx;
      }
   }

   @Override
   public void mouseClicked( MouseEvent me ) {
      super.mouseClicked( me );
     EditPartnershipDialog dialog = new EditPartnershipDialog( null, true );
     dialog.setPartnership( partnership );
     dialog.setInitiator( scoreBox );
     dialog.setVisible( true );
   }

   @Override
   public void paint( ScoreBox scoreBox, Graphics2D gg ) {
      super.paint( scoreBox, gg );
      Rectangle outline = new Rectangle( 0, 0, w, h );
      gg.setColor( bg );
      gg.fill( outline );
      paintBoundary( scoreBox, gg );
      final float glyphHeight = 0.45f * h;
      Font teamFont = new Font( "sans", Font.BOLD, Math.round( glyphHeight ));
      TextLayout tl =
           new TextLayout( teamName, teamFont, gg.getFontRenderContext() );
      gg.setColor( Color.WHITE );
      tl.draw( gg, (w - tl.getAdvance()) / 2, tl.getAscent() * 1.1f );
      float nameHeight = glyphHeight / 3f;
      Font nameFont = new Font( "sans", Font.PLAIN, Math.round( nameHeight ));
      String winLoss = partnership.winLoss();
      Rectangle bigAlpha = tl.getBounds().getBounds();
      tl = new TextLayout( winLoss, nameFont, gg.getFontRenderContext() );
      Rectangle winLossRect = tl.getBounds().getBounds();
      int wlx = (int)(w - winLossRect.width - strokeWidth * 3);
      int wly = (int)(strokeWidth * 3 + winLossRect.getHeight());
      tl.draw( gg, wlx, wly );
      tl = new TextLayout( 
           partnership.surname( 1 ), nameFont, gg.getFontRenderContext() );
      float margin = h / 10f;
      int y = Math.round( h - margin - tl.getDescent() );
      tl.draw( gg, (w - tl.getAdvance()) / 2, y );
      y -= glyphHeight / 3f;
      tl = new TextLayout( 
           partnership.surname( 0 ), nameFont, gg.getFontRenderContext() );
      tl.draw( gg, (w - tl.getAdvance())/ 2, y );
   }

   final private Color bg;
   final private String teamName;
   final private Partnership partnership;
}
