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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bert
 */
public class MatrixTableModel extends AbstractTableModel {

   private InbreedingMatrix matrix = null;
   private double factor = 2.0;

   public MatrixTableModel(double factor) {
      super();
      this.factor = factor;
   }

   public MatrixTableModel(InbreedingMatrix matrix, double factor) {
      this(factor);
      this.setMatrix(matrix);
   }

   public void setMatrix(InbreedingMatrix matrix) {
      this.matrix = matrix;
      this.fireTableStructureChanged();
   }

   public int getRowCount() {
      // System.out.println("getRowCount");
      if (this.matrix == null) {
         return 0;
      }
      try {
         return this.matrix.populationSize();
      } catch (PIPopulationException ex) {
         Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
         return 0;
      }
   }

   @Override
   public Class getColumnClass(int column) {
      if (column == 0) {
         return String.class;
      }
      return Double.class;
   }

   public int getColumnCount() {
      // System.out.println("getColumnCount");
      if (this.matrix == null) {
         return 1;
      }
      try {
         return this.matrix.populationSize() + 1;
      } catch (PIPopulationException ex) {
         Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
         return 1;
      }
   }

   public Object getValueAt(int row, int column) {
      // System.out.println("getValueAt(" + row + "," + column + ")");
      try {
         if (column == 0) {
            return this.matrix.getIndividual(row).getName();
         } else {
            return new Double(this.matrix.getKinship(row, column - 1) * this.factor);
         }
      } catch (PIPopulationException ex) {
         Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
         return "?";
      } catch (NullPointerException ex) {
         Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
         return "?";
      }
   }

   @Override
   public String getColumnName(int column) {
      // System.out.println("getColumnName(" + column + ")");
      if (column == 0) {
         return "AGR Matrix";
      } else {
         try {
            return this.matrix.getIndividual(column - 1).getName();
         } catch (PIPopulationException ex) {
            Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
            return "?";
         } catch (NullPointerException ex) {
            Logger.getLogger(MatrixTableModel.class.getName()).log(Level.WARNING, null, ex);
            return "?";
         }
      }
   }

   @Override
   public boolean isCellEditable(int row, int col) {
      // TODO: needs more complex code!
      return false;
   }

   @Override
   public void setValueAt(Object value, int row, int column) {
   // System.out.println("setValueAt(" + row + "," + column + "): " + value);
   // TODO: setValueAt
   }
}
