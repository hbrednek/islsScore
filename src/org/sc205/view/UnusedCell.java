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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author mre
 */
public class UnusedCell extends ScoreBox.Renderer {

   public UnusedCell( ScoreBox scoreBox ) {
      super( scoreBox );
   }

   @Override
   public void paint( ScoreBox scoreBox, Graphics2D g ) {
      Rectangle bounds = scoreBox.getBounds();
      g.setColor( Color.GREEN );
      Rectangle outline =
           new Rectangle( 1, 1, bounds.width - 2, bounds.height - 2 );
      g.draw(  outline );
   }

}
