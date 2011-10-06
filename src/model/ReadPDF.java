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
 * @version 1.0.1  06/10/2011
 * @copyright 2011
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
//		System.out.println(textStripper.getText(pddDocument));
		
		double not=0;
		double sumCredit = 0;
		double s=0;
		double s1=0;
		double s2=0;
		double sumcredit = 0;
		int fach=0;
		int fs=0;
		String x = textStripper.getText(pddDocument);
		String[] lines = getLines(x);
		for(int i=0; i<lines.length; i++) {
			if(lines[i].contains("BE") && lines[i].contains(".")) {
				courses.addElement(lines[i]);
			}
		}
		
		for(int i=0; i<courses.size(); i++) {
			int sn = courses.elementAt(i).lastIndexOf(".")+6;
			int en = courses.elementAt(i).lastIndexOf(".")+9;
			
//			System.out.println(s+" "+e);
			String note = courses.elementAt(i).substring(sn,en);
			String nott = note.replace(',', '.');
//			System.out.println(courses.elementAt(i).indexOf("PL") > -1);
			if(!note.contains("BE") && getExamArt(courses.elementAt(i))) {
				fach++;
				fs++;
				int sc = courses.elementAt(i).lastIndexOf(".")+13;
				int ec = courses.elementAt(i).lastIndexOf(".")+15;
				String credit = courses.elementAt(i).substring(sc,ec);

				not = Double.parseDouble(nott);
				sumCredit = Double.parseDouble(credit);
				
				s1 = s1 + not*sumCredit;
				s = s + sumCredit;
				
				sumcredit = sumcredit + sumCredit;
//				
//				System.out.println(not);
//				System.out.println(credit);
//				System.out.println(courses.elementAt(i));
			}
			else if(getExamArt(courses.elementAt(i))){
				fs++;
				int sc = courses.elementAt(i).lastIndexOf(".")+9;
				int ec = courses.elementAt(i).lastIndexOf(".")+11;
				String credit = courses.elementAt(i).substring(sc,ec);
				sumCredit = Double.parseDouble(credit);
				sumcredit = sumcredit + sumCredit;
//				System.out.println(credit);
//				System.out.println(courses.elementAt(i));
			}
		}
		setEndMark(s1/s+"");
		setCredits((int)sumcredit+"");
		setSubjects(fs+"");
		setSubjectsWithNote(fach+"");
//		System.out.println((int)sumcredit);
		pddDocument.close();
		
//		System.out.println(s1/s);
		
		
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
	
	public boolean getExamArt(String arg) {
		if(arg.indexOf("SL") > -1)
			return true;
		if(arg.indexOf("WL") > -1)
			return true;
		if(arg.indexOf("GL") > -1)
			return true;
		if(arg.indexOf("PL") > -1)
			return true;
		if(arg.indexOf("BA") > -1)
			return true;
		if(arg.indexOf("BK") > -1)
			return true;
		else
			return false;
	}
}
