package controller;

import model.ReadPDF;
import model.UpdateNotifier;
import view.UpdateView;
import view.View;
/**
 * 
 * RunApp.java
 * 
 * 
 * @author Fadi M.H.Asbih
 * @email fadi_asbih@yahoo.de
 * @version 1.2.0  04/02/2012
 * @copyright 2012
 * 
 * TERMS AND CONDITIONS:
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
public class RunApp {
        
        public static void main(String[] args) throws Exception {
                ReadPDF pdf = new ReadPDF();
                
                /** Notifiy if Update is available **/
                UpdateNotifier un = new UpdateNotifier();
                
                if(un.IsNewVersionAvailable()) {
                        /** Load The Update View **/
                        UpdateView av = new UpdateView(pdf);
                }
                else {
                        /** Load The Main App View **/
                        View view = new View(pdf);
                }       
        }

}