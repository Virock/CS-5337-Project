package springrest.model.dao;

import java.util.List;

import springrest.model.School_Class;

public interface SchoolClassDao {

	List<School_Class> getClasses();

	School_Class getSchoolClass(Long class_no);

	School_Class saveClass(School_Class this_class);

	boolean deleteSchoolClass(School_Class this_class);
}