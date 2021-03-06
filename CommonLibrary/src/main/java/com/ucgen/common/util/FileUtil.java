package com.ucgen.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ValueOperationResult;

public class FileUtil {

	public static void copyFile(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			if (!dest.exists()) {
				dest.getParentFile().mkdirs();
				dest.createNewFile();
			}
			
			fileInputStream = new FileInputStream(source);
			fileOutputStream = new FileOutputStream(dest);
			
			inputChannel = fileInputStream.getChannel();
			outputChannel = fileOutputStream.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			FileUtil.safeCloseInputStream(fileInputStream);
			FileUtil.safeCloseFileOutputStream(fileOutputStream);
			if (inputChannel != null && inputChannel.isOpen()) {
				try {
					inputChannel.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				
			}
			if (outputChannel != null && outputChannel.isOpen()) {
				try {
					outputChannel.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public static void createFolder(String folderPath, Object fileLock) {
		File folder = new File(folderPath);
		if (fileLock != null) {
			synchronized (fileLock) {
				if (!folder.exists()) {
					folder.mkdirs();
				}
			}
		} else {
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
	}
	
	public static String concatPath(String path, String... subPathArr) {
		for (String subPath : subPathArr) {
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
			path += subPath;
		}
		
		return path;
	}
	
	public static void cleanDirectory(String dirPath, boolean recursive) {
		File directory = new File(dirPath);
		if (directory.exists() && directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					file.delete();
				}else if (file.isDirectory() && recursive) {
					cleanDirectory(file.getAbsolutePath(), recursive);
					file.delete();
				}
			}
		}
	}
	
	public static void deleteDirectory(String dirPath) {
		cleanDirectory(dirPath, true);
		File directory = new File(dirPath);
		if (directory.exists() && directory.isDirectory()) {
			directory.delete();
		}
	}
	
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			return file.delete();
		} else {
			return false;
		}
	}
	
	public static void safeCloseInputStream(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void safeCloseFileOutputStream(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void close(Closeable closable) {
		try {
			if (closable != null) {
				closable.close();
			}
		} catch (Exception e) {
			System.out.println("closeStream() : " + e.getMessage());
		}
	}
	
	public static ValueOperationResult<List<String>> readFile(String filePath) {
		ValueOperationResult<List<String>> operationResult = new ValueOperationResult<List<String>>();
		List<String> lineList = null;
		
		File file = new File(filePath);
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			
			lineList = new ArrayList<String>();
			while (line != null) {
				lineList.add(line);
				line = br.readLine();
			}
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(lineList);
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(ExceptionUtils.getStackTrace(e));
		} finally {
			close(br);
		}
		return operationResult;
	}
	
	public static String readFileAsString(String filePath) {
		
		StringBuilder fileContentBuilder = new StringBuilder();
		File file = new File(filePath);
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			
			while (line != null) {
				fileContentBuilder.append(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			return null;
		} finally {
			close(br);
		}
		return fileContentBuilder.toString();
	}
	
	public static String readInputStream(InputStream is, String encoding) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader( new InputStreamReader(is, encoding));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	
	public static void writeToFile(String filePath, List<String> fileLineList, boolean append) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File existingFile = new File(filePath);
			if (!append) {
				existingFile.delete();
			}
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			boolean isFirstLine = true;
			for (String fileLine : fileLineList) {
				if (!isFirstLine) {
					bw.newLine();
				} else {
					isFirstLine = false;
				}
				bw.write(fileLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
