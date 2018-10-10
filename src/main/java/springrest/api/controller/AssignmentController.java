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

import springrest.api.error.RestException;
import springrest.model.Assignment;
import springrest.model.Methods;
import springrest.model.Specific_Class;
import springrest.model.User;
import springrest.model.User.type_of_user;
import springrest.model.User_Assignment;
import springrest.model.dao.AssignmentDao;
import springrest.model.dao.SpecificClassDao;
import springrest.model.dao.UserDao;

@RestController
public class AssignmentController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private AssignmentDao assignmentDao;

	@Autowired
	private SpecificClassDao specificClassDao;

	@Autowired
	private Methods methods;

	// Get an assignment
	@RequestMapping(value = "/assignment/{id}", method = RequestMethod.GET)
	public Assignment getAssignment(@PathVariable Long id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Assignment assignment = assignmentDao.getAssignment(id);
			// If user is admin, return assignment
			if (user.getType() == type_of_user.ADMIN)
				return assignment;
			// If instructor of class, return assignment
			else if (user.getType() == type_of_user.INSTRUCTOR
					&& user.getInstructed_classes().contains(assignment.getAssignment_class()))
				return assignment;
			// If user is in class of assignment, return assignment
			else if (user.getType() == type_of_user.REGULER
					&& user.getClasses().contains(assignment.getAssignment_class()))
				return assignment;
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all assignments
	@RequestMapping(value = "/assignments", method = RequestMethod.GET)
	public List<Assignment> getAssignments(HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return assignmentDao.getAssignments();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get specific class with this assignment
	@RequestMapping(value = "/assignment/{id}/class", method = RequestMethod.GET)
	public Specific_Class getAssignmentClass(@PathVariable Long id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Assignment assignment = assignmentDao.getAssignment(id);
			// If user is admin, return class
			if (user.getType() == type_of_user.ADMIN)
				return assignment.getAssignment_class();
			// If instructor of class, return class
			else if (user.getType() == type_of_user.INSTRUCTOR
					&& user.getInstructed_classes().contains(assignment.getAssignment_class()))
				return assignment.getAssignment_class();
			// If user is in class of assignment, return class
			else if (user.getType() == type_of_user.REGULER
					&& user.getClasses().contains(assignment.getAssignment_class()))
				return assignment.getAssignment_class();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get user_assignments related to this assignment
	@RequestMapping(value = "/assignment/{id}/user_assignments", method = RequestMethod.GET)
	public List<User_Assignment> getAssignmentUserAssignments(@PathVariable Long id, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Assignment assignment = assignmentDao.getAssignment(id);
			// If user is admin, return user_assignments
			if (user.getType() == type_of_user.ADMIN)
				return assignment.getUsers_with_this_assignment();
			// If instructor of class, return user_assignments
			else if (user.getType() == type_of_user.INSTRUCTOR
					&& user.getInstructed_classes().contains(assignment.getAssignment_class()))
				return assignment.getUsers_with_this_assignment();
			else
				throw new RestException(400, "Invalid Authorization");
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Add assignment
	@RequestMapping(value = "/assignment", method = RequestMethod.POST)
	public Assignment addAssignment(@RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request,
					Long.parseLong(String.valueOf(json_object.get("specific_class_id")))))
				throw new RestException(400, "Invalid Authorization");
			Assignment assignment = new Assignment();
			assignment.setDue_date(new Date(Long.parseLong((String) json_object.get("due_date"))));
			assignment.setPost_date(new Date(Long.parseLong((String) json_object.get("post_date"))));
			assignment.setQuestion((String) json_object.get("question"));
			assignment.setAnswer((String) json_object.get("answer"));
			Specific_Class specific_class = specificClassDao
					.getSpecificClass(Long.parseLong(String.valueOf(json_object.get("specific_class_id"))));
			assignment.setAssignment_class(specific_class);
			return assignmentDao.saveAssignment(assignment);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	@RequestMapping(value = "/assignment/{id}", method = RequestMethod.PUT)
	public Assignment editAssignment(@PathVariable Long id, @RequestBody JSONObject json_object,
			HttpServletRequest request) {
		try {
			Assignment assignment = assignmentDao.getAssignment(id);
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request, assignment.getAssignment_class().getId()))
				throw new RestException(400, "Invalid Authorization");
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request, Long.parseLong(String.valueOf(json_object.get("specific_class_id")))))
				throw new RestException(400, "Invalid Authorization");
			assignment.setDue_date(new Date(Long.parseLong((String) json_object.get("due_date"))));
			assignment.setPost_date(new Date(Long.parseLong((String) json_object.get("post_date"))));
			assignment.setQuestion((String) json_object.get("question"));
			assignment.setAnswer((String) json_object.get("answer"));
			Specific_Class specific_class = specificClassDao
					.getSpecificClass(Long.parseLong(String.valueOf(json_object.get("specific_class_id"))));
			assignment.setAssignment_class(specific_class);
			return assignmentDao.saveAssignment(assignment);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete Assignment
	@RequestMapping(value = "/assignment/{id}", method = RequestMethod.DELETE)
	public JSONObject DeleteAssignment(@PathVariable Long id, HttpServletRequest request) {
		try {
			// Assignment can be deleted by admin or instructor of class
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			Assignment assignment = assignmentDao.getAssignment(id);
			JSONObject response = new JSONObject();
			if (user.getType() == type_of_user.ADMIN) {
				response.put("Success", assignmentDao.deleteAssignment(assignment));
				return response;
			} else if (user.getType() == type_of_user.INSTRUCTOR
					&& user.getInstructed_classes().contains(assignment.getAssignment_class())) {
				response.put("Success", assignmentDao.deleteAssignment(assignment));
				return response;
			} else {
				response.put("Success", false);
				throw new RestException(400, response.toJSONString());
			}
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}