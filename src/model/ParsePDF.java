package model;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 * 
 * @author Fadi M. H. Asbih
 * @email fadi_asbih@yahoo.de
 * @copyright 2014
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
public class ParsePDF extends Observable{

        private Vector<String> courses; // A container to save all person passed courses.
        private double subjectGrade; // The grade of a specific Subject.
        private double subjectCredits; // The credit points for a specific subject.
        private double weightValue; // is the calculated value of the weightCredits multiplied with Grade.
        private double weightedCredits; // holds the number of credit points for rated courses.
        private double unweightedCredits; // holds the number of credit points for unrated courses.
        private double credits; // holds the number of all credit points.
        private double percent; // holds the percent value of the passed exams.
        private String numberOfSubjects; // holds the passed number of all subjects(rated and not rated).
        private String numberOfSubjectsWithGrade; // holds the passed number of all RATED subjects.
        private String numberOfSubjectsWithoutGrade; // holds the passed number of all NON RATED subjects.
        private String startThesis; // tells whether you are able to Start your thesis based on the credit points.
        private String subject; // studied subject. i.e. B.Sc.Informatik, M.Sc.Mathematik, etc...
        private String certificate; // Tells whether Bachelor or Master.
        private String finalGrade; // holds the Calculated note based on all passed Exams.
        
        DecimalFormat df = new DecimalFormat("0.00");
        int rankedNumberOfSubjects=0;
        int unrankedNumberOfSubjects=0;
                
        public ParsePDF() {
                
        }
        
        public void parseFile(final String file) throws Exception {
        		reset();// If more than one PDF is parsed
        		courses = new Vector<String>();
                setStartThesis("noch nicht möglich");
                findPassedCourses(file);
                
                //Debug
//                System.out.println(getSubject() +" "+getCertificate());
                for(int i=0; i<courses.size(); i++) { //iterate over the passed courses
                        
                        // Check if the line is an Exam.
                        if(!isExam(courses, i)) {
                                
                                // Check if the Exam is rated.
                                if(isRated(courses, i)) {
                                        
                                        // Get the mark of the Subject.
                                        String mark = getMark(courses, i);
                                        // Count the number of the rankedNumberOfSubjects.
//                                        rankedNumberOfSubjects++;
                                        // Count the number of the numberOfSubjects.
//                                        numberOfSubjects++;
                                        // Get the Credit Points of the Subject.
                                        String credit = getCredit(courses, i);
                                        credit = credit.replace(",", ".");
                                        
                                        calculateAverageValue(mark, credit, 0);
                                        
                                        // Debug
//                                        System.out.println(courses.elementAt(i));
//                                        System.out.println("Note: "+mark);
//                                        System.out.println("Credit: " + credit);
                                        
                                }
                                else { // Not Rated Exams.
//                                        numberOfSubjects++;
//                                        unrankedNumberOfSubjects++;
                                        String credit = getCredit(courses, i);
                                        calculateAverageValue(null, credit, 1);
                                        
                                        // Debug
//                                        System.out.println(courses.elementAt(i));
//                                        System.out.println("Credit: " + credit);
                                }
                        }
                }
                
                loadingBar();
                calculateAverageValue(null, null, 2);
        }
        
        public void loadingBar() {
            if(getCertificate().contains("Master")) {
            	setPercent((getCredits()/120)*100);
            	if((int)getCredits() >= 75)
            		this.setStartThesis("möglich");
            }
            else if(getCertificate().contains("Bachelor")) {
            	setPercent((getCredits()/180)*100);
            	if((int)getCredits() >= 140) 
            		this.setStartThesis("möglich");
            }
        }
        
