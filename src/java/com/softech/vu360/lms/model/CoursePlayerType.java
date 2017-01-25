package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Haider.ali
 * @date: 22-09-2015
 */



@Entity
@Table(name = "COURSE_PLAYERTYPE")
public class CoursePlayerType implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "COURSE_PLAYERTYPE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSE_PLAYERTYPE_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	@Column(name="PLAYER_VERSION")
	private String playerVersion;
	@Column(name="DESCRIPTION")
	private String description;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COURSE_ID")
	private Course course;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id=id;
	}
	public String getPlayerVersion() {
		return playerVersion;
	}
	
	public void setPlayerVersion(String playerVersion) {
		this.playerVersion = playerVersion;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	@Override
	public String toString() {
		return "CoursePlayerType [id=" + id + ", playerVersion="
				+ playerVersion + ", description=" + description + "]";
	}
	
}
