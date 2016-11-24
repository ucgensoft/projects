package com.ucgen.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchieveUtil {

	public static void unZip(String zipFile, String outputFolderPath) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = null;
		FileInputStream zipFileInputStream = null;
		FileOutputStream fos = null;
		try {
			File outputFolder = new File(outputFolderPath);
			if (!outputFolder.exists()) {
				outputFolder.mkdir();
			}
			
			zipFileInputStream = new FileInputStream(zipFile);
			zis = new ZipInputStream(zipFileInputStream);
			
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator
						+ fileName);

				if (ze.isDirectory()) {
					newFile.mkdirs();
				} else {
					File parentFolder = new File(newFile.getParent());
					if (!parentFolder.exists()) {
						parentFolder.mkdirs();
					}
					
					fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}				
				ze = zis.getNextEntry();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			FileUtil.safeCloseInputStream(zis);
			FileUtil.safeCloseInputStream(zipFileInputStream);
			FileUtil.safeCloseFileOutputStream(fos);
		}
	}
	
	public static void zipFile(String sourceFilePath, String zipFilePath) throws IOException {
		ZipOutputStream out = null;
		FileInputStream inputStream = null;
		try {
			File sourceFile = new File(sourceFilePath);
			out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			
			byte[] tmpBuf = new byte[1024];

			inputStream = new FileInputStream(sourceFilePath);
			out.putNextEntry(new ZipEntry(sourceFile.getName()));
			int len;
			while ((len = inputStream.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			inputStream.close();
			
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	public static void zipDir(String dirPath, String zipFilePath) throws IOException {
		ZipOutputStream out = null;
		try {
			File dirObj = new File(dirPath);
			out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			addDir(dirObj, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	private static void addDir(File dirObj, ZipOutputStream out) throws IOException {
		FileInputStream inputStream = null;
		try {
			File[] files = dirObj.listFiles();
			byte[] tmpBuf = new byte[1024];

			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					addDir(files[i], out);
					continue;
				}
				inputStream = new FileInputStream(files[i].getAbsolutePath());
				out.putNextEntry(new ZipEntry(files[i].getName()));
				int len;
				while ((len = inputStream.read(tmpBuf)) > 0) {
					out.write(tmpBuf, 0, len);
				}
				out.closeEntry();
				inputStream.close();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

}