        /**
         * Find all passed Exams with and without grade and save them to a vector.
         * 
         * @param file
         * @throws IOException
         */
        public void findPassedCourses(String file) throws IOException {
            PDDocument pddDocument=PDDocument.load(new File(file));
            PDFTextStripper textStripper = new PDFTextStripper();
            String x = textStripper.getText(pddDocument);
            String[] lines = getLines(x);
            
            // find all passed exams and save them in a Vector called courses.
            for(int i=0; i<lines.length; i++) {
//            	System.out.println(lines[i]);
            	if(lines[i].contains("Fach:")) {
            		setSubject(lines[i]);
            	}
            	if(lines[i].contains("Abschluss:")) {
            		setCertificate(lines[i]);
            	}
            	if(lines[i].contains("BE") && !lines[i].contains("Status:")) {
            		courses.addElement(lines[i]);
                }
            }
            pddDocument.close();
        }
        
        /**
         * 
         * @param mark
         * @param credit
         * @param mode
         * 
         * mode 0: Calculate rated Exams
         * mode 1: Calculate unrated Exams
         * mode 2: Calculate the final Grade
         */
        public void calculateAverageValue(String mark, String credit, int mode) {
          
        	switch (mode) {
        		case 0:
        			setSubjectGrade(Double.parseDouble(mark)); // Get the Exam grade
        			setSubjectCredits(Double.parseDouble(credit)); // Get the Exam credit points
        			setWeightValue(getWeightValue() + getSubjectGrade()*getSubjectCredits());
        			setWeightedCredits(getWeightedCredits()+getSubjectCredits()); // Sum of weighted Credit
        			rankedNumberOfSubjects++; // Count
                    setNumberOfSubjectsWithGrade(rankedNumberOfSubjects+"");
        			break;
        		case 1:
        			setSubjectCredits(Double.parseDouble(credit)); // Get the Exam credit points
        			setUnweightedCredits(getUnweightedCredits()+getSubjectCredits()); // Sum of weighted Credit
        			unrankedNumberOfSubjects++; // Count
                    setNumberOfSubjectsWithoutGrade(unrankedNumberOfSubjects+"");
        			break;
        		case 2:
                    setFinalGrade(df.format(getWeightValue()/getWeightedCredits())+"");
        			break;
        	}

        	setNumberOfSubjects(rankedNumberOfSubjects+unrankedNumberOfSubjects+""); // Calculate the number of all subjects(rated and non-rated)
        	setCredits(getWeightedCredits()+getUnweightedCredits());
        }
        
        /**
         * Help function, just to get each Line from the PDF file and save the lines to an array of Strings.
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
         * check if the Parsed lines are Exams, i.e. not titles or addresses.
         * Here you can add new Exam Types i.e. "MA".
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
                if(vector.elementAt(index).indexOf("MO") > -1)
        				return true;
        		if(vector.elementAt(index).indexOf("LN") > -1)
        				return true;
        		if(vector.elementAt(index).indexOf("LA") > -1)
        				return true;
        		if(vector.elementAt(index).indexOf("TL") > -1)
        				return true;
        		if(vector.elementAt(index).indexOf("PS") > -1)
    				return true;
                else
                        return false;
        }
        
        /**
         * check if the Exam is a rated or not.
         * 
         * @param vector
         * @param index
         * @return Rated Exam or Non-Rated Exam
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
        		int startCreditPosition, endCreditPosition;
        		startCreditPosition = vector.elementAt(index).indexOf("BE")+3; // Position of BE, warning if the exam title have BE, may cause parsing error!!
            	endCreditPosition = vector.elementAt(index).indexOf("BE")+5; // Position of BE
            	// if half credit points
            	if (vector.elementAt(index).charAt(endCreditPosition-1) == ',') {
        			endCreditPosition++;
        		}
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
         * Set the number of Credit Points.
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
		 * 
		 * @param percent
		 */
		public void setPercent(double percent) {
			this.percent = percent;
		}

		/**
		 * 
		 * @return The Subject your Studying i.e. "B.Sc. Informatik"
		 */
		public String getSubject() {
			return subject;
		}

		/**
		 * 
		 * @param subject
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
		
		/**
		 * 
		 * @return Master or Bachelor 
		 */
		public String getCertificate() {
			return certificate;
		}
		
		/**
		 * 
		 * @param Bachelor or Master
		 */
		public void setCertificate(String certificate) {
			this.certificate = certificate;
		}

