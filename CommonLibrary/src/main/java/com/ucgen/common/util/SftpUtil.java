package com.ucgen.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUtil {

	public static boolean getFile(String host, int port, String user,
			String password, String remoteDir, String remoteFilename,
			String outputDir, String localFileName, String tmpPath,
			Long latestFileSize) throws JSchException, SftpException {
		ChannelSftp channelSftp = null;

		try {
			channelSftp = getConnection(host, port, user, password);
			channelSftp.cd(remoteDir);
			
			String tmpOutputFileName = tmpPath + File.separator + localFileName;
			channelSftp.get(remoteFilename, tmpOutputFileName);

			File downloadedFile = new File(tmpOutputFileName);
			if (downloadedFile.exists()) {
				return true;
			} else {
				return false;
			}
		} finally {
			closeConnection(channelSftp);
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean putFile(String host, int port, String user, String password, String remoteDir, String remoteSubFolder, String localFilePath) throws JSchException, SftpException, IOException {
		ChannelSftp channelSftp = null;
		FileInputStream fileInputStream = null;
		try {
			channelSftp = getConnection(host, port, user, password);
			if (remoteDir != null && !remoteDir.trim().isEmpty()) {
				channelSftp.cd(remoteDir);
			}
			
			remoteSubFolder = remoteSubFolder.replace("/", File.separator);
			remoteSubFolder = remoteSubFolder.replace("\\", File.separator);
			
			String splitRegEx = File.separator; 
			if (splitRegEx.equals("\\")) {
				splitRegEx += splitRegEx;
			}
			
			if (remoteSubFolder != null && !remoteSubFolder.trim().isEmpty()) {
				for (String folderName: remoteSubFolder.split(splitRegEx)) {
					if (folderName != null && !folderName.trim().isEmpty()) {
						List<LsEntry> ls = channelSftp.ls(".");
						boolean folderExist = false;
						if (ls.size() > 0) {
							for (ChannelSftp.LsEntry lsEntry : ls) {
								String filename = lsEntry.getFilename();
								if (filename.equalsIgnoreCase(folderName)) {
									folderExist = true;
									break;
								}
							}
						}
						if (!folderExist) {
							channelSftp.mkdir(folderName);
						}
						channelSftp.cd(folderName);
						remoteDir = remoteDir + "/" + folderName;
					}
				}
			}
			
			File localFile = new File(localFilePath);
			fileInputStream = new FileInputStream(localFile);
			channelSftp.put(fileInputStream, localFile.getName());
			return true;
		} finally {
			closeConnection(channelSftp);
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean deleteFile(String host, int port, String user, String password, String remoteDir, String remoteSubFolder, String fileName) throws JSchException, SftpException, IOException {
		ChannelSftp channelSftp = null;
		try {
			channelSftp = getConnection(host, port, user, password);
			channelSftp.cd(remoteDir);
			
			remoteSubFolder.replace("/", File.separator);
			remoteSubFolder.replace("\\", File.separator);
			
			String splitRegEx = File.separator; 
			if (splitRegEx.equals("\\")) {
				splitRegEx += splitRegEx;
			}
			
			if (remoteSubFolder != null && !remoteSubFolder.trim().isEmpty()) {
				for (String folderName: remoteSubFolder.split(splitRegEx)) {
					List<LsEntry> ls = channelSftp.ls(remoteDir);
					boolean folderExist = false;
					if (ls.size() > 0) {
						for (ChannelSftp.LsEntry lsEntry : ls) {
							String filename = lsEntry.getFilename();
							if (filename.matches(folderName)) {
								folderExist = true;
								break;
							}
						}
					}
					if (!folderExist) {
						return true;
					} else {
						channelSftp.cd(folderName);
						remoteDir = remoteDir + "/" + folderName;
					}
				}
				List<LsEntry> ls = channelSftp.ls(remoteDir);
				if (ls.size() > 0) {
					for (ChannelSftp.LsEntry lsEntry : ls) {
						String remoteFilename = lsEntry.getFilename();
						if (remoteFilename.equalsIgnoreCase(fileName)) {
							channelSftp.rm(remoteFilename);
						}
					}
				}
			}
			return true;
		} finally {
			closeConnection(channelSftp);
		}
	}

	private static ChannelSftp getConnection(String host, int port,
			String user, String password) throws JSchException {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");

		JSch jsch = new JSch();
		int sport = port;
		session = jsch.getSession(user, host, sport);
		session.setPassword(password);
		session.setConfig(config);
		session.connect();

		channel = session.openChannel("sftp");
		channel.connect();
		channelSftp = (ChannelSftp) channel;

		return channelSftp;
	}

	private static void closeConnection(ChannelSftp channelSftp) throws JSchException {
		if ((channelSftp != null) && (channelSftp.isConnected())) {
			channelSftp.disconnect();
			channelSftp.getSession().disconnect();
		}
	}
	
	/*
	public static void main(String[] args) throws JSchException, SftpException, IOException {
		String host = "10.201.232.194";
		int port = 22;
		String user = "ttech";
		String password = "Qwerty123456";
		
		String fileName = "C:\\test\\tmp\\test.txt";
		
		putFile(host, port, user, password, "/ttech/SOL/DEV/MEHMET/test/", "tbas_test", fileName);
	}
	
	public static void main(String[] args) throws JSchException, SftpException, IOException {
		String host = args[0];
		int port = Integer.valueOf(args[1]);
		String user = args[2];
		String password = args[3];
		String subPath = args[4];
		String folder = args[5];
		
		String fileName = "test.txt";
		
		putFile(host, port, user, password, subPath, folder, fileName);
	}
	*/

}
