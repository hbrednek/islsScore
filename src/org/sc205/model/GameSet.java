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
package org.sc205.model;

import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.sc205.IslsScoreView;
import org.sc205.controller.Scheduler;
import org.sc205.model.Partnership.Affiliation;
import org.sc205.view.ScoreBox;

/**
 * Provide storage for all games and the means by which they can be looked up.
 * If a game doesn't yet exist, create it at the first request.
 *
 * NB: This class is a singleton
 *
 * @author mre
 */
public class GameSet {

   public static GameSet instance() {
      return INSTANCE;
   }

   public void updateUnplayable() {
      updateMaxGames();
      scheduleNextGame();
      repaintAll();
   }

   private void exclude( Partnership pp ) {
      final Iterator<Game> iterator = gameSet.iterator();
      while (iterator.hasNext()) {
         final Game game = iterator.next();
         if (game.contains( pp ) && game.getStatus() != Game.Status.PLAYED)
            game.setStatus( Game.Status.UNAVAILABLE );
      }
   }

   public void add( Partnership partners ) {
      partnershipLists.get( partners.affiliation ).add( partners );
      for (Partnership opponent:
           partnershipLists.get( partners.affiliation.opponent() )) {
         final Game game = new Game();
         game.team.put( partners.affiliation, partners );
         game.team.put( opponent.affiliation, opponent );
         gameSet.add( game );
         if (isBusy( opponent ))
            game.setStatus( Game.Status.SCHEDULE_CONFLICT );
      }
      scheduleNextGame();
      adjustMaxGames();
   }

   public int getScore( Affiliation affiliation ) {
      int result = 0;
      for (Game game: gameSet)
         if (game.getStatus() == Game.Status.PLAYED
              && game.getWinner().affiliation == affiliation)
            result += 1;
      return result;
   }

   public Set<Game> allGames() {
      return gameSet;
   }

   public EnumMap<Affiliation, Partnership.List> getPartnershipLists() {
      return partnershipLists;
   }

   public Game get( Partnership p1, Partnership p2 ) {
      for (Game game: gameSet)
         if (game.contains( p1 ) && game.contains( p2 ))
            return game;
      return null;
   }

   public void declareWinner( Game game, Affiliation affiliation ) {
      game.setWinner( game.getTeam( affiliation ) );
      for (Partnership pp: game.team.values())
         pp.setLastGamePlayed( new Date() );
      updateMaxGames();
      showConflicts( game );
   }

   public void showConflicts( Game game ) {
      Partnership euro = game.getTeam( Affiliation.EUROPEAN );
      Partnership amcan = game.getTeam( Affiliation.AMERICAN );
      for (Game check: gameSet)
         if (check.contains( amcan ) || check.contains( euro ))
            if (check.getStatus() == Game.Status.SCHEDULE_CONFLICT) {
               if (isBusy( check.getTeam( Affiliation.EUROPEAN ) ))
                  continue;
               if (isBusy( check.getTeam( Affiliation.AMERICAN ) ))
                  continue;
               check.setStatus( Game.Status.AVAILABLE );
               check.getScoreBox().repaint();
            }
      scheduleNextGame();
      repaintAll();
   }

   public void scheduleNextGame() {
      for (Game gg: gameSet)
         gg.setIsNext( false );
      Game game = Scheduler.instance().nextGame();
      if (game != null) {
         game.setIsNext( true );
         if (game.getScoreBox() != null)
            game.getScoreBox().repaint();
      }
   }

   private boolean isBusy( Partnership partnership ) {
      for (Game game: gameSet) {
         if (!game.contains( partnership ))
            continue;
         if (game.getStatus() == Game.Status.IN_PROGRESS)
            return true;
      }
      return false;
   }

   public void repaintAll() {
      for (ScoreBox[] row: scoreBoxes)
         for (ScoreBox box: row)
            box.repaint();
   }

   private GameSet() {
      partnershipLists.put( Affiliation.EUROPEAN, new Partnership.List() );
      partnershipLists.put( Affiliation.AMERICAN, new Partnership.List() );
   }
   EnumMap<Partnership.Affiliation, Partnership.List> partnershipLists =
        new EnumMap( Partnership.Affiliation.class );

   public void setAllScoreBoxes( ScoreBox[][] score ) {
      this.scoreBoxes = score;
   }

   private void updateMaxGames() {
      final int maxGames = IslsScoreView.INSTANCE.getMaxGames();
      final Iterator<Game> iterator = gameSet.iterator();
      while (iterator.hasNext()) {
         Game game = iterator.next();
         for (Affiliation aff: Affiliation.values()) {
            final Partnership pp = game.getTeam( aff );
            if (pp.gamesPlayed() >= maxGames)
               exclude( pp );
            else if (game.getStatus() == Game.Status.UNAVAILABLE) {
               Partnership opponent = game.getTeam( pp.affiliation.opponent());
               if (pp.isCurrentlyActive() || opponent.isCurrentlyActive())
                  game.setStatus( Game.Status.SCHEDULE_CONFLICT );
               else
                  game.setStatus( Game.Status.AVAILABLE );
            }
         }
      }
   }

   private void adjustMaxGames() {
      final HashMap<Affiliation, HashSet<Partnership>> map =
           new HashMap<Affiliation, HashSet<Partnership>>();
      map.put( Affiliation.EUROPEAN, new HashSet<Partnership>() );
      map.put( Affiliation.AMERICAN, new HashSet<Partnership>() );
      final Iterator<Game> iterator = gameSet.iterator();
      while (iterator.hasNext()) {
         Game game = iterator.next();
         for (Affiliation aff: Affiliation.values()) {
            final Partnership pp = game.getTeam( aff );
            map.get( aff ).add( pp );
         }
      }
      final int euroSize = map.get( Affiliation.EUROPEAN ).size();
      final int amcanSize = map.get( Affiliation.AMERICAN ).size();
      final int largestAffiliation = Math.max( euroSize, amcanSize );
      final int smallestAffiliation = Math.min( euroSize, amcanSize );
      if (smallestAffiliation > 0)
         IslsScoreView.INSTANCE.setMaxGames( largestAffiliation );
   }

   private ScoreBox[][] scoreBoxes;
   private HashSet<Game> gameSet = new HashSet<Game>();
   final private static GameSet INSTANCE = new GameSet();
}
