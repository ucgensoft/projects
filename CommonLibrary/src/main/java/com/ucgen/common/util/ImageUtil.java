package com.ucgen.common.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void resizeImage(String originalImagePath, String resizedImagePath, float maxWidth, float maxHeight) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
		resizeImage(originalImage, resizedImagePath, maxWidth, maxHeight);
	}
	
	public static void resizeImage(InputStream originalFileStream, String resizedImagePath, float maxWidth, float maxHeight) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(originalFileStream);
		resizeImage(originalImage, resizedImagePath, maxWidth, maxHeight);
	}
	
	public static void resizeImage(File originalFile, String resizedImagePath, float maxWidth, float maxHeight) throws IOException {
		
		BufferedImage originalImage = ImageIO.read(originalFile);
		resizeImage(originalImage, resizedImagePath, maxWidth, maxHeight);
	}
	
	private static void resizeImage(BufferedImage originalImage, String resizedImagePath, float maxWidth, float maxHeight) throws IOException {
		
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		
		Float IMG_NEW_WIDTH = maxWidth;
		Float IMG_NEW_HEIGHT = maxHeight;
		
		float widthRate = maxWidth / originalImage.getWidth();
		float heightRate = maxHeight / originalImage.getHeight();
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
