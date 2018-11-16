/**
 * 
 */
package com.nokor.common.app.content.service.file;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.content.model.file.FileData;

/**
 * 
 * @author prasnar
 *
 */
public interface MediaService extends BaseEntityService {

	void forceRemoveFileData(Long fileId) throws Exception;
	FileData findFileExist(String fileName);
	
}
