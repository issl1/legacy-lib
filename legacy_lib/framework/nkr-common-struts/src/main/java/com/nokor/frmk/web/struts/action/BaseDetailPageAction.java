package com.nokor.frmk.web.struts.action;

import com.nokor.common.app.web.session.AppSessionKeys;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author prasnar
 *
 */
public abstract class BaseDetailPageAction extends BasePageAction {
    /** */
	private static final long serialVersionUID = -1999130441740084915L;

	private String moveTo = "";

    private long nbTotalEntities;
    private int currentEntityIndex;
    
    private String viewUrl;
    
    /**
     * 
     */
    public BaseDetailPageAction() {
    }

    @Override
    public void viewAction() throws Exception {
    }

    public abstract void newAction() throws Exception;

    public abstract void editAction() throws Exception;

    public abstract void saveAction() throws Exception;

    /**
     * 
     * @return
     */
    public Long getCurrentId() {
        Long id = getSelectedIdAsLong();
        final Long[] ids = (Long[]) ActionContext.getContext().getSession().get(SESS_KEY_ENTITY_IDS);
        if (ids != null) {
            int currentIndex = getIndex(ids, id);
            if (!getMoveTo().equals("")) {
                if (getMoveTo().equals("FirstPage")) {
                    currentIndex = 0;
                }
                else if (getMoveTo().equals("PreviousPage")) {
                    currentIndex--;
                }
                else if (getMoveTo().equals("NextPage")) {
                    currentIndex++;
                }
                else if (getMoveTo().equals("LastPage")) {
                    currentIndex = ids.length - 1;
                }
                id = ids[currentIndex];
                setSelectedId(id.toString());
                clearMoveTo();
            }
            setNbTotalEntities(ids.length);
            setCurrentEntityIndex(currentIndex);
        }
        return id;
    }
    
    /**
     * 
     * @return
     */
    public Long getSecondCurrentId() {
        Long id = getSecondSelectedIdAsLong();
        final Long[] ids = (Long[]) ActionContext.getContext().getSession().get(SESS_KEY_ENTITY_IDS);
        if (ids != null) {
            int currentIndex = getIndex(ids, id);
            if (!getMoveTo().equals("")) {
                if (getMoveTo().equals("FirstPage")) {
                    currentIndex = 0;
                }
                else if (getMoveTo().equals("PreviousPage")) {
                    currentIndex--;
                }
                else if (getMoveTo().equals("NextPage")) {
                    currentIndex++;
                }
                else if (getMoveTo().equals("LastPage")) {
                    currentIndex = ids.length - 1;
                }
                id = ids[currentIndex];
                setSecondSelectedId(id.toString());
                clearMoveTo();
            }
            setNbTotalEntities(ids.length);
            setCurrentEntityIndex(currentIndex);
        }
        return id;
    }
    
     
    /**
     * 
     * @return
     */
    public Long findSelectedEmployeeId () {
    	if (getSecUser() != null 
    			&& getUserInfo() != null 
    			&& getUserInfo().getEmpId() > 0) {
    		return getUserInfo().getEmpId();
    	}
    	
    	return getCurrentId();
    }

    /**
     * 
     * @param ids
     * @param searchId
     * @return
     */
    private int getIndex(final Long[] ids, final Long searchId) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equals(searchId)) {
                return i;
            }
        }
        return 0;
    }

    public String getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(final String moveTo) {
        this.moveTo = moveTo;
    }

    private void clearMoveTo() {
        moveTo = "";
    }

    public long getNbTotalEntities() {
        return nbTotalEntities;
    }

    public void setNbTotalEntities(final long nbTotalEntities) {
        this.nbTotalEntities = nbTotalEntities;
    }

    public int getCurrentEntityIndex() {
        return currentEntityIndex;
    }

    public void setCurrentEntityIndex(final int currentEntityIndex) {
        this.currentEntityIndex = currentEntityIndex;
    }

	/**
	 * @return the viewUrl
	 */
	public String getViewUrl() {
		return viewUrl;
	}

	/**
	 * @param viewUrl the viewUrl to set
	 */
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	


}
