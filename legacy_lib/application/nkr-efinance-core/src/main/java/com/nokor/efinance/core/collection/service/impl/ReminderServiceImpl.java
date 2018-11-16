package com.nokor.efinance.core.collection.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.collection.service.ReminderRestriction;
import com.nokor.efinance.core.collection.service.ReminderService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.history.service.FinHistoryService;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.frmk.vaadin.util.i18n.I18N;


/**
 * Collection Service
 * @author youhort.ly
 *
 */
@Service("reminderService")
public class ReminderServiceImpl extends BaseEntityServiceImpl implements ReminderService {
	
	/** */
	private static final long serialVersionUID = -3345384289342017566L;
	
	@Autowired
    private EntityDao dao;
	
	@Autowired
	private FinHistoryService finHistoryService;
		
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.ReminderService#saveOrUpdateReminder(com.nokor.efinance.core.collection.model.Reminder)
	 */
	@Override
	public void saveOrUpdateReminder(Reminder reminder) {
		saveOrUpdate(reminder);
		String comment = I18N.message("reminder") + StringUtils.SPACE + DateUtils.getDateLabel(reminder.getCreateDate()) + " : " + reminder.getComment(); 
		finHistoryService.addFinHistory(reminder.getContract(), FinHistoryType.FIN_HIS_REM, comment);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.ReminderService#saveOrUpdateComment(com.nokor.efinance.core.quotation.model.Comment)
	 */
	@Override
	public void saveOrUpdateComment(Comment comment) {
		saveOrUpdate(comment);
		String desc = I18N.message("comment") + StringUtils.SPACE + DateUtils.getDateLabel(comment.getCreateDate()) + " : " + comment.getDesc();
		finHistoryService.addFinHistory(comment.getContract(), FinHistoryType.FIN_HIS_CMT, desc);
	}
	
	/**
	 * 
	 */
	@Override
	public List<Reminder> getReminderByContract(Contract contract) {
		ReminderRestriction	 restrictions = new ReminderRestriction();
		restrictions.setConId(contract.getId());
		restrictions.addCriterion(Restrictions.eq("dismiss", false));
		return list(restrictions);
	}

}
