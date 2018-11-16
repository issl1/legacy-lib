package com.nokor.frmk.web.struts.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;

/**
 * @author prasnar
 *
 */
public abstract class BaseListPageAction extends BasePageAction {

    /** */
    private static final long serialVersionUID = -4580123683065002916L;

    private static int DEFAULT_MAX_ROW_PER_PAGE = 20;

    private String moveTo = "";

    private int nbRecordsPerPage = DEFAULT_MAX_ROW_PER_PAGE;
    private int nbTotalPages;
    private long nbTotalRecords;
    private Boolean showRecycledBin;
    private String entityClassname;
    private Boolean isActiveToArchive = Boolean.FALSE;
    private Boolean isRestoreToActive = Boolean.FALSE;
    private Boolean isAllList = false;
    private Boolean needSessionCriteria = false;
    private Boolean isShowAdvancedSearch = false;
    private String sortIndex;
	
    /**
     * 
     */
    public BaseListPageAction() {
        setFirstCurrentPageIndex(1);
        setSecondCurrentPageIndex(1);
        setThirdCurrentPageIndex(1);
        if (isApplicationBO()) {
            nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListBO();
        }
        else if (isApplicationFO()) {
            nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListFO();
        }
        else if (isApplicationRA()) {
            nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListRA();
        }
        else if (isApplicationCMS()) {
            nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListCMS();
        } else {
        	nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListMAIN();
        	
        } 
        if (nbRecordsPerPage <= 0) {
            nbRecordsPerPage = AppSettingConfigHelper.getNbRecordsPerPageInListDefault();
        }
    }

    /**
     * @see com.nokor.frmk.BasePageAction.web.struts.action.authentication.struts.action.PageAction#processPage()
     */
    @Override
    protected void processPage() throws Exception {
        logger.info("processPage");
    }
    
    @Override
    public void viewAction() throws Exception {
    }

    /**
     * 
     */
    public void listAction() {
        processList();
    }

    /**
     * 
     */
    private void processList() {
        beforeList();

        list();

        afterList();
    }

    protected int getFirstResultFirst() {
        setFirstCurrentPageIndex(getMoveToPage()); // Navigation in FO
        final int startIndex = (nbRecordsPerPage * getFirstCurrentPageIndex()) - nbRecordsPerPage;
        return startIndex;
    }

    protected int getFirstResultSecond() {
        final int startIndex = (nbRecordsPerPage * getSecondCurrentPageIndex()) - nbRecordsPerPage;
        return startIndex;
    }

    protected int getFirstResultThird() {
        final int startIndex = (nbRecordsPerPage * getThirdCurrentPageIndex()) - nbRecordsPerPage;
        return startIndex;
    }

    protected int getMaxRowPerPage() {
        return nbRecordsPerPage;
    }

    public void deleteAction() throws Exception {
        logger.info("deleteAction");
        delete();
        listFirstTabAction();
    }
    
    /**
     * 
     */
    public void deleteFirstTabAction() throws Exception {
        logger.info("deleteFirstTabAction");
        delete();
        listFirstTabAction();
    }

    public void deleteSecondTabAction() throws Exception {
        logger.info("deleteSecondTabAction");
        delete();
        listSecondTabAction();
    }

    public void deleteThirdTabAction() throws Exception {
        logger.info("deleteThirdTabAction");
        delete();
        listThirdTabAction();
    }
    
    public void archiveFirstTabAction() {
        logger.info("archiveFirstTabAction");
        archive();
        listFirstTabAction();
    }
    
    /**
     * 
     */
    public void listFirstTabAction() {
        setFirstSelectedId("");
        setFirstMessageTitleLabel(null);
        setFirstMessageTitle(null);
        processList();
    }

    public void listSecondTabAction() {
        setSecondSelectedId("");
        setSecondMessageTitleLabel(null);
        setSecondMessageTitle(null);
        processList();
    }

    public void listThirdTabAction() {
        setThirdSelectedId("");
        setThirdMessageTitleLabel(null);
        setThirdMessageTitle(null);
        processList();
    }

    /**
     * 
     */
	protected void delete() throws Exception {
		delete(getFirstSelectedIdAsLong());
	}
	

