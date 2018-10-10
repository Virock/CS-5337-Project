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
			School_Class school_class = schoolClassDao.getSchoolClass(class_id);
			if (school_class == null)
				throw new RestException(400, "Class does not exist");
			if (user != null)
				return schoolClassDao.getSchoolClass(class_id);
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all classes
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
			School_Class school_class = schoolClassDao.getSchoolClass(class_id);
			if (school_class == null)
				throw new RestException(400, "Class does not exist");
			if (user != null)
				return school_class.getSpecific_classes();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	//Add school class
	@RequestMapping(value = "/class", method = RequestMethod.POST)
	public School_Class addClass(@RequestBody School_Class this_class, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			if (this_class.getDescription().equals(""))
				throw new RestException(400, "Invalid Description");
			return schoolClassDao.saveClass(this_class);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	//Edit school class
	@RequestMapping(value = "/class/{id}", method = RequestMethod.PUT)
	public School_Class editClass(@PathVariable Long id, @RequestBody JSONObject json_object,
			HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			School_Class school_class = schoolClassDao.getSchoolClass(id);
			if (school_class == null)
				throw new RestException(400, "Class does not exist");
			if (json_object.get("description").equals(""))
				throw new RestException(400, "Invalid description");
			school_class.setDescription((String) json_object.get("description"));
			return schoolClassDao.saveClass(school_class);
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
			School_Class school_class = schoolClassDao.getSchoolClass(id);
			if (school_class == null)
				throw new RestException(400, "Class does not exist");
			JSONObject response = new JSONObject();
			response.put("Success", schoolClassDao.deleteSchoolClass(school_class));
			return response;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}