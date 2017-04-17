package com.ucgen.letserasmus.library.reservation.enumeration;

public enum EnmReservationStatus {

	INQUIRY(0),
	PENDING(1),
	ACCEPTED(2),
	DECLINED(3),
	EXPIRED(4),
	RECALLED(5),
	HOST_CANCELLED(6),
	CLIENT_CANCELLED(7),
	WAITING_PAYMENT(8),
	CLOSED(9);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	private EnmReservationStatus(Integer id) {
		this.id = id;
	}
	
	public static EnmReservationStatus getReservationStatus(Integer id) {
		if (id != null) {
			for (EnmReservationStatus reservationStatus : EnmReservationStatus.values()) {
				if (reservationStatus.getId().equals(id)) {
					return reservationStatus;
				}
			}
		}
		return null;
	}
	
}
