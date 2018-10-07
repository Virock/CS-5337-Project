package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.Term;
import springrest.model.dao.TermDao;

@Repository
public class TermDaoImpl implements TermDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Term getTerm(Long id) {
		return entityManager.find(Term.class, id);
	}

	@Override
	@Transactional
	public Term saveTerm(Term term) {
		return entityManager.merge(term);
	}

	@Override
	public List<Term> getTerms() {
		return entityManager.createQuery("from Term order by id", Term.class).getResultList();
	}

	@Override
	@Transactional
	public boolean deleteTerm(Term term) {
		try {
			entityManager.remove(term);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}