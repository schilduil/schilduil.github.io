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

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author bert
 */
public class MatrixCellDoubleRenderer extends JLabel implements TableCellRenderer {

   private boolean isBordered = true;
   private NumberFormat format = null;
   private double factor;
   private Border selectedBorder = null;
   private Border unselectedBorder = null;

   public MatrixCellDoubleRenderer(String pattern, double factor) {
      this.format = new DecimalFormat(pattern);
      this.factor = factor;
      setOpaque(true); //MUST do this for background to show up.
   }

   public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {

      this.setHorizontalAlignment(JLabel.CENTER);
      double value = (Double) object;
      this.setText(format.format(value));

      int red = (int) Math.round(255 * (this.factor - value) / this.factor);
      Color bColor = new Color(255, red, 255);
      Color fColor = Color.BLACK;

      if ((row + 1) == column) {
         bColor = Color.BLACK;
         fColor = Color.WHITE;
      }

      this.setBackground(bColor);
      this.setForeground(fColor);
      if (this.isBordered) {
         if (isSelected) {
            //selectedBorder is a solid border in the color
            //table.getSelectionBackground().
            if (selectedBorder == null) {
               selectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2,
                       table.getSelectionBackground());
            }
            setBorder(selectedBorder);
         } else {
            //unselectedBorder is a solid border in the color
            //table.getBackground().
            if (unselectedBorder == null) {
               unselectedBorder = BorderFactory.createMatteBorder(0, 0, 0, 0,
                       table.getBackground());
            }
            setBorder(unselectedBorder);
         }
      }
      // setToolTipText("Dummy");
      return this;

   }
}
