package com.nokor.frmk.config.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.common.app.cfield.model.ECusType;


/**
 * Table Reference model
 * @author kong.phirun
 */
@Entity
@Table(name = "ts_ref_table",
	indexes = {
		@Index(name = "ts_ref_table_ref_typ_id_idx", columnList = "ref_typ_id")
	}
)
public class RefTable extends EntityRefA implements MRefTable {
    /** */
	private static final long serialVersionUID = 2908383140059233403L;

	private String shortName;
	
    private Boolean readOnly;
    private Boolean useSortIndex;
    private Boolean generateCode;
	protected Boolean fetchValuesFromDB; // true: fetch from RefData / false: from "public final static XX"
	protected Boolean fetchI18NFromDB; 	 // true: fetch from RefData / false: from I18N properties files
	private Boolean visible;
	private Boolean cached;				 // is cached
	
	private ERefType type;
	
	private String fieldName1;
	private String fieldName2;
	private String fieldName3;
	private String fieldName4;
	private String fieldName5;
	private ECusType cusType1;
	private ECusType cusType2;
	private ECusType cusType3;
	private ECusType cusType4;
	private ECusType cusType5;


	private List<RefData> data;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_tab_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "ref_tab_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}

    @Transient
    public String getName() {
    	return getCode();
    }
    
    /**
     * 
     * @param name
     */
    public void setName(String name) {
    	setCode(name);
    }

    /**
	 * @return the shortName
	 */
    @Column(name = "ref_tab_shortname", nullable = true)
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "ref_tab_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

    @Column(name = "ref_tab_desc_en", nullable = true)
   	@Override	
    public String getDescEn() {
        return descEn;
    }

