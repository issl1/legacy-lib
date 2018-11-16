package org.seuksa.frmk.tools.amount;

import java.io.Serializable;

import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

/**
 * @author ly.youhort
 *
 */
public class Amount implements Serializable {
	private static final long serialVersionUID = 3612496365754262305L;
	
	private Double tiAmount;
	private Double vatAmount;
	private Double teAmount;
	private int nbDecimal = 2;
	
	public Amount() {
		
	}
	
	/**
	 * @param teAmount
	 * @param vatAmount
	 * @param tiAmount
	 */
	public Amount(Double teAmount, Double vatAmount, Double tiAmount) {
		this(teAmount, vatAmount, tiAmount, 2);
	}
	
	/**
	 * @param teAmount
	 * @param vatAmount
	 * @param tiAmount
	 */
	public Amount(Double teAmount, Double vatAmount, Double tiAmount, int nbDecimal) {
		this.nbDecimal = nbDecimal;
		this.teAmount = MyMathUtils.roundTo(MyNumberUtils.getDouble(teAmount), nbDecimal);
		this.vatAmount = MyMathUtils.roundTo(MyNumberUtils.getDouble(vatAmount), nbDecimal);
		this.tiAmount = MyMathUtils.roundTo(MyNumberUtils.getDouble(tiAmount), nbDecimal);
	}
	
	/**
	 * @return the tiAmount
	 */
	public Double getTiAmount() {
		return tiAmount;
	}
	
	/**
	 * @param tiAmount the tiAmount to set
	 */
	public void setTiAmount(Double tiAmount) {
		this.tiAmount = tiAmount;
	}
	
	/**
	 * @return the vatAmount
	 */
	public Double getVatAmount() {
		return vatAmount;
	}
	
	/**
	 * @param vatAmount the vatAmount to set
	 */
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}
	
	/**
	 * @return the teAmount
	 */
	public Double getTeAmount() {
		return teAmount;
	}
	
	/**
	 * @param teAmount the teAmount to set
	 */
	public void setTeAmount(Double teAmount) {
		this.teAmount = teAmount;
	}
	
	/**
	 * @param tiAmount
	 */
	public void plusTiAmount(Double tiAmount) {
		if (tiAmount != null) {
			this.tiAmount = MyNumberUtils.getDouble(this.tiAmount) + MyNumberUtils.getDouble(tiAmount);
		}
	}
	
	/**
	 * @param tiAmount
	 */
	public void minusTiAmount(Double tiAmount) {
		if (tiAmount != null) {
			this.tiAmount = MyNumberUtils.getDouble(this.tiAmount) - MyNumberUtils.getDouble(tiAmount);
		}
	}
	
	/**
	 * @param teAmount
	 */
	public void plusTeAmount(Double teAmount) {
		if (teAmount != null) {
			this.teAmount = MyNumberUtils.getDouble(this.teAmount) + MyNumberUtils.getDouble(teAmount);
		}
	}
	
	/**
	 * @param teAmount
	 */
	public void minusTeAmount(Double teAmount) {
		if (teAmount != null) {
			this.teAmount = MyNumberUtils.getDouble(this.teAmount) - MyNumberUtils.getDouble(teAmount);
		}
	}
	
	/**
	 * @param vatAmount
	 */
	public void plusVatAmount(Double vatAmount) {
		if (vatAmount != null) {
			this.vatAmount = MyNumberUtils.getDouble(this.vatAmount) + MyNumberUtils.getDouble(vatAmount);
		}
	}
	
	/**
	 * @param vatAmount
	 */
	public void minusVatAmount(Double vatAmount) {
		if (vatAmount != null) {
			this.vatAmount = MyNumberUtils.getDouble(this.vatAmount) - MyNumberUtils.getDouble(vatAmount);
		}
	}
		
	/**
	 * @return the nbDecimal
	 */
	public int getNbDecimal() {
		return nbDecimal;
	}

	/**
	 * @param nbDecimal the nbDecimal to set
	 */
	public void setNbDecimal(int nbDecimal) {
		this.nbDecimal = nbDecimal;
	}

	/**
	 * @param amount
	 */
	public void plus(Amount amount) {
		plusTeAmount(amount.getTeAmount());
		plusVatAmount(amount.getVatAmount());
		plusTiAmount(amount.getTiAmount());
	}
	
	/**
	 * @param amount
	 */
	public void minus(Amount amount) {
		minusTeAmount(amount.getTeAmount());
		minusVatAmount(amount.getVatAmount());
		minusTiAmount(amount.getTiAmount());
	}
}
