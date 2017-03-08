package com.ucgen.letserasmus.library.complaint.enumeration;

public enum EnmComplaintStatus {

	OPEN(0),
	PROCESSING(1),
	CLOSED(2);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	private EnmComplaintStatus(Integer id) {
		this.id = id;
	}
	
	public static EnmComplaintStatus getComplaintStatus(Integer id) {
		if (id != null) {
			for (EnmComplaintStatus enmComplaintStatus : EnmComplaintStatus.values()) {
				if (enmComplaintStatus.getId().equals(id)) {
					return enmComplaintStatus;
				}
			}
		}
		return null;
	}
	
}