	/**
	 * 
	 * @param id
	 */
	protected void delete(Long id) {
		recyledBinAction(id, true);
	}
	
	/**
     * 
     */
    public void restoreAction() {
    	restore();
    }
    
    /**
     * 
     */
    protected void restore() {
    	restore(getFirstSelectedIdAsLong());
    }
    
    /**
	 * 
	 * @param id
	 */
	protected void restore(Long id) {
		recyledBinAction(id, false);
	}
	
	/**
     * 
     */
    protected void archive() {
    	archive(getFirstSelectedIdAsLong());
    }
    
	/**
	 * 
	 * @param id
	 */
	protected void archive(Long id) {
		recyledBinAction(id, false);
	}

    /**
     * 
     * @param id
     * @param throwIntoRecylceBin
     */
	protected void recyledBinAction(Long id, boolean throwIntoRecylceBin) {
		String msg;

		if (id == null || id <= 0) {
			msg = I18N.message("message.no.id.selected");
			getActionResult().setWarningMessage(msg);
			return;
		}
		
		Class<? extends EntityStatusRecordAware> entityClass = null;
		try {
			entityClass = (Class<? extends EntityStatusRecordAware>) ClassUtils.getClass(entityClassname);
		} catch (Exception e) {
			String errMsg = "Error while instanciating the class [" + entityClassname + "]";
			logger.error(errMsg, e);
			getActionResult().setErrorMessage(errMsg);
		}

		EntityStatusRecordAware entity = ENTITY_SRV.getById(entityClass, id);
		if (entity == null) {
			msg = I18N.message("message.no.record.found.with.id", id + "");
			getActionResult().setWarningMessage(msg);
			return;
		}

		if (throwIntoRecylceBin) {
			ENTITY_SRV.throwIntoRecycledBin(entity);
			msg = I18N.message("message.delete.success");
		} else if (isRestoreToActive()) {
			ENTITY_SRV.restoreFromRecycledBin(entity);
			msg = I18N.message("message.restore.success");
		} else if (isActiveToArchive) {
			ENTITY_SRV.changeStatusRecord(entity, EStatusRecord.ARCHI);
			msg = I18N.message("message.archive.success");
		} else {
			ENTITY_SRV.restoreFromRecycledBin(entity);
			msg = I18N.message("message.restore.success");
		}
		getActionResult().setInfoMessage(msg);

		listAction();
	}        

    /**
     * 
     */
    protected abstract void list();

    /**
     * Event before displaying the list
     * By default do nothing before
     * Can be Override
     */
    protected void beforeList() {
    }

    /**
     * Event after displaying the list
     * By default do nothing before
     * Can be Override
     */
    protected void afterList() {
    }

    /**
     * 
     * @param nbTotalRecords
     * @return
     */
    protected int computeNbPages(final long nbTotalRecords) {
        final Double totalRow = (double) nbTotalRecords;
        final double pageNums = totalRow / nbRecordsPerPage;
        final BigDecimal nbPages = new BigDecimal(pageNums).setScale(0, BigDecimal.ROUND_UP);
        return nbPages.intValue();
    }

    /**
     * 
     * @return
     */
    protected List<Serializable> getEntityIDsInCurrentPage(final List<Serializable> ids) {
        final List<Serializable> idsInPage = new ArrayList<Serializable>();
        if (ids != null) {
            final int startIndex = (nbRecordsPerPage * getFirstCurrentPageIndex()) - nbRecordsPerPage;
            int endIndex = (nbRecordsPerPage * getFirstCurrentPageIndex()) - 1;
            if (endIndex >= ids.size()) {
                endIndex = ids.size() - 1;
            }

            for (int i = startIndex; i <= endIndex; i++) {
                idsInPage.add(ids.get(i));
            }
        }
        return idsInPage;
    }

