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

import java.util.Vector;

public class PIIndividualImpl implements PIIndividual {
  
  private String name = null;
  private Vector<PIIndividual> parents = new Vector<PIIndividual>(2);
  private String comment = null;
  
  public PIIndividualImpl(String name, Vector parents) {
    
    this(name, parents, null);
    
  }
  
  public PIIndividualImpl(String name, Vector parents, String comment) {
    
    this.name = name;
    this.parents = parents;
    this.comment = comment;
    
  }
  public PIIndividualImpl(String name) {
    
    this.name = name;
    
  }

  public String getComment() {

    return this.comment;
    
  }

  public String getName() {
    
    return this.name;
    
  }

  public PIIndividual getParent(int i) {

    try {

      return this.parents.get(i);
      
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      
      return null;
      
    }
    
  }
  
  public String toLine() {
    
    StringBuffer result = new StringBuffer();
    
    String name = this.getName();
    String comment = this.getComment();
    PIIndividual p1 = this.getParent(0);
    PIIndividual p2 = this.getParent(1);
    
    if (name != null) {
      result.append(name + " ");
    } else {
      result.append("# ? ");
    }

    if (p1 != null) {
      result.append(p1.getName() + " ");
    } else {
      result.append("? ");
    }
    
    if (p2 != null) {
      result.append(p2.getName() + " ");
    } else {
      result.append("? ");
    }
    
    if (comment != null) {
      // TODO: newlines should be removed from this comment to be extra save.
      result.append(comment);
    }
    
    return result.toString();

  }
  
   @Override
  public String toString() {
     
     return this.getName();
     
  }

}
