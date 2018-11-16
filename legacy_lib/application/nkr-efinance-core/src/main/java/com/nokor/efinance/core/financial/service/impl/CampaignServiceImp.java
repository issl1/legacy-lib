package com.nokor.efinance.core.financial.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignAssetCategory;
import com.nokor.efinance.core.financial.model.CampaignAssetMake;
import com.nokor.efinance.core.financial.model.CampaignAssetModel;
import com.nokor.efinance.core.financial.model.CampaignAssetRange;
import com.nokor.efinance.core.financial.model.CampaignCreditBureauGrade;
import com.nokor.efinance.core.financial.model.CampaignDealer;
import com.nokor.efinance.core.financial.model.CampaignDocument;
import com.nokor.efinance.core.financial.model.CampaignTerm;
import com.nokor.efinance.core.financial.service.CampaignSequenceImpl;
import com.nokor.efinance.core.financial.service.CampaignSequenceManager;
import com.nokor.efinance.core.financial.service.CampaignService;
import com.nokor.efinance.core.quotation.SequenceGenerator;

@Service("campaignService")
public class CampaignServiceImp extends BaseEntityServiceImpl implements CampaignService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7532743920270117117L;
	
	@Autowired
    private EntityDao dao;
	
	/**
	 * @see com.nokor.efinance.core.financial.service.CampaignService#saveOrUpdateCampagin(com.nokor.efinance.core.financial.model.Campaign)
	 */
	@Override
	public void saveOrUpdateCampagin(Campaign campaign) {
		if (StringUtils.isEmpty(campaign.getCode())) {
			SequenceGenerator sequenceGenerator = new CampaignSequenceImpl(DateUtils.today(), CampaignSequenceManager.getInstance().getSequenceCampaign());
			campaign.setCode(sequenceGenerator.generate());
		}
		saveOrUpdate(campaign);
	}
	
	
	/**
	 * @param campaign
	 */
	public void validAllDealers(Campaign campaign) {
		if (campaign.isValidForAllDealers()) {
			List<CampaignDealer> campaignDealers = campaign.getDealers();
			if (campaignDealers != null && !campaignDealers.isEmpty()) {
				for (CampaignDealer campaignDealer : campaignDealers) {
					delete(campaignDealer);
				}
			}
		}
		saveOrUpdate(campaign);
	}
	
	/**
	 * 
	 * @param campaign
	 */
	@Override
	public void deleteCampaign(Campaign campaign) {
		List<CampaignTerm> campaignTerms = campaign.getTerms();
		if (campaignTerms != null && !campaignTerms.isEmpty()) {
			for (CampaignTerm campaignTerm : campaignTerms) {
				delete(campaignTerm);
			}
		}
		
		List<CampaignAssetCategory> campaignAssetCategories = campaign.getAssetCategories();
		if (campaignAssetCategories != null && !campaignAssetCategories.isEmpty()) {
			for (CampaignAssetCategory campaignAssetCategory : campaignAssetCategories) {
				delete(campaignAssetCategory);
			}
		}
		
		List<CampaignAssetMake> campaignAssetMakes = campaign.getAssetMakes();
		if (campaignAssetMakes != null && !campaignAssetMakes.isEmpty()) {
			for (CampaignAssetMake campaignAssetMake : campaignAssetMakes) {
				delete(campaignAssetMake);
			}
		}
		
		List<CampaignAssetRange> campaignAssetRanges = campaign.getAssetRanges();
		if (campaignAssetRanges != null && !campaignAssetRanges.isEmpty()) {
			for (CampaignAssetRange campaignAssetRange : campaignAssetRanges) {
				delete(campaignAssetRange);
			}
		}
		
		List<CampaignAssetModel> campaignAssetModels = campaign.getAssetModels();
		if (campaignAssetModels != null && !campaignAssetModels.isEmpty()) {
			for (CampaignAssetModel campaignAssetModel : campaignAssetModels) {
				delete(campaignAssetModel);
			}
		}
		
		List<CampaignDealer> campaignDealers = campaign.getDealers();
		if (campaignDealers != null && !campaignDealers.isEmpty()) {
			for (CampaignDealer campaignDealer : campaignDealers) {
				delete(campaignDealer);
			}
		}
		
		List<CampaignCreditBureauGrade> campaignCreditBureauGrades = campaign.getGrades();
		if (campaignCreditBureauGrades != null && !campaignCreditBureauGrades.isEmpty()) {
			for (CampaignCreditBureauGrade campaignCreditBureauGrade : campaignCreditBureauGrades) {
				delete(campaignCreditBureauGrade);
			}
		}
		
		List<CampaignDocument> campaignDocuments = campaign.getDocuments();
		if (campaignDocuments != null && !campaignDocuments.isEmpty()) {
			for (CampaignDocument campaignDocument : campaignDocuments) {
				delete(campaignDocument);
			}
		}
		
		delete(campaign);	
	} 

	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

}
