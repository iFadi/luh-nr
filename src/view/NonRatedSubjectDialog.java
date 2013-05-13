package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.ParsePDF;

public class NonRatedSubjectDialog extends JDialog implements ActionListener {
	private JTextField textFieldCP;
	private ParsePDF pdf;
	/**
	 * Create the dialog.
	 */
	public NonRatedSubjectDialog(ParsePDF pdf) {
		this.setPdf(pdf);
		setBounds(100, 100, 300, 100);
		getContentPane().setLayout(null);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		JLabel lblNewLabel = new JLabel("Credit Points\n");
		lblNewLabel.setBounds(27, 8, 106, 28);
		getContentPane().add(lblNewLabel);
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBounds(196, 48, 86, 29);
			getContentPane().add(cancelButton);
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(this);
		}
		{
			JButton okButton = new JButton("Update");
			okButton.setBounds(121, 47, 75, 29);
			getContentPane().add(okButton);
			okButton.setActionCommand("Update");
			okButton.addActionListener(this);
			getRootPane().setDefaultButton(okButton);
		}
		
		textFieldCP = new JTextField();
		textFieldCP.setBounds(145, 8, 134, 28);
		getContentPane().add(textFieldCP);
		textFieldCP.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")) {
			dispose();
		}
		if(e.getActionCommand().equals("Update")) {
			getPdf().addExtraSubject(null, textFieldCP.getText(),1);
			dispose();
		}
	}

	/**
	 * @return the pdf
	 */
	public ParsePDF getPdf() {
		return pdf;
	}

	/**
	 * @param pdf the pdf to set
	 */
	public void setPdf(ParsePDF pdf) {
		this.pdf = pdf;
	}
}
