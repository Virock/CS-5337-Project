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
import springrest.model.Specific_Class;
import springrest.model.Term;
import springrest.model.dao.TermDao;
import springrest.model.dao.UserDao;

@RestController
public class TermController {

	@Autowired
	private TermDao termDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private Methods methods;

	// Get all terms by admin
	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public List<Term> getTerms(HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return termDao.getTerms();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get specific term (id) by admin
	@RequestMapping(value = "/term/{id}", method = RequestMethod.GET)
	public Term getTerm(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			Term term = termDao.getTerm(id);
			if (term == null)
				throw new RestException(400, "Term does not exist");
			return term;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Get all specific classes for a term by admin
	@RequestMapping(value = "/term/{id}/classes", method = RequestMethod.GET)
	public List<Specific_Class> getTermClasses(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			Term term = termDao.getTerm(id);
			return term.getClasses();
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	//Create term
	@RequestMapping(value = "/term", method = RequestMethod.POST)
	public Term addTerm(@RequestBody Term term, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			return termDao.saveTerm(term);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	//Edit term
	@RequestMapping(value = "/term/{term_id}", method = RequestMethod.PUT)
	public Term editTerm(@PathVariable Long term_id, @RequestBody JSONObject json_object, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			Term term = termDao.getTerm(term_id);
			if (term == null)
				throw new RestException(400, "Term does not exist");
			if (json_object.get("description") == null)
				throw new RestException(400, "No description");
			term.setDescription((String) json_object.get("description"));
			return termDao.saveTerm(term);
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

	// Delete term
	@RequestMapping(value = "/term/{id}", method = RequestMethod.DELETE)
	public JSONObject DeleteTerm(@PathVariable Long id, HttpServletRequest request) {
		try {
			if (!methods.proceedOnlyIfAdmin(request))
				throw new RestException(400, "Invalid Authorization");
			JSONObject response = new JSONObject();
			Term term = termDao.getTerm(id);
			if (term == null)
				throw new RestException(400, "Term does not exist");
			response.put("Success", termDao.deleteTerm(termDao.getTerm(id)));
			return response;
		} catch (Exception e) {
			throw new RestException(400, e.getMessage());
		}
	}

}