package springrest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "terms")
public class Term implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = false)
    private String description;
    
    @JsonIgnore
	@OneToMany(mappedBy = "term", orphanRemoval = true, cascade=CascadeType.ALL)
	private List<Specific_Class> classes;
    
    public List<Specific_Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Specific_Class> classes) {
		this.classes = classes;
	}

	public Term()
    {

    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
}