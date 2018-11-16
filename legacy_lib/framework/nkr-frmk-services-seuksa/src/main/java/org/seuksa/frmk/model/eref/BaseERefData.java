package org.seuksa.frmk.model.eref;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.seuksa.frmk.model.entity.RefDataId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.cfield.model.ECusType;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.helper.SeuksaServicesHelper;



/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseERefData implements MBaseERefData, RefDataId, EntityStatusRecordAware, SeuksaServicesHelper {
	/** */
	private static final long serialVersionUID = 8577133853503741806L;
	
	protected static final Logger logger = LoggerFactory.getLogger(BaseERefData.class);

    private static ColERefData colERefData;
    
    public static Long DEFAULT_ID = 1L; 

	protected Long id;
	protected String code;
	protected String desc;
	protected String descEn;
	protected String descFr;
	protected EGroup group;

	protected Integer sortIndex;
	protected boolean hasConstraint = true;
    private EStatusRecord statusRecord;

    static {
    	initCache();
    }
    
	/**
	 * 
	 */
	public BaseERefData() {
		// DO NOT REMOVE
		// USED BY HIBERNATE
	}

    /**
     * 
     * @param value
     * @param id
     */
	public BaseERefData(final String code, final long id) {
		this.id = id;
		this.code = code;
	}
	
	/**
     * 
     * @param value
     * @param id
     */
	public BaseERefData(final String code, final long id, final EGroup group) {
		this.id = id;
		this.code = code;
		this.group = group;
	}
	
	/**
	 * 
	 * @param code
	 * @param desc
	 * @param id
	 */
	public BaseERefData(final String code, final String desc, final long id) {
		this.id = id;
		this.code = code;
		this.desc = desc;
		this.descEn = this.desc;
	}
	
	/**
	 * 
	 * @param code
	 * @param desc
	 * @param descEn
	 * @param id
	 */
	public BaseERefData(final String code, final String desc, final String descEn, final long id) {
		this.id = id;
		this.code = code;
		this.desc = desc;
		this.descEn = descEn;
	}
	
	/**
	 * 
	 */
	public static void initCache() {
		colERefData = new ColERefData();
	}
	
	/**
	 * 
	 * @param eRef
	 * @return
	 */
	public <T extends RefDataId> Long convertToDatabaseColumn(T eRef) {
		return eRef != null ? eRef.getId() : null;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public <T extends RefDataId> T convertToEntityAttribute(Long id) {
		return getRefDataEntityFromId(id);
	}
	
	
	@Override
	public Long getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	protected void setId(long id) {
		this.id = id;
	}

	/**
	 * return code
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * return code
	 */
	public String getName() {
		return getCode();
	}
	
	/**
	 * return code
	 */
	public String getEntityName() {
		return getCode();
	}
	
	/**
	 * 
	 * @param lang
	 * @return
	 */
	public String getDesc(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return getDesc();
		} else if (lang == ELanguage.FRENCH) {
			return getDescFr();
		} else {
			return getDescEn();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String getDesc() {
		if (desc == null) {
			desc = REFDATA_SRV.getDesc(this, false);
		}
		return desc;
	}
	
	/**
     * @param desc the desc to set
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
	/**
	 * 
	 * @return
	 */
	public String getDescEn() {
		if (descEn == null) {
			descEn = REFDATA_SRV.getDesc(this, true);
		}
		return descEn;
	}

	/**
     * @param desc the descEn to set
     */
    public void setDescEn(final String descEn) {
        this.descEn = descEn;
    }
    
    /**
	 * @return the descFr
	 */
	public String getDescFr() {
		if (descFr == null) {
			descFr = REFDATA_SRV.getDesc(this, true);
		}
		return descFr;
	}

	/**
	 * @param descFr the descFr to set
	 */
	public void setDescFr(String descFr) {
		this.descFr = descFr;
	}

	/**
     * 
     * @return
     */
    public String getDescLocale() {
    	if (I18N.isEnglishLocale()) {
    		return getDescEn();
    	} else {
    		return getDesc();
    	}
    }
  
	/**
     * @return SortIndex
     */
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param SortIndex
     */
    public void setSortIndex(final Integer SortIndex) {
        this.sortIndex = SortIndex;
    }

	/**
	 * @return the group
	 */
	public EGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(EGroup group) {
		this.group = group;
	}

	/**
	 * @return the hasConstraint
	 */
	public boolean hasConstraint() {
		return hasConstraint;
	}

	/**
	 * @param hasConstraint the hasConstraint to set
	 */
	public void setHasConstraint(boolean hasConstraint) {
		this.hasConstraint = hasConstraint;
	}
	

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public <T extends RefDataId> T getRefDataEntityFromId(Long id) {
		T eRef = (T) getById(getClass(), id);
		if (eRef == null && hasConstraint) {
    		//throw new IllegalStateException("id [" + id + "] is not a value of [" + getClass().getName() + "]");
			// TODO
    	}
		return eRef;
	}
	
	/**
	 * 
	 * @return
	 */
	public <T extends RefDataId> T getRefDataEntityFromId() {
		T eRef = (T) getById(getClass(), getId());
		return eRef;
	}
	
	/**
	 * 
	 * @return
	 */
    public EStatusRecord getStatusRecord() {
        return statusRecord;
    }

    /**
     * 
     * @param statusRecord
     */
    public void setStatusRecord(EStatusRecord statusRecord) {
        this.statusRecord = statusRecord;
    }
    
    /**
     * 
     * @return
     */
    public boolean isActive() {
        return statusRecord == null || EStatusRecord.ACTIV.equals(statusRecord);
    }
    
    /**
     * 
     * @param isActive
     */
    public void setActive(boolean isActive) {
		statusRecord = isActive ? EStatusRecord.ACTIV : EStatusRecord.INACT;
    }
    
    /**
     * 
     */
    public void switchStatus() {
    	setActive(isActive());
    }


    /**
	 * 
	 * @param refDataClazz
	 * @param id
	 * @return
	 */
	public static <T extends RefDataId> T getByDefaultId(Class<T> refDataClazz) {
		return colERefData.getById(refDataClazz.getName(), DEFAULT_ID);
	}
	
    /**
	 * 
	 * @param dataClazz
	 * @param id
	 * @return
	 */
	public static <T extends RefDataId> T getById(Class<T> refDataClazz, Long id) {
		return colERefData.getById(refDataClazz.getName(), id);
	}
		
	/**
	 * 
	 * @param refDataClazzName
	 * @param id
	 * @return
	 */
	public static <T extends RefDataId> T getById(String refDataClazzName, Long id) {
		return colERefData.getById(refDataClazzName, id);
	}

	
	/**
	 * 
	 * @param refDataClazz
	 * @param code
	 * @return
	 */
	public static <T extends RefDataId> T valueOf(Class<T> refDataClazz, String code) {
		return colERefData.getByCode(refDataClazz.getName(), code);
	}
    
	/**
	 * 
	 * @param dataClazz
	 * @param code
	 * @return
	 */
	public static <T extends RefDataId> T getByCode(Class<T> refDataClazz, String code) {
		return colERefData.getByCode(refDataClazz.getName(), code);
    }
	
	/**
	 * 
	 * @param refDataClazzName
	 * @param code
	 * @return
	 */
	public static <T extends RefDataId> T getByCode(String refDataClazzName, String code) {
		return colERefData.getByCode(refDataClazzName, code);
    }

	/**
	 * 
	 * @param dataClazz
	 * @return
	 */
	public static <T extends RefDataId> List<T> getValues(Class<T> refDataClazz) {
		return getValues(refDataClazz.getName());
	}
	

	/**
	 * 
	 * @param refDataClazzName
	 * @return
	 */
	public static <T extends RefDataId> List<T> getValues(String refDataClazzName) {
		return colERefData.getValues(refDataClazzName);
	}
	
	/**
	 * 
	 * @param refDataClazzName
	 */
	public static <T extends RefDataId> void initCacheRefDataStaticValues(String refDataClazzName) {
		colERefData.initCacheRefDataStaticValues(refDataClazzName);
	}
	
	/**
	 * 
	 * @param refData
	 */
	public static <T extends RefDataId> void addERefData(T refData) {
		colERefData.addERefData(refData);
	}
	
	/**
	 * 
	 * @param refData
	 * @return
	 */
    public static <T extends BaseERefData> T getFromRefData(RefData refData) {
    	Class<T> refDataClazz;
    	T eRefData = null;

     	try {
     		refDataClazz = (Class<T>) Class.forName(refData.getTable().getCode());
        	eRefData = refDataClazz.newInstance();
        	eRefData.setId(refData.getIde());
        	eRefData.setCode(refData.getCode());
//        	eRefData.setDesc(refData.getDesc());
//        	eRefData.setDescEn(refData.getDescEn());
//        	eRefData.setDescFr(refData.getDescFr());
//        	eRefData.setGroup(refData.getGroup());
        	eRefData.setSortIndex(refData.getSortIndex());
        	eRefData.loadCustomFields(refData);
    		         	
    	} catch (Exception e) {
    		String errMsg = e.getMessage();
    	    logger.error(errMsg, e);
    	    throw new IllegalStateException(errMsg, e);
    	}
  	
    	return eRefData;
    }
    
    /**
     * 
     * @param refData
     * @throws Exception
     */
    protected void loadCustomFields(RefData refData) throws Exception {
    	RefTable table = refData.getTable();
		assignValueToCustomField(table.getFieldName1(), table.getCusType1(), refData.getFieldValue1());
		assignValueToCustomField(table.getFieldName2(), table.getCusType2(), refData.getFieldValue2());
		assignValueToCustomField(table.getFieldName3(), table.getCusType3(), refData.getFieldValue3());
		assignValueToCustomField(table.getFieldName4(), table.getCusType4(), refData.getFieldValue4());
		assignValueToCustomField(table.getFieldName5(), table.getCusType5(), refData.getFieldValue5());
    	
    }

    /**
     * 
     * @param fieldName
     * @param cusType
     * @param fieldValue
     */
    private void assignValueToCustomField(String fieldName, ECusType cusType, String fieldValue) throws Exception {
    	if (StringUtils.isEmpty(fieldName) || cusType == null) {
    		return;
    	}
    	
    	try {
	    	Field field = getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(this, cusType.getCusValue(fieldValue, field.getType()));
    	} catch (Exception e) {
    		String errMsg = "Error with Field [" + fieldName + "] [" + cusType.getCode() + "] [" + (fieldValue != null ? fieldValue : "<empty>") + "] - " + e.getMessage();
    	    logger.error(errMsg, e);
    		throw new IllegalStateException(errMsg, e);
    		
    	}
    }
    
	/**
	 * 
	 * @param refDataClazz
	 * @return
	 */
	public static <T extends RefDataId> T getDefault(Class<T> refDataClazz) {
		return REFDATA_SRV.getERefDefaultValue(refDataClazz);
	}
	
    @Override
	public boolean equals(Object o) {
		return o != null && id != null && id.equals(((RefDataId) o).getId());
	}

    @Override
	public String toString() {
		if (getDesc() != null) {
			return getDesc();
		} 
		if (getDescEn() != null) {
			return getDesc();
		}
		return getCode();
	}
	

}