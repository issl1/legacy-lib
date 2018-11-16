package com.nokor.common.app.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.content.service.FileDataService;
import com.nokor.common.app.content.service.article.ArticleService;
import com.nokor.common.app.content.service.doc.DocumentService;
import com.nokor.common.app.content.service.text.TextService;
import com.nokor.common.app.tools.helper.AppServicesHelper;

/**
 * 
 * @author prasnar
 * 
 */
public interface ContentServicesHelper extends AppServicesHelper {
	public static ArticleService ART_SRV = SpringUtils.getBean(ArticleService.class);
	public static DocumentService DOC_SRV = SpringUtils.getBean(DocumentService.class);
	public static TextService TEXT_SRV = SpringUtils.getBean(TextService.class);
    public static FileDataService FILE_DATA_SRV = SpringUtils.getBean(FileDataService.class);

}
