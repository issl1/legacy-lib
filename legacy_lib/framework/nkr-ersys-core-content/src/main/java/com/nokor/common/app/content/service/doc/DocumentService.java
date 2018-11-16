package com.nokor.common.app.content.service.doc;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.common.app.content.model.doc.DocContent;
import com.nokor.common.app.content.model.eref.EDocDependencyType;
import com.nokor.common.app.eref.ELanguage;

/**
 * 
 * @author prasnar
 * 
 */
public interface DocumentService extends MainEntityService {
	List<DocContent> listOnlyPDF(boolean isOnlyPdf);

	long getTotalPDFCount(boolean isOnlyPdf);

	List<DocContent> listByTitle(String title, ELanguage language);

	long getTotal(ELanguage language);

	long getTotalByCategoryAndLang(long catId, ELanguage language);

	List<DocContent> listByDistinctDocIds(DocumentRestriction restriction);

	void createDependency(DocContent text, DocContent other, EDocDependencyType type);

	void createDependency(DocContent text, DocContent other, EDocDependencyType type, Integer sortIndex);

	
}
