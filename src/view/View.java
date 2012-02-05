package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.ReadPDF;

/**
 * 
 * View.java
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
public class View extends JFrame implements ActionListener {

        /**
         * 
         */
        private static final long serialVersionUID = 6177350218996491783L;
        private JButton open;
        private JButton generate;
        private JButton exit;
        private JButton bug;
        private JTextArea status;
        private String filename;
        private String dir;
        private String path;
        private ReadPDF pdf;
        public Desktop d;

        public View(ReadPDF pdf) throws Exception {
                this.pdf = pdf;

                this.setTitle("LUH Notenspiegel Rechner");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                this.add(panel, BorderLayout.NORTH);
                this.pack();
                this.setSize(280, 180);
//              this.setLocation(500, 100);

                open = new JButton("Open");
//              generate = new JButton("Generate XML");
                exit = new JButton("Exit");
                bug = new JButton("Bug/Issue Report");
                status = new JTextArea(2, 10);
//              status = new JTextArea(6, 20);
//              status.setHorizontalAlignment(JTextField.CENTER);
                status.setEditable(false);
//              generate.setEnabled(false);
                status.setText("LUH-NR\nVersion 1.1.0\n04.02.2012");
                status.setForeground(Color.black.darker());

                this.add(status, BorderLayout.CENTER);
                panel.setBorder(new TitledBorder("Notenspiegel wählen: "));
//              panel.add(generate);
                panel.add(open);
//              panel.add(bug);
                this.add(bug, BorderLayout.SOUTH);
                panel.add(exit);
                panel.setLayout(new GridLayout(1, 2));

                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setLocationRelativeTo(null);
                this.setResizable(false);

                panel.setVisible(true);
                this.setVisible(true);

//              generate.addActionListener(this);
                open.addActionListener(this);
                exit.addActionListener(this);
                bug.addActionListener(this);
                
                if(!d.isDesktopSupported())
                        bug.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Open")) {
                        this.Browse();
                }
                if (e.getActionCommand().equals("Exit")) {
                        System.exit(0);
                }
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

        public String getFileNameWithoutExtension(String file) {
                int index = file.lastIndexOf('.');
                if (index > 0 && index <= file.length() - 2) {
                        return file.substring(0, index);
                }
                return file.substring(0, index);
        }
        
        public void Browse() {
                JFileChooser c = new JFileChooser();
                c.setMultiSelectionEnabled(false);
                c.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", "pdf");
                c.setFileFilter(filter);
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(View.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                        filename = c.getSelectedFile().getName();
                        dir = c.getCurrentDirectory().toString();
                        setPath(dir + "/" + filename);
                        try {
                                pdf.ReadPDF(getPath());
//                              pdf = new ReadExcel(getPath());
                                this.getStatus().setText("Anzahl gesamte Fächer: "+pdf.getSubjects()+"\nAnzahl benotete Fächer: "+pdf.getSubjectsWithNote()+"\nCredits: "+pdf.getCredits()+"\nNote: "+pdf.getEndMark());
//                              this.getStatus().
                                this.getStatus().setForeground(Color.blue.darker());
//                              generate.setEnabled(true);
                        } catch (Exception e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                                this.getStatus().setText("ERROR");
                                this.getStatus().setForeground(Color.red.darker());
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
        
        public JTextArea getStatus() {
                return status;
        }

        public void setStatus(JTextArea status) {
                this.status = status;
        }
}