//Image Comparator Panel

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ImageComparatorPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private ArrayList<ImageIcon> images;
	private int comparison = 1;
	private int index = 0;
	private int subindex = 1;
	private Stack<Integer> previousComparison;
	private Stack<Integer> previousSubindex;
	private boolean[] isDuplicate;
	private boolean[] isSimilar;
	private JLabel comparisonLabel;
	private JLabel leftImageLabel;
	private JLabel leftNameLabel;
	private JLabel rightImageLabel;
	private JLabel rightNameLabel;
	
	public ImageComparatorPanel(JFrame frame, ArrayList<ImageIcon> images) {
		//Initialize Panel Data
		this.frame = frame;
		this.images = images;
		previousComparison = new Stack<>();
		previousSubindex = new Stack<>();
		isDuplicate = new boolean[images.size()];
		isSimilar = new boolean[images.size()];
		//Set Panel Properties
		setLayout(new BorderLayout());
		//Dual Portion
		JPanel dualPanel = new JPanel(new GridLayout(1, 2));
		//Left Panel
		JPanel leftPanel = new JPanel(new BorderLayout());
		comparisonLabel = new JLabel("Comparison " + comparison + ":", SwingConstants.CENTER);
		leftPanel.add(comparisonLabel, BorderLayout.NORTH);
		leftImageLabel = new JLabel("", SwingConstants.CENTER);
		leftImageLabel.setIcon(images.get(index));
		leftPanel.add(leftImageLabel, BorderLayout.CENTER);
		leftNameLabel = new JLabel(images.get(index).getDescription(), SwingConstants.CENTER);
		leftPanel.add(leftNameLabel, BorderLayout.SOUTH);
		dualPanel.add(leftPanel);
		//Right Panel
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(new JLabel("Images: " + images.size() + ", Total comparisons: " +
				((images.size() * (images.size() - 1)) / 2), SwingConstants.CENTER), BorderLayout.NORTH);
		rightImageLabel = new JLabel("", SwingConstants.CENTER);
		rightImageLabel.setIcon(images.get(subindex));
		rightPanel.add(rightImageLabel, BorderLayout.CENTER);
		rightNameLabel = new JLabel(images.get(subindex).getDescription(), SwingConstants.CENTER);
		rightPanel.add(rightNameLabel, BorderLayout.SOUTH);
		dualPanel.add(rightPanel);
		add(dualPanel, BorderLayout.CENTER);
		//Button Portion
		JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
		JButton duplicatesButton = new JButton("Duplicates");
		duplicatesButton.setActionCommand("Duplicates");
		duplicatesButton.addActionListener(this);
		buttonPanel.add(duplicatesButton);
		JButton similarButton = new JButton("Similar");
		similarButton.setActionCommand("Similar");
		similarButton.addActionListener(this);
		buttonPanel.add(similarButton);
		JButton notIdenticalButton = new JButton("Not Identical");
		notIdenticalButton.setActionCommand("Not Identical");
		notIdenticalButton.addActionListener(this);
		buttonPanel.add(notIdenticalButton);
		JButton undoButton = new JButton("Undo");
		undoButton.setActionCommand("Undo");
		undoButton.addActionListener(this);
		buttonPanel.add(undoButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Duplicates") || e.getActionCommand().equals("Similar") ||
				e.getActionCommand().equals("Not Identical")) {
			previousComparison.push(comparison);
			previousSubindex.push(subindex);
			if (e.getActionCommand().equals("Duplicates")) {
				isDuplicate[index] = true;
				isDuplicate[subindex] = true;
				comparison += images.size() - subindex;
				subindex = images.size();
			} else if (e.getActionCommand().equals("Similar")) {
				isSimilar[index] = true;
				isSimilar[subindex] = true;
				comparison += images.size() - subindex;
				subindex = images.size();
			} else {
				comparison++;
				subindex++;
			}
			comparisonLabel.setText("Comparison " + comparison + ":");
			if (subindex < images.size()) {
				rightImageLabel.setIcon(images.get(subindex));
				rightNameLabel.setText(images.get(subindex).getDescription());
			} else {
				if (isDuplicate[index] && isSimilar[index]) {
					System.out.println("Duplicate & Similar: " + images.get(index).getDescription());
				} else if (isDuplicate[index]) {
					System.out.println("Duplicate:           " + images.get(index).getDescription());
				} else if (isSimilar[index]) {
					System.out.println("Similar:             " + images.get(index).getDescription());
				} else {
					System.out.println("Not Identical:       " + images.get(index).getDescription());
				}
				index++;
				previousComparison.clear();
				previousSubindex.clear();
				if (index < images.size() - 1) {
					subindex = index + 1;
					leftImageLabel.setIcon(images.get(index));
					leftNameLabel.setText(images.get(index).getDescription());
					rightImageLabel.setIcon(images.get(subindex));
					rightNameLabel.setText(images.get(subindex).getDescription());
				} else {
					if (isDuplicate[images.size() - 1] && isSimilar[images.size() - 1]) {
						System.out.println("Duplicate & Similar: " + images.get(images.size() - 1).getDescription());
					} else if (isDuplicate[images.size() - 1]) {
						System.out.println("Duplicate:           " + images.get(images.size() - 1).getDescription());
					} else if (isSimilar[images.size() - 1]) {
						System.out.println("Similar:             " + images.get(images.size() - 1).getDescription());
					} else {
						System.out.println("Not Identical:       " + images.get(images.size() - 1).getDescription());
					}
					frame.dispose();
					System.exit(0);
				}
			}
		} else if (e.getActionCommand().equals("Undo")) {
			if (!previousComparison.isEmpty() && !previousSubindex.isEmpty()) {
				comparison = previousComparison.pop();
				subindex = previousSubindex.pop();
				comparisonLabel.setText("Comparison " + comparison + ":");
				rightImageLabel.setIcon(images.get(subindex));
				rightNameLabel.setText(images.get(subindex).getDescription());
			}
		}
	}
}
