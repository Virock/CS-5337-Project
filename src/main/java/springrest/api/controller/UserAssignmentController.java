package springrest.api.controller;

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
import org.springframework.web.client.RestTemplate;

import springrest.api.error.RestException;
import springrest.model.Assignment;
import springrest.model.Methods;
import springrest.model.Secret;
import springrest.model.Specific_Class;
import springrest.model.User;
import springrest.model.User_Assignment;
import springrest.model.User.type_of_user;
import springrest.model.dao.AssignmentDao;
import springrest.model.dao.SpecificClassDao;
import springrest.model.dao.UserDao;
import springrest.model.dao.User_AssignmentDao;

@RestController
public class UserAssignmentController {

	@Autowired
	private User_AssignmentDao user_assignmentDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AssignmentDao assignmentDao;

	@Autowired
	private SpecificClassDao specificClassDao;

	@Autowired
	private Methods methods;

	@Autowired
	private Secret secret;

	// Get user assignment
	@RequestMapping(value = "/user_assignment/{id}", method = RequestMethod.GET)
	public User_Assignment getUserAssignment(@PathVariable Long id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User requesting_user = userDao.getUserWithToken(token);
			User_Assignment user_assignment = user_assignmentDao.getUserAssignment(id);
			// Check token, if admin, return assignment
			// If instructor of class, return assignment
			// If user who has this assignment, return assignment
			if (requesting_user.getType() == type_of_user.ADMIN
					|| requesting_user.getInstructed_classes()
							.contains(user_assignment.getAssignment().getAssignment_class())
					|| requesting_user.getUser_assignments().contains(user_assignment))
				return user_assignment;
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all user_assignments by admin
	@RequestMapping(value = "/user_assignments", method = RequestMethod.GET)
	public List<User_Assignment> getUserAssignments(HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return user_assignmentDao.getUserAssignments();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all user_assignments of assignment by instructor or admin
	@RequestMapping(value = "/user_assignments/{assignment_id}", method = RequestMethod.GET)
	public List<User_Assignment> getUserAssignmentsForAssignment(@PathVariable Long assignment_id,
			HttpServletRequest request) {
		try {
			Assignment assignment = assignmentDao.getAssignment(assignment_id);
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request, assignment.getAssignment_class().getId()))
				throw new RestException(400, "Invalid Authorization");
			return assignment.getUsers_with_this_assignment();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// See result of code
	@RequestMapping(value = "/compile", method = RequestMethod.POST)
	public JSONObject compileCode(@RequestBody JSONObject json_object) {
		String uri = "https://api.jdoodle.com/execute";
		RestTemplate restTemplate = new RestTemplate();

		json_object.put("clientId", secret.getJdoodle_client_id());
		json_object.put("clientSecret", secret.getJdoodle_client_secret());
		json_object.put("versionIndex", "0");

		JSONObject result = restTemplate.postForObject(uri, json_object, JSONObject.class);
		return result;
	}

	// Attach comment to user assignment by admin or instructor of class
	@RequestMapping(value = "/user_assignment/{user_assignment_id}/comment", method = RequestMethod.POST)
	public User_Assignment commentOnUserAssignment(@PathVariable Long user_assignment_id,
			@RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			User_Assignment user_assignment = user_assignmentDao.getUserAssignment(user_assignment_id);
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request,
					user_assignment.getAssignment().getAssignment_class().getId()))
				throw new RestException(400, "Invalid Authorization");
			user_assignment.setComment((String) json_object.get("comment"));
			return user_assignmentDao.saveUserAssignment(user_assignment);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Submit assignment only by user
	@RequestMapping(value = "/user_assignment/{assignment_id}", method = RequestMethod.POST)
	public User_Assignment addOrEditUserAssignment(@PathVariable Long assignment_id,
			@RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user.getType() != type_of_user.REGULER)
				throw new RestException(400, "Invalid Authorization");
			Assignment assignment = assignmentDao.getAssignment(assignment_id);
			// If due date has passed, return error
			if (new Date().after(assignment.getDue_date()))
				throw new RestException(400, "Due date passed");
			// Check if the user is going to a class that has this assignment
			Specific_Class assignment_class = assignment.getAssignment_class();
			List<Specific_Class> classes = user.getClasses();
			// If not, return null
			if (!classes.contains(assignment_class))
				throw new RestException(400, "Invalid Authorization");
			// Check if the association already exists
			User_Assignment user_assignment = user_assignmentDao.getUserAssignment(user.getId(), assignment_id);
			if (user_assignment == null) {
				// If so, attach the two together along with the user's code and score
				user_assignment = new User_Assignment();
				user_assignment.setUser(user);
				user_assignment.setAssignment(assignment);
			}

			// Send the code to be run
			String uri = "https://api.jdoodle.com/execute";
			RestTemplate restTemplate = new RestTemplate();

			json_object.put("clientId", secret.getJdoodle_client_id());
			json_object.put("clientSecret", secret.getJdoodle_client_secret());
			json_object.put("versionIndex", "0");

			JSONObject result = restTemplate.postForObject(uri, json_object, JSONObject.class);

			// If error, return error
			if ((int) result.get("statusCode") != 200)
				throw new RestException(400, result.toJSONString());
			// Else, check if output is same as answer
			// If not, store 0 as score
			if (!String.valueOf(result.get("output")).equals(assignment.getAnswer()))
				user_assignment.setScore(0L);
			// If so, store 10 as score
			else
				user_assignment.setScore(10L);
			// Also store the code and language
			user_assignment.setCode((String) json_object.get("script"));
			user_assignment.setLanguage((String) json_object.get("language"));
			return user_assignmentDao.saveUserAssignment(user_assignment);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete user_assignment
	@RequestMapping(value = "/user_assignment/{id}", method = RequestMethod.DELETE)
	public JSONObject DeleteUserAssignment(@PathVariable Long id, HttpServletRequest request) {
		try {
			// Admin and user who uploaded assignment can delete user_assignment
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			User_Assignment user_assignment = user_assignmentDao.getUserAssignment(id);
			
			if (user_assignment == null)
				throw new RestException(400, "User assignment does not exist");
			
			Assignment assignment = user_assignment.getAssignment();
			if (new Date().after(assignment.getDue_date()))
				throw new RestException(400, "Assignment due date has passed");
			
			JSONObject response = new JSONObject();
			if (user.getType() == type_of_user.ADMIN) {
				response.put("Success", user_assignmentDao.deleteUserAssignment(user_assignment));
				return response;
			} else if (user.getType() == type_of_user.REGULER && user.getUser_assignments().contains(user_assignment)) {
				response.put("Success", user_assignmentDao.deleteUserAssignment(user_assignment));
				return response;
			} else {
				throw new RestException(400, "Invalid Authorization");
			}
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}