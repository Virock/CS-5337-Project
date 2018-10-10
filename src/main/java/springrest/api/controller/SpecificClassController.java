package springrest.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springrest.api.error.RestException;
import springrest.model.Assignment;
import springrest.model.Methods;
import springrest.model.Specific_Class;
import springrest.model.User;
import springrest.model.User.type_of_user;
import springrest.model.User_Assignment;
import springrest.model.dao.SchoolClassDao;
import springrest.model.dao.SpecificClassDao;
import springrest.model.dao.TermDao;
import springrest.model.dao.UserDao;

@RestController
public class SpecificClassController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private TermDao termDao;

	@Autowired
	private SpecificClassDao specificClassDao;

	@Autowired
	private SchoolClassDao schoolClassDao;

	@Autowired
	private Methods methods;

	// Get specific class by admin, instructor or regular user
	@RequestMapping(value = "/specific_class/{id}", method = RequestMethod.GET)
	public Specific_Class getSpecificClass(@PathVariable Long id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User requesting_user = userDao.getUserWithToken(token);
			Specific_Class specific_class = specificClassDao.getSpecificClass(id);
			if (specific_class == null)
				throw new RestException(400, "Specific class does not exist");
			if (requesting_user.getType() == type_of_user.ADMIN
					|| specific_class.getInstructor().getId() == requesting_user.getId()
					|| requesting_user.getClasses().contains(specific_class))
				return specificClassDao.getSpecificClass(id);
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all specific classes by all users
	@RequestMapping(value = "/specific_classes", method = RequestMethod.GET)
	public List<Specific_Class> getSpecificClasses(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user.getType() == type_of_user.REGULER)
				return user.getClasses();
			else if (user.getType() == type_of_user.INSTRUCTOR)
				return user.getInstructed_classes();
			else if (user.getType() == type_of_user.ADMIN)
				return specificClassDao.getSpecificClasses();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all users in specific class by all users
	@RequestMapping(value = "/specific_class/{specific_class_id}/users", method = RequestMethod.GET)
	public List<User> getSpecificClassUsers(@PathVariable Long specific_class_id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
			if (user.getType() == type_of_user.INSTRUCTOR && user.getInstructed_classes().contains(specific_class))
				return specific_class.getUsers_in_this_class();
			else if (user.getType() == type_of_user.ADMIN)
				return specific_class.getUsers_in_this_class();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all assignments in specific class
	@RequestMapping(value = "/specific_class/{specific_class_id}/assignments", method = RequestMethod.GET)
	public List<Assignment> getSpecificClassAssignments(@PathVariable Long specific_class_id,
			HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
			if (user.getType() == type_of_user.INSTRUCTOR && user.getInstructed_classes().contains(specific_class))
				return specific_class.getClass_assignments();
			else if (user.getType() == type_of_user.ADMIN)
				return specific_class.getClass_assignments();
			else if (user.getType() == type_of_user.REGULER && user.getClasses().contains(specific_class))
				return specific_class.getClass_assignments();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all user assignments in specific class
	@RequestMapping(value = "/specific_class/{specific_class_id}/user_assignments", method = RequestMethod.GET)
	public List<User_Assignment> getSpecificClassUserAssignments(@PathVariable Long specific_class_id,
			HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			List<User_Assignment> user_assignments = new ArrayList<>();
			Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
			List<Assignment> assignments = specific_class.getClass_assignments();
			for (Assignment assignment : assignments)
				user_assignments.addAll(assignment.getUsers_with_this_assignment());
			if (user.getType() == type_of_user.INSTRUCTOR && user.getInstructed_classes().contains(specific_class))
				return user_assignments;
			else if (user.getType() == type_of_user.ADMIN)
				return user_assignments;
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Add specific class
	@RequestMapping(value = "/specific_class", method = RequestMethod.POST)
	public Specific_Class addSpecificClass(@RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User instructor = userDao.getUser(Long.valueOf(String.valueOf(json_object.get("instructor_id"))));
			// Check if instructor_id points to an instructor
			if (instructor.getType() != type_of_user.INSTRUCTOR)
				throw new RestException(400, "Invalid Authorization");
			Specific_Class specific_class = new Specific_Class();
			specific_class.setEnd_time(new Date(Long.parseLong((String) json_object.get("end_time"))));
			specific_class.setClass_start_date(new Date(Long.parseLong((String) json_object.get("class_start_time"))));
			specific_class.setClass_end_date(new Date(Long.parseLong((String) json_object.get("class_end_time"))));
			specific_class.setRoom((String) json_object.get("room"));
			specific_class.setSection(Long.parseLong(String.valueOf(json_object.get("section_id"))));
			specific_class.setStart_time(new Date(Long.parseLong(String.valueOf(json_object.get("start_time")))));
			specific_class.setInstructor(instructor);
			specific_class.setShool_class(
					schoolClassDao.getSchoolClass(Long.valueOf(String.valueOf(json_object.get("class_no")))));
			specific_class.setTerm(termDao.getTerm(Long.valueOf(String.valueOf(json_object.get("term_id")))));
			return specificClassDao.saveSpecificClass(specific_class);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	//Edit specific class
	@RequestMapping(value = "/specific_class/{class_id}", method = RequestMethod.PUT)
	public Specific_Class editSpecificClass(@PathVariable Long class_id, @RequestBody JSONObject json_object,
			HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			Specific_Class specific_class = specificClassDao.getSpecificClass(class_id);
			User instructor = userDao.getUser(Long.valueOf(String.valueOf(json_object.get("instructor_id"))));
			// Check if instructor_id points to an instructor
			if (instructor.getType() != type_of_user.INSTRUCTOR)
				throw new RestException(400, "Invalid Authorization");
			specific_class.setEnd_time(new Date(Long.parseLong((String) json_object.get("end_time"))));
			specific_class.setClass_start_date(new Date(Long.parseLong((String) json_object.get("class_start_time"))));
			specific_class.setClass_end_date(new Date(Long.parseLong((String) json_object.get("class_end_time"))));
			specific_class.setRoom((String) json_object.get("room"));
			specific_class.setSection(Long.parseLong(String.valueOf(json_object.get("section_id"))));
			specific_class.setStart_time(new Date(Long.parseLong(String.valueOf(json_object.get("start_time")))));
			specific_class.setInstructor(instructor);
			specific_class.setShool_class(
					schoolClassDao.getSchoolClass(Long.valueOf(String.valueOf(json_object.get("class_no")))));
			specific_class.setTerm(termDao.getTerm(Long.valueOf(String.valueOf(json_object.get("term_id")))));
			return specificClassDao.saveSpecificClass(specific_class);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete specific_class
	@RequestMapping(value = "/specific_class/{id}", method = RequestMethod.DELETE)
	public JSONObject DeleteSpecficClass(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			JSONObject json_object = new JSONObject();
			json_object.put("Success", specificClassDao.deleteSpecficClass(specificClassDao.getSpecificClass(id)));
			return json_object;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}