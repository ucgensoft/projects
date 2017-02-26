package com.ucgen.letserasmus.library.reservation.enumeration;

public enum EnmReservationStatus {

	INQUIRY(0),
	PENDING(1),
	CONFIRMED(2),
	DECLINED(3),
	EXPIRED(4),
	RECALLED(5),
	CANCELLED(6),
	WAITING_PAYMENT(7),
	CLOSED(8);
	
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
