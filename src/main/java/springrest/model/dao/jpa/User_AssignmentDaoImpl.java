package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.User_Assignment;
import springrest.model.dao.User_AssignmentDao;

@Repository
public class User_AssignmentDaoImpl implements User_AssignmentDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User_Assignment getUserAssignment(Long id) {
		return entityManager.find(User_Assignment.class, id);
	}

	@Override
	public User_Assignment getUserAssignment(Long user_id, Long assignment_id) {
		try {
			return entityManager
					.createQuery("from User_Assignment WHERE user_id = ?1 AND assignment_id = ?2",
							User_Assignment.class)
					.setParameter(1, user_id).setParameter(2, assignment_id).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User_Assignment> getUserAssignments() {
		return entityManager.createQuery("from User_Assignment order by id", User_Assignment.class).getResultList();
	}

	@Override
	@Transactional
	public User_Assignment saveUserAssignment(User_Assignment user_assignment) {
		return entityManager.merge(user_assignment);
	}

	@Override
	@Transactional
	public boolean deleteUserAssignment(User_Assignment user_assignment) {
		try {
			entityManager.remove(user_assignment);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}