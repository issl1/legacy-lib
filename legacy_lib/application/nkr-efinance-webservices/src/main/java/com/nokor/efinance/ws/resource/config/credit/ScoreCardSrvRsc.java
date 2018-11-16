package com.nokor.efinance.ws.resource.config.credit;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.scoring.ScoreCard;
import com.nokor.efinance.core.scoring.ScoreGroup;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.share.credit.ScoreCardDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Score card web service
 * @author uhout.cheng
 */
@Path("/configs/credits/scorecards")
public class ScoreCardSrvRsc extends FinResourceSrvRsc implements FMEntityField {
	
	/**
	 * List all score cards
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<ScoreCard> scoreCards = ENTITY_SRV.list(getScoreCardRestriction());
			ScoreCardDTO scoreCardDTO = toScoreCardDTO(scoreCards);
			
			return ResponseHelper.ok(scoreCardDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
		
	/**
	 * Convert to ScoreCard data transfer
	 * @param scoreCard
	 * @return
	 */
	private ScoreCardDTO toScoreCardDTO(List<ScoreCard> scoreCards) {
		ScoreCardDTO scoreCardDTO = new ScoreCardDTO();
		for (ScoreCard scoreCard : scoreCards) {
			Integer sortIndex = MyNumberUtils.getInteger(scoreCard.getSortIndex());
			ScoreGroup scoreGroup = scoreCard.getScoreGroup();
			if (scoreGroup != null) {
				if (scoreGroup.getId() == 1) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreMaritalStatusSingle(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreMaritalStatusMarried(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreMaritalStatusDivorced(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreMaritalStatusSeparated(scoreCard.getScoreValue());
					} else if (sortIndex == 5) {
						scoreCardDTO.setScoreMaritalStatusWidow(scoreCard.getScoreValue());
					} else if (sortIndex == 6) {
						scoreCardDTO.setScoreMaritalStatusDefacto(scoreCard.getScoreValue());
					} else if (sortIndex == 7) {
						scoreCardDTO.setScoreMaritalStatusUnknown(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 2) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreNbDependentLessThanZero(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreNbDependentZeroToOne(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreNbDependentOneToTwo(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreNbDependentTwoToThree(scoreCard.getScoreValue());
					} else if (sortIndex == 5) {
						scoreCardDTO.setScoreNbDependentThreePlus(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 3) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreEducationHighSchool(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreEducationTechnicalCollege(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreEducationBachelor(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreEducationMaster(scoreCard.getScoreValue());
					} else if (sortIndex == 5) {
						scoreCardDTO.setScoreEducationPhd(scoreCard.getScoreValue());
					} else if (sortIndex == 6) {
						scoreCardDTO.setScoreEducationMiddleSchool(scoreCard.getScoreValue());
					} else if (sortIndex == 7) {
						scoreCardDTO.setScoreEducationJuniorHighSchool(scoreCard.getScoreValue());
					} else if (sortIndex == 8) {
						scoreCardDTO.setScoreEducationSeniorHighSchool(scoreCard.getScoreValue());
					} else if (sortIndex == 9) {
						scoreCardDTO.setScoreEducationVocationalCertificate(scoreCard.getScoreValue());
					} else if (sortIndex == 10) {
						scoreCardDTO.setScoreEducationHighVocationalCertificate(scoreCard.getScoreValue());
					} else if (sortIndex == 11) {
						scoreCardDTO.setScoreEducationTechnicalCertificate(scoreCard.getScoreValue());
					} else if (sortIndex == 12) {
						scoreCardDTO.setScoreEducationNonFormalEducation(scoreCard.getScoreValue());
					} else if (sortIndex == 13) {
						scoreCardDTO.setScoreEducationCompulsoryEducation(scoreCard.getScoreValue());
					} else if (sortIndex == 14) {
						scoreCardDTO.setScoreEducationAdultEducation(scoreCard.getScoreValue());
					} else if (sortIndex == 15) {
						scoreCardDTO.setScoreEducationPolytechnicSchool(scoreCard.getScoreValue());
					}	
				} else if (scoreGroup.getId() == 4) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreCompanySizeLessThanFifty(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreCompanySizeFiftyToOneHundred(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreCompanySizeOneHundredToTwoHundredFifty(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreCompanySizeTwoHundredFiftyPlus(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 6) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreEmploymentDurationLessThanSix(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreEmploymentDurationSixToTwelve(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreEmploymentDurationTwelveToThirtySix(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreEmploymentDurationThirtySixToSixty(scoreCard.getScoreValue());
					} else if (sortIndex == 5) {
						scoreCardDTO.setScoreEmploymentDurationSixtyPlus(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 7) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreIncomeTypeEmployed(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreIncomeTypeSelfEmployed(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 8) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreApartmentPropertyYes(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreApartmentPropertyNo(scoreCard.getScoreValue());
					}
				} else if (scoreGroup.getId() == 9) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreDebtIncomeLessThanTwentySix(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreDebtIncomeTwentySixToThirtySeven(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreDebtIncomeThirtySevenToFortyFour(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreDebtIncomeFortyFourPlus(scoreCard.getScoreValue());
					} 
				} else if (scoreGroup.getId() == 10) {
					if (sortIndex == 1) {
						scoreCardDTO.setScoreFICOScoreA(scoreCard.getScoreValue());
					} else if (sortIndex == 2) {
						scoreCardDTO.setScoreFICOScoreB(scoreCard.getScoreValue());
					} else if (sortIndex == 3) {
						scoreCardDTO.setScoreFICOScoreBPlus(scoreCard.getScoreValue());
					} else if (sortIndex == 4) {
						scoreCardDTO.setScoreFICOScoreC(scoreCard.getScoreValue());
					} else if (sortIndex == 5) {
						scoreCardDTO.setScoreFICOScoreCPlus(scoreCard.getScoreValue());
					} else if (sortIndex == 6) {
						scoreCardDTO.setScoreFICOScoreD(scoreCard.getScoreValue());
					}
				}
			}
		}
		return scoreCardDTO;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<ScoreCard> getScoreCardRestriction() {
		BaseRestrictions<ScoreCard> restrictions = new BaseRestrictions<>(ScoreCard.class);
		restrictions.addAssociation("scoreGroup", "scrgrp", JoinType.INNER_JOIN);
		restrictions.addOrder(Order.asc("scrgrp.id"));
		return restrictions;
	}
}
