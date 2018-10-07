package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.Assignment;
import springrest.model.dao.AssignmentDao;

@Repository
public class AssignmentDaoImpl implements AssignmentDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Assignment getAssignment(Long id) {
		return entityManager.find(Assignment.class, id);
	}

	@Override
	@Transactional
	public Assignment saveAssignment(Assignment assignment) {
		return entityManager.merge(assignment);
	}

	@Override
	public List<Assignment> getAssignments() {
		return entityManager.createQuery("from Assignment order by id", Assignment.class).getResultList();
	}

	@Override
	@Transactional
	public boolean deleteAssignment(Assignment assignment) {
		try {
			entityManager.remove(assignment);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}