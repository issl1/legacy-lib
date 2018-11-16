package com.nokor.common.app.eventlog;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.eventlog.model.ESecEventLevel;
import com.nokor.common.app.eventlog.model.SecEventLog;

/**
 * @author prasnar
 * 
 */
public class SecAppEventRemoveRestriction extends BaseRestrictions<SecEventLog> {

    private Long appId;
    private Date fromDate;
    private Date toDate;


    /**
     * 
     */
    public SecAppEventRemoveRestriction(Long appId, Date fromDate, Date toDate) {
        super(SecEventLog.class);
        this.appId = appId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    

    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {
        addAssociation("secApplication", "app", JoinType.INNER_JOIN);
        addAssociation("levelSecEvent", "lev", JoinType.INNER_JOIN);
        
    	addCriterion(Restrictions.eq("app.id", appId));

        if (fromDate != null && toDate != null) {
        	fromDate = DateUtils.getDateAtBeginningOfDay(fromDate);
        	toDate = DateUtils.getDateAtEndOfDay(toDate);
            addCriterion("evenDate", FilterMode.BETWEEN, fromDate, toDate);
        }
        else if (fromDate != null) {
        	fromDate = DateUtils.getDateAtBeginningOfDay(fromDate);
            addCriterion("evenDate", FilterMode.GREATER_OR_EQUALS, fromDate);
        }
        else if (toDate != null) {
        	toDate = DateUtils.getDateAtEndOfDay(toDate);
            addCriterion("evenDate", FilterMode.LESS_OR_EQUALS, toDate);
        }
        addCriterion(Restrictions.eq("lev", ESecEventLevel.DEBUG));
       
    }

}
