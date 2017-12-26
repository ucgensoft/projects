package com.ucgen.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.imgscalr.Scalr.Rotation;

import com.ucgen.common.model.Size;

public class ImageUtil {

	public static void resizeImage(String originalImagePath, String resizedImagePath, Size size, Integer rotationDegree)
			throws IOException {

		BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
		resizeImage(originalImage, originalImagePath, resizedImagePath, size, rotationDegree);
	}

	public static void resizeImage(File originalFile, String resizedImagePath, Size size, Integer rotationDegree)
			throws IOException {

		BufferedImage originalImage = ImageIO.read(originalFile);
		resizeImage(originalImage, originalFile.getAbsolutePath(), resizedImagePath, size, rotationDegree);
	}

	private static void resizeImage(BufferedImage originalImage, String originalFilePath, String resizedImagePath, Size size,
			Integer rotationDegree) throws IOException {
		if (rotationDegree != null && rotationDegree > 0) {
			if (rotationDegree == 90) {
				originalImage = Scalr.rotate(originalImage, Rotation.CW_90, null);
			} else if (rotationDegree == 180) {
				originalImage = Scalr.rotate(originalImage, Rotation.CW_180, null);
			} else if (rotationDegree == 270) {
				originalImage = Scalr.rotate(originalImage, Rotation.CW_270, null);
			}
			//originalImage = rotate(originalImage, rotationDegree);
		}
		
		if (originalImage.getColorModel().hasAlpha()) {
			originalImage = dropAlphaChannel(originalImage);
		}
		
		Integer IMG_NEW_WIDTH = size.getWidth().intValue();
		Integer IMG_NEW_HEIGHT = size.getHeight().intValue();

		float widthRate = size.getWidth() / originalImage.getWidth();
		float heightRate = size.getHeight() / originalImage.getHeight();
		float appliedRate = 0;

		// Smaller size photo is uploaded.
		if (widthRate > 1 && heightRate > 1) {
			IMG_NEW_WIDTH = originalImage.getWidth();
			IMG_NEW_HEIGHT = originalImage.getHeight();
		} else {
			if (widthRate <= heightRate) {
				appliedRate = widthRate;
			} else {
				appliedRate = heightRate;
			}
			
			IMG_NEW_WIDTH = new Float(originalImage.getWidth() * appliedRate).intValue();
			IMG_NEW_HEIGHT = new Float(originalImage.getHeight() * appliedRate).intValue();
		}
		
		BufferedImage resizedImage = resizeAndCrop(originalImage, IMG_NEW_WIDTH.intValue(), IMG_NEW_HEIGHT.intValue());
		String fileSuffix = resizedImagePath.substring(resizedImagePath.lastIndexOf(".") + 1);
		ImageIO.write(resizedImage, fileSuffix, new File(resizedImagePath));
		
	}
	
	public static BufferedImage dropAlphaChannel(BufferedImage src) {
		BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImg.getGraphics().drawImage(src, 0, 0, null);

		return convertedImg;
	}
	
