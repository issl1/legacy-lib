package com.nokor.efinance.ws.resource.config.organization;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nokor.efinance.core.auction.model.Auction;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationSubTypes;
import com.nokor.ersys.messaging.ws.resource.organization.BaseLocationSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/auctions/locations")
public class AuctionLocationSrvRsc extends BaseLocationSrvRsc {
	private static final ESubTypeOrganization SUB_TYPE_ORGANIZATION = OrganizationSubTypes.AUCTION_LOCATION;

	/**
	 * @see com.nokor.ersys.messaging.ws.resource.organization.BaseLocationSrvRsc#getSubTypeOrganization()
	 */
	@Override
	protected ESubTypeOrganization getSubTypeOrganization() {
		return SUB_TYPE_ORGANIZATION;
	}
	
	/**
	 * GET  Accounts
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			List<Auction> auctions = ENTITY_SRV.list(Auction.class);
//			List<AuctionDTO> auctionDTOs = toAuctionDTOs(auctions);
			
			return ResponseHelper.ok("auctionDTOs");
		} catch (Exception e) {
			String errMsg = "Error while searching Auction [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
}
