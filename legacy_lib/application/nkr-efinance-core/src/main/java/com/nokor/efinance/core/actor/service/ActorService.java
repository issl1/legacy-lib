package com.nokor.efinance.core.actor.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.efinance.core.actor.model.Actor;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * Actor service interface
 * @author uhout.cheng
 */
public interface ActorService extends BaseEntityService {

	Actor getFinancialCompany();
	Actor getActorByDealer(Dealer dealer);
	Actor getActorByApplicant(Applicant applicant);
}
