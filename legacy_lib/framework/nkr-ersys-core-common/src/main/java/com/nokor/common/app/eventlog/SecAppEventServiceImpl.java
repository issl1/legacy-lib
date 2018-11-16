package com.nokor.common.app.eventlog;

import java.util.Date;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.eventlog.model.ESecEventLevel;

/**
 * 
 * @author prasnar
 *
 */
@Service("secAppEventService")
public class SecAppEventServiceImpl extends BaseEntityServiceImpl implements SecAppEventService {
	
    /** */
	private static final long serialVersionUID = -7433947939377526553L;
	
	@Autowired
    private EntityDao dao;

    /**
     * 
     */
    public SecAppEventServiceImpl() {
        super();
    }

    /**
     * @see org.seuksa.frmk.mvc.service.BaseEntityService#getDao()
     */
    @Override
    public BaseEntityDao getDao() {
        return dao;
    }
    
    @Override
    public void cleanBeforeNbDays(Long appId, int beforeNbDays) {
    	Date toDate = DateUtils.addDaysDate(new Date(), -beforeNbDays);
    	cleanFrom(appId, null, toDate);
    }
    
    @Override
    public void cleanBeforeNbMonths(Long appId, int beforeNbMonths) {
    	Date toDate = DateUtils.getDateAtBeginningOfLastNbMonths(beforeNbMonths);
    	cleanFrom(appId, null, toDate);
    }

    /**
     * 
     * @param appId
     * @param fromDate
     * @param toDate
     */
    @Override
    public void cleanFrom(Long appId, Date fromDate, Date toDate) {
    	String strFromDate = "";
    	String strToDate = "";
    	
    	try {
//    	SecAppEventRemoveRestriction restriction = new SecAppEventRemoveRestriction(appId, fromDate, toDate);
    	
	    	String hql = "delete from SecAppEvent "
	    				+ " inner join secApplication app "
	    				+ " inner join levelSecEvent lev "
	    				+ " where app.id=" + appId
	    				+ " and lev=" + ESecEventLevel.DEBUG;
	    	
	    	if (fromDate != null) {
        		strFromDate = DateUtils.date2StringYYYYMMDD_HHMMSS(fromDate);
	        	hql += " and evenDate >= '" + strFromDate + "'";
	        } 
	    	if (toDate != null) {
	        	toDate = DateUtils.getDateAtEndOfDay(toDate);
        		strToDate = DateUtils.date2StringYYYYMMDD_HHMMSS(toDate);
	        	hql += " and evenDate <= '" + strToDate + "'";
	        }
	    	getDao().deleteViaHQL(hql);
    	} catch (Exception e) {
    		logger.error("cleanFrom [" + appId + "," + strFromDate + "," + strToDate , e);
    	}

    }
}
