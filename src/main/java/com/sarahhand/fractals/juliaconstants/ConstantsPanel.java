package com.sarahhand.fractals.juliaconstants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Extension from JPanel that adds a JLabel and a JTextField to itself.
 * 
 * @author M00031
 *
 */
public class ConstantsPanel extends JPanel {

	private static final long serialVersionUID = -3108288542354398812L;

	private JLabel constantNameLabel;
	private JTextField constantValueInput;

	private volatile float constantValue = 0;
	private String constantName;

	public float getConstantValue() {
		try {
			constantValue = Float.parseFloat(constantValueInput.getText());
		} catch(Exception e) {
			JOptionPane.showConfirmDialog(getThis(), "You must enter in a number");
		}
		return constantValue;
	}

	public String getConstantName() {
		return constantName;
	}

	private ConstantsPanel getThis() {
		return this;
	}

	public ConstantsPanel(String name) {
		super();

		constantName = name;

		constantNameLabel = new JLabel(constantName + " : ");
		constantValueInput = new JTextField(5);

		add(constantNameLabel);
		add(constantValueInput);
	}
}