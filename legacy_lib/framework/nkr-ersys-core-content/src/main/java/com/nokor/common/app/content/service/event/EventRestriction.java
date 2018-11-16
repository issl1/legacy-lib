package com.nokor.common.app.content.service.event;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.content.model.event.Event;
import com.nokor.common.app.content.service.article.ArticleRestriction;

/**
 * 
 * @author prasnar
 *
 */
public class EventRestriction extends BaseRestrictions<Event> {
	
    private ArticleRestriction cmsRestriction;

    private Long typEveId;
    private Long artId;
    private Date startDate;
    private Date endDate;
    
    private Boolean isPrivate;
    private Boolean isRestrict;
    private Long wkgId;
    
    private Boolean isCheckAccess;
    private Long employeeId;
    
    /**
     * 
     */
    public EventRestriction() {
        super(Event.class);
        cmsRestriction = new ArticleRestriction();
    }

    @Override 
    public void preBuildAssociation() {
        addAssociation("article", "art", JoinType.INNER_JOIN);
        addAssociation("art.topic", "top", JoinType.INNER_JOIN);
        addAssociation("typeEvent", "typ", JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
    	if (wkgId != null && wkgId > 0) {
    		addAssociation("art.accessWorkGroups", "accWkg", JoinType.INNER_JOIN);    		
    		addCriterion("accWkg.id", wkgId);
        }
    	
    	if (getIsPrivate() != null && getIsPrivate() == false) {
            addCriterion("art.isPrivate", false);
        }
    	
        if (cmsRestriction.getTopArtId() != null && cmsRestriction.getTopArtId() > 0) {
            addCriterion("top.id", cmsRestriction.getTopArtId());
        }
        if (typEveId != null && typEveId > 0) {
            addCriterion("typ.id", typEveId);
        }
        if (artId != null && artId > 0) {
            addCriterion("art.id", artId);
        }
                
        if (startDate != null && endDate != null) {
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.between("art.startDate", startDate, endDate));
			or.add(Restrictions.between("art.endDate", startDate, endDate));
			addCriterion(or);
        }
        else if (startDate != null) {
            addCriterion("art.startDate", FilterMode.GREATER_OR_EQUALS, startDate);
        }
        else if (endDate != null) {
        	addCriterion("art.endDate", FilterMode.LESS_OR_EQUALS, endDate);
        }
                
        if (getIsRestrict() != null && getIsRestrict() == false) {
        	addCriterion("art.isRestrict", false);
        }
        
        if(!StringUtils.isEmpty(cmsRestriction.getArtTitle())){
        	String textTitle =  "%" + StringEscapeUtils.escapeHtml(cmsRestriction.getArtTitle()) + "%";
        	addCriterion(Restrictions.or(
        			Restrictions.ilike("art.title", textTitle),
        			Restrictions.ilike("art.subTitle", textTitle),
        			Restrictions.ilike("art.keywords" , textTitle),
        			Restrictions.ilike("theme" , textTitle),
        			Restrictions.ilike("place" , textTitle)
        		));
        }
        // Check with Working Group / Working Group Member
        if (isCheckAccess != null && isCheckAccess.equals(true)) {
        	addAssociation("art.accessWorkGroups", "accWg", JoinType.LEFT_OUTER_JOIN);
        	addAssociation("art.accessEmployees", "accEmp", JoinType.LEFT_OUTER_JOIN);
        	addAssociation("accWg.members", "accWgMem", JoinType.LEFT_OUTER_JOIN);
        	addAssociation("accWgMem.employee", "accWgMemEmp", JoinType.LEFT_OUTER_JOIN);
        	Criterion cr1 = Restrictions.eq("accWgMemEmp.id", employeeId);
        	Criterion cr2 = Restrictions.eq("art.isPrivate", false);
        	Criterion cr3 = Restrictions.isNull("art.isPrivate");        	
        	Criterion cr4 = Restrictions.eq("accEmp.id", employeeId);
        	addCriterion(Restrictions.or(cr1, cr2, cr3, cr4));
        }
    }

	/**
	 * @return the cmsRestriction
	 */
	public ArticleRestriction getCmsRestriction() {
		return cmsRestriction;
	}

	/**
	 * @param cmsRestriction the cmsRestriction to set
	 */
	public void setCmsRestriction(ArticleRestriction cmsRestriction) {
		this.cmsRestriction = cmsRestriction;
	}

	/**
	 * @return the artId
	 */
	public Long getArtId() {
		return artId;
	}

	/**
	 * @param artId the artId to set
	 */
	public void setArtId(Long artId) {
		this.artId = artId;
	}

	/**
	 * @return the isPrivate
	 */
	public Boolean getIsPrivate() {
		return isPrivate;
	}

	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	/**
	 * @return the wkgId
	 */
	public Long getWkgId() {
		return wkgId;
	}

	/**
	 * @param wkgId the wkgId to set
	 */
	public void setWkgId(Long wkgId) {
		this.wkgId = wkgId;
	}

	/**
	 * @return the typEveId
	 */
	public Long getTypEveId() {
		return typEveId;
	}

	/**
	 * @param typEveId the typEveId to set
	 */
	public void setTypEveId(Long typEveId) {
		this.typEveId = typEveId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the isRestrict
	 */
	public Boolean getIsRestrict() {
		return isRestrict;
	}

	/**
	 * @param isRestrict the isRestrict to set
	 */
	public void setIsRestrict(Boolean isRestrict) {
		this.isRestrict = isRestrict;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the isCheckAccess
	 */
	public Boolean getIsCheckAccess() {
		return isCheckAccess;
	}

	/**
	 * @param isCheckAccess the isCheckAccess to set
	 */
	public void setIsCheckAccess(Boolean isCheckAccess) {
		this.isCheckAccess = isCheckAccess;
	}
	
	

}
