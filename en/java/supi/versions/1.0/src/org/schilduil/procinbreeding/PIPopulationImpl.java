/*
 * Copyright (C) 2008, The Schilduil Software Team. All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Schilduil Software Team (http://www.schilduil.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "Schilduil Software Team" or any of its contributors must
 * not be used to endorse or promote products derived from this
 * software without prior written permission. For written permission,
 * please contact license@schilduil.org.
 * 
 * 5. Products derived from this software may not be called "Schilduil",
 * nor may "Schilduil" appear in their name, without prior written
 * permission of the Schilduil Software Team.
 * 
 * 
 * DISCLAIMER
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCHILDUIL DEVELOPMENT TEAM OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE. 
 */
package org.schilduil.procinbreeding;

import java.util.List;
import java.util.Vector;

public class PIPopulationImpl implements PIPopulation {

   private List<String> memberNames = new Vector<String>();
   private List<PIIndividual> members = new Vector<PIIndividual>();
   private boolean inconsistent = false;
   private PopulationAddMemberListener addListener = null;
   private PopulationRemoveMemberListener removeListener = null;
   private PopulationReorganizedListener reorganizedListener = null;

   private void check() throws PIPopulationInconsistenceException {
      if (this.inconsistent) {
         throw new PIPopulationInconsistenceException();
      }
      if (this.memberNames.size() != this.members.size()) {
         throw new PIPopulationInconsistenceException();
      }
   }

   public synchronized PIIndividual addMember(PIIndividual member) throws PIPopulationException {

      this.check();

      if (member == null) {
         throw new NullPointerException("Member is null.");
      }
      String name = member.getName();
      if (name == null) {
         throw new NullPointerException("Member name is null.");
      }

      int position = this.memberNames.indexOf(name);
      if (position < 0) {

         // Append at the end.
         position = this.memberNames.size();
         if (!this.memberNames.add(name)) {
            throw new PIPopulationException("Couldn't add the member name due to unknown reasons.");
         }
         if (!this.members.add(member)) {
            throw new PIPopulationException("Couldn't add the member due to unknown reasons: the population is probably in a wrong state.");
         }

         if (this.addListener != null) {
            this.addListener.populationMemberAddedAction(member, position);
         }
         return null;

      } else {

         throw new PIPopulationException("Member with name " + name + " already added.");

      }

   }

   public PIIndividual find(String name) throws PIPopulationException {

      this.check();
      int position = this.memberNames.indexOf(name);
      if (position < 0) {
         return null;
      // throw new PIPopulationException("The individual " + name + " does not belong to the population.");
      }
      return (PIIndividual) this.members.get(position);

   }

   public boolean isMember(PIIndividual member) throws PIPopulationException {

      String name = member.getName();
      if (name == null) {
         throw new NullPointerException("Member name is null.");
      }

      return this.isMember(name);

   }

   public boolean isMember(String member) throws PIPopulationException {

      this.check();
      return this.members.contains(member);

   }

   public int size() throws PIPopulationException {

      this.check();
      return this.members.size();

   }

   public int findNumber(String name) throws PIPopulationException {

      this.check();
      return this.memberNames.indexOf(name);

   }

   public PIIndividual get(int position) throws PIPopulationException {

      this.check();
      return (PIIndividual) this.members.get(position);

   }

   private PIIndividual removeMember(int position) throws PIPopulationException {

      this.memberNames.remove(position);
      PIIndividual result = (PIIndividual) this.members.remove(position);
      if (this.removeListener != null) {
         removeListener.populationMemberRemovedAction(result, position);
      }
      return result;

   }

   public void removeMember(PIIndividual member) throws PIPopulationException {

      String name = member.getName();
      if (name == null) {
         throw new NullPointerException("Member name is null.");
      }
      this.check();
      int position = this.memberNames.indexOf(name);
      this.removeMember(position);

   }

   public void setAddMemberListener(PopulationAddMemberListener listener) {

      this.addListener = listener;

   }

   public void setRemoveMemberListener(PopulationRemoveMemberListener listener) {

      this.removeListener = listener;

   }

   public void setReorganizedListener(PopulationReorganizedListener listener) {

      this.reorganizedListener = listener;

   }
}
