import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/* adapted from https://learn-java-by-example.com/java/add-checkbox-items-jlist/
 * made by Daniel Neelappa
 * */
public class User {

	public static void main(String args[]) {
		// container for tasks
		LinkedList<Task> tasks = new LinkedList<Task>();
		TimeUnit timeUnit = new TimeUnit();
		// timing

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a list containing CheckboxListItem's
		DefaultListModel<CheckboxListItem> model = new DefaultListModel<CheckboxListItem>();
		JList<CheckboxListItem> list = new JList<CheckboxListItem>(model);

		// Use a CheckboxListRenderer (see below)
		// to renderer list cells

		list.setCellRenderer(new CheckboxListRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Add a mouse listener to handle changing selection

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();

				// Get index of item clicked

				int index = list.locationToIndex(event.getPoint());
				CheckboxListItem item = (CheckboxListItem) list.getModel().getElementAt(index);

				// Toggle selected state

				item.setSelected(!item.isSelected());

				// Repaint cell

				list.repaint(list.getCellBounds(index, index));
			}
		});

		// create java text field for input new tasks
		JTextField textField = new JTextField("");
		textField.setSize(200, 50);
		JLabel status = new JLabel("");
		JButton enter = new JButton("ENTER");
		JButton START = new JButton("START");
		// timer
		START.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timeUnit.timing = true;
				timeUnit.time = System.nanoTime();
				status.setText("" + 0);
			}
		});

		// enter button creation of action
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (textField.getText().length() >= 4 && textField.getText().substring(0, 4).equals("<d> ")
						&& textField.getText().contains(" ")) {
					System.out.println("deleting");
					String[] field = textField.getText().split(" ");
					int index;
					try {
						index = Integer.parseInt(field[1]);
					} catch (Exception e) {
						index = -1;
					}
					if (index != -1) {
						tasks.remove(index);
						model.removeAllElements();
						DefaultListModel<CheckboxListItem> model2 = new DefaultListModel<CheckboxListItem>();
						// update list
						for (int i = 0; i < tasks.size(); i++) {
							if (tasks.get(i).getElapsedString() == null){
							model.addElement(new CheckboxListItem(i + ": " + tasks.get(i).getTask()));
							}else{
								model.addElement(new CheckboxListItem(i + ": " + tasks.get(i).getTask() + " | " + tasks.get(i).getElapsedString() + " Seconds"));
								model.get(i).setDone(true);
							}
							}
					}

				} else {

					Task newTask;
					if (timeUnit.timing) {
						long time = System.nanoTime();
						newTask = new Task(textField.getText(), time);
					} else {
						newTask = new Task(textField.getText());
					}
					tasks.add(newTask);
					model.addElement(new CheckboxListItem(tasks.size() - 1 + ": " + textField.getText()));
				}
			}
		});
		// top level creation
		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints bagC = new GridBagConstraints();
		bagC.weightx = 1.0;
		bagC.fill = GridBagConstraints.HORIZONTAL;

		topPanel.add(status, bagC);
		topPanel.add(textField, bagC);
		topPanel.add(enter, bagC);
		topPanel.add(START, bagC);

		// bottom creation
		JTextField fileLocation = new JTextField("");
		textField.setSize(200, 50);
		JLabel loadStatus = new JLabel("");
		JButton loadButton = new JButton("LOAD");
		JButton saveButton = new JButton("SAVE");

		JPanel botPanel = new JPanel(new GridBagLayout());

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timeUnit.timing = false;
				timeUnit.time = 0;
				status.setText("");
				OpenFile op = new OpenFile(fileLocation.getText());
				LinkedList<Task> newOnes = op.read();
				int size = newOnes.size();
				tasks.clear();
				model.clear();
				for (int i = 0; i < size; i++) {
					tasks.add(newOnes.get(i));
					if(tasks.get(i).getElapsedString()==null){
						model.addElement(new CheckboxListItem(i + ": " + tasks.get(i).getTask()));

					}
					else{
						model.addElement(new CheckboxListItem(i + ": " + tasks.get(i).getTask() + " | " + tasks.get(i).getElapsedString() + " Seconds"));
						model.getElementAt(i).setDone(true);
					}
				}

			}
		});
		
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OpenFile op = new OpenFile(fileLocation.getText());
				try {
					LinkedList<Task> transfer = new LinkedList<Task>();
				
					op.save(fileLocation.getText(), tasks);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		botPanel.add(loadStatus, bagC);
		botPanel.add(fileLocation, bagC);
		botPanel.add(loadButton, bagC);
		botPanel.add(saveButton, bagC);

		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		frame.getContentPane().add(botPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(new JScrollPane(list));
		frame.pack();
		frame.setVisible(true);

		while (true) {
			int size = model.size();

			if (timeUnit.timing) {
				long tempTime = System.nanoTime();
				status.setText(" " + Math.round(((tempTime - timeUnit.time) / 1000000000.0) * 100) / 100 + " Seconds");

			}

			for (int i = 0; i < size; i++) {
				try {
					Task currentTask = tasks.get(i);
					CheckboxListItem currentCheckItem = model.get(i);

					if (timeUnit.timing && currentTask.getTime() == -1) {
						currentTask.setTime(timeUnit.time);

					}

					if (currentCheckItem.isSelected() && !currentCheckItem.isDone()) {
						System.out.println("Checked");
						currentCheckItem.setDone(true);
						double time = currentTask.getElapsed();
						time = Math.round(time * 100) / 100;
						currentCheckItem.setLabel(currentCheckItem.toString() + " | " + time + " Seconds");

					}
				} catch (Exception e) {

				}
			}
		}
	}
}

// Represents items in the list that can be selected

class CheckboxListItem {
	private String label;
	private boolean isDone = false;
	private boolean isSelected = false;

	public CheckboxListItem(String label) {
		this.label = label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String toString() {
		return label;
	}
}

// Handles rendering cells in the list using a check box

class CheckboxListRenderer extends JCheckBox implements ListCellRenderer<CheckboxListItem> {

	@Override
	public Component getListCellRendererComponent(JList<? extends CheckboxListItem> list, CheckboxListItem value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setEnabled(list.isEnabled());
		setSelected(value.isSelected());
		setFont(list.getFont());
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
}
