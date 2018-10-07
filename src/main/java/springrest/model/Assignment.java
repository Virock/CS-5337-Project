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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "assignments")
public class Assignment implements Serializable {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Date getPost_date() {
		return post_date;
	}

	public void setPost_date(Date post_date) {
		this.post_date = post_date;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public List<User_Assignment> getUsers_with_this_assignment() {
		return users_with_this_assignment;
	}

	public void setUsers_with_this_assignment(List<User_Assignment> users_with_this_assignment) {
		this.users_with_this_assignment = users_with_this_assignment;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = false)
	private String question;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false, unique = false)
	private String answer;

	@Column(nullable = false, unique = false)
	private Date post_date;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "specific_class_id", nullable = false)
	private Specific_Class assignment_class;

	public Specific_Class getAssignment_class() {
		return assignment_class;
	}

	public void setAssignment_class(Specific_Class assignment_class) {
		this.assignment_class = assignment_class;
	}

	@Column(nullable = false, unique = false)
	private Date due_date;

	@JsonIgnore
	@OneToMany(mappedBy = "assignment", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<User_Assignment> users_with_this_assignment;

	public Assignment() {

	}

}