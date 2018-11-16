package com.nokor.common.app.content.service.text;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.content.model.doc.Text;
import com.nokor.common.app.content.model.doc.TextDependency;
import com.nokor.common.app.content.model.eref.ETextDependencyType;

/**
 * 
 * @author prasnar
 *
 */
@Service("textService")
public class TextServiceImpl extends MainEntityServiceImpl implements TextService {
	/** */
	private static final long serialVersionUID = -3122646903255624022L;
	
	@Autowired
	private EntityDao entityDao;

	/**
	 * 
	 */
	public TextServiceImpl() {
	}

	@Override
	public BaseEntityDao getDao() {
		return entityDao;
	}

	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		entityDao.create(mainEntity);
	}

	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		entityDao.update(mainEntity);
	}

	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		entityDao.delete(mainEntity);

	}

	@Override
	public List<TextDependency> getParents(long texId) {
		BaseRestrictions<TextDependency> restriction = new BaseRestrictions<>(TextDependency.class);
		restriction.addCriterion(Restrictions.eq("other.id", texId));
		List<TextDependency> lst = entityDao.list(restriction);
		return lst;
	}

	@Override
	public void createDependency(Text text, Text other, ETextDependencyType type) {
		createDependency(text, other, type, 1);
	}
	
	@Override
	public void createDependency(Text text, Text other, ETextDependencyType type, Integer sortIndex) {
		TextDependency dep = new TextDependency();
		dep.setDependencyDate(new Date());
		dep.setSortIndex(sortIndex);

		dep.setType(type.getEntity());

		dep.setText(text);
		dep.setOther(other);
		entityDao.create(dep);
	
	}
}
