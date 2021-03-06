package com.justinmobile.tsm.process.mocam.impl;

import org.springframework.stereotype.Service;

import com.justinmobile.core.exception.PlatformErrorCode;
import com.justinmobile.core.exception.PlatformException;
import com.justinmobile.core.utils.ConvertUtils;
import com.justinmobile.tsm.card.domain.CardInfo;
import com.justinmobile.tsm.cms2ac.SessionStatus;
import com.justinmobile.tsm.cms2ac.domain.ApduCommand;
import com.justinmobile.tsm.cms2ac.domain.Cms2acParam;
import com.justinmobile.tsm.cms2ac.exception.ApduException;
import com.justinmobile.tsm.cms2ac.response.GetDataReadTokenResponse;
import com.justinmobile.tsm.process.mocam.MocamResult;
import com.justinmobile.tsm.process.mocam.MocamResult.ApduName;
import com.justinmobile.tsm.transaction.domain.LocalTransaction;

@Service("loginProcessor")
public class LoginProcessor extends PublicOperationProcessor {

	public MocamResult processTrans(LocalTransaction localTransaction) {
		MocamResult result = null;
		switch (localTransaction.getSessionStatus()) {
		case SessionStatus.INIT:
			result = startup(localTransaction);
			break;
		case SessionStatus.OPEN_RW_WAIT_OPEN_REQ:
			result = launchSelectSd(localTransaction, securityDomainManager.getIsd(), SessionStatus.LOGIN_SELECT_ISD);
			break;
		case SessionStatus.LOGIN_SELECT_ISD:
			parseSelectAppRsp(localTransaction);
			result = launchInitUpdate(localTransaction, SessionStatus.LOGIN_INIT_UPDATE);
			break;
		case SessionStatus.LOGIN_INIT_UPDATE:
			result = parseInitUpdateSdRsp(localTransaction, SessionStatus.LOGIN_EXT_AUTH);
			break;
		case SessionStatus.LOGIN_EXT_AUTH:
			parseExtAuthSdRsp(localTransaction);
			result = launchReadToken(localTransaction);
			break;
		case SessionStatus.LOGIN_READ_TOEKN:
			parseReadToken(localTransaction);
			result = launchNextOperation(localTransaction);
			break;
		default:
			result = super.processTrans(localTransaction);
		}
		return result;
	}

	private MocamResult launchNextOperation(LocalTransaction localTransaction) {
		localTransaction.setSessionStatus(SessionStatus.COMPLETED);
		return processTrans(localTransaction);
	}

	private void parseReadToken(LocalTransaction localTransaction) {
		Cms2acParam cms2acParam = localTransaction.getLastCms2acParam();
		parseCms2acMoMocamMessage(localTransaction, cms2acParam);

		GetDataReadTokenResponse response;
		try {
			response = apduEngine.parseReadTokenRsp(cms2acParam);
		} catch (ApduException ae) {
			throw new PlatformException(PlatformErrorCode.APDU_WRITE_TOKEN_ERROR, ae);
		}

		CardInfo card = cardInfoManager.getByCardNo(localTransaction.getCardNo());
		if (!card.getToken().equals(ConvertUtils.byteArray2HexString(response.getToken()))) {
			throw new PlatformException(PlatformErrorCode.TOKEN_MISMATCH);
		}
	}

	private MocamResult launchReadToken(LocalTransaction localTransaction) {
		Cms2acParam cms2acParam = localTransaction.getLastCms2acParam();

		ApduCommand apdu = apduEngine.buildReadTokenCmd(cms2acParam);

		contactApduCommand(cms2acParam, apdu);
		MocamResult result = buildMocamMessage(localTransaction, cms2acParam, SessionStatus.LOGIN_READ_TOEKN);
		result.setApduName(ApduName.Load);
		return result;
	}

	@Override
	protected void check(LocalTransaction localTransaction) {
		// CardInfo card =
		// cardInfoManager.getByCardNo(localTransaction.getCardNo());
		//
		// if (null == card.getRegisterable()) {// 如果没有收到注册短信，抛出异常
		// throw new PlatformException(PlatformErrorCode.TRANS_REG_REFUSE);
		// }
		//
		// customerCardInfoManager.checkCardBindable(card);
	}
}
