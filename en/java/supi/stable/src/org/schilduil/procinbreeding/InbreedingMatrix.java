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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InbreedingMatrix implements PopulationAddMemberListener, PopulationRemoveMemberListener, PopulationReorganizedListener {

   public static void main(String[] args) {

      try {

         System.out.println("Opening " + args[0] + "...");
         InbreedingMatrix matrix = new InbreedingMatrix(new File(args[0]));
         if (args.length > 1) {
            if ("purified".equalsIgnoreCase(args[1])) {
               matrix.doPurified = true;
            }
         }
         int size = matrix.populationSize();
         System.out.println("Population size = " + size);
         if (matrix.unknowns != null) {
            System.out.println("Number of unknown parents found: " + matrix.unknowns.length + "(may contain duplicates)");
            for (int i = 0; i < matrix.unknowns.length; i++) {
               System.out.println("  - " + matrix.unknowns[i]);
            }
         }
         PIPopulation population = matrix.getPopulation();
         for (int column = 0; column < size; column++) {

            System.out.print(population.get(column).getName() + ";");

         }
         System.out.println();
         for (int row = 0; row < size; row++) {

            for (int column = 0; column < size; column++) {

               System.out.print(matrix.getKinship(row, column) + ";");

            }
            System.out.println();

         }

      } catch (IOException e) {

         // TODO Auto-generated catch block
         e.printStackTrace();

      } catch (PIPopulationException e) {

         // TODO Auto-generated catch block
         e.printStackTrace();

      }

   }
   private String[] unknowns = null;
   private boolean doPurified = false;

   public PIPopulation getPopulation() {

      return this.population;

   }

   public int populationSize() throws PIPopulationException {

      return this.population.size();

   }

   static int getMatrixPosition(int i) {

      return getMatrixPosition(i, i);

   }

   static int getMatrixPosition(int row, int column) {

      if (row < 0) {
         throw new IndexOutOfBoundsException("The matrix row can't be negative.");
      }
      if (column < 0) {
         throw new IndexOutOfBoundsException("The matrix row can't be negative.");
      }

      if (row < column) {
         int temp = row;
         row = column;
         column = temp;
      }

      return (row * (row + 1) / 2) + column;

   }

   public InbreedingMatrix(File file) throws IOException, PIPopulationException {

      this(file, false);

   }

   public InbreedingMatrix(URL file) throws IOException, PIPopulationException {

      this(file, false);

   }

   public InbreedingMatrix(File file, boolean purified) throws IOException, PIPopulationException {

      this.population = new PIPopulationImpl();
      this.unknowns = PedigreeFileParser.parse(file, population);
      // TODO: Check the unknowns and maybe repair it? Shouldn't this be a feature
      // of the called function?
      this.init(population, purified);

   }

   public InbreedingMatrix(URL file, boolean purified) throws IOException, PIPopulationException {

      this.population = new PIPopulationImpl();
      this.unknowns = PedigreeFileParser.parse(file, population);
      // TODO: Check the unknowns and maybe repair it? Shouldn't this be a feature
      // of the called function?
      this.init(population, purified);

   }

   private void calculate() throws PIPopulationException {

      this.recalculate(0);

   }

   public int recalculateFromIndividual(PIIndividual individual) throws PIPopulationException {

      int fromrow = this.population.findNumber(individual.getName());
      this.recalculate(fromrow);
      return fromrow;

   }

   private void recalculate(int fromrow) throws PIPopulationException {

      while (fromrow < this.population.size()) {

         PIIndividual individual = this.population.get(fromrow);
         this.recalculateRow(individual, fromrow++);

      }

   }

   private void recalculateRow(PIIndividual individual, int row) throws PIPopulationException {

      int pRow1 = -1;
      int pRow2 = -1;

      PIIndividual p1 = individual.getParent(0);
      PIIndividual p2 = individual.getParent(1);

      if (p1 != null) {
         pRow1 = this.population.findNumber(p1.getName());
      }
      if (p2 != null) {
         pRow2 = this.population.findNumber(p2.getName());
      }

      // Anything up to (row,row)
      for (int i = 0; i < row; i++) {

         // Initially on 0
         double value = 0.0;

         // Parents added
         if (pRow1 >= 0) {
            if ((this.doPurified) && (pRow1 == i)) {
               value += 1 - this.getKinship(pRow1, i);
            } else {
               value += this.getKinship(pRow1, i);
            }
         }
         if (pRow2 >= 0) {
            if ((this.doPurified) && (pRow2 == i)) {
               value += 1 - this.getKinship(pRow2, i);
            } else {
               value += this.getKinship(pRow2, i);
            }
         }

         // Averaging the parents
         value = value / 2.0;

         this.setKinship(row, i, value);

      }

      // Setting the inbreeding
      if ((pRow1 >= 0) && (pRow2 >= 0)) {

         Double value = this.getKinship(pRow1, pRow2);
         if (value == null) {
            this.setInbreeding(row, 0.0);
         } else {
            this.setInbreeding(row, value.doubleValue());
         }

      }

   }
   private PIPopulation population = null;
   private Vector<Double> matrix = new Vector<Double>();

   // TODO: Doesn't work anymore if you change the population size - should be
   // possible by adding 'rows' to the matrix
   // TODO: When people disappear from the population it really gets messy... -
   // ADDED THE LISTENERS FOR THIS
   public InbreedingMatrix(PIPopulation population) throws PIPopulationException {

      this(population, false);

   }

   public InbreedingMatrix(PIPopulation population, boolean purified) throws PIPopulationException {

      this.init(population, purified);

   }

   private void init(PIPopulation population, boolean purified) throws PIPopulationException {

      this.doPurified = purified;
      this.population = population;
      int matrixsize = getMatrixPosition(population.size() - 1) + 1;
      this.matrix = new Vector<Double>(matrixsize);
      for (int i = 0; i < population.size(); i++) {
         for (int j = 0; j < i; j++) {
            // System.out.println("(" + i + "," + j + ")=" + getMatrixPosition(i,j));
            this.setKinship(j, i, 0.0);
         }
         // System.out.println("(" + i + "," + i + ")*=" + getMatrixPosition(i,i));
         this.setInbreeding(i, 0.0);
      }
      calculate();

   }

   public double getKinship(int row, int column) throws PIPopulationException {

      if (row >= this.population.size()) {
         throw new IndexOutOfBoundsException("Row " + row + " does not exist (only " + this.population.size() + " rows)");
      }
      if (column >= this.population.size()) {
         throw new IndexOutOfBoundsException("Column " + column + " does not exist (only " + this.population.size() + " columns)");
      }

      int position = getMatrixPosition(row, column);

      Double result = (Double) this.matrix.get(position);
      if (result == null) {
         if (row == column) {
            return 0.5;
         } else {
            return 0.0;
         }
      }
      return result.doubleValue();

   }

   public void setKinship(int row, int column, double value) throws PIPopulationException {

      if (row >= this.population.size()) {
         throw new IndexOutOfBoundsException("Row " + row + " does not exist (only " + this.population.size() + " rows)");
      }
      if (column >= this.population.size()) {
         throw new IndexOutOfBoundsException("Column " + column + " does not exist (only " + this.population.size() + " columns)");
      }

      int position = getMatrixPosition(row, column);

      if (value < 0.0) {

         throw new IndexOutOfBoundsException("Value can't be negative.");

      }

      if (value > 1.0) {

         throw new IndexOutOfBoundsException("Value must be between 0.0 and 1.0");

      }

      if (position == this.matrix.size()) {
         this.matrix.add(new Double(value));
      } else {
         this.matrix.set(position, new Double(value));
      }

   }

   public void setInbreeding(int i, Double value) throws PIPopulationException {

      this.setKinship(i, i, (1 + value) / 2.0);

   }

   public void setInbreeding(String one, Double value) throws PIPopulationException {

      int i = this.population.findNumber(one);
      this.setInbreeding(i, value);

   }

   public void setKinship(String one, String two, Double value) throws PIPopulationException {

      int row = this.population.findNumber(one);
      int column = this.population.findNumber(two);
      this.setKinship(row, column, value);

   }

   public void populationMemberAddedAction(PIIndividual individual, int position) {
   // TODO Auto-generated method stub

   }

   public void populationMemberRemovedAction(PIIndividual individual, int position) {
   // TODO Auto-generated method stub

   }

   public void populationReorganisedAction(Map mapping) {
   // TODO Auto-generated method stub

   }

   public PIIndividual getIndividual(int i) throws PIPopulationException {

      return this.population.get(i);

   }

   public void saveToCsv(File file, char decimalpoint, char comma, boolean doubleValues) throws IOException, PIPopulationException {

      FileWriter writer = null;
      PrintWriter out = null;
      try {

         writer = new FileWriter(file);
         out = new PrintWriter(writer);
         int size = this.populationSize();

         // Title line
         if (doubleValues) {
            out.print("AVG");
            out.print(comma);
         } else {
            out.print("Kinship");
            out.print(comma);
         }
         for (int i = 0; i < size; i++) {

            out.print(this.getIndividual(i).getName());
            out.print(comma);

         }
         out.println();

         // Data lines
         for (int i = 0; i < size; i++) {

            out.print(this.getIndividual(i).getName());
            out.print(comma);
            for (int j = 0; j < size; j++) {

               String value;
               if (doubleValues) {
                  value = Double.toString(this.getKinship(i, j) * 2.0);
               } else {
                  value = Double.toString(this.getKinship(i, j));
               }
               out.print(value.replace('.', decimalpoint));
               out.print(comma);

            }
            out.println();

         }
         out.flush();

      } catch (PIPopulationException ex) {

         throw ex;

      } catch (IOException ioe) {

         throw ioe;

      } finally {

         out.close();

      }

   }
}
