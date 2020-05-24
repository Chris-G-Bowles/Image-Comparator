//Image Comparator

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageComparator {
	public static void main(String[] args) {
		System.out.println("* Image Comparator *");
		if (args.length == 0 || args.length == 1) {
			Scanner input = new Scanner(System.in);
			String directoryLocation;
			if (args.length == 0) {
				System.out.print("Enter a directory location containing 2 or more images: ");
				directoryLocation = input.nextLine();
			} else {
				directoryLocation = args[0];
			}
			input.close();
			File directory = new File(directoryLocation);
			if (directory.isDirectory()) {
				System.out.println("(Please wait a few seconds for the images to load.)");
				ArrayList<ImageIcon> images = addImagesFromDirectory(directory);
				if (images.size() >= 2) {
					System.out.println("-----");
					JFrame frame = new JFrame("Image Comparator");
					ImageComparatorPanel panel = new ImageComparatorPanel(frame, images);
					frame.setContentPane(panel);
					int frameWidth = 1280;
					int frameHeight = 720;
					if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
						frameHeight += 22;
					} else if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
						frameWidth += 16;
						frameHeight += 39;
					}
					frame.setSize(frameWidth, frameHeight);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} else {
					System.out.println("Error: " + directoryLocation + " contains less than 2 images.");
				}
			} else {
				System.out.println("Error: " + directoryLocation + " is not a valid directory.");
			}
		} else {
			System.out.println("This program's usage is as follows:");
			System.out.println("java ImageComparator");
			System.out.println("java ImageComparator <directory location>");
		}
	}
	
	private static ArrayList<ImageIcon> addImagesFromDirectory(File directory) {
		File[] files = directory.listFiles();
		ArrayList<ImageIcon> images = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				try {
					ImageIcon image = new ImageIcon(ImageIO.read(files[i]).getScaledInstance(600, 600,
							Image.SCALE_DEFAULT));
					image.setDescription(files[i].getPath());
					images.add(image);
				} catch (Exception e) {
					System.out.println("Error: " + files[i].getPath() + " does not contain a readable image.");
				}
			} else if (files[i].isDirectory()) {
				images.addAll(addImagesFromDirectory(files[i]));
			}
		}
		return images;
	}
	
	private static void error(String message) {
		System.out.println("Error: " + message);
		System.exit(1);
	}
}
