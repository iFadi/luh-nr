package model;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 * 
 * ReadPDF.java
 * 
 * 
 * @author Fadi M. H. Asbih
 * @email fadi_asbih@yahoo.de
 * @version 1.2.1  10/11/2012
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
        private String finalGrade; // holds the Calculated note based on all passed Exams.
        private double subjectGrade;
        private double subjectCredits;
        private double weightValue;
        private double weightCredits;
        private double credits; // holds the number of Credit Points.
        private String numberOfSubjects; // holds the passed number of all Subjects(rated and not rated).
        private String numberOfSubjectsWithGrade; // holds the passed number of all RATED Subjects.
        private String startThesis; // tells whether you are able to Start your Thesis based on the Credit Points.
        private String subject; // studied subject. i.e. B.Sc.Informatik, M.Sc.Mathematik, etc...
        private String certificate; // Tells whether Bachelor or Master.
        private double percent; // holds the percent value of the passed Exams.
        
//        ArrayList<String> x = new ArrayList();
        
        public ReadPDF() {
                
        }
        
        public void parseFile(final String file) throws Exception {
        		courses.clear(); //If more than one PDF is parsed
                PDDocument pddDocument=PDDocument.load(new File(file));
                PDFTextStripper textStripper=new PDFTextStripper();
                
                this.setStartThesis("noch nicht mšglich");
                int rankedNumberOfSubjects=0;
                int numberOfSubjects=0;
                String x = textStripper.getText(pddDocument);
                String[] lines = getLines(x);
                
                // find all passed rankedNumberOfSubjects and save them in a Vector.
                for(int i=0; i<lines.length; i++) {
                	System.out.println(lines[i]);
                	if(lines[i].contains("Fach:")) {
                		setSubject(lines[i]);
                	}
                	if(lines[i].contains("Abschluss:")) {
                		setCertificate(lines[i]);
                	}
                	if(lines[i].contains("BE")) {
                		courses.addElement(lines[i]);
                    }
                }
                //Debug
//                System.out.println(getSubject() +" "+getCertificate());
                for(int i=0; i<courses.size(); i++) {
                        
                        // first Check if the line is an Exam.
                        if(isExam(courses, i)) {
                                
                                // second Check if the Exam is rated.
                                if(isRated(courses, i)) {
                                        
                                        // Get the mark of the Subject.
                                        String mark = getMark(courses, i);
                                        // Count the number of the rankedNumberOfSubjects.
                                        rankedNumberOfSubjects++;
                                        // Count the number of the numberOfSubjects.
                                        numberOfSubjects++;
                                        // Get the Credit Points of the Subject.
                                        String credit = getCredit(courses, i);
                                        
                                        calculateAverageValue(mark, credit, 0);
                                        
//                                        not = Double.parseDouble(mark); // Get the Exam grade
//                                        sumCredit = Double.parseDouble(credit); // Get the Exam credit points
//                                
//                                        s1 = s1 + not*sumCredit;
//                                        s = s + sumCredit;
//                                        sumcredit = sumcredit + sumCredit;
                                        
                                        // Debug
//                                        System.out.println("Note: "+mark);
//                                        System.out.println("Credit: " + credit);
//                                        System.out.println(courses.elementAt(i));
                                }
                                else { // Not Rated Exams.
                                        numberOfSubjects++;
                                        String credit = getCredit(courses, i);
                                        calculateAverageValue(null, credit, 1);
//                                        sumCredit = Double.parseDouble(credit);
//                                        sumcredit = sumcredit + sumCredit;
                                        
                                        // Debug
//                                        System.out.println("Credit: " + credit);
//                                        System.out.println(courses.elementAt(i));
                                }
                        }
                }
                calculateAverageValue(null, null, 2);
//                setFinalGrade(df.format(s1/s)+"");
//                setCredits((int)sumcredit+"");
                setNumberOfSubjects(numberOfSubjects+"");
                
                if(getCertificate().contains("Master")) {
                	setPercent((getCredits()/120)*100);
                	if((int)getCredits() >= 75)
                		this.setStartThesis("mšglich");
                }
                else if(getCertificate().contains("Bachelor")) {
                	setPercent((getCredits()/180)*100);
                	if((int)getCredits() >= 140) 
                		this.setStartThesis("mšglich");
                }
              
                setNumberOfSubjectsWithGrade(rankedNumberOfSubjects+"");
                pddDocument.close();    
        }
        
        public void calculateAverageValue(String mark, String credit, int mode) {
            DecimalFormat df = new DecimalFormat("0.00");
            
        	switch (mode) {
        		case 0:
        			setSubjectGrade(Double.parseDouble(mark)); // Get the Exam grade
        			setSubjectCredits(Double.parseDouble(credit)); // Get the Exam credit points
        			setWeightValue(getWeightValue() + getSubjectGrade()*getSubjectCredits());
        			setWeightCredits(getWeightCredits()+getSubjectCredits()); // Sum of weighted Credit
        			break;
        		case 1:
        			setSubjectCredits(Double.parseDouble(credit)); // Get the Exam credit points
        			setCredits(getWeightCredits()+getSubjectCredits());
//        			setCredits(getCredits()+getSubjectCredits());
        			break;
        		case 2:
                    setFinalGrade(df.format(getWeightValue()/getWeightCredits())+"");
//                    setCredits(getCredits());
        			break;
        	}
        }
        
        /**
         * Help function
         *  
         * @param input
         * @return 
         */
        public String[] getLines(String input) {
                String[] lines = input.split("\n");
                return lines;
        }

        /**
         * @return the grade based on all passed rated subjects.
         */
        public String getFinalGrade() {
                return finalGrade;
        }

        /**
         * Saves the grade based on all passed rated subjects.
         * 
         * @param finalGrade
         */
        public void setFinalGrade(String finalGrade) {
                this.finalGrade = finalGrade;
        }
        
        /**
         * check if the Parsed lines are Exams, not i.e. Titles or addresses.
         * 
         * @param vector
         * @param index
         * @return Exam
         */
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
                if(vector.elementAt(index).indexOf("BA") > -1)
                		return true;
                else
                        return false;
        }
        /**
         * check if the Exam is a rated or not.
         * 
         * @param vector
         * @param index
         * @return Rated or Not
         */
        public boolean isRated(Vector<String> vector, int index) {
                if(vector.elementAt(index).contains(","))
                        return true;
                else
                        return false;
        }
        
        /**
         * get the Credit Points for the Exam.
         * 
         * @param vector
         * @param index
         * @return Credit Points
         */
        public String getCredit(Vector<String> vector, int index) {
                int startCreditPosition = vector.elementAt(index).indexOf("BE")+3; // Position of BE, warning if the exam title have BE, may cause parsing error!!
                int endCreditPosition = vector.elementAt(index).indexOf("BE")+5; // Position of BE
                String credit = vector.elementAt(index).substring(startCreditPosition,endCreditPosition);
                
                return credit;
        }
        
        /**
         * if Exam is rated, get the mark of that Exam.
         * 
         * @param vector
         * @param index
         * @return grade of the passed Exam.
         */
        public String getMark(Vector<String> vector, int index) {
                // find the start & end mark position of the Exam.
                int startMarkPosition = vector.elementAt(index).lastIndexOf("B")-4;
                int endMarkPosition = vector.elementAt(index).lastIndexOf("B")-1;

                // Replace the Comma with a dot in order to be able to calculate.
                String mark = vector.elementAt(index).substring(startMarkPosition,endMarkPosition).replace(',', '.');
                return mark;
        }
        
        /**
         * 
         * @return All passed Exams(rated and not rated).
         */
        public String getNumberOfSubjects() {
                return numberOfSubjects;
        }
        
        /**
         * Set the number of passed Subjects(rated and not rated).
         * 
         * @param numberOfSubjects
         */
        public void setNumberOfSubjects(String numberOfSubjects) {
                this.numberOfSubjects = numberOfSubjects;
        }
        
        /**
         * 
         * @return All rated passed Exams.
         */
        public String getNumberOfSubjectsWithGrade() {
                return numberOfSubjectsWithGrade;
        }
        
        /**
         * Set the number of passed rated Subjects.
         * 
         * @param numberOfSubjectsWithGrade
         */
        public void setNumberOfSubjectsWithGrade(String numberOfSubjectsWithGrade) {
                this.numberOfSubjectsWithGrade = numberOfSubjectsWithGrade;
        }
        
        /**
         * 
         * @return Credit Points.
         */
        public double getCredits() {
                return credits;
        }
        
        /**
         * Set the number of Credit Pionts.
         * 
         * @param credits
         */
        public void setCredits(double credits) {
                this.credits = credits;
        }

        /**
         * 
         * @return results of the credit points displayed in Percent.
         */
		public double getPercent() {
			return percent;
		}
		
		/**
		 * Saves the percent result 
		 * @param percent
		 */
		public void setPercent(double percent) {
			this.percent = percent;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}
		
		/**
		 * 
		 * @return certificate
		 */
		public String getCertificate() {
			return certificate;
		}
		
		/**
		 * 
		 * @param certificate
		 */
		public void setCertificate(String certificate) {
			this.certificate = certificate;
		}

		/**
		 * 
		 * @return status about starting the Thesis work
		 */
		public String getStartThesis() {
			return startThesis;
		}

		/**
		 * 
		 * @param startThesis
		 */
		public void setStartThesis(String startThesis) {
			this.startThesis = startThesis;
		}

		/**
		 * @return the subjectGrade
		 */
		public double getSubjectGrade() {
			return subjectGrade;
		}

		/**
		 * @param subjectGrade the subjectGrade to set
		 */
		public void setSubjectGrade(double subjectGrade) {
			this.subjectGrade = subjectGrade;
		}

		/**
		 * @return the subjectCredits
		 */
		public double getSubjectCredits() {
			return subjectCredits;
		}

		/**
		 * @param subjectCredits the subjectCredits to set
		 */
		public void setSubjectCredits(double subjectCredits) {
			this.subjectCredits = subjectCredits;
		}

		public double getWeightValue() {
			return weightValue;
		}

		public void setWeightValue(double weightValue) {
			this.weightValue = weightValue;
		}

		/**
		 * @return the weightCredits
		 */
		public double getWeightCredits() {
			return weightCredits;
		}

		/**
		 * @param weightCredits the weightCredits to set
		 */
		public void setWeightCredits(double weightCredits) {
			this.weightCredits = weightCredits;
		}
        
}