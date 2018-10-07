package springrest.model.dao;

import java.util.List;

import springrest.model.Specific_Class;

public interface SpecificClassDao {

	Specific_Class getSpecificClass(Long id);

	List<Specific_Class> getSpecificClasses();

	Specific_Class saveSpecificClass(Specific_Class specific_class);

	boolean deleteSpecficClass(Specific_Class specific_class);
}