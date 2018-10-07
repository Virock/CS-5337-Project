package springrest.model.dao;

import java.util.List;

import springrest.model.Assignment;

public interface AssignmentDao {

	Assignment getAssignment(Long id);

	boolean deleteAssignment(Assignment assignment);

	List<Assignment> getAssignments();

	Assignment saveAssignment(Assignment assignment);
}