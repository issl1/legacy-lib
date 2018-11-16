/**
 * 
 */
package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author sun.vanndy
 *
 */
public class Summary implements Serializable {

	private static final long serialVersionUID = 8266172012091519288L;
	
	private Integer cntpe;
	private Integer cntmtde;
	private Integer cntacc;
	private Integer cntgacc;
	private Integer cntloss;
	private String eisdt;
	private List<TotLimits> totlimits;
	private List<TotGlimits> totglimits;
	private List<TotLiabilities> totliabilities;
	private List<TotGliabilities> totgliabilities;
	private List<TotLosses> totlosses;
	private List<CurDelBalances> curdelbalances;
	/**
	 * @return the cntpe
	 */
	public Integer getCntpe() {
		return cntpe;
	}
	/**
	 * @param cntpe the cntpe to set
	 */
	public void setCntpe(Integer cntpe) {
		this.cntpe = cntpe;
	}
	/**
	 * @return the cntmtde
	 */
	public Integer getCntmtde() {
		return cntmtde;
	}
	/**
	 * @param cntmtde the cntmtde to set
	 */
	public void setCntmtde(Integer cntmtde) {
		this.cntmtde = cntmtde;
	}
	/**
	 * @return the cntacc
	 */
	public Integer getCntacc() {
		return cntacc;
	}
	/**
	 * @param cntacc the cntacc to set
	 */
	public void setCntacc(Integer cntacc) {
		this.cntacc = cntacc;
	}
	/**
	 * @return the cntgacc
	 */
	public Integer getCntgacc() {
		return cntgacc;
	}
	/**
	 * @param cntgacc the cntgacc to set
	 */
	public void setCntgacc(Integer cntgacc) {
		this.cntgacc = cntgacc;
	}
	/**
	 * @return the cntloss
	 */
	public Integer getCntloss() {
		return cntloss;
	}
	/**
	 * @param cntloss the cntloss to set
	 */
	public void setCntloss(Integer cntloss) {
		this.cntloss = cntloss;
	}
	/**
	 * @return the eisdt
	 */
	public String getEisdt() {
		return eisdt;
	}
	/**
	 * @param eisdt the eisdt to set
	 */
	public void setEisdt(String eisdt) {
		this.eisdt = eisdt;
	}
	/**
	 * @return the totlimits
	 */
	public List<TotLimits> getTotlimits() {
		return totlimits;
	}
	/**
	 * @param totlimits the totlimits to set
	 */
	public void setTotlimits(List<TotLimits> totlimits) {
		this.totlimits = totlimits;
	}
	/**
	 * @return the totglimits
	 */
	public List<TotGlimits> getTotglimits() {
		return totglimits;
	}
	/**
	 * @param totglimits the totglimits to set
	 */
	public void setTotglimits(List<TotGlimits> totglimits) {
		this.totglimits = totglimits;
	}
	/**
	 * @return the totliabilities
	 */
	public List<TotLiabilities> getTotliabilities() {
		return totliabilities;
	}
	/**
	 * @param totliabilities the totliabilities to set
	 */
	public void setTotliabilities(List<TotLiabilities> totliabilities) {
		this.totliabilities = totliabilities;
	}
	/**
	 * @return the totgliabilities
	 */
	public List<TotGliabilities> getTotgliabilities() {
		return totgliabilities;
	}
	/**
	 * @param totgliabilities the totgliabilities to set
	 */
	public void setTotgliabilities(List<TotGliabilities> totgliabilities) {
		this.totgliabilities = totgliabilities;
	}
	/**
	 * @return the totlosses
	 */
	public List<TotLosses> getTotlosses() {
		return totlosses;
	}
	/**
	 * @param totlosses the totlosses to set
	 */
	public void setTotlosses(List<TotLosses> totlosses) {
		this.totlosses = totlosses;
	}
	/**
	 * @return the curdelbalances
	 */
	public List<CurDelBalances> getCurdelbalances() {
		return curdelbalances;
	}
	/**
	 * @param curdelbalances the curdelbalances to set
	 */
	public void setCurdelbalances(List<CurDelBalances> curdelbalances) {
		this.curdelbalances = curdelbalances;
	}
}
