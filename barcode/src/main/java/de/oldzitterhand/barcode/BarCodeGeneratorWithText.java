package de.oldzitterhand.barcode;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;

/**
 * Just an example of how to generate a bar code with text.
 * It generates two images (one with the bar code, the other with the text) and combine them into one image.
 * 
 * @author Patrick Metz
 */
public class BarCodeGeneratorWithText {

	public static void main(String[] args) {
		writeBarCode();
	}

	public static void writeBarCode() {
		
		int width = 400, height = 90, textHeight = 25;
		String value = "1978-11-20";
		int barCodeColor	= 0xFF777777;	//   0% transparent gray
		int backGroundColor	= 0x00FFFFFF;	// 100% transparent white
		
		try {
			BufferedImage bufferedImage =
					createBarCodeImageWithText(width, height, textHeight, 16, value, barCodeColor, backGroundColor);
			try {
				ImageIO.write(bufferedImage, "png", new File("barcode.png"));
			} catch (IOException e) {
				System.err.println("ERROR: Could not save image to disk!");
				System.exit(-1);
			}
		} catch (WriterException e) {
			System.err.println("ERROR: Could not create image!");
			e.printStackTrace();
			System.exit(-2);
		}		
	}

	private static BufferedImage createBarCodeImageWithText(int width, int height, int textHeight, int textSize,
			String value, int barCodeColor, int backGroundColor) throws WriterException {
		BufferedImage barCodeImage = createBarCode(value, width, height, barCodeColor, backGroundColor);
		// the library might adjust the width and height of the bar code image, so take the actual values
		int imageWidth = barCodeImage.getWidth();
		int imageHeight = barCodeImage.getHeight();
		BufferedImage textImage = createText(value, imageWidth, textHeight, textSize, barCodeColor, backGroundColor);
		// combine the two images
		BufferedImage combined = new BufferedImage(imageWidth, imageHeight + textHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();
		g.drawImage(barCodeImage, 0, 0, null);
		g.drawImage(textImage, 0, imageHeight, null);
		return combined;
	}

	private static BufferedImage createText(
			String value, int width, int height, int textSize, int barCodeColor, int backGroundColor) {
		BufferedImage bufferedImage = new BufferedImage(width, height, TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		drawCenteredString(g, value, width, height, textSize, barCodeColor, backGroundColor);
		return bufferedImage;
	}
	
	private static void drawCenteredString(
			Graphics g, String text, int width, int height, int textSize, int textColor, int backGroundColor) {
		Rectangle rectangle = new Rectangle(width, height);
		Font font = new Font("Arial", Font.BOLD, textSize);

		// Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rectangle.x + (rectangle.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rectangle.y + ((rectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();

	    // Paint the background
	    g.setColor(new Color(backGroundColor, true));
	    g.fillRect(0, 0, rectangle.width, rectangle.height);

	    // Draw the String
	    g.setColor(new Color(textColor));
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	
	private static BufferedImage createBarCode(
			String value, int width, int height, int barCodeColor, int backGroundColor)	throws WriterException {
		Code39Writer code39Writer = new Code39Writer();
		BitMatrix bitMatrix =
				code39Writer.encode(value, BarcodeFormat.CODE_39, width, height, null);			
		BufferedImage bufferedImage =
				MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(barCodeColor, backGroundColor));
		return bufferedImage;
	}

}