    /**
     * @return readOnly
     */
    @Column(name = "ref_tab_readonly")
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly
     */
    public void setReadOnly(final Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return useSortIndex
     */
    @Column(name = "ref_tab_use_sort_index")
    public Boolean getUseSortIndex() {
        return useSortIndex;
    }

    /**
     * @param useSortIndex
     */
    public void setUseSortIndex(final Boolean useSortIndex) {
        this.useSortIndex = useSortIndex;
    }

	/**
	 * @return the generateCode
	 */
    @Column(name = "ref_tab_generate_code")
	public Boolean getGenerateCode() {
		return generateCode;
	}

	/**
	 * @param generateCode the generateCode to set
	 */
	public void setGenerateCode(Boolean generateCode) {
		this.generateCode = generateCode;
	}

	/**
	 * @return the fetchValuesFromDB
	 */
	@Column(name = "ref_tab_fetch_values_from_db", nullable = false)
	public Boolean getFetchValuesFromDB() {
		return BooleanUtils.isNotFalse(fetchValuesFromDB);
	}

	/**
	 * @param fetchValuesFromDB the fetchValuesFromDB to set
	 */
	public void setFetchValuesFromDB(Boolean fetchValuesFromDB) {
		this.fetchValuesFromDB = fetchValuesFromDB;
	}
	
	/**
	 * @return the fetchI18NFromDB
	 */
	@Column(name = "ref_tab_fetch_i18n_from_db", nullable = true)
	public Boolean getFetchI18NFromDB() {
		return BooleanUtils.isNotFalse(fetchI18NFromDB);
	}

	/**
	 * @param fetchI18NFromDB the fetchI18NFromDB to set
	 */
	public void setFetchI18NFromDB(Boolean fetchI18NFromDB) {
		this.fetchI18NFromDB = fetchI18NFromDB;
	}
	
	/**
	 * @return the visible
	 */
	@Column(name = "ref_tab_visible")
	public Boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	
	/**
	 * @return the cached
	 */
	@Column(name = "ref_tab_cached")
	public Boolean isCached() {
		return cached;
	}

	/**
	 * @param cached the cached to set
	 */
	public void setCached(Boolean cached) {
		this.cached = cached;
	}

	/**
	 * @return the data
	 */
	@OneToMany(mappedBy = "table", fetch = FetchType.LAZY)
	public List<RefData> getData() {
		if (data == null) {
			data = new ArrayList<RefData>();
    	}
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<RefData> data) {
		this.data = data;
	}

	/**
	 * @return the type
	 */
    @Column(name = "ref_typ_id", nullable = true)
    @Convert(converter = ERefType.class)
	public ERefType getType() {
    	if (type == null) {
    		type = ERefType.REFDATA;
    	}
		return type;
	}
    
    /**
     * 
     * @return
     */
    @Transient
    public boolean isEntity() {
    	return ERefType.ENTITY_REF == type;
    }

	/**
	 * @param type the type to set
	 */
	public void setType(ERefType type) {
		this.type = type;
	}


	/**
	 * @return the fieldName1
	 */
    @Column(name = "ref_tab_field_name1", nullable = true)
	public String getFieldName1() {
		return fieldName1;
	}

	/**
	 * @param fieldName1 the fieldName1 to set
	 */
	public void setFieldName1(String fieldName1) {
		this.fieldName1 = fieldName1;
	}

	/**
	 * @return the fieldName2
	 */
    @Column(name = "ref_tab_field_name2", nullable = true)
	public String getFieldName2() {
		return fieldName2;
	}

	/**
	 * @param fieldName2 the fieldName2 to set
	 */
	public void setFieldName2(String fieldName2) {
		this.fieldName2 = fieldName2;
	}

	/**
	 * @return the fieldName3
	 */
    @Column(name = "ref_tab_field_name3", nullable = true)
	public String getFieldName3() {
		return fieldName3;
	}

	/**
	 * @param fieldName3 the fieldName3 to set
	 */
	public void setFieldName3(String fieldName3) {
		this.fieldName3 = fieldName3;
	}

	/**
	 * @return the fieldName4
	 */
    @Column(name = "ref_tab_field_name4", nullable = true)
	public String getFieldName4() {
		return fieldName4;
	}

	/**
	 * @param fieldName4 the fieldName4 to set
	 */
	public void setFieldName4(String fieldName4) {
		this.fieldName4 = fieldName4;
	}

	/**
	 * @return the fieldName5
	 */
    @Column(name = "ref_tab_field_name5", nullable = true)
	public String getFieldName5() {
		return fieldName5;
	}

	/**
	 * @param fieldName5 the fieldName5 to set
	 */
	public void setFieldName5(String fieldName5) {
		this.fieldName5 = fieldName5;
	}

	/**
	 * @return the cusType1
	 */
    @Column(name = "ref_tab_field1_cus_typ_id", nullable = true)
    @Convert(converter = ECusType.class)
	public ECusType getCusType1() {
		return cusType1;
	}

	/**
	 * @param cusType1 the cusType1 to set
	 */
	public void setCusType1(ECusType cusType1) {
		this.cusType1 = cusType1;
	}

	/**
	 * @return the cusType2
	 */
    @Column(name = "ref_tab_field2_cus_typ_id", nullable = true)
    @Convert(converter = ECusType.class)
	public ECusType getCusType2() {
		return cusType2;
	}

	/**
	 * @param cusType2 the cusType2 to set
	 */
	public void setCusType2(ECusType cusType2) {
		this.cusType2 = cusType2;
	}

	/**
	 * @return the cusType3
	 */
    @Column(name = "ref_tab_field3_cus_typ_id", nullable = true)
    @Convert(converter = ECusType.class)
	public ECusType getCusType3() {
		return cusType3;
	}

	/**
	 * @param cusType3 the cusType3 to set
	 */
	public void setCusType3(ECusType cusType3) {
		this.cusType3 = cusType3;
	}

	/**
	 * @return the cusType4
	 */
    @Column(name = "ref_tab_field4_cus_typ_id", nullable = true)
    @Convert(converter = ECusType.class)
	public ECusType getCusType4() {
		return cusType4;
	}

	/**
	 * @param cusType4 the cusType4 to set
	 */
	public void setCusType4(ECusType cusType4) {
		this.cusType4 = cusType4;
	}

	/**
	 * @return the cusType5
	 */
    @Column(name = "ref_tab_field5_cus_typ_id", nullable = true)
    @Convert(converter = ECusType.class)
	public ECusType getCusType5() {
		return cusType5;
	}

	/**
	 * @param cusType5 the cusType5 to set
	 */
	public void setCusType5(ECusType cusType5) {
		this.cusType5 = cusType5;
	}

}
