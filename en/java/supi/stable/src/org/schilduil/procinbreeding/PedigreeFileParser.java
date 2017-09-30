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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

public class PedigreeFileParser {

   public static PIPopulation parse(URL file) throws IOException, PIPopulationException {

      PIPopulation population = new PIPopulationImpl();
      parse(file, population);
      return population;

   }

   public static PIPopulation parse(File file) throws IOException, PIPopulationException {

      PIPopulation population = new PIPopulationImpl();
      parse(file, population);
      return population;

   }

   public static String[] parse(URL file, PIPopulation population) throws IOException, PIPopulationException {

      return parse(file.openStream(), population);

   }

   public static String[] parse(File file, PIPopulation population) throws IOException, PIPopulationException {

      return parse(new FileInputStream(file), population);

   }

   public static String[] parse(InputStream in, PIPopulation population) throws IOException, PIPopulationException {

      Vector<String> unknowns = new Vector<String>();
      try {
         // Open the file
         // Get the object of DataInputStream
         // in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         // Read File Line By Line
         String line;
         while ((line = br.readLine()) != null) {

            // Some preprocessing
            line = line.replaceAll("\t", " ");
            line = line.trim();
            if (line.length() == 0) {
               
               continue;
               
            }
            if (line.charAt(0) == '#') {

               // Comment line: ignore
               continue;

            }

            String[] parts = line.split(" ");
            Vector<PIIndividual> parents = new Vector<PIIndividual>(2);

            // START DEBUGGING
            //for (int debug = 0; debug < parts.length; debug++) {
            //   System.out.print(parts[debug] + "\t");
            //}
            //System.out.println();
            // END DEBUGGING

            switch (parts.length) {

               case 0:
                  break;

               case 1:
                  population.addMember(new PIIndividualImpl(parts[0]));
                  break;

               case 2:
                  if (!"?".equals(parts[1])) {

                     PIIndividual parent = population.find(parts[1]);
                     if (parent != null) {

                        parents.add(parent);

                     } else {

                        unknowns.add(parts[1]);

                     }

                  }

                  population.addMember(new PIIndividualImpl(parts[0], parents));
                  break;

               default:
                  StringBuffer comment = new StringBuffer();
                  if (!"?".equals(parts[1])) {

                     PIIndividual parent = population.find(parts[1]);
                     if (parent != null) {

                        parents.add(parent);

                     } else {

                        unknowns.add(parts[1]);

                     }

                  }

                  if (!"?".equals(parts[2])) {

                     PIIndividual parent = population.find(parts[2]);
                     if (parent != null) {

                        parents.add(parent);

                     } else {

                        unknowns.add(parts[2]);

                     }

                  }

                  for (int i = 3; i < parts.length; i++) {

                     if (i > 3) {
                        comment.append(' ');
                     }
                     comment.append(parts[i]);

                  }

                  population.addMember(new PIIndividualImpl(parts[0], parents, comment.toString()));

            }

         }

         String[] contents = new String[unknowns.size()];
         unknowns.toArray(contents);
         return contents;

      } catch (IOException ioe) {

         throw ioe;

      } finally {

         if (in != null) {

            // Close the input stream
            in.close();

         }

      }

   }
}
