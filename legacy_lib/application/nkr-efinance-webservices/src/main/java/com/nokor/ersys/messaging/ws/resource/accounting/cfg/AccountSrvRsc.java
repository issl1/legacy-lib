package com.nokor.ersys.messaging.ws.resource.accounting.cfg;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.messaging.share.accounting.AccountDTO;
import com.nokor.ersys.messaging.ws.resource.accounting.AccountingWsPath;
import com.nokor.ersys.messaging.ws.resource.accounting.BaseAccountingSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author prasnar
 *
 */
@Path(BaseWsPath.PATH_CONFIGS + AccountingWsPath.ACCOUNTING + AccountingWsPath.ACCOUNTS)
public class AccountSrvRsc extends BaseAccountingSrvRsc {

	/**
	 * GET  Accounts
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			List<Account> accounts = ENTITY_SRV.list(Account.class);
			List<AccountDTO> accountDTOs = toAccountDTOs(accounts);
			
			return ResponseHelper.ok(accountDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Account [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * GET Account BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(BaseWsPath.PATH_ID)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Account - id. [" + id + "]");
		
			Account acc = ENTITY_SRV.getById(Account.class, id);
			
			if (acc == null) {
				String errMsg = "Account [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			AccountDTO accDTO = toAccountDTO(acc);
			
			return ResponseHelper.ok(accDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	
	/**
	 * 
	 * @param accDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(AccountDTO accDTO) {
		try {
			Account acc = toAccount(accDTO, null);
			ENTITY_SRV.saveOrUpdate(acc);
			accDTO.setId(acc.getId());
			return ResponseHelper.ok(accDTO);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * UPDATE 
	 * @param id
	 * @param individualDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path(BaseWsPath.PATH_ID)
	public Response update(@PathParam(BaseWsPath.ID) Long id, AccountDTO accDTO) {
		try {
			LOG.debug("Account - id. [" + id + "]");
			Account acc = null; //toAccount(accDTO, id);
			
			ENTITY_SRV.saveOrUpdate(acc);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
}
