package springrest.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "specific_classes")
public class Specific_Class implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public School_Class getShool_class() {
		return school_class;
	}

	public void setShool_class(School_Class shool_class) {
		this.school_class = shool_class;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Long getSection() {
		return section;
	}

	public void setSection(Long section) {
		this.section = section;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}

	public List<Assignment> getClass_assignments() {
		return class_assignments;
	}

	public void setClass_assignments(List<Assignment> class_assignments) {
		this.class_assignments = class_assignments;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public List<User> getUsers_in_this_class() {
		return users_in_this_class;
	}

	public void setUsers_in_this_class(List<User> users_in_this_class) {
		this.users_in_this_class = users_in_this_class;
	}

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = false)
	private Date class_start_date;

	@Column(nullable = false, unique = false)
	private Date class_end_date;

	public Date getClass_start_date() {
		return class_start_date;
	}

	public void setClass_start_date(Date class_start_date) {
		this.class_start_date = class_start_date;
	}

	public Date getClass_end_date() {
		return class_end_date;
	}

	public void setClass_end_date(Date class_end_date) {
		this.class_end_date = class_end_date;
	}

	// Class
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "class_no", nullable = false)
	private School_Class school_class;

	// Term
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "term_id", nullable = false)
	private Term term;

	// Section
	@Column(nullable = false, unique = false)
	private Long section;

	// Instructor
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "instructor_id", nullable = false)
	private User instructor;

	// Assignments
	@JsonIgnore
	@OneToMany(mappedBy = "assignment_class", orphanRemoval = true, cascade=CascadeType.ALL)
	private List<Assignment> class_assignments;

	// Room
	@Column(nullable = false, unique = false)
	private String room;

	// Start_time
	@Column(nullable = false, unique = false)
	private Date start_time;

	// End_time
	@Column(nullable = false, unique = false)
	private Date end_time;

	// Users in class
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_classes", joinColumns = @JoinColumn(name = "specific_class_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	List<User> users_in_this_class;

}