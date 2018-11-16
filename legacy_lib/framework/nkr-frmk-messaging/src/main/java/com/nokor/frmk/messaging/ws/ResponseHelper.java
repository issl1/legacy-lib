package com.nokor.frmk.messaging.ws;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


/**
 * 
 * @author prasnar
 *
 */
public class ResponseHelper {
	private static final String _SLASH = "/";
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public static ResponseBuilder prepareResponse(int status) {
		return prepareResponse(status, null, (URI) null);
	}
	
	/**
	 * 
	 * @param status
	 * @param outputEntity
	 * @return
	 */
	public static ResponseBuilder prepareResponse(int status, Object outputEntity) {
		return prepareResponse(status, outputEntity, (URI) null);
	}
	
	/**
	 * 
	 * @param status
	 * @param outputEntity
	 * @param location
	 * @return
	 */
	public static ResponseBuilder prepareResponse(int status, Object outputEntity, String location) {
		return prepareResponse(status, outputEntity, location != null ? URI.create(location) : null);
	}
	/**
	 * 
	 * @param status
	 * @param outputEntity
	 * @param location
	 * @return
	 */
	public static ResponseBuilder prepareResponse(int status, Object outputEntity, URI location) {
		ResponseBuilder builder = Response.status(status);
		if (outputEntity != null) {
			builder = builder.entity(outputEntity);
		}
		builder = builder.location(location);
		return builder;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Response ok() {
		return success(null);
	}
	
	/**
	 * 
	 * @param outputEntity
	 * @return
	 */
	public static Response ok(Object outputEntity) {
		return success(outputEntity);
	}
	
	/**
	 * 
	 * @param outputEntity
	 * @return
	 */
	public static Response success(Object outputEntity) {
		return success(outputEntity, EResponseStatus.OK);
	}
	

	/**
	 * 
	 * @param outputEntity
	 * @param status
	 * @return
	 */
	public static Response success(Object outputEntity, EResponseStatus status) {
		return prepareResponse(status.getIdInt(), outputEntity).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response badRequest(String message) {
		return error(EResponseStatus.BAD_REQUEST, message, null);
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public static Response error(String message) {
		return error(message, null);
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response error(String message, String location) {
		return error(EResponseStatus.KO, message, location);
	}
	
	/**
	 * 
	 * @param errStatus
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response error(EResponseStatus errStatus, String message, String location) {
		ErrorResult errRes = new ErrorResult();
		errRes.setStatus(errStatus.getIdInt());
		errRes.setDesc(message);
		errRes.setLongDesc(errRes.getDesc());
//		errRes.setUri(uri);
		
		return error(errRes, location);
	}
	
	/**
	 * 
	 * @param errRes
	 * @return
	 */
	public static Response error(ErrorResult errRes) {
		return error(errRes, null);
	}
	
	/**
	 * 
	 * @param errRes
	 * @param location
	 * @return
	 */
	public static Response error(ErrorResult errRes, String location) {
		return prepareResponse(errRes.getStatus() != null ? errRes.getStatus().intValue() : EResponseStatus.KO.getIdInt(), errRes, location).build();
	}

		
	/**
	 * 
	 * @param suffixeLocation
	 * @return
	 */
	public static Response creationSuccess(Long id, String suffixeLocation) {
		return prepareResponse(EResponseStatus.CREATION_OK.getIdInt(), null, suffixeLocation + _SLASH + id).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response creationError(String message, String location) {
		return prepareResponse(EResponseStatus.CREATION_KO.getIdInt(), message, location).build();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Response updateSucess() {
		return prepareResponse(EResponseStatus.UPDATE_OK.getIdInt()).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response updateError(String message, String location) {
		return prepareResponse(EResponseStatus.UPDATE_KO.getIdInt(), message, location).build();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Response deleteSucess() {
		return prepareResponse(EResponseStatus.DELETE_OK.getIdInt()).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response deleteError(String message, String location) {
		return prepareResponse(EResponseStatus.DELETE_KO.getIdInt(), message, location).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response getUniqueError(String message, String location) {
		return prepareResponse(EResponseStatus.GET_UNIQUE_KO.getIdInt(), message, location).build();
	}
	
	/**
	 * 
	 * @param message
	 * @param location
	 * @return
	 */
	public static Response getListError(String message, String location) {
		return prepareResponse(EResponseStatus.GET_LIST_KO.getIdInt(), message, location).build();
	}
}
