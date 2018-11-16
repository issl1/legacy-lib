package com.nokor.efinance.ra.ui.panel.history;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.workflow.model.WkfHistory;
import com.nokor.efinance.core.auction.model.AuctionWkfHistory;
import com.nokor.efinance.core.collection.model.CollectionWkfHistory;
import com.nokor.efinance.core.contract.model.ContractWkfHistory;
import com.nokor.efinance.core.payment.model.PaymentWkfHistory;
import com.nokor.ersys.vaadin.ui.history.IWkfHistoryDataProvider;

public class HistoryDataProvider implements IWkfHistoryDataProvider, AppServicesHelper {
	/** */
	private static final long serialVersionUID = -7065201627217191448L;

	/**
	 * 
	 * @param histoRestrictions
	 * @return
	 */
	@Override
	public long getTotalRecords(BaseRestrictions<WkfHistory> histoRestrictions, String customEntity) {
		if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_PAYMENT)) {
			return getTotalPaymentWkfHistories(histoRestrictions);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_CONTRACT)) {
			return getTotalContractWkfHistories(histoRestrictions);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_COLLECTION)) {
			return getTotalCollectionWkfHistories(histoRestrictions);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_AUCTION)) {
			return getTotalAuctionWkfHistories(histoRestrictions);
		}
		return 0;
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @param customEntity
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	@Override
	public List<? extends Entity> fetchCustomEntities(BaseRestrictions<WkfHistory> histoRestrictions,
			String customEntity, Integer firstResult, Integer maxResults) {

		List<? extends Entity> entities = new ArrayList<Entity>();;
		
		if (customEntity != null){
			if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_PAYMENT)) {
				entities = fetchPaymentWkfHistories(histoRestrictions, firstResult, maxResults);
			} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_CONTRACT)) {
				entities = fetchContractWkfHistories(histoRestrictions, firstResult, maxResults);
			} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_COLLECTION)) {
				entities = fetchCollectionWkfHistories(histoRestrictions, firstResult, maxResults);
			} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_AUCTION)) {
				entities = fetchAuctionWkfHistories(histoRestrictions, firstResult, maxResults);
			}
		}
		return entities;
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	private List<? extends Entity> fetchPaymentWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions, Integer firstResult, Integer maxResults) {
		BaseRestrictions<PaymentWkfHistory> baseRestrictions = new BaseRestrictions<PaymentWkfHistory>(PaymentWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		baseRestrictions.setOrders(baseRestrictions.getOrders());
		baseRestrictions.setFirstResult(firstResult);
		baseRestrictions.setMaxResults(maxResults);
		return ENTITY_SRV.list(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	private List<? extends Entity> fetchContractWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions, Integer firstResult, Integer maxResults) {
		BaseRestrictions<ContractWkfHistory> baseRestrictions = new BaseRestrictions<ContractWkfHistory>(ContractWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		baseRestrictions.setOrders(baseRestrictions.getOrders());
		baseRestrictions.setFirstResult(firstResult);
		baseRestrictions.setMaxResults(maxResults);
		return ENTITY_SRV.list(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	private List<? extends Entity> fetchCollectionWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions, Integer firstResult, Integer maxResults) {
		BaseRestrictions<CollectionWkfHistory> baseRestrictions = new BaseRestrictions<CollectionWkfHistory>(CollectionWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		baseRestrictions.setOrders(baseRestrictions.getOrders());
		baseRestrictions.setFirstResult(firstResult);
		baseRestrictions.setMaxResults(maxResults);
		return ENTITY_SRV.list(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	private List<? extends Entity> fetchAuctionWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions, Integer firstResult, Integer maxResults) {
		BaseRestrictions<AuctionWkfHistory> baseRestrictions = new BaseRestrictions<AuctionWkfHistory>(AuctionWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		baseRestrictions.setOrders(baseRestrictions.getOrders());
		baseRestrictions.setFirstResult(firstResult);
		baseRestrictions.setMaxResults(maxResults);
		return ENTITY_SRV.list(baseRestrictions);
	}

	/**
	 * 
	 * @param histoRestrictions
	 * @return
	 */
	private long getTotalPaymentWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions) {
		BaseRestrictions<PaymentWkfHistory> baseRestrictions = new BaseRestrictions<PaymentWkfHistory>(PaymentWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		return ENTITY_SRV.count(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @return
	 */
	private long getTotalContractWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions) {
		BaseRestrictions<ContractWkfHistory> baseRestrictions = new BaseRestrictions<ContractWkfHistory>(ContractWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		return ENTITY_SRV.count(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @return
	 */
	private long getTotalCollectionWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions) {
		BaseRestrictions<CollectionWkfHistory> baseRestrictions = new BaseRestrictions<CollectionWkfHistory>(CollectionWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		return ENTITY_SRV.count(baseRestrictions);
	}
	
	/**
	 * 
	 * @param histoRestrictions
	 * @return
	 */
	private long getTotalAuctionWkfHistories(BaseRestrictions<WkfHistory> histoRestrictions) {
		BaseRestrictions<AuctionWkfHistory> baseRestrictions = new BaseRestrictions<AuctionWkfHistory>(AuctionWkfHistory.class);
		baseRestrictions.setCriterions(histoRestrictions.getCriterions());
		return ENTITY_SRV.count(baseRestrictions);
	}
}
