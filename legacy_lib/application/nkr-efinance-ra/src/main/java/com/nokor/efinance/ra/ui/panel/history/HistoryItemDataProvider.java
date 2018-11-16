package com.nokor.efinance.ra.ui.panel.history;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.workflow.model.WkfBaseHistory;
import com.nokor.efinance.core.auction.model.AuctionWkfHistory;
import com.nokor.efinance.core.auction.model.AuctionWkfHistoryItem;
import com.nokor.efinance.core.collection.model.CollectionWkfHistory;
import com.nokor.efinance.core.collection.model.CollectionWkfHistoryItem;
import com.nokor.efinance.core.contract.model.ContractWkfHistory;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.payment.model.PaymentWkfHistory;
import com.nokor.efinance.core.payment.model.PaymentWkfHistoryItem;
import com.nokor.ersys.vaadin.ui.history.item.IWkfHistoryItemDataProvider;

/**
 * 
 * @author pengleng.huot
 *
 */
public class HistoryItemDataProvider implements IWkfHistoryItemDataProvider, AppServicesHelper {
	/** */
	private static final long serialVersionUID = 2981299303115770651L;

	@Override
	public List<? extends Entity> fetchCustomEntities(WkfBaseHistory baseWkfHistory, String customEntity, Long entityId) {
		List<? extends Entity> entities = new ArrayList<Entity>();
		
		if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_AUCTION)) {
			baseWkfHistory = ENTITY_SRV.getById(AuctionWkfHistory.class, entityId);
			entities = fetchAuctionHistoryItems(baseWkfHistory);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_COLLECTION)) {
			baseWkfHistory = ENTITY_SRV.getById(CollectionWkfHistory.class, entityId);
			entities = fetchCollectionHistoryItems(baseWkfHistory);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_CONTRACT)) {
			baseWkfHistory = ENTITY_SRV.getById(ContractWkfHistory.class, entityId);
			entities = fetchContractHistoryItems(baseWkfHistory);
		} else if (customEntity.equals(HistoryHolderPanel.CUSTOM_ENTITY_PAYMENT)) {
			baseWkfHistory = ENTITY_SRV.getById(PaymentWkfHistory.class, entityId);
			entities = fetchPaymentHistoryItems(baseWkfHistory);
		}
		return entities;
	}

	/**
	 * 
	 * @param baseWkfHistory
	 * @return
	 */
	private List<? extends Entity> fetchAuctionHistoryItems(WkfBaseHistory baseWkfHistory) {
		BaseRestrictions<AuctionWkfHistoryItem> restrictions = new BaseRestrictions<AuctionWkfHistoryItem>(AuctionWkfHistoryItem.class);			
		restrictions.addCriterion("wkfHistory.id", baseWkfHistory.getId());
		restrictions.addOrder(Order.desc("changeDate"));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param baseWkfHistory
	 * @return
	 */
	private List<? extends Entity> fetchCollectionHistoryItems(WkfBaseHistory baseWkfHistory) {
		BaseRestrictions<CollectionWkfHistoryItem> restrictions = new BaseRestrictions<CollectionWkfHistoryItem>(CollectionWkfHistoryItem.class);			
		restrictions.addCriterion("wkfHistory.id", baseWkfHistory.getId());
		restrictions.addOrder(Order.desc("changeDate"));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param baseWkfHistory
	 * @return
	 */
	private List<? extends Entity> fetchContractHistoryItems(WkfBaseHistory baseWkfHistory) {
		BaseRestrictions<ContractWkfHistoryItem> restrictions = new BaseRestrictions<ContractWkfHistoryItem>(ContractWkfHistoryItem.class);			
		restrictions.addCriterion("wkfHistory.id", baseWkfHistory.getId());
		restrictions.addOrder(Order.desc("changeDate"));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param baseWkfHistory
	 * @return
	 */
	private List<? extends Entity> fetchPaymentHistoryItems(WkfBaseHistory baseWkfHistory) {
		BaseRestrictions<PaymentWkfHistoryItem> restrictions = new BaseRestrictions<PaymentWkfHistoryItem>(PaymentWkfHistoryItem.class);			
		restrictions.addCriterion("wkfHistory.id", baseWkfHistory.getId());
		restrictions.addOrder(Order.desc("changeDate"));
		return ENTITY_SRV.list(restrictions);
	}
	
}
