package com.nokor.common.app.content.service.file;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.content.model.file.FileData;


/**
 * 
 * @author prasnar
 *
 */
public class FileRestriction extends BaseRestrictions<FileData> {
    private String filename;
    private Date updateDate;
    private Long typFileId;
   
    /**
     * 
     */
    public FileRestriction() {
        super(FileData.class);
    }

    @Override 
    public void preBuildAssociation() {
    	
    }
    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

        if (typFileId != null && typFileId > 0) {
            addCriterion("typ.id", typFileId);
        }
        
        if (StringUtils.isNotEmpty(filename)) {
        	String text = "%" + filename + "%";
            addCriterion(Restrictions.or(
        			Restrictions.ilike("filename", text),
        			Restrictions.ilike("desc", text),
        			Restrictions.ilike("caption", text),
        			Restrictions.ilike("alt", text)
        		));
        }
        
        if (updateDate != null) {
            addCriterion("art.updateDate", FilterMode.GREATER_OR_EQUALS, updateDate);
        }
       
       
    }



	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the typFileId
	 */
	public Long getTypFileId() {
		return typFileId;
	}

	/**
	 * @param typFileId the typFileId to set
	 */
	public void setTypFileId(Long typFileId) {
		this.typFileId = typFileId;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


}
