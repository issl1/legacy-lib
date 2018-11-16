package org.seuksa.frmk.dao.hql;

import java.io.Serializable;

import org.hibernate.criterion.Criterion;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.model.entity.Entity;

/**
 * 
 * @author prasnar
 *
 */
public class Association implements Serializable {
	/** */
	private static final long serialVersionUID = 2204191232367985114L;

	private String associationPath;
	private String alias;
	private JoinType joinType;
	private Criterion withClause;
	
	/**
	 * 
	 * @param entityClass
	 * @param alias
	 * @param joinType
	 */
	public Association(Class<? extends Entity> entityClass, String alias, JoinType joinType) {
		this.associationPath = entityClass.getSimpleName().toLowerCase();
		this.joinType = joinType;
		this.alias = alias;
	}
	
	/**
	 * 
	 * @param entityClass
	 * @param alias
	 * @param joinType
	 * @param withClause
	 */
	public Association(Class<? extends Entity> entityClass, String alias, JoinType joinType, Criterion withClause) {
		this.associationPath = entityClass.getSimpleName().toLowerCase();
		this.joinType = joinType;
		this.alias = alias;
		this.withClause = withClause;
	}
	
	/**
	 * 
	 * @param entityClass
	 * @param joinType
	 */
	public Association(Class<? extends Entity> entityClass, JoinType joinType) {
		this(entityClass.getSimpleName().substring(0, 1).toLowerCase() +  entityClass.getSimpleName().substring(1), 
				 entityClass.getSimpleName().substring(0, 1).toLowerCase() +  entityClass.getSimpleName().substring(1), 
				 joinType);
	}
	
	/**
	 * 
	 * @param entityClass
	 */
	public Association(Class<? extends Entity> entityClass) {
		this(entityClass.getSimpleName().substring(0, 1).toLowerCase() +  entityClass.getSimpleName().substring(1),  
				entityClass.getSimpleName().substring(0, 1).toLowerCase() +  entityClass.getSimpleName().substring(1), 
				JoinType.INNER_JOIN);
	}

	/**
	 * 
	 * @param associationPath
	 * @param alias
	 * @param joinType
	 */
	public Association(String associationPath, String alias, JoinType joinType) {
		this.associationPath = associationPath;
		this.joinType = joinType;
		this.alias = alias;
	}

	/**
	 * 
	 * @param associationPath
	 * @param alias
	 * @param joinType
	 * @param withClause
	 */
	public Association(String associationPath, String alias, JoinType joinType, Criterion withClause) {
		this.associationPath = associationPath;
		this.joinType = joinType;
		this.alias = alias;
		this.withClause = withClause;
	}

	/**
	 * @return the associationPath
	 */
	public String getAssociationPath() {
		return associationPath;
	}

	/**
	 * @param associationPath the associationPath to set
	 */
	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the joinType
	 */
	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * @param joinType the joinType to set
	 */
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	/**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        Association assoc = (Association) obj;
		if (obj == null) {
			return false;
		}
		return assoc.getAlias().equals(alias);
	}

	/**
	 * @return the withClause
	 */
	public Criterion getWithClause() {
		return withClause;
	}

	/**
	 * @param withClause the withClause to set
	 */
	public void setWithClause(Criterion withClause) {
		this.withClause = withClause;
	}
}
