/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author haider.ali,
 * @dated: 15-09-15
 *
 */
@Entity
@Table(name = "INSTRUCTOR")
public class Instructor implements SearchableKey {

	private static final long serialVersionUID = 1L;
	@Id
	@javax.persistence.TableGenerator(name = "INSTRUCTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "INSTRUCTOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "INSTRUCTOR_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "VU360USER_ID")
	private VU360User user ;
	
	@Column(name="AREAOFEXPERTISE") 
	private String areaOfExpertise = null;
	
	@Column(name="FIRSTNAME") 
	private String firstName = null;
	
	@Column(name="LASTNAME") 
	private String lastName = null;
	
	@Column(name="active") 
	private  Boolean active = true;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="INSTRUCTOR_SYNCHRONOUSCLASS", joinColumns = @JoinColumn(name="INSTRUCTOR_ID"),inverseJoinColumns = @JoinColumn(name="SYNCHRONOUSCLASS_ID"))
	private List<SynchronousClass> synchClasses = new ArrayList<SynchronousClass>();

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="INSTRUCTOR_COURSE", joinColumns = @JoinColumn(name="INSTRUCTOR_ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID"))
	private List<Course> approvedCourses = new ArrayList<Course>();

	@ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="INSTRUCTOR_CUSTOMFIELD", joinColumns = @JoinColumn(name="INSTRUCTOR_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();

	@ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="INSTRUCTOR_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="INSTRUCTOR_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();

	@Transient
	private String userName= null;
	
	@Override
	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Course> getApprovedCourses() {
		return approvedCourses;
	}

	public void setApprovedCourses(List<Course> approvedCourses) {
		this.approvedCourses = approvedCourses;
	}

	public List<CustomField> getCustomfields() {
		return customfields;
	}

	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}

	public List<CustomFieldValue> getCustomfieldValues() {
		return customfieldValues;
	}

	public void setCustomfieldValues(List<CustomFieldValue> customfieldValues) {
		this.customfieldValues = customfieldValues;
	}

	public String getAreaOfExpertise() {
		return areaOfExpertise;
	}

	public void setAreaOfExpertise(String areaOfExpertise) {
		this.areaOfExpertise = areaOfExpertise;
	}

	public  Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<SynchronousClass> getSynchClasses() {
		return synchClasses;
	}
	public void setSynchClasses(List<SynchronousClass> synchClasses) {
		this.synchClasses = synchClasses;
	}
	
	public void addSynchClasses(SynchronousClass synchClass) {
		synchClasses.add(synchClass);
	}

	public void setInstructorInfo(VU360User user) {
		setFirstName(user.getFirstName()); /* by default newly created instructor firstname is same to user firstname*/
		setLastName(user.getLastName()); /* by default newly created instructor lastname is same to user lastname*/
		/*Noman
		 * it was created as  Boolean and i(noman) created as int
		 * therefore at the time of merging I need to change to
		 *  Boolean and also required to change into other classes.
		*/
		setActive(true); /* by default newly created instructor is set to active = 1*/
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public ContentOwner getContentOwner() {
		return contentOwner;
	}
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public VU360User getUser() {
		return user;
	}

	public void setUser(VU360User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Instructor(Long id, String userName){
		this.id = id;
		this.userName = userName;
	}

	public Instructor() {
		// TODO Auto-generated constructor stub
	}
}
