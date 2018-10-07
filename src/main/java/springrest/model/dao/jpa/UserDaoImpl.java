package springrest.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springrest.model.User;
import springrest.model.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User getUser(Long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public List<User> getUsers() {
		return entityManager.createQuery("from User order by id", User.class).getResultList();
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		return entityManager.merge(user);
	}

	@Override
	public User getUserWithCIN(Long CIN) {
		try {
			return entityManager.createQuery("from User WHERE CIN = ?1", User.class).setParameter(1, CIN)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User getUser(String email) {
		try {
			return entityManager.createQuery("from User WHERE email = ?1", User.class).setParameter(1, email)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User getUserWithToken(String token) {
		try {
			return entityManager.createQuery("from User WHERE token = ?1", User.class).setParameter(1, token)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional
	public boolean deleteUser(User user) {
		try {
			entityManager.remove(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}