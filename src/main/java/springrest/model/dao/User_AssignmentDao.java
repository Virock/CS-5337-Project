package springrest.model.dao;

import java.util.List;

import springrest.model.User_Assignment;

public interface User_AssignmentDao {

    User_Assignment getUserAssignment( Long id );
    
    User_Assignment getUserAssignment( Long user_id, Long assignment_id );
    
    List<User_Assignment> getUserAssignments();
    
    User_Assignment saveUserAssignment( User_Assignment user_assignment );
    
    boolean deleteUserAssignment( User_Assignment user_assignment );
}