    protected int getMoveToPage() {
        final boolean isValid = getNbTotalPages() > 1;
        if (!getMoveTo().equals("") && isValid) {
            if (getMoveTo().equals("FirstPage")) {
                setFirstCurrentPageIndex(1);
            }
            else if (getMoveTo().equals("PreviousPage")) {
                setFirstCurrentPageIndex(getFirstCurrentPageIndex() - 1);
            }
            else if (getMoveTo().equals("NextPage")) {
                setFirstCurrentPageIndex(getFirstCurrentPageIndex() + 1);
            }
            else if (getMoveTo().equals("LastPage")) {
                setFirstCurrentPageIndex(getNbTotalPages());
            }
            clearMoveTo();
        }
        return getFirstCurrentPageIndex();
    }

    /**
     * @return the nbRecordsPerPage
     */
    public int getNbRecordsPerPage() {
        return nbRecordsPerPage;
    }

    /**
     * @param nbRecordsPerPage the nbRecordsPerPage to set
     */
    public void setNbRecordsPerPage(final int nbRecordsPerPage) {
        this.nbRecordsPerPage = nbRecordsPerPage;
    }

    /**
     * @return the nbTotalPages
     */
    public int getNbTotalPages() {
    	return nbTotalPages;
    }

    /**
     * @param nbTotalPages the nbTotalPages to set
     */
    public void setNbTotalPages(final int nbTotalPages) {
        this.nbTotalPages = nbTotalPages;
    }

    /**
     * @return the nbTotalRecords
     */
    public long getNbTotalRecords() {
        return nbTotalRecords;
    }

    /**
     * @param nbTotalRecords the nbTotalRecords to set
     */
    public void setNbTotalRecords(final long nbTotalRecords) {
        this.nbTotalRecords = nbTotalRecords;
    }

    /**
	 * @return the entityClassname
	 */
	public String getEntityClassname() {
		return entityClassname;
	}

	/**
	 * @param entityClassname the entityClassname to set
	 */
	public void setEntityClassname(String entityClassname) {
		this.entityClassname = entityClassname;
	}

	public String getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(final String moveTo) {
        this.moveTo = moveTo;
    }

   

	/**
	 * @return the showRecycledBin
	 */
	public Boolean getShowRecycledBin() {
		return showRecycledBin;
	}

	/**
	 * @param showRecycledBin the showRecycledBin to set
	 */
	public void setShowRecycledBin(Boolean showRecycledBin) {
		this.showRecycledBin = showRecycledBin;
	}

	private void clearMoveTo() {
        moveTo = "";
    }

    public boolean isSearch() {
        return moveTo.equals("");
    }

	/**
	 * @return the isRestoreToActive
	 */
	public Boolean isRestoreToActive() {
		return isRestoreToActive;
	}

	/**
	 * @param isRestoreToActive the isRestoreToActive to set
	 */
	public void setIsRestoreToActive(Boolean isRestoreToActive) {
		this.isRestoreToActive = isRestoreToActive;
	}

	/**
	 * @return the isAllList
	 */
	public Boolean getIsAllList() {
		return isAllList;
	}

	/**
	 * @param isAllList the isAllList to set
	 */
	public void setIsAllList(Boolean isAllList) {
		this.isAllList = isAllList;
	}

	/**
	 * @return the needSessionCriteria
	 */
	public Boolean getNeedSessionCriteria() {
		return needSessionCriteria;
	}

	/**
	 * @param needSessionCriteria the needSessionCriteria to set
	 */
	public void setNeedSessionCriteria(Boolean needSessionCriteria) {
		this.needSessionCriteria = needSessionCriteria;
	}

	/**
	 * @return the isShowAdvancedSearch
	 */
	public Boolean getIsShowAdvancedSearch() {
		return isShowAdvancedSearch;
	}

	/**
	 * @param isShowAdvancedSearch the isShowAdvancedSearch to set
	 */
	public void setIsShowAdvancedSearch(Boolean isShowAdvancedSearch) {
		this.isShowAdvancedSearch = isShowAdvancedSearch;
	}

	/**
	 * @return the sortIndex
	 */
	public String getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(String sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * @return the isActiveToArchive
	 */
	public Boolean getIsActiveToArchive() {
		return isActiveToArchive;
	}

	/**
	 * @param isActiveToArchive the isActiveToArchive to set
	 */
	public void setIsActiveToArchive(Boolean isActiveToArchive) {
		this.isActiveToArchive = isActiveToArchive;
	}

}
