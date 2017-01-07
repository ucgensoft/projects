package com.ucgen.letserasmus.library.file.enumeration;

public enum EnmFileType {

	DOC(1, "doc"),
	PDF(2, "pdf"),
	XML(3, "xml"),
	TXT(4, "txt"),
	PNG(5, "png"),
	JPG(6, "jpg"),
	JPEG(7, "jpeg");
	
	private Integer value;
	private String fileSuffix;
	
	public Integer getValue() {
		return this.value;
	}
	
	public String getFileSuffix() {
		return this.fileSuffix;
	}
	
	private EnmFileType(Integer value, String fileSuffix) {
		this.value = value;
		this.fileSuffix = fileSuffix;
	}
	
	public static EnmFileType getFileType(Integer fileType) {
		for (EnmFileType enmFileType : EnmFileType.values()) {
			if (enmFileType.getValue().equals(fileType)) {
				return enmFileType;
			}
		}
		return null;
	}
	
	public static EnmFileType getFileTypeWithSuffix(String fileSuffix) {
		for (EnmFileType enmFileType : EnmFileType.values()) {
			if (enmFileType.getFileSuffix().equalsIgnoreCase(fileSuffix)) {
				return enmFileType;
			}
		}
		return null;
	}
	
}
