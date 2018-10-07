package springrest.api.controller;

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
import springrest.model.Methods;
import springrest.model.School_Class;
import springrest.model.Specific_Class;
import springrest.model.User;
import springrest.model.dao.SchoolClassDao;
import springrest.model.dao.UserDao;

@RestController
public class SchoolClassController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SchoolClassDao schoolClassDao;

	@Autowired
	private Methods methods;

	// Get a class
	@RequestMapping(value = "/class/{class_id}", method = RequestMethod.GET)
	public School_Class getClass(@PathVariable Long class_id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user != null)
				return schoolClassDao.getSchoolClass(class_id);
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all class
	@RequestMapping(value = "/classes", method = RequestMethod.GET)
	public List<School_Class> getClasses(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user != null)
				return schoolClassDao.getClasses();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get specific classes for a class
	@RequestMapping(value = "/class/{class_id}/specific_classes", method = RequestMethod.GET)
	public List<Specific_Class> getClassSpecficClasses(@PathVariable Long class_id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user != null)
				return schoolClassDao.getSchoolClass(class_id).getSpecific_classes();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	@RequestMapping(value = "/class", method = RequestMethod.POST)
	public School_Class addClass(@RequestBody School_Class this_class, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return schoolClassDao.saveClass(this_class);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	@RequestMapping(value = "/class/{id}", method = RequestMethod.PUT)
	public School_Class editClass(@PathVariable Long id, @RequestBody School_Class this_class,
			HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			if (schoolClassDao.getSchoolClass(id) == null)
				throw new RestException(400, "Class does not exist");
			this_class.setClass_no(id);
			return schoolClassDao.saveClass(this_class);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete class
	@RequestMapping(value = "/class/{id}", method = RequestMethod.DELETE)
	public JSONObject DeleteClass(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			JSONObject response = new JSONObject();
			response.put("Success", schoolClassDao.deleteSchoolClass(schoolClassDao.getSchoolClass(id)));
			return response;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}