package view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.ReadPDF;

public class InputPanel extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9017507416738367162L;
	private JButton open;
    private JButton exit;
    private Browse browse;
    private ReadPDF pdf;
    private View view;
    
	public InputPanel(View view, ReadPDF pdf) {
		this.setPdf(pdf);
		this.setView(view);
		new JPanel();

		open = new JButton("Open");
        exit = new JButton("Exit");
        
        this.add(open);
        this.add(exit);
        this.setBorder(new TitledBorder("Notenspiegel wählen: "));
        this.setLayout(new GridLayout(1,1));
        this.setVisible(true);
        
        open.addActionListener(this);
        exit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open")) {
                setBrowse(new Browse(getView(), getPdf()));
        }
        if (e.getActionCommand().equals("Exit")) {
                System.exit(0);
        }
        
	}

	public Browse getBrowse() {
		return browse;
	}

	public void setBrowse(Browse browse) {
		this.browse = browse;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ReadPDF getPdf() {
		return pdf;
	}

	public void setPdf(ReadPDF pdf) {
		this.pdf = pdf;
	}
}
