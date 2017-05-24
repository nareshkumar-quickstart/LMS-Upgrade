package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Parameter;
/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "COURSEGROUP")
public class CourseGroup implements SearchableKey, SearchableCourses {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCourseGroupId")
	@GenericGenerator(name = "seqCourseGroupId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "COURSEGROUP") })
	private Long id;
	@Nationalized
	@Column(name = "NAME")
	private String name = null;
	
	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@OneToOne
	@JoinColumn(name = "ROOTCOURSEGROUP_ID")
	private CourseGroup rootCourseGroup ;
	
	@OneToOne
	@JoinColumn(name = "PARENTCOURSEGROUP_ID")
	private CourseGroup parentCourseGroup ;
	
	
	@Column(name = "COURSEGROUP_GUID")
	private String guid = null;
	
	@OneToMany(mappedBy="parentCourseGroup" )
	private List<CourseGroup> childrenCourseGroups = new ArrayList<CourseGroup>();
	
	@ManyToMany
	@JoinTable(name="COURSE_COURSEGROUP", joinColumns = @JoinColumn(name="COURSEGROUP_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID",referencedColumnName="ID"))
	private List<Course> courses = new ArrayList<Course>();
	
	@Column(name = "KEYWORDS")
	private String keywords = null;
	
	@Column(name = "CourseGroupID")
	private String courseGroupID= null;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CONTENTOWNER_ID" )
	private ContentOwner contentOwner ;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}

	/**
	 * @param dist
	 *            the course to add to the courses set
	 */
	public void addCourse(Course course) {
		courses.add(course);
	}

	/**
	 * @param courses
	 *            the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the rootCourseGroup
	 */
	public CourseGroup getRootCourseGroup() {
		return this.rootCourseGroup;
	}

	/**
	 * @param rootCourseGroup
	 *            the rootCourseGroup to set
	 */
	public void setRootCourseGroup(CourseGroup rootCourseGroup) {
		this.rootCourseGroup = rootCourseGroup;
	}

	/**
	 * @return the parentCourseGroup
	 */
	public CourseGroup getParentCourseGroup() {
		return this.parentCourseGroup;
	}

	/**
	 * @param parentCourseGroup
	 *            the parentCourseGroup to set
	 */
	public void setParentCourseGroup(CourseGroup parentCourseGroup) {
		this.parentCourseGroup = parentCourseGroup;
	}

	public void addChildCourseGroup(CourseGroup courseGroup) {
		courseGroup.setParentCourseGroup(this);
		courseGroup.setRootCourseGroup(this.rootCourseGroup);
		childrenCourseGroups.add(courseGroup);
	}

	/**
	 * @return the childrenCourseGroups
	 */
	public List<CourseGroup> getChildrenCourseGroups() {
		return childrenCourseGroups;
	}

	/**
	 * @param childrenCourseGroups
	 *            the childrenCourseGroups to set
	 */
	public void setChildrenCourseGroups(List<CourseGroup> childrenCourseGroups) {
		this.childrenCourseGroups = childrenCourseGroups;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();

	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	
	public ContentOwner getContentOwner() {
		return this.contentOwner;
	}
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public  boolean equals(Object obj) {
    	if (obj instanceof CourseGroup){
	    	CourseGroup other = (CourseGroup) obj;
	        if (this == obj)
	            return true;
	        else if (id == null && other.id == null)
	        	return true;
	        else if (this.id.equals(other.id))
	        	return true;
	    }
    	
        return false;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	public Set<Course> getActiveCoursesInHierarchy(){
		Set<Course> courses = new HashSet<Course>();
		courses.addAll(this.courses);
		Stack<CourseGroup> cgStack = new Stack<CourseGroup>();
		if(CollectionUtils.isNotEmpty(childrenCourseGroups)){
			cgStack.addAll(childrenCourseGroups);	
		}
		
		List<Course> cgCourses = null;
		while(!cgStack.isEmpty()){
			CourseGroup cg = cgStack.pop();
			cgCourses = cg.getCourses();
			for(Course course:cgCourses){
				if(course.isActive()){
					courses.add(course);
				}
			}
			cgStack.addAll(cg.getChildrenCourseGroups());
		}
		return courses;
	}
	// [07/22/2010] LMS-6388 :: Implement Retire Course functionality 
	public List<Course> getCoursesNotRetired() {
		
		List<Course> courseList = new ArrayList<Course>();
		
		for ( Course course : this.courses ) {
			if ( !course.isRetired() ) {
				courseList.add(course);
			}
		}
		return courseList;
	}
	public List<Course> getActiveCourses(){
		List<Course> courseList = new ArrayList<Course>();
		if(courses != null) {
			for ( Course course : courses ) {
				if ( course.isActive()) {
					courseList.add(course);
				}
			}
		}
		return courseList;
	}
	public Set<CourseGroup> getAllChildrenInHierarchy(){
		Stack<CourseGroup> stack = new Stack<CourseGroup>();
		Long parentCourseGroupID = this.getId();
		List<CourseGroup> children = this.getChildrenCourseGroups();
		stack.addAll(children);
		Set<CourseGroup> cgSet = new HashSet<CourseGroup>();
		cgSet.addAll(children);
		CourseGroup cg = null;
		while(!stack.isEmpty()){
			cg = stack.pop();
			// @MariumSaud : LMS-22222 Optimize the code to not fetch children if Given Parent ID is the children ID ; in order to avoid recursion.
			if(!parentCourseGroupID.equals(cg.getId())){
				children = cg.getChildrenCourseGroups();
				if(CollectionUtils.isNotEmpty(children)){
					cgSet.addAll(children);
					stack.addAll(children);	
				}
			}
		}
		return cgSet; 
	}

	/**
	 * @return the courseGroupID
	 */
	public String getCourseGroupID() {
		return courseGroupID;
	}

	/**
	 * @param courseGroupID the courseGroupID to set
	 */
	public void setCourseGroupID(String courseGroupID) {
		this.courseGroupID = courseGroupID;
	}
	
}