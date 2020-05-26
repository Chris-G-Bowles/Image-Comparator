//Image Comparator

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageComparator {
	public static void main(String[] args) {
		System.out.println("* Image Comparator *");
		if (args.length != 0 && args.length != 1) {
			error("This program's usage is as follows:\n" +
					"java ImageComparator\n" +
					"java ImageComparator <directory location>");
		}
		Scanner input = new Scanner(System.in);
		String directoryLocation;
		if (args.length == 0) {
			System.out.print("Enter a directory location containing 2 or more images: ");
			directoryLocation = input.nextLine();
		} else {
			directoryLocation = args[0];
		}
		File directory = new File(directoryLocation);
		if (!directory.isDirectory()) {
			error(directoryLocation + " is not a valid directory.");
		}
		input.close();
		System.out.println("(Please wait a few seconds for the images to load.)");
		ArrayList<ImageIcon> images = addImagesFromDirectory(directory);
		if (images.size() < 2) {
			error(directoryLocation + " contains less than 2 images.");
		}
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
	}
	
	private static ArrayList<ImageIcon> addImagesFromDirectory(File directory) {
		ArrayList<ImageIcon> images = new ArrayList<>();
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				BufferedImage image;
				try {
					image = ImageIO.read(file);
				} catch (Exception e) {
					image = null;
				}
				if (image == null) {
					System.out.println(file.getPath() + " does not contain a readable image, and is being skipped.");
					continue;
				}
				ImageIcon scaledImage = new ImageIcon(image.getScaledInstance(600, 600, Image.SCALE_DEFAULT));
				scaledImage.setDescription(file.getPath());
				images.add(scaledImage);
			} else if (file.isDirectory()) {
				images.addAll(addImagesFromDirectory(file));
			}
		}
		return images;
	}
	
	private static void error(String message) {
		System.out.println("Error: " + message);
		System.exit(1);
	}
}
