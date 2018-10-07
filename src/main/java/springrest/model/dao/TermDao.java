package springrest.model.dao;

import java.util.List;

import springrest.model.Term;

public interface TermDao {

    Term getTerm( Long id );
    
    List<Term> getTerms();
    
    Term saveTerm( Term term );
    
    boolean deleteTerm(Term term);
}