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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import org.sc205.model.Partnership.Affiliation;

/**
 *
 * @author mre
 */
public class ImageProvider {

   public static ImageProvider instance() {
      return INSTANCE;
   }
   public BufferedImage get( Affiliation affiliation ) {
      BufferedImage result = map.get( affiliation );
      if (result == null) {
         String file = affiliation == Affiliation.EUROPEAN ? EUFLAG : AMCANFLAG;
         map.put( affiliation, result = getBufferedImage( file ));
      }
      return result;
   }

   public BufferedImage get( Affiliation affiliation, int w, int h ) {
      BufferedImage flag = get( affiliation );
      float wScale = (float)w / flag.getWidth();
      float hScale = (float)h / flag.getHeight();
      BufferedImage result =
           new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
      Graphics2D bg = result.createGraphics();
      bg.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
           RenderingHints.VALUE_INTERPOLATION_BILINEAR );
      bg.scale( wScale, hScale );
      bg.drawImage( flag, 0, 0, null );
      bg.dispose();
      return result;
   }

   private BufferedImage getBufferedImage( String iFile ) {
      BufferedImage result = null;
      final java.net.URL imgURL = ImageProvider.class.getResource( iFile );
      try {
         result = ImageIO.read( imgURL );
      } catch (Exception e) {
         e.printStackTrace();
      }
      return result;
   }

   private ImageProvider() {}

   private EnumMap<Affiliation, BufferedImage> map = new EnumMap( Affiliation.class );
   final private static String EUFLAG = "euro.png";
   final private static String AMCANFLAG = "amcan.jpg";
   final private static ImageProvider INSTANCE = new ImageProvider();
}
