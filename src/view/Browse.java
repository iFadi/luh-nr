package view;

import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.ReadPDF;

public class Browse {
	
    private String filename;
    private String dir;
    private String path;
	
	public Browse(View view, ReadPDF pdf) {

		JFileChooser c = new JFileChooser();
        c.setMultiSelectionEnabled(false);
        c.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", "pdf");
        c.setFileFilter(filter);
        // Demonstrate "Open" dialog:
        int rVal = c.showOpenDialog(view);
        if (rVal == JFileChooser.APPROVE_OPTION) {
        	view.progressBar.setIndeterminate(true);
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            setPath(dir + "/" + filename);
            try {
                    pdf.ReadPDF(getPath());
//                  pdf = new ReadExcel(getPath());
                    view.getStatus().setText(
							 " "+ pdf.getSubject()+
							 "\n " + pdf.getCertificate()+
							 "\n Anzahl gesamte FŠcher: "+pdf.getNumberOfSubjects()+
      						 "\n Anzahl benotete FŠcher: "+pdf.getNumberOfSubjectsWithGrade()+
      						 "\n Credit Points: "+pdf.getCredits()+
      						 "\n Note: "+pdf.getFinalGrade()+
      						 "\n Abschlussarbeit starten: "+pdf.getStartThesis()+
      						 "\n Studium Geschafft in Prozent... ");
//                  this.getStatus().
                    view.getStatus().setForeground(Color.black.darker());
                    view.progressBar.setIndeterminate(false);
                    view.progressBar.setValue((int)pdf.getProcent());
                    view.progressBar.setStringPainted(true);
//                  generate.setEnabled(true);
            } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    view.getStatus().setText("ERROR");
                    view.getStatus().setForeground(Color.red.darker());
            }
            // System.out.println(dir+"/"+filename);
            
        }
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
