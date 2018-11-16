package com.nokor.common.app.history;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.history.model.History;

/**
 * 
 * @author prasnar
 *
 */
@Service("historyService")
public class HistoryServiceImpl extends BaseEntityServiceImpl implements HistoryService {
    /** */
	private static final long serialVersionUID = -1060718710413443397L;
	
	@Autowired
    private EntityDao dao;

    /**
     * Constructor.
     */
    public HistoryServiceImpl() {
    	super();
    }


    //@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	

    @Override
    public void addHistory(EntityA entity,String propertyName, EHistoReason reason, String oldValue, String newValue) {

        History history = History.createInstance();
        history.setEntityId(entity.getId());
        history.setEntity(EMainEntity.getByClass(entity.getClass()));
        history.setPropertyName(propertyName);
        history.setReason(reason);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        
        //history.setEventLog(eventLog)
        
        dao.saveOrUpdate(history);
    }

    @Override
    public History getLastHistory(Date dtReference, final String entity, final Long entityId, final String property) {
        logger.debug("GetLastHistory for [" + entity + "] [" + entityId + "] [" + property + "]");
        HistoryRestriction histCri = new HistoryRestriction();
        histCri.setProperty(property);
        histCri.setEntity(entity);
        histCri.setEntityId(entityId);
        

        histCri.setDateCreationMax(DateUtils.plus(dtReference, -1));

        final List<History> lstHist = dao.list(histCri);
        if (lstHist == null || lstHist.size() == 0) {
        	logger.warn("No history found for [" + entity + "] [" + entityId + "] [" + property + "]");
            return null;
        }
        return lstHist.get(0);
    }

	@Override
    public History getLastHistory(final String entity, final Long entityId, final String property) {
        logger.debug("GetLastHistory for [" + entity + "] [" + entityId + "] [" + property + "]");
        HistoryRestriction histCri = new HistoryRestriction();
        histCri.setProperty(property);
        histCri.setEntity(entity);
        histCri.setEntityId(entityId);
        histCri.addOrder(Order.desc("createDate"));
        

        List<History> lstHist = dao.list(histCri);
        if (lstHist == null || lstHist.size() == 0) {
        	logger.warn("No history found for [" + entity + "] [" + entityId + "] [" + property + "]");
            return null;
        }
        return lstHist.get(0);
    }


	
}
