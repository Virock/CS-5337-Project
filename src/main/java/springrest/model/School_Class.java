package springrest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "classes")
public class School_Class implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long class_no;

	public Long getClass_no() {
		return class_no;
	}

	public void setClass_no(Long class_no) {
		this.class_no = class_no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false, unique = false)
	private String description;
	
	@JsonIgnore
	@OneToMany(mappedBy = "school_class", orphanRemoval = true, cascade=CascadeType.ALL)
	private List<Specific_Class> specific_classes;

	public List<Specific_Class> getSpecific_classes() {
		return specific_classes;
	}

	public void setSpecific_classes(List<Specific_Class> specific_classes) {
		this.specific_classes = specific_classes;
	}

	public School_Class() {

	}
}