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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import org.sc205.IslsScoreView;

/**
 *
 * @author mre
 *
 * Provide a class representing a single partnership with an affiliation,
 * European or American.  Provide a list of partnerships for each affiliation.
 *
 * When a Partnership is created it is automatically added to the list of
 * partnerships for that affiliation.
 */
public class Partnership implements Comparable {

   public int compareTo( Object t ) {
      Partnership other = (Partnership)t;
      if (effectiveGamesPlayed() != other.effectiveGamesPlayed())
         return (int)(effectiveGamesPlayed() - other.effectiveGamesPlayed());
      long diff = lastGamePlayed.getTime() - other.lastGamePlayed.getTime();
      if (diff != 0)
         return (int)diff;
      return index - other.index;
   }

   public static class List extends ArrayList<Partnership> {
   }

   public enum Affiliation {

      EUROPEAN, AMERICAN;

      public Affiliation opponent() {
         return (this == EUROPEAN) ? AMERICAN : EUROPEAN;
      }
   }
   final public int index;
   final public Affiliation affiliation;
   private String[] baseName = new String[2];
   private String[] currentName = new String[2];

   @Override
   public String toString() {
      return surname( 0 ) + ", " + surname( 1 );
   }

   public boolean isAvailableForPlay() {
      if (isCurrentlyActive())
           return false;
      return gamesPlayed() < IslsScoreView.INSTANCE.getMaxGames();
   }

   public boolean isCurrentlyActive() {
      for (Game game: GAME_SET.allGames())
         if (game.getStatus() == Game.Status.IN_PROGRESS
              && game.contains( this ))
            return true;
      return false;
   }

   public int lastGameEndTime() {
      long result = 0;
      for (Game game: GAME_SET.allGames())
         if (game.contains( this )
              && game.getStatus() == Game.Status.PLAYED
              && result < game.getEnd().getTime())
            result = game.getEnd().getTime();
      return (int)((result - tourneyStart) / 1000);
   }

   public int wins() {
      int result = 0;
      for (Game game: GAME_SET.allGames())
         if (game.contains( this ) && game.getWinner() == this)
            result += 1;
      return result;
   }

   public int losses() {
      int result = 0;
      for (Game game: GAME_SET.allGames())
         if (game.contains( this ) && game.getStatus() == Game.Status.PLAYED
              && game.getWinner() != this)
            result += 1;
      return result;
   }

   public int gamesPlayed() {
      return wins() + losses();
   }

   public float effectiveGamesPlayed() {
      if (!isAvailableForPlay())
         return 1000;
      else if( isCurrentlyActive() )
         return (gamesPlayed() + 1) * 10;
      else
         return gamesPlayed() + 1;
   }

   public String winLoss() {
      return String.format( "%d-%d", wins(), losses() );
   }

   public Partnership( Affiliation affiliation ) {
      this.affiliation = affiliation;
      final ArrayList<Partnership> affiliationList = list.get( affiliation );
      index = affiliationList.size();
      affiliationList.add( this );
   }

   public String surname( int ndx ) {
      final String fullName = currentName[ndx];
      if (fullName.contains( "," ))
         return fullName.split( "," )[0].trim();
      String[] pieces = fullName.split( " " );
      return pieces[pieces.length - 1].trim();
   }

   public String nameAsEntered( int ndx ) {
      return currentName[ndx];
   }

   public void setBaseName( int ndx, String name ) {
      this.baseName[ndx] = name;
      this.currentName[ndx] = name;
   }

   public void setCurrentName( int ndx, String name ) {
      this.currentName[ndx] = name;
   }

   public static ArrayList<Partnership> get( Affiliation affiliation ) {
      return list.get( affiliation );
   }
   final public static EnumMap<Affiliation, ArrayList<Partnership>> list =
        new EnumMap( Affiliation.class );

   @Override
   public boolean equals( Object obj ) {
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      final Partnership other = (Partnership)obj;
      if (this.index != other.index)
         return false;
      if (this.affiliation != other.affiliation)
         return false;
      if (!Arrays.deepEquals( this.baseName, other.baseName ))
         return false;
      return true;
   }

  public Date timeSinceLastGame() {
     long currentTimeMs    = System.currentTimeMillis();
     long lastPlayedTimeMs = this.lastGamePlayed.getTime();
     long elapsedTime      = currentTimeMs - lastPlayedTimeMs;
     return (new Date(elapsedTime));
  }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 83 * hash + this.index;
      hash = 83 * hash + (this.affiliation != null
           ? this.affiliation.hashCode() : 0);
      hash = 83 * hash + Arrays.deepHashCode( this.baseName );
      return hash;
   }

   public Date getLastGamePlayed() {
      return lastGamePlayed;
   }

   public void setLastGamePlayed( Date lastGamePlayed ) {
      this.lastGamePlayed = lastGamePlayed;
   }

   private Date lastGamePlayed = new Date();

   static {
      list.put( Affiliation.EUROPEAN, new ArrayList<Partnership>() );
      list.put( Affiliation.AMERICAN, new ArrayList<Partnership>() );
   }
   final private static GameSet GAME_SET = GameSet.instance();
   final private static long tourneyStart = new Date().getTime();
}
