package springrest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum type_of_user {
		ADMIN, INSTRUCTOR, REGULER
	};

	@Id
	@GeneratedValue
	private Long id;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Size(min = 1)
	@Column(nullable = false, unique = false)
	private String name;

	@Size(min = 1)
	@Column(nullable = false, unique = true)
	private String email;

	@Size(min = 1)
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false, unique = true)
	private Long cin;

	@Column(nullable = false, unique = false)
	private type_of_user type;

	private boolean enabled = true;

	@JsonIgnore
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<User_Assignment> user_assignments;

	public List<Specific_Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Specific_Class> classes) {
		this.classes = classes;
	}

	@JsonIgnore
	@ManyToMany(mappedBy = "users_in_this_class", cascade = CascadeType.ALL)
	private List<Specific_Class> classes;

	@JsonIgnore
	@OneToMany(mappedBy = "instructor", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Specific_Class> instructed_classes;

	public List<Specific_Class> getInstructed_classes() {
		return instructed_classes;
	}

	public void setInstructed_classes(List<Specific_Class> instructed_classes) {
		this.instructed_classes = instructed_classes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCin() {
		return cin;
	}

	public void setCin(Long cin) {
		this.cin = cin;
	}

	public type_of_user getType() {
		return type;
	}

	public void setType(type_of_user type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<User_Assignment> getUser_assignments() {
		return user_assignments;
	}

	public void setUser_assignments(List<User_Assignment> user_assignments) {
		this.user_assignments = user_assignments;
	}

	public User() {

	}
}