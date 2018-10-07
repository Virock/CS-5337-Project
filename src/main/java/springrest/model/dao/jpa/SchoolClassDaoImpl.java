package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.School_Class;
import springrest.model.dao.SchoolClassDao;

@Repository
public class SchoolClassDaoImpl implements SchoolClassDao {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public School_Class getSchoolClass(Long id) {
		return entityManager.find(School_Class.class, id);
	}

	@Override
	public List<School_Class> getClasses() {
		return entityManager.createQuery("from School_Class order by id", School_Class.class).getResultList();
	}

	@Override
	@Transactional
	public springrest.model.School_Class saveClass(springrest.model.School_Class this_class) {
		return entityManager.merge(this_class);
	}

	@Override
	@Transactional
	public boolean deleteSchoolClass(School_Class this_class) {
		try {
			entityManager.remove(this_class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}