package de.oldzitterhand.barcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Just an example of how to generate a QR code.
 * 
 * @author Patrick Metz
 */
public class QRCodeGenerator {

	public static void main(String[] args) {
		writeQRCode();
	}

	public static void writeQRCode() {
		QRCodeWriter writer = new QRCodeWriter();
		
		int width = 256, height = 256;
		try {
			BitMatrix bitMatrix =
					writer.encode("https://github.com/oldzitterhand/", BarcodeFormat.QR_CODE, width, height);
			
			int onColor		= 0xFF777777;	//   0% transparent gray
			int offColor	= 0x00FFFFFF;	// 100% transparent white
			
			BufferedImage bufferedImage =
					MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(onColor, offColor));
			
			try {
				ImageIO.write(bufferedImage, "png", new File("qrcode.png"));
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

}
