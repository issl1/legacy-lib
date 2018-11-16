package com.nokor.common.app.content.service;

import java.util.List;

import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.springframework.stereotype.Service;

import com.nokor.common.app.content.model.article.ArtAttachment;
import com.nokor.common.app.content.model.file.FileData;
import com.nokor.common.app.content.service.file.MediaServiceImpl;
import com.nokor.common.app.helper.FileDataUtils;

/**
 * 
 * @author piseth.ing
 *
 */
@Service("fileDataService")
public class FileDataServiceImpl extends MediaServiceImpl implements FileDataService {

	@Override
	public void forceRemoveFileData(Long fileId) throws Exception {
		// Remove file relationship (article attachment)
		BaseRestrictions<ArtAttachment> artAttRestriction = new BaseRestrictions<ArtAttachment>(ArtAttachment.class);
		artAttRestriction.addAssociation("file", "file", JoinType.INNER_JOIN);
		artAttRestriction.addCriterion("file.id", fileId);
		
		List<ArtAttachment> artAttList = getDao().list(artAttRestriction);
		if (artAttList != null && artAttList.size() > 0) {
			for (ArtAttachment artAtt : artAttList) {
				getDao().delete(artAtt);
			}
		}
		// Remove file from disk
		FileDataUtils.deleteExistingFile(fileId);
		
		// remove file data
		FileData fileData = getDao().getById(FileData.class, fileId);
		getDao().delete(fileData);

	}
}
