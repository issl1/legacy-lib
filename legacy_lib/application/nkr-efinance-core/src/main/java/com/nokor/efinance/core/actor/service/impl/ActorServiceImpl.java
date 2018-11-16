package com.nokor.efinance.core.actor.service.impl;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.actor.model.Actor;
import com.nokor.efinance.core.actor.service.ActorRestriction;
import com.nokor.efinance.core.actor.service.ActorService;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * ActorServiceImpl service
 * @author uhout.cheng
 */
@Service("actorService")
public class ActorServiceImpl extends BaseEntityServiceImpl implements ActorService {

	/**
	 */
	private static final long serialVersionUID = 5360785010176559084L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
		
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}

	@Override
	public Actor getFinancialCompany() {
		ActorRestriction restrictions = new ActorRestriction();
		Organization financialCompany = new Organization();
		financialCompany.setId(1l);
		restrictions.setFinancialCompany(financialCompany);
		return getActor(restrictions);
	}

	@Override
	public Actor getActorByDealer(Dealer dealer) {
		ActorRestriction restrictions = new ActorRestriction();
		restrictions.setDealer(dealer);
		return getActor(restrictions);
	}

	@Override
	public Actor getActorByApplicant(Applicant applicant) {
		ActorRestriction restrictions = new ActorRestriction();
		restrictions.setApplicant(applicant);
		return getActor(restrictions);
	}
	
	/**
	 * @param restrictions
	 * @return
	 */
	private Actor getActor(ActorRestriction restrictions) {
		return getFirst(restrictions);
	}
}