	/*
	private static void resizeImage_old(BufferedImage originalImage, String resizedImagePath, Size size,
			Integer rotationDegree) throws IOException {
		if (rotationDegree != null) {
			originalImage = rotate(originalImage, rotationDegree);
		}
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
	*/
	/**
	 * Converts a given Image into a BufferedImage
	 * 
	 * @param img
	 *            The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		// Return the buffered image
		return bimage;
	}

	/**
	 * Splits an image into a number of rows and columns
	 * 
	 * @param img
	 *            The image to be split
	 * @param rows
	 *            The number of rows
	 * @param cols
	 *            The number of columns
	 * @return The array of split images in the vertical order
	 */
	public static BufferedImage[] splitImage(Image img, int rows, int cols) {
		// Determine the width of each part
		int w = img.getWidth(null) / cols;
		// Determine the height of each part
		int h = img.getHeight(null) / rows;
		// Determine the number of BufferedImages to be created
		int num = rows * cols;
		// The count of images we'll use in looping
		int count = 0;
		// Create the BufferedImage array
		BufferedImage[] imgs = new BufferedImage[num];
		// Start looping and creating images [splitting]
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				// The BITMASK type allows us to use bmp images with coloured
				// text and any background
				imgs[count] = new BufferedImage(w, h, BufferedImage.BITMASK);
				// Get the Graphics2D object of the split part of the image
				Graphics2D g = imgs[count++].createGraphics();
				// Draw only the required portion of the main image on to the
				// split image
				g.drawImage(img, 0, 0, w, h, w * y, h * x, w * y + w, h * x + h, null);
				// Now Dispose the Graphics2D class
				g.dispose();
			}
		}
		return imgs;
	}

	/**
	 * Converts a given BufferedImage into an Image
	 * 
	 * @param bimage
	 *            The BufferedImage to be converted
	 * @return The converted Image
	 */
	public static Image toImage(BufferedImage bimage) {
		// Casting is enough to convert from BufferedImage to Image
		Image img = (Image) bimage;
		return img;
	}

	/**
	 * Resizes a given image to given width and height
	 * 
	 * @param img
	 *            The image to be resized
	 * @param width
	 *            The new width
	 * @param height
	 *            The new height
	 * @return The resized image
	 */
	public static Image resize(Image img, int width, int height) {
		// Create a null image
		Image image = null;
		// Resize into a BufferedImage
		BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimg.createGraphics();
		bGr.drawImage(img, 0, 0, width, height, null);
		bGr.dispose();
		// Convert to Image and return it
		image = toImage(bimg);
		return image;
	}

	/**
	 * Creates a tiled image with an image upto given width and height
	 * 
	 * @param img
	 *            The source image
	 * @param width
	 *            The width of image to be created
	 * @param height
	 *            The height of the image to be created
	 * @return The created image
	 */
	public static Image createTiledImage(Image img, int width, int height) {
		// Create a null image
		Image image = null;
		BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// The width and height of the given image
		int imageWidth = img.getWidth(null);
		int imageHeight = img.getHeight(null);
		// Start the counting
		int numX = (width / imageWidth) + 2;
		int numY = (height / imageHeight) + 2;
		// Create the graphics context
		Graphics2D bGr = bimg.createGraphics();
		for (int y = 0; y < numY; y++) {
			for (int x = 0; x < numX; x++) {
				bGr.drawImage(img, x * imageWidth, y * imageHeight, null);
			}
		}
		// Convert and return the image
		image = toImage(bimg);
		return image;
	}

	/**
	 * Creates an empty image with transparency
	 * 
	 * @param width
	 *            The width of required image
	 * @param height
	 *            The height of required image
	 * @return The created image
	 */
	public static Image getEmptyImage(int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		return toImage(img);
	}

	/**
	 * Creates a colored image with a specified color
	 * 
	 * @param color
	 *            The color to be filled with
	 * @param width
	 *            The width of the required image
	 * @param height
	 *            The height of the required image
	 * @return The created image
	 */
	public static Image getColoredImage(Color color, int width, int height) {
		BufferedImage img = toBufferedImage(getEmptyImage(width, height));
		Graphics2D g = img.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		g.dispose();
		return img;
	}

	/**
	 * Flips an image horizontally. (Mirrors it)
	 * 
	 * @param img
	 *            The source image
	 * @return The image after flip
	 */
	public static Image flipImageHorizontally(Image img) {
		int w = img.getWidth(null);
		int h = img.getHeight(null);
		BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
		Graphics2D g = bimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		return toImage(bimg);
	}

	/**
	 * Flips an image vertically. (Mirrors it)
	 * 
	 * @param img
	 *            The source image
	 * @return The image after flip
	 */
	public static Image flipImageVertically(Image img) {
		int w = img.getWidth(null);
		int h = img.getHeight(null);
		BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
		Graphics2D g = bimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();
		return toImage(bimg);
	}

	/**
	 * Clones an image. After cloning, a copy of the image is returned.
	 * 
	 * @param img
	 *            The image to be cloned
	 * @return The clone of the given image
	 */
	public static Image clone(Image img) {
		BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null), img.getHeight(null)));
		Graphics2D g = bimg.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return toImage(bimg);
	}

	/**
	 * Rotates an image. Actually rotates a new copy of the image.
	 * 
	 * @param img
	 *            The image to be rotated
	 * @param angle
	 *            The angle in degrees
	 * @return The rotated image
	 * @throws IOException
	 */
	public static BufferedImage rotate(Image img, double angle) throws IOException {
		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math.abs(Math.cos(Math.toRadians(angle)));
		int w = img.getWidth(null), h = img.getHeight(null);
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
		BufferedImage bimg = (BufferedImage) getEmptyImage(neww, newh);
		Graphics2D g = bimg.createGraphics();
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawRenderedImage((BufferedImage) img, null);
		g.dispose();
		return bimg;
		// ImageIO.write(bimg, "png", new
		// File("D:\\tmp\\image_operations\\Capture_rotated.png"));
		// return toImage(bimg);
	}

	public static void main_(String[] args) throws IOException {

		File input = new File("C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\20170707_211517.jpg");
		BufferedImage image = ImageIO.read(input);

		File compressedImageFile = new File(
				"C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\compressed\\compress_20170707_211517.jpg");
		OutputStream os = new FileOutputStream(compressedImageFile);

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();

		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.5f);
		writer.write(null, new IIOImage(image, null, null), param);

		os.close();
		ios.close();
		writer.dispose();

		resizeImage("C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\compressed\\compress_20170707_211517.jpg",
				"C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\compressed\\compress_20170707_211517_resized.jpg",
				new Size(1000f, 1000f), 0);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String path = "C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\image\\source\\turkcell_sign_1.jpg";
		BufferedImage originalImage = ImageIO.read(new File(path));   
		
		//originalImage = Scalr.rotate(originalImage, Rotation.CW_90, null);
		
		Size size = new Size(1000f, 1000f);
		
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
		
		BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, originalImage.getWidth() / 2, originalImage.getHeight() / 2);
		
		//BufferedImage resizedImage = resizeAndCrop(originalImage, originalImage.getWidth(), originalImage.getHeight());
		
		ImageIO.write(resizedImage, "jpg", new File("C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\image\\dest\\turkcell_sign_1.jpg"));
		
		/*
		String path = "C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\image\\source\\jpeg.jpeg";
		final int THUMB_SIDE = 140;
		try {
		    BufferedImage masterImage = ImageIO.read(new File(path));
		    BufferedImage thumbImage = new BufferedImage(THUMB_SIDE, THUMB_SIDE, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = thumbImage.createGraphics();
		    g2d.drawImage(masterImage.getScaledInstance(THUMB_SIDE, THUMB_SIDE, Image.SCALE_DEFAULT), 0, 0, THUMB_SIDE, THUMB_SIDE, null);
		    g2d.dispose();
		    String thumb_path = path.substring(0, path.indexOf(".jpeg")) + "_thumb.jpeg";
		    ImageIO.write(thumbImage, "jpg", new File(thumb_path));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		*/
		
		/*
		File imageFile = new File("C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\image\\source\\jpeg.jpeg");
		File compressedImageFile = new File("C:\\Users\\ttmbuyukasik\\Desktop\\LetsErasmus\\image\\dest\\jpeg.jpeg");

		InputStream inputStream = new FileInputStream(imageFile);
		OutputStream outputStream = new FileOutputStream(compressedImageFile);

		float imageQuality = 0.5f;

		// Create the buffered image
		BufferedImage bufferedImage = ImageIO.read(inputStream);

		Iterator<ImageWriter> imageWriters = null;
		// Get image writers
		if (imageFile.getName().toUpperCase().endsWith("JPG")) {
			imageWriters = ImageIO.getImageWritersByFormatName("jpg");
		} else if (imageFile.getName().toUpperCase().endsWith("PNG")) {
			imageWriters = ImageIO.getImageWritersByFormatName("png");
		} else if (imageFile.getName().toUpperCase().endsWith("JPEG")) {
			imageWriters = ImageIO.getImageWritersByFormatName("jpeg");
		}

		if (!imageWriters.hasNext())
			throw new IllegalStateException("Writers Not Found!!");

		ImageWriter imageWriter = (ImageWriter) imageWriters.next();
		ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
		imageWriter.setOutput(imageOutputStream);

		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

		// Set the compress quality metrics
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(imageQuality);

		// Created image
		imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

		// close all streams
		inputStream.close();
		outputStream.close();
		imageOutputStream.close();
		imageWriter.dispose();
		
		*/

	}

	public BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	private static BufferedImage resizeAndCrop(BufferedImage bufferedImage, Integer width, Integer height) {

	    Mode mode = (double) width / (double) height >= (double) bufferedImage.getWidth() / (double) bufferedImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH
	            : Scalr.Mode.FIT_TO_HEIGHT;

	    bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, mode, width, height);
	    
	    int x = 0;
	    int y = 0;

	    if (mode == Scalr.Mode.FIT_TO_WIDTH) {
	        y = (bufferedImage.getHeight() - height) / 2;
	    } else if (mode == Scalr.Mode.FIT_TO_HEIGHT) {
	        x = (bufferedImage.getWidth() - width) / 2;
	    }

	    bufferedImage = Scalr.crop(bufferedImage, x, y, width, height);

	    return bufferedImage;
	}
	
}
