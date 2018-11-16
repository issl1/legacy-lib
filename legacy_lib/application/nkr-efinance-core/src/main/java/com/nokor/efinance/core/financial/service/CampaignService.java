package com.nokor.efinance.core.financial.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.financial.model.Campaign;

/**
 * Campaign Service
 * @author buntha.chea
 *
 */
public interface CampaignService extends BaseEntityService {
	
	/**
	 * saveOrUpdate Campaign
	 * @param campaign
	 */
	void saveOrUpdateCampagin(Campaign campaign);
	
	/**
	 * @param campaign
	 */
	void validAllDealers(Campaign campaign);
	
	/**
	 * Delete campaign
	 * @param campaign
	 */
	void deleteCampaign(Campaign campaign);
}
