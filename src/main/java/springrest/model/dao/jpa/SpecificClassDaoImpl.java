package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.Specific_Class;
import springrest.model.dao.SpecificClassDao;

@Repository
public class SpecificClassDaoImpl implements SpecificClassDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Specific_Class saveSpecificClass(Specific_Class specific_class) {
		return entityManager.merge(specific_class);
	}

	@Override
	public Specific_Class getSpecificClass(Long id) {
		return entityManager.find(Specific_Class.class, id);
	}

	@Override
	public List<Specific_Class> getSpecificClasses() {
		return entityManager.createQuery("from Specific_Class order by id", Specific_Class.class).getResultList();
	}

	@Override
	@Transactional
	public boolean deleteSpecficClass(Specific_Class specific_class) {
		try {
			entityManager.remove(specific_class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}