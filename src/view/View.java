package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import model.ParsePDF;
import model.UpdateNotifier;
import model.Version;
import net.iharder.dnd.FileDrop;

/**
 * $Id$
 * $LastChangedDate$
 * 
 * @author Fadi M. H. Asbih
 * @email fadi_asbih@yahoo.de
 * @version $Revision$
 * @copyright $Date$
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
public class View extends JFrame implements ActionListener, Observer {

        private static final long serialVersionUID = 6177350218996491783L;
        private JButton bug;
        private JEditorPane status;
        private String path;
        private ParsePDF pdf;
        public JProgressBar progressBar;
        public Desktop d;   
        private InputPanel ip;

        public View(final ParsePDF pdf, Version version) throws Exception {
                this.setPdf(pdf);
        		UpdateNotifier un = new UpdateNotifier(version); // Notify if Update is available 

                this.setTitle("LUH Notenspiegel Rechner");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JPanel panel = new JPanel(); //Main Panel
                
                ip = new InputPanel(this, pdf);
                
                panel.setLayout(new BorderLayout());
                panel.add(ip, BorderLayout.NORTH);
                
                this.add(panel);
                this.pack();
                this.setSize(330, 250);
                          
                // Output
                bug = new JButton();
                progressBar = new JProgressBar();
                status = new JEditorPane();
                status.setEditable(false);
                status.setText(" Notenspiegel einfach hier ziehen geht auch :-)\n\n LUH-NR\n Version: "+version.toString());
                status.setForeground(Color.black.darker());
                panel.add(status);
                panel.add(progressBar, BorderLayout.AFTER_LAST_LINE);
                
        		if(un.IsNewVersionAvailable()) {
        			bug.setText("DOWNLOAD NOW"); //Download link to the new App
        		}

                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setLocationRelativeTo(null);
                this.setResizable(false);

                
                this.setVisible(true);
                bug.addActionListener(this);
                
                if(!d.isDesktopSupported())
                        bug.setEnabled(false);
                
                //TESTING Drag n Drop
                new  FileDrop(panel, new FileDrop.Listener()
                {   public void  filesDropped( java.io.File[] files )
                    {   
                	setPath(files[0].getAbsolutePath());
                	try {
						pdf.parseFile(getPath());
						getStatus().setText(
								 " "+ pdf.getSubject()+
								 "\n " + pdf.getCertificate()+
		      				     "\n Anzahl benotete FŠcher: "+pdf.getNumberOfSubjectsWithGrade()+" ["+(int)pdf.getWeightedCredits()+" CP]"+
		      					 "\n Anzahl unbenotete FŠcher: "+pdf.getNumberOfSubjectsWithoutGrade()+" ["+(int)pdf.getUnweightedCredits()+" CP]"+
		      					 "\n Anzahl gesamte FŠcher: "+pdf.getNumberOfSubjects()+
	      						 "\n Credit Points: "+(int)pdf.getCredits()+
	      						 "\n Note: "+pdf.getFinalGrade()+
	      						 "\n Abschlussarbeit starten: "+pdf.getStartThesis()+
	      						 "\n Studium Geschafft in Prozent... ");
	                    getStatus().setForeground(Color.black.darker());
	                    progressBar.setIndeterminate(false);
	                    progressBar.setValue((int)pdf.getPercent());
	                    progressBar.setStringPainted(true);
					} catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        getStatus().setText("ERROR");
                        getStatus().setForeground(Color.red.darker());
					}
                    }   // end filesDropped
                }); // end F
        }

        public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Bug/Issue Report")) {
                                try {
                                         
                                                URI u;
                                                d = Desktop.getDesktop();
                                                u = new URI("http://code.google.com/p/luh-nr/issues/list");
                                                d.browse(u); 
                                        
                                } catch (URISyntaxException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                        this.getStatus().setText("ERROR");
                                        this.getStatus().setForeground(Color.red.darker());
                                } catch (IOException e2) {
                                        // TODO Auto-generated catch block
                                        e2.printStackTrace();
                                        this.getStatus().setText("ERROR");
                                        this.getStatus().setForeground(Color.red.darker());
                                }
                }
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }
        
        public JEditorPane getStatus() {
                return status;
        }

        public void setStatus(JEditorPane status) {
                this.status = status;
        }

		public ParsePDF getPdf() {
			return pdf;
		}

		public void setPdf(ParsePDF pdf) {
			this.pdf = pdf;
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
}