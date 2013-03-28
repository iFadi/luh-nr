package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.ParsePDF;
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
public class InputPanel extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9017507416738367162L;
	private JButton open;
    private JButton exit;
    private Browse browse;
    private ParsePDF pdf;
    private View view;
    
	public InputPanel(View view, ParsePDF pdf) {
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

	public ParsePDF getPdf() {
		return pdf;
	}

	public void setPdf(ParsePDF pdf) {
		this.pdf = pdf;
	}
}
