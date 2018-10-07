package springrest.model;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import springrest.model.User.type_of_user;
import springrest.model.dao.SpecificClassDao;
import springrest.model.dao.UserDao;

@Component
public class Methods {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SpecificClassDao specificClassDao;

	public boolean proceedOnlyIfAdmin(HttpServletRequest request) {
		String token = request.getHeader("Authorization").replace("Bearer ", "");
		User requesting_user = userDao.getUserWithToken(token);
		if (requesting_user.getType() != type_of_user.ADMIN)
			return false;
		return true;
	}
	
//	public boolean proceedOnlyIfAdminOrInstructor(HttpServletRequest request) {
//		String token = request.getHeader("Authorization").replace("Bearer ", "");
//		User requesting_user = userDao.getUserWithToken(token);
//		if (requesting_user.getType() == type_of_user.ADMIN || requesting_user.getType() == type_of_user.INSTRUCTOR)
//			return true;
//		return false;
//	}

	public boolean proceedOnlyIfAdminOrInstructorForThisClass(HttpServletRequest request, Long specific_class_id) {
		String token = request.getHeader("Authorization").replace("Bearer ", "");
		User requesting_user = userDao.getUserWithToken(token);
		// Check if the user is the instructor for the class.
		Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
		if (requesting_user.getType() == type_of_user.ADMIN)
			return true;
		else if (requesting_user.getType() != type_of_user.INSTRUCTOR)
			return false;
		else if (specific_class.getInstructor().getId() == requesting_user.getId())
			return true;
		else
			return false;
	}

	/**
	 * Generates random strings of length 5 to 10
	 * 
	 * @return String - A random string with 5 to 10 characters
	 */
	public String getAlphaNumericToken() {
		Random rand = new Random();
		int len = rand.nextInt(5) + 5;
		char[] ch = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
				'z' };

		char[] c = new char[len];
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			c[i] = ch[random.nextInt(ch.length)];
		}

		return new String(c);
	}
}