		/**
		 * i.e if 140CP reached for Bachelor or 75CP for Master
		 * 
		 * @return "möglich" or "nicht möglich"
		 */
		public String getStartThesis() {
			return startThesis;
		}

		/**
		 * 
		 * @param "möglich" or "nicht möglich"
		 */
		public void setStartThesis(String startThesis) {
			this.startThesis = startThesis;
		}

		/**
		 * @return the subjectGrade i.e. 2.3
		 */
		public double getSubjectGrade() {
			return subjectGrade;
		}

		/**
		 * @param subjectGrade i.e. 2.3
		 */
		public void setSubjectGrade(double subjectGrade) {
			this.subjectGrade = subjectGrade;
		}

		/**
		 * @return the subjectCredits i.e. 4 CP
		 */
		public double getSubjectCredits() {
			return subjectCredits;
		}

		/**
		 * @param subjectCredits i.e. 4 CP
		 */
		public void setSubjectCredits(double subjectCredits) {
			this.subjectCredits = subjectCredits;
		}

		/**
		 * weightValue is the calculated Value of the weightCredits multiplied with Grade.
		 * 
		 * @return weightValue
		 */
		public double getWeightValue() {
			return weightValue;
		}

		/**
		 * 
		 * @param weightValue
		 */
		public void setWeightValue(double weightValue) {
			this.weightValue = weightValue;
		}

		/**
		 * The weighted Credits are calculated based on the number of Credit Point multiplied with the Grade.
		 * 
		 * @return the weightCredits i.e. 4CP*3.0
		 */
		public double getWeightedCredits() {
			return weightedCredits;
		}

		/**
		 * The weighted Credits are calculated based on the number of Credit Point multiplied with the Grade.
		 * 
		 * @param weightCredits
		 */
		public void setWeightedCredits(double weightCredits) {
			this.weightedCredits = weightCredits;
		}

		/**
		 * The unweighed Credits are the Credit Points without a Grade.
		 * 
		 * @return the unweightedCredits
		 */
		public double getUnweightedCredits() {
			return unweightedCredits;
		}

		/**
		 * The unweighed Credits are the Credit Points without a Grade.
		 * 
		 * @param unweightedCredits
		 */
		public void setUnweightedCredits(double unweightedCredits) {
			this.unweightedCredits = unweightedCredits;
		}

		/**
		 * @return numberOfSubjectsWithoutGrade
		 */
		public String getNumberOfSubjectsWithoutGrade() {
			return numberOfSubjectsWithoutGrade;
		}

		/**
		 * @param numberOfSubjectsWithoutGrade
		 */
		public void setNumberOfSubjectsWithoutGrade(
				String numberOfSubjectsWithoutGrade) {
			this.numberOfSubjectsWithoutGrade = numberOfSubjectsWithoutGrade;
		}

		/**
		 * The ability to add extra Subjects which are not listed in the PDF File.
		 * 
		 * @param mark
		 * @param credit
		 * @param mode
		 */
		public void addExtraSubject(String mark, String credit, int mode) {
			calculateAverageValue(mark, credit, mode); // Add rated Subject
			calculateAverageValue(null, null, 2); // Calculate Note
			loadingBar();
			setChanged();
			notifyObservers();
		}
		
		/**
		 * A reset function, if someone parses more than one PDF. Without closing and reopening the App.
		 */
		private void reset() {
	        setSubjectGrade(0);
	        setSubjectCredits(0);
	        setWeightValue(0);
	        setWeightedCredits(0); 
	        setUnweightedCredits(0); 
	        setCredits(0); 
	        setPercent(0); 
	        setNumberOfSubjects(null); 
	        setNumberOfSubjectsWithGrade(null); 
	        setNumberOfSubjectsWithoutGrade(null);
	        setStartThesis(null); 
	        setSubject(null); 
	        setCertificate(null); 
	        setFinalGrade(null);
	        rankedNumberOfSubjects=0;
	        unrankedNumberOfSubjects=0;
		}
}
