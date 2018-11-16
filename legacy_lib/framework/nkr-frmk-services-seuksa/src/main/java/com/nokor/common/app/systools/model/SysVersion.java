package com.nokor.common.app.systools.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.SimpleEntity;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "ts_sys_version")
public class SysVersion extends SimpleEntity  {
	/** */
	private static final long serialVersionUID = -6222658721213658079L;
	
	private String dbVersion;
	private String appVersion;
	private Date when;
	private String script;

	/**
     * 
     * @return
     */
    public static SysVersion createInstance() {
    	SysVersion ver = EntityFactory.createInstance(SysVersion.class);
        return ver;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_ver_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the dbVersion
	 */
    @Column(name = "sys_ver_db_version", unique = true, nullable = false)
	public String getDbVersion() {
		return dbVersion;
	}

	/**
	 * @param dbVersion the dbVersion to set
	 */
	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	/**
	 * @return the appVersion
	 */
    @Column(name = "sys_ver_app_version", unique = true, nullable = false)
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the when
	 */
    @Column(name = "sys_ver_dt_when", nullable = false)
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return the script
	 */
    @Column(name = "sys_ver_script", nullable = true)
	public String getScript() {
		return script;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	
	
	
}
