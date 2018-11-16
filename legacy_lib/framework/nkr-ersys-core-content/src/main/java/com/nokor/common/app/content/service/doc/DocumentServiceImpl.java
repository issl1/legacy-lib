package com.nokor.common.app.content.service.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.content.model.doc.DocContent;
import com.nokor.common.app.content.model.doc.DocDependency;
import com.nokor.common.app.content.model.eref.EDocDependencyType;
import com.nokor.common.app.eref.ELanguage;

/**
 * 
 * @author prasnar
 *
 */
@Service("cmsDocumentService")
public class DocumentServiceImpl extends MainEntityServiceImpl implements DocumentService {
	/** */
	private static final long serialVersionUID = -1965343856826472125L;
	
	@Autowired
	private EntityDao entityDao;

	/**
	 * 
	 */
	public DocumentServiceImpl() {
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
	public List<DocContent> listByDistinctDocIds(DocumentRestriction restriction) {
		Long[] ids = getIds(restriction);
		
		List<DocContent> list = new ArrayList<DocContent>();
		if (ids.length > 0) {
			list = entityDao.listByIds(restriction.getEntityClass(), Arrays.asList(ids));
		}
		
		List<Long> idList = Arrays.asList(ids);
		DocContent[] sortedEntities = new DocContent[idList.size()];
        for (final DocContent doc : list) {
                int index = idList.indexOf(doc.getId());
                sortedEntities[index] = doc;
        }
        
    	List<DocContent> lst = (List<DocContent>) Arrays.asList(sortedEntities);
		
    	return lst;
	}

	@Override
	public long getTotal(ELanguage language) {
		return getTotalByCategoryAndLang(0, language);
	}

	@Override
	public long getTotalByCategoryAndLang(long catId, ELanguage language) {
		DocumentRestriction restriction = new DocumentRestriction();
		restriction.setCategoryId(catId);
		restriction.setLanguage(language);
		
		if (language == null) { // all
			restriction.addCriterion(Restrictions.or(
					Restrictions.and(Restrictions.isNotNull("titleKh"),Restrictions.ne("titleKh", "")),
					Restrictions.and(Restrictions.isNotNull("titleFr"),Restrictions.ne("titleFr","")),
					Restrictions.and(Restrictions.isNotNull("titleEn"),Restrictions.ne("titleEn","")))
						);
		}
		else if (ELanguage.KHMER.equals(language)) {
			restriction.addCriterion(Restrictions.and(Restrictions.isNotNull("titleKh"),Restrictions.ne("titleKh", "")));
		} else if (ELanguage.FRENCH.equals(language)) {
			restriction.addCriterion(Restrictions.and(Restrictions.isNotNull("titleFr"),Restrictions.ne("titleFr","")));
		} else if (ELanguage.ENGLISH.equals(language)) {
			restriction.addCriterion(Restrictions.and(Restrictions.isNotNull("titleEn"),Restrictions.ne("titleEn","")));
		}
		
		long total = entityDao.count(restriction);
		return total;
	}

	@Override
	public List<DocContent> listByTitle(String title, ELanguage language) {
		DocumentRestriction restriction = new DocumentRestriction();
		restriction.setLanguage(language);
		restriction.setInTitle(true);
		restriction.setSearchedText(title);
		List<DocContent> lst = entityDao.list(restriction);
		return lst;
	}

	@Override
	public long getTotalPDFCount(boolean isOnlyPdf) {
		DocumentRestriction restriction = new DocumentRestriction();
		restriction.setHasOnlyPdfFileKh(isOnlyPdf);
		long total = entityDao.count(restriction);
		return total;
	}

	@Override
	public List<DocContent> listOnlyPDF(boolean isOnlyPdf) {
		DocumentRestriction restriction = new DocumentRestriction();
		restriction.setHasOnlyPdfFileKh(isOnlyPdf);
		List<DocContent> lst = entityDao.list(restriction);
		return lst;
	}

	
	@Override
	public void createDependency(DocContent text, DocContent other, EDocDependencyType type) {
		createDependency(text, other, type, 1);
	}
	
	@Override
	public void createDependency(DocContent text, DocContent other, EDocDependencyType type, Integer sortIndex) {
		DocDependency dep = new DocDependency();
		dep.setDependencyDate(new Date());
		dep.setSortIndex(sortIndex);

		dep.setType(type.getEntity());

		dep.setDoc(text);
		dep.setOther(other);
		entityDao.create(dep);
	
	}
}
