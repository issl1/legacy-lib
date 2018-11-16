package com.nokor.frmk.messaging.ws;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.ws.rs.core.Response.Status;

import org.seuksa.frmk.model.eref.BaseERefData;

/**
 * 
 * @author prasnar
 *
 */
public class EResponseStatus extends BaseERefData implements AttributeConverter<EResponseStatus, Long> {
	/** */
	private static final long serialVersionUID = 1831012630481423351L;

	public static final EResponseStatus OK = new EResponseStatus("Success", Status.OK.getStatusCode()); // 200 OK.
	public static final EResponseStatus KO = new EResponseStatus("Error", Status.INTERNAL_SERVER_ERROR.getStatusCode()); // 500 Server error 
	public static final EResponseStatus CREATION_OK = new EResponseStatus("Creation OK", Status.CREATED.getStatusCode()); // 201 Created
	public static final EResponseStatus BAD_REQUEST = new EResponseStatus("Bad Request", Status.BAD_REQUEST.getStatusCode()); // 400 Bad request.
	public static final EResponseStatus CREATION_KO = KO; //new EResponseStatus("Creation Error", 4L);
	public static final EResponseStatus UPDATE_OK = OK; //new EResponseStatus("Update OK", 5L);
	public static final EResponseStatus UPDATE_KO = KO; //new EResponseStatus("Update Error", 6L);
	public static final EResponseStatus GET_UNIQUE_OK = OK; //new EResponseStatus("Get Unique OK", 7L);
	public static final EResponseStatus GET_UNIQUE_KO = KO; //new EResponseStatus("Get Unique Error", 7L);
	public static final EResponseStatus GET_LIST_OK = OK; //new EResponseStatus("Get List OK", 8L);
	public static final EResponseStatus GET_LIST_KO = KO; //new EResponseStatus("Get List Error", 8L);
	public static final EResponseStatus ALREADY_EXISTS = KO; //new EResponseStatus("Already Exists Error", 9L);
	public static final EResponseStatus NOT_FOUND = new EResponseStatus("Not Found Error", Status.NOT_FOUND.getStatusCode());  // 404 Not Found 
	public static final EResponseStatus PARAMETER_NOT_VALID = KO; //new EResponseStatus("Parameter Not Valid Error", 11L);
	public static final EResponseStatus DELETE_OK = OK; //new EResponseStatus("Delete OK", 12L);
	public static final EResponseStatus DELETE_KO = KO; //new EResponseStatus("Delete Error", 12L);
	
	

	/**
	 * 
	 */
	public EResponseStatus() {
	}

	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EResponseStatus(String code, long id) {
		super(code, id);
	}
	
	/**
	 * 
	 */
	public Integer getIdInt() {
		return Integer.valueOf(String.valueOf(id));
	}
	


	@Override
	public EResponseStatus convertToEntityAttribute(Long id) {
		return ((EResponseStatus) super.convertToEntityAttribute(id));
	}
	
	@Override
	public Long convertToDatabaseColumn(EResponseStatus arg0) {
		return super.convertToDatabaseColumn(arg0);
	}

	/**
	 * 
	 * @return
	 */
	public static List<EResponseStatus> values() {
		return getValues(EResponseStatus.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EResponseStatus getById(long id) {
		return getById(EResponseStatus.class, id);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodeInt() {
		return Integer.valueOf(code);
	}	
}