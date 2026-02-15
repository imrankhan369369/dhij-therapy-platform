package com.dhij.app.com.dhij.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKING", uniqueConstraints = {
    @UniqueConstraint(name = "uk_helper_start", columnNames = {"HELPER_ID", "START_TIME"})
})
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="HELPER_ID", nullable=false)
    private Long helperId;

    @Column(name="USER_NAME", nullable=false) // simple: store username from JWT
    private String username;

    @Column(name="START_TIME", nullable=false)
    private LocalDateTime startTime;

    @Column(name="END_TIME", nullable=false)
    private LocalDateTime endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHelperId() {
		return helperId;
	}

	public void setHelperId(Long helperId) {
		this.helperId = helperId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

    // getters/setters...
    
}
