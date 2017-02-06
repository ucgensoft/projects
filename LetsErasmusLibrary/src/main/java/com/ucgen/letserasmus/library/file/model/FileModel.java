package com.ucgen.letserasmus.library.file.model;

import org.springframework.web.multipart.MultipartFile;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;

public class FileModel extends BaseModel {

	private static final long serialVersionUID = -2166580530245686052L;

	private Long id;
	private String fileName;
	private String fileSuffix;
	private Long fileSize;
	private Integer fileType;
	private Integer entityType;
	private Long entityId;
	
	private MultipartFile file;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSuffix() {
		if (this.fileSuffix == null) {
			if (this.fileType != null) {
				this.fileSuffix = EnmFileType.getFileType(this.fileType).getFileSuffix();
			}
		}
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public Integer getEntityType() {
		return entityType;
	}
	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
