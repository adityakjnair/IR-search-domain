package index;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class TokenDAO {

	private EntityManagerFactory emFactory;
	
    private EntityManager getEntityManager() {
    	this.emFactory = Persistence.createEntityManagerFactory("irsearchengine");
    	return emFactory.createEntityManager();
    }
    
	private static final String CHECK_DOCUMENT = "SELECT t FROM Token t WHERE t.tokenPK.tokenName = :tokenName AND t.tokenPK.documentID = :documentID";
	public static final String GET_TOKEN_BY_NAME = "SELECT t FROM Token t WHERE t.tokenPK.tokenName = :tokenName";
	public static final String GET_DOCID_BY_TOKEN = "SELECT t.tokenPK.documentID FROM Token t WHERE t.tokenPK.tokenName = :tokenName";

	public void createNewToken(String tokenName, String documentID, Integer frequency) {
        EntityManager em = getEntityManager();
        Token token = new Token();
        TokenPK tokenPK = new TokenPK(tokenName, documentID);
        token.setTokenPK(tokenPK);
        token.setFrequency(frequency);

        em.getTransaction().begin();
        em.persist(token);
        em.getTransaction().commit();

        em.close();
    }
    
	public boolean documentExists(String tokenName, String documentID) {
		TypedQuery<Token> query = getEntityManager().createQuery(CHECK_DOCUMENT, Token.class);

		query.setParameter("tokenName", tokenName);
		query.setParameter("documentID", documentID);

		List<Token> resultList = query.getResultList();
		return !resultList.isEmpty();
	}

	public List<Token> getTokenByName(String tokenName){
		TypedQuery<Token> query = getEntityManager().createQuery(GET_TOKEN_BY_NAME, Token.class);
		query.setParameter("tokenName", tokenName);
		return query.getResultList();
	}
	
	public List<String> getDocIDByToken(String tokenName) {
		TypedQuery<String> query = getEntityManager().createQuery(GET_DOCID_BY_TOKEN, String.class);
		query.setParameter("tokenName", tokenName);
		return query.getResultList();
	}

}
