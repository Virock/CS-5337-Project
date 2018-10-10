package springrest.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import springrest.model.User.type_of_user;
import springrest.model.User_Assignment;
import springrest.model.dao.SpecificClassDao;
import springrest.model.dao.UserDao;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SpecificClassDao specificClassDao;

	@Autowired
	private Methods methods;

	@Autowired
	private Secret secret;

	// Get user details with ID
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User getUserById(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(id);
			if (user == null)
				throw new RestException(400, "User does not exist");
			return user;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get current requesting user's details
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public User getRequestingUserDetails(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user == null)
				throw new RestException(400, "Invalid token");
			return user;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get user details with CIN
	@RequestMapping(value = "/user/cin/{cin}", method = RequestMethod.GET)
	public User getUserByCin(@PathVariable Long cin, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUserWithCIN(cin);
			if (user == null)
				throw new RestException(400, "Invalid CIN");
			return user;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get user details with email
	@RequestMapping(value = "/user/email/{email}/", method = RequestMethod.GET)
	public User getUserByEmail(@PathVariable String email, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(email);
			if (user == null)
				throw new RestException(400, "Invalid email");
			return user;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all users
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers(HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return userDao.getUsers();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all of this user's assignments by admin
	@RequestMapping(value = "/user/{user_id}/assignments", method = RequestMethod.GET)
	public List<User_Assignment> getUserAssignments(@PathVariable Long user_id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			return user.getUser_assignments();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all of this user's assignments by the same user
	@RequestMapping(value = "/user/assignments", method = RequestMethod.GET)
	public List<User_Assignment> getUserAssignments(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			return user.getUser_assignments();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all of this user's classes by admin
	@RequestMapping(value = "/user/{user_id}/classes", method = RequestMethod.GET)
	public List<Specific_Class> getUserClasses(@PathVariable Long user_id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			return user.getClasses();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all of this user's classes by the same user
	@RequestMapping(value = "/user/classes", method = RequestMethod.GET)
	public List<Specific_Class> getUserClasses(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			return user.getClasses();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get classes a user instructs by admin
	@RequestMapping(value = "/user/{user_id}/instructed_classes", method = RequestMethod.GET)
	public List<Specific_Class> getUserInstructedClasses(@PathVariable Long user_id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			return user.getInstructed_classes();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get classes this user instructs by instructor or admin
	@RequestMapping(value = "/user/instructed_classes", method = RequestMethod.GET)
	public List<Specific_Class> getUserInstructedClasses(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user.getType() == type_of_user.REGULER)
				throw new RestException(400, "Invalid Authorization");
			return user.getInstructed_classes();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get assignments given by instructor by admin
	@RequestMapping(value = "/user/{user_id}/assignments_given", method = RequestMethod.GET)
	public List<Assignment> getInstructorAssignments(@PathVariable Long user_id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			List<Specific_Class> instructed_specific_classes = user.getInstructed_classes();
			List<Assignment> assignments = new ArrayList<Assignment>();
			// Loop through all specific_classes and get all assignments
			for (Specific_Class instructed_specfic_class : instructed_specific_classes) {
				assignments.addAll(instructed_specfic_class.getClass_assignments());
			}
			return assignments;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get assignments given by this instructor by instructor
	@RequestMapping(value = "/user/assignments_given", method = RequestMethod.GET)
	public List<Assignment> getInstructorAssignments(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").replace("Bearer ", "");
			User user = userDao.getUserWithToken(token);
			if (user.getType() == type_of_user.REGULER)
				throw new RestException(400, "Invalid Authorization");
			List<Specific_Class> instructed_specific_classes = user.getInstructed_classes();
			List<Assignment> assignments = new ArrayList<Assignment>();
			// Loop through all specific_classes and get all assignments
			for (Specific_Class instructed_specfic_class : instructed_specific_classes) {
				assignments.addAll(instructed_specfic_class.getClass_assignments());
			}
			return assignments;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public User addUser(@RequestBody User user, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			if (user.getPassword().isEmpty())
				throw new RestException(400, "Invalid Password");
			user.setToken(String.valueOf(new Date().getTime()) + methods.getAlphaNumericToken());
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10)));
			return userDao.saveUser(user);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Admin can make minor changes to the user account
	@RequestMapping(value = "/user/{user_id}", method = RequestMethod.PUT)
	public User editUser(@PathVariable Long user_id, @RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			user.setEnabled((boolean) json_object.get("enabled"));
			user.setName((String) json_object.get("name"));
			user.setType(type_of_user.values()[(int) json_object.get("type")]);
			return userDao.saveUser(user);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete user
	@RequestMapping(value = "/user/{user_id}", method = RequestMethod.DELETE)
	public JSONObject DeleteUser(@PathVariable Long user_id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			User user = userDao.getUser(user_id);
			JSONObject json = new JSONObject();
			json.put("Success", userDao.deleteUser(user));
			return json;
		} catch (Exception e) {
			throw new RestException(400, "Invalid Authorization");
		}
	}

	// Login user
	@RequestMapping(value = "/user/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject loginUser(@RequestBody JSONObject json_object) {
		try {
			// Check if email and password match, Bcrypt
			// Also check if enabled bit is disabled
			User user = userDao.getUser((String) json_object.get("email"));
			if (user == null || !user.isEnabled())
				throw new RestException(400, "Invalid Authorization");
			boolean matched = BCrypt.checkpw((String) json_object.get("password"), user.getPassword());
			// Return token if they match
			if (matched) {
				JSONObject token = new JSONObject();
				token.put("token", user.getToken());
				return token;
			} else {
				JSONObject error = new JSONObject();
				error.put("error", "Wrong email/password combination");
				return error;
			}
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Place user in a class
	// Can be done by admin or instructor of class
	@RequestMapping(value = "/user/{user_id}/specific_class/{specific_class_id}", method = RequestMethod.POST)
	public JSONObject addUserToClass(@PathVariable Long user_id, @PathVariable Long specific_class_id,
			HttpServletRequest request) {
		JSONObject status = new JSONObject();
		try {
			// Check if user is an admin or instructor for this class
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request, specific_class_id))
				throw new RestException(400, "Invalid Authorization");
			// If so,
			else {
				// Add user to class and return success message
				User user = userDao.getUser(user_id);
				Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
				user.getClasses().add(specific_class);
				specific_class.getUsers_in_this_class().add(user);
				userDao.saveUser(user);
				specificClassDao.saveSpecificClass(specific_class);
				status.put("status", "Successfully added to class");
				return status;
			}
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Remove user from a class
	// Can be done by admin or instructor of class
	@RequestMapping(value = "/user/{user_id}/specific_class/{specific_class_id}", method = RequestMethod.DELETE)
	public JSONObject deleteUserFromClass(@PathVariable Long user_id, @PathVariable Long specific_class_id,
			HttpServletRequest request) {
		JSONObject status = new JSONObject();
		try {
			// Check if user is an admin or instructor for this class
			if (!methods.proceedOnlyIfAdminOrInstructorForThisClass(request, specific_class_id))
				throw new RestException(400, "Invalid Authorization");
			// If so,
			else {
				// Delete user from class and return success message
				User user = userDao.getUser(user_id);
				Specific_Class specific_class = specificClassDao.getSpecificClass(specific_class_id);
				user.getClasses().remove(specific_class);
				specific_class.getUsers_in_this_class().remove(user);
				userDao.saveUser(user);
				specificClassDao.saveSpecificClass(specific_class);
				status.put("status", "Deleted Successfully");
				return status;
			}

		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Users can request for their password to be changed in which case a new
	// password will be mailed to them and their tokens changed.
	@RequestMapping(value = "/user/{email}/change_password", method = RequestMethod.GET)
	public JSONObject changePassword(@PathVariable String email) {
		try {
			User user = userDao.getUser(email);
			if (user != null) {
				// Change password
				String new_password = methods.getAlphaNumericToken();
				user.setPassword(BCrypt.hashpw(new_password, BCrypt.gensalt(10)));
				// Change token
				user.setToken(String.valueOf(new Date().getTime()) + methods.getAlphaNumericToken());
				// Mail password
				String uri = "https://api.sendgrid.com/v3/mail/send";
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + secret.getSend_email_API());
				headers.setContentType(MediaType.APPLICATION_JSON);
				String test = "{\"personalizations\": [{\"to\": [{\"email\": \"" + user.getEmail()
						+ "\"}],\"subject\": \"New password\"}],\"from\": {\"email\": \"viirockn7@gmail.com\"},\"content\": [{\"type\": \"text/plain\",\"value\": \"Your new password: "
						+ new_password + "\"}]}";
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(test);
				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<JSONObject> requestBody = new HttpEntity<>(json, headers);
				JSONObject result = restTemplate.postForObject(uri, requestBody, JSONObject.class);
				// Save user
				userDao.saveUser(user);
				return result;
			}
			throw new RestException(400, "User does not exist");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new RestException(400, e.getMessage());
		}
	}

}