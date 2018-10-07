package springrest.model.dao;

import java.util.List;

import springrest.model.User;

public interface UserDao {

    User getUser( Long id );
    
    User getUserWithCIN( Long CIN );
    
    User getUserWithToken( String token );
    
    User getUser( String email );
    
    List<User> getUsers();

    User saveUser( User user );
    
    boolean deleteUser(User user);
}