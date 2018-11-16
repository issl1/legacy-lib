/**
 * 
 */
package com.nokor.common.app.content.service.file;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.content.model.file.FileData;

/**
 * @author sok.pongsametrey
 * 
 */
@Service("mediaService")
public class MediaServiceImpl extends MainEntityServiceImpl implements MediaService {
	/** */
	private static final long serialVersionUID = 4093706036445941051L;
	
	@Autowired
	private EntityDao dao;

	@Override
	public BaseEntityDao getDao() {
		return this.dao;
	}


	@Override
	public FileData findFileExist(String fileName) {
		FileData fileData = null;
		final Criteria criteriaFileData = createCriteria(FileData.class);
		criteriaFileData.add(Restrictions.eq("filename", fileName));
	    List<FileData> listFileDatas = criteriaFileData.list();
	    if (listFileDatas != null && listFileDatas.size() > 0) {
	    	fileData = (FileData) listFileDatas.get(0);
	    }
	    
	    return fileData;
	}

	@Override
	public void forceRemoveFileData(Long fileId) throws Exception {
		
		
	}

}
