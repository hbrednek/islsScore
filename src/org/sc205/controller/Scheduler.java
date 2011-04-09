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

package org.sc205.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.Random;
import org.sc205.IslsScoreView;
import org.sc205.model.Game;
import org.sc205.model.GameSet;
import org.sc205.model.Partnership;
import org.sc205.model.Partnership.Affiliation;
import static org.sc205.model.Partnership.Affiliation.EUROPEAN;
import static org.sc205.model.Partnership.Affiliation.AMERICAN;
import org.sc205.model.Partnership.List;

/**
 *
 * @author Michael R. Elliott <mre@m79.net>
 */
public class Scheduler {

   public static Scheduler instance() {
      return INSTANCE;
   }

   private EnumMap<Affiliation, Partnership.List> teams =
        new EnumMap<Affiliation, Partnership.List>( Affiliation.class );
   
   private void sortTeams() {
      teams.get( EUROPEAN ).clear();
      teams.get( AMERICAN ).clear();
      EnumMap<Affiliation, List> pLists = GameSet.instance().getPartnershipLists();
      teams.get( EUROPEAN ).addAll( pLists.get( EUROPEAN ));
      teams.get( AMERICAN ).addAll( pLists.get( AMERICAN ));
      Collections.sort( teams.get( EUROPEAN ));
      Collections.sort( teams.get( AMERICAN ));
   }

   private String text( Partnership.Affiliation aff ) {
      final StringBuilder result = new StringBuilder( " " );
      final String prefix = aff == EUROPEAN ? "E" : "A";
      for (Partnership pp: teams.get( aff )) {
         if (!pp.isAvailableForPlay())
            continue;
         result.append( prefix ).append( pp.index + 1 ).append( " " );
         result.append( pp.gamesPlayed() ).append( " " );
         result.append( DF.format( pp.timeSinceLastGame() ));
         result.append( "\n " );
      }
      return result.toString();
   }

   final private static DateFormat DF = new SimpleDateFormat( "HH:mm:ss" );
   static {
      DF.setTimeZone( java.util.TimeZone.getTimeZone( "UTC" ));
   }

   private void updatePanel( ) {
      IslsScoreView view = IslsScoreView.INSTANCE;
      if (view == null)
         return;
      view.setText( IslsScoreView.Col.LEFT, text( AMERICAN ));
      view.setText( IslsScoreView.Col.RIGHT, text( EUROPEAN ));
   }

   final static Random ran = new Random( new Date().getTime() );

   public Game nextGame() {
      sortTeams();
      updatePanel();
      Affiliation lead, cover;
      if (ran.nextBoolean()) {
         lead = AMERICAN;
         cover = EUROPEAN;
      }
      else {
         lead =  EUROPEAN;
         cover = AMERICAN;
      }
      for (Partnership amCan: teams.get( lead )) {
         if (!amCan.isCurrentlyActive()) {
            for (Partnership euro: teams.get( cover ))
               if (!euro.isCurrentlyActive()) {
                  final Game game = GameSet.instance().get( euro, amCan );
                  if (game.getStatus() == Game.Status.AVAILABLE)
                     return game;
               }
         }
      }
      return null;
   }

   private Scheduler() {
      teams.put( EUROPEAN, new Partnership.List() );
      teams.put( AMERICAN, new Partnership.List() );
   }

   final private static Scheduler INSTANCE = new Scheduler();
}
