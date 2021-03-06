package com.justinmobile.tsm.process.mocam.impl;

import org.springframework.stereotype.Service;

import com.justinmobile.core.exception.PlatformErrorCode;
import com.justinmobile.core.exception.PlatformException;
import com.justinmobile.tsm.application.domain.SecurityDomain;
import com.justinmobile.tsm.card.domain.CardInfo;
import com.justinmobile.tsm.card.domain.CardSecurityDomain;
import com.justinmobile.tsm.cms2ac.SessionStatus;
import com.justinmobile.tsm.cms2ac.domain.ApduCommand;
import com.justinmobile.tsm.process.mocam.MocamResult;
import com.justinmobile.tsm.transaction.domain.LocalTransaction;

@Service("unlockSdProcessor")
public class UnlockSdProcessorImpl extends AbstractUnlockProcessor {
	
	@Override
	public MocamResult processTrans(LocalTransaction localTransaction) {
		MocamResult result = null;
		switch (localTransaction.getSessionStatus()) {
		case SessionStatus.INIT:
			result = preOperation(localTransaction);
			break;
		case SessionStatus.PRE_OPERATION_SUCCESS:
			result = startupUnLock(localTransaction);
			break;
		case SessionStatus.UNLOCK_APP_UNLOCK_CMD:// 校验set status，通知业务平台
			parseUnlockAppRsp(localTransaction);
			result = operationResult(localTransaction);
			break;
		default:
			result = super.processTrans(localTransaction);
		}
		return result;
	}

	@Override
	protected void check(LocalTransaction localTransaction) {
		String cardNo = localTransaction.getCardNo();
		CardInfo card = cardInfoManager.getByCardNo(cardNo);

		// 校验卡
		validateCard(card);

		// 校验安全域
		SecurityDomain sd = securityDomainManager.getByAid(localTransaction.getAid());
		if (sd == null) {// 如果AID对应的安全域不存在，抛出异常
			throw new PlatformException(PlatformErrorCode.TRANS_SD_AID_NOT_FOUND);
		}
		validateSd(sd);

		// 校验卡上安全域状态
		CardSecurityDomain cardSd = cardSecurityDomainManager.getByCardNoAid(cardNo, sd.getAid());
		if (cardSd == null) {
			throw new PlatformException(PlatformErrorCode.CARD_SD_NOT_FOUND);
		}
		if (!CardSecurityDomain.STATUS_UNLOCKABLE.contains(cardSd.getStatus())) {
			throw new PlatformException(PlatformErrorCode.CARD_SD_STATUS_ERROR);
		}

	}

	@Override
	protected void changeStatus(LocalTransaction localTransaction) {
		String cardNo = localTransaction.getCardNo();
		String aid = localTransaction.getAid();
		CardSecurityDomain cardSecurityDomain = cardSecurityDomainManager.getByCardNoAid(cardNo, aid);
		changeCardSecurityDomainStatus(cardNo, aid, cardSecurityDomain.getOrginalStatus());
	}

	@Override
	protected ApduCommand launchUnLock(LocalTransaction localTransaction) {
		return apduEngine.buildSetStatusCmd(localTransaction.getLastCms2acParam(), (byte) 0x07, false);
	}
	
	@Override
	protected MocamResult preOperation(LocalTransaction localTransaction) {
		SecurityDomain securityDomain = securityDomainManager.getByAid(localTransaction.getAid());
		if (SecurityDomain.MODEL_TOKEN == securityDomain.getModel().intValue()) {// 委托模式安全域，执行预处理
			return super.preOperation(localTransaction);// 其他模式安全域，跳过预处理
		} else {
			localTransaction.setSessionStatus(SessionStatus.PRE_OPERATION_SUCCESS);
			return processTrans(localTransaction);
		}
	}

	@Override
	protected MocamResult operationResult(LocalTransaction localTransaction, int successSessionStatus) {
		SecurityDomain securityDomain = securityDomainManager.getByAid(localTransaction.getAid());
		if (SecurityDomain.MODEL_TOKEN == securityDomain.getModel().intValue()) {// 委托模式安全域，执行结果通知
			return super.operationResult(localTransaction, successSessionStatus);
		} else {
			localTransaction.setSessionStatus(SessionStatus.COMPLETED);// 其他模式，结束流程
			return processTrans(localTransaction);
		}
	}
}
