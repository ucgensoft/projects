package com.ucgen.common.util.aws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AwsS3Util {

	private static String APP_FILE_BUCKET = "static.letserasmus.com";
	private AmazonS3 s3client;
	
	private static AwsS3Util awsS3UtilInstance;
	
	public AwsS3Util(String configFilePath, String profileName) {
		ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider(configFilePath, profileName); 
		this.s3client = AmazonS3Client.builder().withCredentials(profileCredentialsProvider).withRegion(Regions.EU_WEST_1).build();
	}
	
	public static void initialize(String configFilePath, String profileName) {
		awsS3UtilInstance = new AwsS3Util(configFilePath, profileName);
	}
	
	public static AwsS3Util getInstance() {
		return awsS3UtilInstance;
	}
	
	public static void main(String[] args) throws IOException {
		AwsS3Util.initialize("D:\\tmp\\aws\\credentials", "default");
		//AwsS3Util.getInstance().deleteFile(APP_FILE_BUCKET, "user/3/config.txt");
		AwsS3Util.getInstance().uploadFile(APP_FILE_BUCKET, "D:\\tmp\\test.txt", "user/3/config.txt");
	}
	
	/*
	public static void main(String[] args) throws IOException {
        try {
        	AmazonS3 client = AmazonS3Client.builder().build();
        	System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            client.putObject(new PutObjectRequest(
            		                 bucketName, keyName, file));
			
         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        } catch (Exception e) { 
        	System.out.println(CommonUtil.getExceptionMessage(e));
        }
    }
	*/
	private static final String SUFFIX = "/";	
	
	public void createFolder(String bucketName, String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent, metadata);

		// send request to S3 to create folder
		this.s3client.putObject(putObjectRequest);
	}

	public void deleteFolder(String bucketName, String folderName) {
		List<S3ObjectSummary> fileList = this.s3client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			this.s3client.deleteObject(bucketName, file.getKey());
		}
		this.s3client.deleteObject(bucketName, folderName);
	}
	
	public void deleteFile(String bucketName, String remoteFilePath) {
		this.s3client.deleteObject(bucketName, remoteFilePath);
	}
	
	public void uploadFile(String bucketName, String localFilePath, String remoteFilePath) {
		this.s3client.putObject(new PutObjectRequest(bucketName, remoteFilePath, new File(localFilePath))
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
}
