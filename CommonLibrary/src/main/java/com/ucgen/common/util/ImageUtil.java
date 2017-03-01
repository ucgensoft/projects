package com.ucgen.common.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.ucgen.common.model.Size;

public class ImageUtil {

	public static void resizeImage(String originalImagePath, String resizedImagePath, Size size) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
		resizeImage(originalImage, resizedImagePath, size);
	}
	
	public static void resizeImage(InputStream originalFileStream, String resizedImagePath, Size size) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(originalFileStream);
		resizeImage(originalImage, resizedImagePath, size);
	}
	
	public static void resizeImage(File originalFile, String resizedImagePath, Size size) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(originalFile);
		resizeImage(originalImage, resizedImagePath, size);
	}
	
	private static void resizeImage(BufferedImage originalImage, String resizedImagePath, Size size) throws IOException {
		
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		
		Float IMG_NEW_WIDTH = size.getWidth();
		Float IMG_NEW_HEIGHT = size.getHeight();
		
		float widthRate = size.getWidth() / originalImage.getWidth();
		float heightRate = size.getHeight() / originalImage.getHeight();
		float appliedRate = 0;
		
		if (widthRate <= heightRate) {
			appliedRate = widthRate;
		} else {
			appliedRate = heightRate;
		}
		
		IMG_NEW_WIDTH = originalImage.getWidth() * appliedRate;
		IMG_NEW_HEIGHT = originalImage.getHeight() * appliedRate;
		
		BufferedImage resizedImage = new BufferedImage(IMG_NEW_WIDTH.intValue(), IMG_NEW_HEIGHT.intValue(), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_NEW_WIDTH.intValue(), IMG_NEW_HEIGHT.intValue(), null);
		g.dispose();
		
		String fileSuffix = resizedImagePath.substring(resizedImagePath.lastIndexOf(".") + 1);

		ImageIO.write(resizedImage, fileSuffix, new File(resizedImagePath));
	}
	
}
