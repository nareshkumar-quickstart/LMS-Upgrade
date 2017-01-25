package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

public class OrganizationalGroup {

	private String name;
    private Integer seats;
    private Integer seatsUsed;
    private Long id;
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getSeats() {
		return seats;
	}
	
	public void setSeats(Integer seats) {
		this.seats = seats;
	}
	
	public Integer getSeatsUsed() {
		return seatsUsed;
	}
	
	public void setSeatsUsed(Integer seatsUsed) {
		this.seatsUsed = seatsUsed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
