package com.nokor.efinance.core.stock.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.stock.model.EStockReason;
import com.nokor.efinance.core.stock.model.Product;
import com.nokor.efinance.core.stock.model.ProductStock;

/**
 * @author ly.youhort
 */
public interface StockService extends BaseEntityService {
	
	ProductStock getProductStock(Dealer dealer, String code);
	void saveStock(Dealer dealer, String code, Integer initialQty, Integer qty);
	void saveStock(Dealer dealer, String code, Integer qty, EStockReason stockReason);	
	void saveStock(Dealer dealer, Product product, Integer qty, EStockReason stockReason);
}