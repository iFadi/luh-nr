package model;

import java.io.File;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 * 
 * ReadPDF.java
 * 
 * 
 * @author Fadi Asbih
 * @email fadi_asbih@yahoo.de
 * @version 1.1.0  04/02/2012
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
public class ReadPDF {

        Vector<String> courses = new Vector();
        private String endMark;
        private String Credits;
        private String subjects;
        private String subjectsWithNote;
        
        public String getSubjects() {
                return subjects;
        }

        public void setSubjects(String subjects) {
                this.subjects = subjects;
        }

        public String getSubjectsWithNote() {
                return subjectsWithNote;
        }

        public void setSubjectsWithNote(String subjectsWithNote) {
                this.subjectsWithNote = subjectsWithNote;
        }

        public String getCredits() {
                return Credits;
        }

        public void setCredits(String credits) {
                Credits = credits;
        }

        public ReadPDF() {
                
        }
        
        public void ReadPDF(String file) throws Exception {
                PDDocument pddDocument=PDDocument.load(new File(file));
                PDFTextStripper textStripper=new PDFTextStripper();
                
                double not=0;
                double sumCredit = 0;
                double s=0;
                double s1=0;
                double sumcredit = 0;
                int rankedSubjects=0;
                int subjects=0;
                String x = textStripper.getText(pddDocument);
                String[] lines = getLines(x);
                
                // find all passed rankedSubjects and save them in a Vector.
                for(int i=0; i<lines.length; i++) {
                        if(lines[i].contains("BE")) {
                                courses.addElement(lines[i]);
                        }
                }
                
                for(int i=0; i<courses.size(); i++) {
                        
                        // first Check if the line is an Exam.
                        if(isExam(courses, i)) {
                                
                                // second Check if the Exam is rated.
                                if(isRated(courses, i)) {
                                        
                                        // Get the mark of the Subject.
                                        String mark = getMark(courses, i);
                                        // Count the number of the rankedSubjects.
                                        rankedSubjects++;
                                        // Count the number of the Subjects.
                                        subjects++;
                                        // Get the Credit Points of the Subject.
                                        String credit = getCredit(courses, i);
                                        
                                        not = Double.parseDouble(mark);
                                        sumCredit = Double.parseDouble(credit);
                                
                                        s1 = s1 + not*sumCredit;
                                        s = s + sumCredit;
                                        sumcredit = sumcredit + sumCredit;
                                        
                                        // Debug
//                                        System.out.println("Note: "+mark);
//                                        System.out.println("Credit: " + credit);
//                                        System.out.println(courses.elementAt(i));
                                }
                                else { // Not Rated Exams.
                                        subjects++;
                                        String credit = getCredit(courses, i);
                                        sumCredit = Double.parseDouble(credit);
                                        sumcredit = sumcredit + sumCredit;
                                        
                                        // Debug
//                                        System.out.println("Credit: " + credit);
//                                        System.out.println(courses.elementAt(i));
                                }
                        }
                }
                
                
                setEndMark(s1/s+"");
                setCredits((int)sumcredit+"");
                setSubjects(subjects+"");
                setSubjectsWithNote(rankedSubjects+"");
                pddDocument.close();    
        }
        
        public String[] getLines(String input) {
                String[] lines = input.split("\n");
                return lines;
        }

        public String getEndMark() {
                return endMark;
        }

        public void setEndMark(String endMark) {
                this.endMark = endMark;
        }
        
        public boolean isExam(Vector<String> vector, int index) {
                if(vector.elementAt(index).indexOf("PL") > -1)
                        return true;
                if(vector.elementAt(index).indexOf("SL") > -1)
                        return true;
                if(vector.elementAt(index).indexOf("WL") > -1)
                        return true;
                if(vector.elementAt(index).indexOf("PR") > -1)
                        return true;
                if(vector.elementAt(index).indexOf("GL") > -1)
                    return true;
                else
                        return false;
        }
        
        public boolean isRated(Vector<String> vector, int index) {
                if(vector.elementAt(index).contains(","))
                        return true;
                else
                        return false;
        }
        
        public String getCredit(Vector<String> vector, int index) {
                int startCreditPosition = vector.elementAt(index).lastIndexOf("BE")+3;
                int endCreditPosition = vector.elementAt(index).lastIndexOf("BE")+5;
                String credit = vector.elementAt(index).substring(startCreditPosition,endCreditPosition);
                
                return credit;
        }
        
        public String getMark(Vector<String> vector, int index) {
                // find the start & end mark position of the Exam.
                int startMarkPosition = vector.elementAt(index).lastIndexOf("B")-4;
                int endMarkPosition = vector.elementAt(index).lastIndexOf("B")-1;

                // Replace the Comma with a dot in order to be able to calculate.
                String mark = vector.elementAt(index).substring(startMarkPosition,endMarkPosition).replace(',', '.');
                return mark;
        }
        
}