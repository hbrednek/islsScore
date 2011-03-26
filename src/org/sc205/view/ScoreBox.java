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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import javax.swing.JPanel;

/**
 *
 * @author mre
 */
public class ScoreBox extends JPanel implements MouseListener {

   final public static Color EURO = new Color( 0, 33, 148 );
   final public static Color AM_CAN = new Color( 206, 30, 50 );
   final public static Color IN_PROGRESS = new Color( 68, 133, 41 );
   final public static Color UNSCHEDULED = new Color( 242, 226, 142 );
   final public static Color SCHEDULE_CONFLICT = new Color( 210, 210, 210 );
   final public static Color ON_DECK = Color.ORANGE;

   public ScoreBox() {
      addMouseListener( this );
   }

   public Observable getObservable() {
      return observable;
   }

   public void setTag( Object tag ) {
      this.tag = tag;
   }

   abstract public static class Renderer {

      public Renderer( ScoreBox scoreBox ) {
         this.scoreBox = scoreBox;
      }

      public void paint( ScoreBox scoreBox, Graphics2D gg ) {
         gg.setRenderingHint(
              RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
         Rectangle bounds = scoreBox.getBounds();
         w = bounds.width;
         h = bounds.height;
      }

      public void mouseClicked( MouseEvent me ) {
      }

      protected void paintBoundary( ScoreBox scoreBox, Graphics2D gg ) {
         strokeWidth = w / 45f;
         BasicStroke stroke = new BasicStroke(
              strokeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER );
         GeneralPath gp = new GeneralPath();
         gp.moveTo( strokeWidth / 2f, strokeWidth / 2f );
         gp.lineTo( w - strokeWidth / 2f, strokeWidth / 2f );
         gp.lineTo( w - strokeWidth / 2f, h - strokeWidth / 2f );
         gp.lineTo( strokeWidth / 2f, h - strokeWidth / 2f );
         gp.lineTo( strokeWidth / 2f, strokeWidth / 2f );
         gg.setColor( Color.WHITE );
         gg.setStroke( stroke );
         gg.draw( gp );
      }
      protected int w, h;
      protected float strokeWidth;
      final protected ScoreBox scoreBox;
   }

   public void setRenderer( Renderer renderer ) {
      this.renderer = renderer;
   }

   @Override
   public void paint( Graphics grphcs ) {
      renderer.paint( this, (Graphics2D)grphcs );
   }

   public void mouseClicked( MouseEvent me ) {
      observable.notifyObservers();
      renderer.mouseClicked( me );
   }

   public void mousePressed( MouseEvent me ) {
   }

   public void mouseReleased( MouseEvent me ) {
   }

   public void mouseEntered( MouseEvent me ) {
   }

   public void mouseExited( MouseEvent me ) {
   }
   final private Observable observable = new Observable() {

      @Override
      public void notifyObservers() {
         setChanged();
         super.notifyObservers( tag );
      }
   };
   private Renderer renderer;
   private Object tag;
}
