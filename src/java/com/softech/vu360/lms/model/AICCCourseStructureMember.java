package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AICC_COURSESTRUCTURE_MEMBERS")
public class AICCCourseStructureMember implements SearchableKey {
	
	private static final long serialVersionUID = 6421166690155127382L;

	@Id
    @javax.persistence.TableGenerator(name = "AICC_COURSESTRUCTURE_MEMBERS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AICC_COURSEDESCRIPTOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AICC_COURSESTRUCTURE_MEMBERS_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="COURSESTRUCTURE_ID")
	private AICCCourseStructure aiccCourseStructure;
	
	@Column(name = "MEMBER_SYSTEM_ID")
	private String memberSystemId;

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the courseStructureId
	 */
	public AICCCourseStructure getCourseStructureId() {
		return aiccCourseStructure;
	}

	/**
	 * @param courseStructureId the courseStructureId to set
	 */
	public void setCourseStructureId(AICCCourseStructure aiccCourseStructure) {
		this.aiccCourseStructure = aiccCourseStructure;
	}

	/**
	 * @return the memberSystemId
	 */
	public String getMemberSystemId() {
		return memberSystemId;
	}

	/**
	 * @param memberSystemId the memberSystemId to set
	 */
	public void setMemberSystemId(String memberSystemId) {
		this.memberSystemId = memberSystemId;
	}

}
