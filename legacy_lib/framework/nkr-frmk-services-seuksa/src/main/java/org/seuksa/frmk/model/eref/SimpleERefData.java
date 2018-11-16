package org.seuksa.frmk.model.eref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Converter;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.helper.SeuksaServicesHelper;



/**
 * SimpleERefData
 * 	- EStatusRecord
 *  - ERefType
 *  - ECusType
 *  - ESessionType
 *  - EGroup
 *  used with {@link Converter} annotation
 *  
 * @author prasnar
 *
 */
public abstract class SimpleERefData implements RefDataId, SeuksaServicesHelper {
    /** */
	private static final long serialVersionUID = -7717457262722046177L;

	protected static final Logger logger = LoggerFactory.getLogger(SimpleERefData.class);

	private static ColERefData colERefData;

    public static List<Class<? extends SimpleERefData>> REFDATA_FROM_XML_LIST = Arrays.asList(EGroup.class);
    protected boolean loadFromXml = false;
    
	protected Long id;
	protected String code;
	protected String desc;
	protected String descEn;

	protected Integer sortIndex;
	protected boolean hasConstraint = true;

	static {
    	initCache();
	}
	

	/**
	 * 
	 */
	public SimpleERefData() {
		// DO NOT REMOVE
		// USED BY HIBERNATE
		
	}

	/**
	 * 
	 */
	public static void initCache() {
		colERefData = new ColERefData();
		for (Class<? extends SimpleERefData> refDataClazz : REFDATA_FROM_XML_LIST) {
			colERefData.addERefDatas(refDataClazz);
		}
	}
	
    /**
     * 
     * @param value
     * @param id
     */
	public SimpleERefData(final String code, final long id) {
		this.id = id;
		this.code = code;
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
	 */
	@Override
	public String getDesc() {
		if (desc == null) {
			if (!I18N.isInitialized()) {
				return getCode();
			}
			desc = ERefDataHelper.getDescI18N(this, false);
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
			descEn = ERefDataHelper.getDescI18N(this, true);
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
	 * @return the loadFromXml
	 */
	public boolean isLoadFromXml() {
		return loadFromXml;
	}

	/**
	 * @param loadFromXml the loadFromXml to set
	 */
	public void setLoadFromXml(boolean loadFromXml) {
		this.loadFromXml = loadFromXml;
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
	 * @param refDataClazz
	 * @param code
	 * @return
	 */
	public static <T extends RefDataId> T valueOf(Class<T> refDataClazz, String code) {
		return getByCode(refDataClazz, code);
	}
    
	/**
	 * 
	 * @param refDataClazz
	 * @param id
	 * @return
	 */
	public static <T extends RefDataId> T getById(Class<T> refDataClazz, Long id) {
		return getById(refDataClazz.getName(), id);
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
	 * @param id
	 * @return
	 */
	public static <T extends RefDataId> T getByCode(Class<T> refDataClazz, String code) {
		return getByCode(refDataClazz.getName(), code);
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
	 * @param refDataClazz
	 * @param listStr
	 * @return
	 */
	public static <T extends RefDataId> List<T> fetchValuesFromStringList(Class<T> refDataClazz, String listStr) {
		List<Long> ids = MyStringUtils.getValuesLong(listStr);
		List<T> values = new ArrayList<>();
		for (Long id : ids) {
			T val = getById(refDataClazz, id);
			values.add(val);
		}
		return values;
	}

		
	/**
	 * 
	 * @param refDataClazz
	 * @return
	 */
	public static <T extends RefDataId> T getDefault(Class<T> refDataClazz) {
		return null;
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