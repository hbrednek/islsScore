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
import org.sc205.model.Partnership.Affiliation;
import org.sc205.view.ScoreBox;

/**
 * Provide the information concerning a game
 *
 * @author mre
 */
public class Game {

   public void rollback() {
      winner = null;
      setStatus( Status.AVAILABLE );
      GameSet.instance().updateUnplayable();
   }

   public enum Status {

      AVAILABLE, SCHEDULE_CONFLICT, IN_PROGRESS, PLAYED, UNAVAILABLE
   }

   public boolean contains( Partnership partnership ) {
      return team.get( partnership.affiliation ) == partnership;
   }

   public Partnership getTeam( Affiliation affiliation ) {
      return team.get( affiliation );
   }
   public EnumMap<Partnership.Affiliation, Partnership> team =
        new EnumMap( Partnership.Affiliation.class );

   public Date getEnd() {
      return end;
   }

   public Date getStart() {
      return start;
   }

   public void start() {
      setStatus( Status.IN_PROGRESS );
      this.start = new Date();
   }

   public void setStatus( Status status ) {
      this.status = status;
      if (status != Status.PLAYED)
         start = null;
   }

   public Status getStatus() {
      return status;
   }

   public Partnership getWinner() {
      return winner;
   }

   public void setWinner( Partnership winner ) {
      this.winner = winner;
      end = new Date();
      status = Status.PLAYED;
   }

   public ScoreBox getScoreBox() {
      return scoreBox;
   }

   public void setScoreBox( ScoreBox scoreBox ) {
      this.scoreBox = scoreBox;
   }

   public void setIsNext( boolean isNext ) {
      this.isNext = isNext;
   }

   public boolean isNext() {
      return isNext;
   }

   private boolean isNext = false;
   private ScoreBox scoreBox;
   private Date start, end;
   private Partnership winner;
   private Status status = Status.AVAILABLE;
}
