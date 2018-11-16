package com.nokor.efinance.core.stock.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.dao.PaymentDao;
import com.nokor.efinance.core.stock.model.EStockMovement;
import com.nokor.efinance.core.stock.model.EStockReason;
import com.nokor.efinance.core.stock.model.Product;
import com.nokor.efinance.core.stock.model.ProductStock;
import com.nokor.efinance.core.stock.model.ProductStockInventory;
import com.nokor.efinance.core.stock.service.StockService;


@Service("stockService")
@Transactional
public class StockServiceImpl extends BaseEntityServiceImpl implements StockService {
		
	/**
	 */
	private static final long serialVersionUID = 1857502439129977419L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private PaymentDao dao;

	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public PaymentDao getDao() {
		return dao;
	}

	@Override
	public ProductStock getProductStock(Dealer dealer, String code) {
		BaseRestrictions<ProductStock> restrictions = new BaseRestrictions<>(ProductStock.class);
		restrictions.addAssociation("product", "prod", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		restrictions.addCriterion(Restrictions.eq("prod.code", code));
		List<ProductStock> productStocks = list(restrictions);
		return (productStocks != null && !productStocks.isEmpty()) ? productStocks.get(0) : null;
	}
	
	/**
	 * @param dealer
	 * @param code
	 * @param initialQty
	 * @param qty
	 */
	public void saveStock(Dealer dealer, String code, Integer initialQty, Integer qty) {
		Product product = getByCode(Product.class, code);
		ProductStock productStock = getProductStock(dealer, code);
		if (productStock == null) {
			productStock = new ProductStock();
			productStock.setDealer(dealer);
			productStock.setProduct(product);
		}		
		if (productStock.getQty() != qty.intValue()) {
			int diff = qty.intValue() - productStock.getQty();
			ProductStockInventory productStockInventory = new ProductStockInventory();
			productStockInventory.setDealer(dealer);
			productStockInventory.setProduct(product);
			productStockInventory.setDate(DateUtils.today());
			productStockInventory.setQty(diff);
			productStockInventory.setStockReason(diff < 0 ? EStockReason.OUT_STOCK : EStockReason.IN_STOCK);
			saveOrUpdate(productStockInventory);
		}
		productStock.setInitialQty(initialQty);
		productStock.setQty(qty);
		saveOrUpdate(productStock);
	}
	
	/**
	 * @param dealer
	 * @param code
	 * @param qty
	 * @param stockReason
	 */
	public void saveStock(Dealer dealer, String code, Integer qty, EStockReason stockReason) {
		if (MyNumberUtils.getInteger(qty) > 0) {
			Product product = getByCode(Product.class, code);
			saveStock(dealer, product, qty, stockReason);
		}
	}
	
	/**
	 * @param dealer
	 * @param product
	 * @param qty
	 * @param stockReason
	 */
	public void saveStock(Dealer dealer, Product product, Integer qty, EStockReason stockReason) {
		if (MyNumberUtils.getInteger(qty) > 0) {
			ProductStockInventory productStockInventory = new ProductStockInventory();
			productStockInventory.setDealer(dealer);
			productStockInventory.setProduct(product);
			productStockInventory.setDate(DateUtils.today());
			productStockInventory.setQty(stockReason.getStockMovement().equals(EStockMovement.IN) ? qty : -qty);
			productStockInventory.setStockReason(stockReason);
			saveOrUpdate(productStockInventory);
			
			ProductStock productStock = getProductStock(dealer, product.getCode());
			if (productStock == null) {
				productStock = new ProductStock();
				productStock.setDealer(dealer);
				productStock.setProduct(product);
			}		
			productStock.setQty(productStock.getQty() + (stockReason.getStockMovement().equals(EStockMovement.IN) ? qty : -qty));
			saveOrUpdate(productStock);
		}
	}
}
