package com.em;

/*
 * author: 		guizy
 * Date:		2008-06-24
 * Last Modify:	2008-06-24
 */
import com.em.UnionUtil;
import java.io.File;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.watchdata.util.HexStr;
//import com.watchdata.nfcota.util.RsaUtil;

public class EMClient {
	private static EMClient _instance = null;
	private static Log logger = LogFactory.getLog("EMClient.class");
	private String fn;
	private String HsmHost = "";
	private int HsmPort = 0; // ���������˿�
	private int HsmMessaLen = 0; // �������Ϣ����
	private static int messhead = 0; // ��Ϣͷ����,������
	private int hexMessFlag = 0; // mac��������Hex��ʶ
	private final String shapadStr = "3021300906052B0E03021A05000414";
	private final String md5padStr = "3020300C06082A864886F70D020505000410";

	private static final String CONFIG_PATH = "config/log4j.properties";
	private String logpath = CONFIG_PATH;
	private String filename = "";

	public void setLogPath(String sVal) {
		if (sVal == null)
			logpath = CONFIG_PATH;
		else if (sVal.trim() == "")
			logpath = CONFIG_PATH;
		else
			logpath = sVal.trim();
	}

	public String getLogPath() {
		return logpath.trim();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private EMClient() throws Exception {

		// this("c:/share/hsmapi.conf");
		this("config/emconfig.properties");

	}

	/**
	 * 
	 * @param fn
	 * @throws Exception
	 */
	private EMClient(String fn) throws Exception {
		this.fn = fn;
		loadConfig();
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void loadConfig() throws Exception {
		File f = new File(fn);
		if (!f.exists()) {
			throw new Exception("���ܻ������ļ������ڣ�fn:" + fn);
		}
		// ��ȡ���ܻ���Ϣ
		GetConf gc = new GetConf(fn);
		logger.error("---------=");
		HsmHost = gc.getValue("hsmip");
		logger.error("HsmHost=" + HsmHost);
		HsmPort = Integer.parseInt(gc.getValue("hsmport"));
		logger.error("port=" + HsmPort);
		if (gc.getValue("hsmmesslen") == null
				|| gc.getValue("hsmmesslen").length() == 0)
			HsmMessaLen = 0;
		else
			HsmMessaLen = Integer.parseInt(gc.getValue("hsmmesslen"));
		logger.error("bbb");
		gc.clear();
	}

	/**
	 * 
	 * @return
	 */
	public static EMClient getInstance() {
		if (_instance == null) {
			try {
				_instance = new EMClient();
			} catch (Exception ex) {
				logger.error("�������ܻ�ʵ���쳣��", ex);
			}
		}
		return _instance;
	}

	/**
	 * 
	 * @param fn
	 * @return
	 * @throws Exception
	 */
	public static EMClient newInstance(String fn) throws Exception {
		if (_instance != null) {
			_instance.release();
			_instance = null;
		}
		_instance = new EMClient(fn);
		return _instance;
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void release() throws Exception {
		// destoty
	}

	public synchronized String genHsmMessageHead() {
		String HsmMessageHead = "";
		long maxVal = 99999999;
		long val = 0;
		if (HsmMessaLen <= 0)
			return "";
		messhead++;
		val = Long.parseLong("1" + UnionUtil.LeftAddZero("0", HsmMessaLen)) - 1;
		if (messhead >= 99999999 || messhead >= val)
			messhead = 0;
		HsmMessageHead += UnionUtil.LeftAddZero(Integer.toString(messhead),
				HsmMessaLen);
		return HsmMessageHead;
	}

	private String commWithHsm(int cmdLen, String cmdBuff, String CommRet,
			EMResult result) throws Exception {
		String cmdStr = "";
		String outStr = "";
		String messHead = "";

		result.setRecode(-1);
		if (this.HsmMessaLen < 0)
			this.HsmMessaLen = 0;
		messHead = this.genHsmMessageHead();
		/* ���㳤�� */
		logger.info("commWithHsm len[" + (cmdLen + messHead.length()) + "]");
		cmdStr = getPackageLen(cmdLen + messHead.length()) + messHead + cmdBuff;
		/* ���ݽ��� */
		logger.info("cmdStr=[" + cmdStr + "]");
		outStr = ExchangeData(cmdStr);
		logger.info("outStr=[" + outStr + "]");
		/* ���ݼ��� */
		// String retcode=this.CheckResult(outStr, CommRet);
		String retcode = this.CheckMessHeadAndResult(outStr, messHead, CommRet);
		if (retcode == null) {
			result.setRecode(-1);
			return null;
		}
		logger.info("commWithHsm retcode=[" + retcode + "]");
		if (retcode.equals("00"))
			result.setRecode(0);
		else {
			if (Integer.parseInt(retcode) == 0) { // ��ֹ��"00"��" 0"�����
				result.setRecode(-1);
				return null;
			}
			result.setRecode(Integer.parseInt(retcode));
		}
		return outStr.substring(this.HsmMessaLen);
	}

	/********************************* <�ӿ�ʵ��Start> **************************************/
	public synchronized EMResult HsmGenerateRSAKey(String vkIndex, int lenOfVK)
			throws Exception {
		EMResult result = new EMResult();
		result = this.HSM_34(vkIndex, lenOfVK);
		return result;
	}

	public synchronized EMResult HsmGenSignature(int vkIndex, int flag,
			int dataLen, byte[] data

	) throws Exception {
		EMResult result = new EMResult();

		byte[] digs = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			digs = md.digest(data);
			logger.info("digs=[" + UnionUtil.Bytes2HexString(digs) + "]");
		} catch (Exception e) {
		}
		String dataStr = shapadStr + UnionUtil.Bytes2HexString(digs);
		logger.info("der digs=[" + dataStr + "]");
		byte[] zz = UnionUtil.HexString2Bytes(dataStr);
		dataStr = new String(zz, "ISO-8859-1");
		dataLen = dataStr.length();
		result = this.HSM_37(1, vkIndex, dataLen, dataStr);

		return result;
	}

	public synchronized EMResult HsmVerifySignature(int vkIndex, int flag,
			int dataLen, byte[] data, int signLen, byte[] signature)
			throws Exception {
		EMResult result = new EMResult();
		EMResult res = new EMResult();
		byte[] tmpPk = new byte[1024 + 1];
		String signatureStr = new String(signature, "ISO-8859-1");

		// �����Ĺ�Կ����DER����
		res = this.HSM_C7(vkIndex, 2);
		tmpPk = res.getPk();
		// String tmpPkstr = new String(tmpPk);
		// tmpIkstrHex= UnionUtil.Bytes2HexString(tmpPk);

		String pkStr = new String(tmpPk, "ISO-8859-1");
		// sha-1 dataStr

		byte[] digs = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			digs = md.digest(data);
			logger.info("digs=[" + UnionUtil.Bytes2HexString(digs) + "]");
		} catch (Exception e) {
		}
		String dataStr = shapadStr + UnionUtil.Bytes2HexString(digs);
		logger.info("der digs=[" + dataStr + "]");
		byte[] zz = UnionUtil.HexString2Bytes(dataStr);
		dataStr = new String(zz, "ISO-8859-1");
		dataLen = dataStr.length();
		result = this.HSM_38(1, signLen, signatureStr, dataLen, dataStr, pkStr);
		return result;
	}

	public synchronized EMResult getHSMGenerateMAC(int MAK_len, int MAK_type,
			byte[] MAK_mk, int MAC_len, byte[] MACdata

	) throws Exception {
		EMResult result = new EMResult();
		String sData = new String(MAK_mk);
		if (MACdata.length != MAC_len)
			throw new Exception("getHSMGenerateMAC,���ݳ��ȴ�");
		byte[] mac8m = UnionUtil.AllRightZreoTo8Multiple(MACdata);
		MAC_len = mac8m.length;
		String sData2 = "";

		if (hexMessFlag == 1) {
			sData2 = UnionUtil.Bytes2HexString(mac8m);
			MAC_len = MAC_len * 2;
		} else
			sData2 = new String(mac8m, "ISO-8859-1");
		if (MAK_len != 2 && MAK_len != 3)
			throw new Exception("getHSMGenerateMAC,��Կ���Ȳ��ڹ涨��Χ��");
		int mackeylen = 0;
		if (MAK_len == 2)
			mackeylen = 1;
		else
			mackeylen = 2;
		// result=this.HSM_82(MAK_len,MAK_type,sData,MAC_len,sData2);
		if (MAK_type != 1 && MAK_type != 2 && MAK_type != 3)
			throw new Exception("getHSMGenerateMAC,MAC�㷨��ʶ���ڹ涨��Χ��");
		if (MAK_type == 2)
			result = this.HSM_MU(mackeylen, 0, 1, hexMessFlag, sData, "",
					MAC_len, sData2);
		else if (MAK_type == 3)
			result = this.HSM_MS(mackeylen, 0, 1, hexMessFlag, sData, "",
					MAC_len, sData2);
		else
			throw new Exception("getHSMGenerateMAC,���ܻ���֧�ָ�MAC�㷨��ʶ��");
		return result;
	}

	public synchronized EMResult getHSMGenerateVerify(int MAK_len,
			int MAK_type, byte[] MAK_mk, byte[] Mac, int MAC_len, byte[] MACdata)
			throws Exception {
		EMResult result = new EMResult();
		String sData = new String(MAK_mk);
		// String sData2=new String(MACdata);
		if (MACdata.length != MAC_len)
			throw new Exception("getHSMGenerateVerify,���ݳ��ȴ�");
		byte[] mac8m = UnionUtil.AllRightZreoTo8Multiple(MACdata);
		MAC_len = mac8m.length;
		String sData2 = "";

		if (hexMessFlag == 1) {
			sData2 = UnionUtil.Bytes2HexString(mac8m);
			MAC_len = MAC_len * 2;
		} else
			sData2 = new String(mac8m, "ISO-8859-1");

		String sData3 = new String(Mac);
		if (MAK_len != 2 && MAK_len != 3)
			throw new Exception("getHSMGenerateVerify,��Կ���Ȳ��ڹ涨��Χ��");
		int mackeylen = 0;
		if (MAK_len == 2)
			mackeylen = 1;
		else
			mackeylen = 2;
		// result=this.HSM_82(MAK_len,MAK_type,sData,MAC_len,sData2);
		if (MAK_type != 1 && MAK_type != 2 && MAK_type != 3)
			throw new Exception("getHSMGenerateVerify,MAC�㷨��ʶ���ڹ涨��Χ��");
		if (MAK_type == 2)
			result = this.HSM_MU(mackeylen, 0, 1, hexMessFlag, sData, "",
					MAC_len, sData2);
		else if (MAK_type == 3)
			result = this.HSM_MS(mackeylen, 0, 1, hexMessFlag, sData, "",
					MAC_len, sData2);
		else
			throw new Exception("getHSMGenerateVerify,���ܻ���֧�ָ�MAC�㷨��ʶ��");

		if (result.getRecode() != 0)
			return result;

		String myMac = new String(result.getData());
		myMac = myMac.substring(0, sData3.length());
		if (myMac.equals(sData3) == false) // macУ��ûͨ��01
		{
			logger.info("macУ��ûͨ��.myMac[" + myMac + "]" + " Mac[" + sData3
					+ "]");
			result.setRecode(1);
		}
		return result;
	}

	public synchronized int HsmGenarateRandom(int RandomLen, byte[] Rand)
			throws Exception {
		EMResult result = new EMResult();
		// String sData3=new String(DivData);
		result = this.HSM_R1(RandomLen);
		// KeysLen[0]=result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, Rand, 0,
				result.getLsData().length);

		return 0;
	}

	public synchronized int GenerateAndExportSDKey(int KeyVer, int KeyIndex,
			int AlgFlag, int DivNum, byte[] DivData, int[] KeysLen, byte[] Keys)
			throws Exception {
		EMResult result = new EMResult();
		String sData3 = UnionUtil.byte2hex(DivData);
		// String sData3=new String(DivData);
		// result =
		// this.HSM_U9(KeyVer,KeyIndex,AlgFlag,sData3.length()/32,DivNum,sData3,"ZMK");
		result = this.HSM_U9(KeyVer, KeyIndex, AlgFlag, 1, DivNum, sData3,
				"ZMK");
		KeysLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, Keys, 0,
				result.getLsData().length);

		return 0;
	}

	public synchronized int GenerateCheckValue(byte[] Keys, byte[] CheckValue)
			throws Exception {
		EMResult result = new EMResult();
		String sData3 = UnionUtil.byte2hex(Keys);
		// String sData3=new String(Keys);
		result = this.HSM_W4(2, sData3);
		System.arraycopy(result.getLsData(), 0, CheckValue, 0, result
				.getLsData().length);

		return 0;
	}

	public synchronized int HsmGenerateMAC(int KeyID, int KeyVer, int KeyIndex,
			int AlgFlag, int PadFlag, int DivNum, byte[] DivData,
			int SessionKeyFlag, byte[] SkeySeed, int DataLen, byte Data[],
			int MACDataLen, byte[] MACData) throws Exception {
		EMResult result = new EMResult();
		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);

		// String sData1=new String(DivData);
		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		result = this.HSM_W0(1, AlgFlag, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, PadFlag, "0000000000000000", DataLen,
				sData3, MACDataLen, "1234");

		System.arraycopy(result.getLsData(), 0, MACData, 0,
				result.getLsData().length);
		return 0;
	}

	public synchronized int HsmVerifyMAC(int KeyID, int KeyVer, int KeyIndex,
			int AlgFlag, int PadFlag, int DivNum, byte[] DivData,
			int SessionKeyFlag, byte[] SkeySeed, int DataLen, byte Data[],
			int MACDataLen, byte[] MACData) throws Exception {
		EMResult result = new EMResult();

		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);
		String sData4 = UnionUtil.byte2hex(MACData);
		/*
		 * String sData1=new String(DivData); String sData2=new
		 * String(SkeySeed); String sData3=new String(Data); String sData4=new
		 * String(MACData); }
		 */
		result = this.HSM_W0(2, AlgFlag, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, PadFlag, "0000000000000000", DataLen,
				sData3, MACDataLen, sData4);
		return 0;
	}

	public synchronized int HsmGenerateCMAC(int KeyID, int KeyVer,
			int KeyIndex, int AlgFlag, int PadFlag, int DivNum, byte[] DivData,
			int SessionKeyFlag, byte[] SkeySeed, byte[] IcvData, int DataLen,
			byte Data[], int MACDataLen, byte[] MACData, byte[] ICVResult)
			throws Exception {
		EMResult result = new EMResult();

		// String sData1=new String(DivData);
		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		// String sData4=new String(IcvData);
		//
		// String sData5=new String(MACData);
		// String sData6=new String(ICVResult);
		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);
		String sData4 = UnionUtil.byte2hex(IcvData);

		result = this.HSM_W0(3, AlgFlag, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, PadFlag, sData4, DataLen, sData3,
				MACDataLen, "1234");
		System.arraycopy(result.getLsData(), 0, MACData, 0,
				result.getLsData().length);
		System.arraycopy(result.getICV(), 0, ICVResult, 0,
				result.getICV().length);
		return 0;
	}

	public synchronized int HsmDataEncryptOrDecrypt(int KeyID, int KeyVer,
			int KeyIndex, int AlgFlag, int PadFlag, int DivNum, byte[] DivData,
			int SessionKeyFlag, byte[] SkeySeed, int DataLen, byte Data[],
			int[] CipheredDataLen, byte[] CipheredData) throws Exception {
		EMResult result = new EMResult();

		// String sData1=UnionUtil.Bytes2HexString(DivData);
		// String sData2=UnionUtil.Bytes2HexString(SkeySeed);
		// String sData3=UnionUtil.Bytes2HexString(Data);

		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);

		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		
		result = this.HSM_W2(AlgFlag, 0, "MK-SMI", null, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, 0, 0, null, 0, null,PadFlag, DataLen, sData3);

		CipheredDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, CipheredData, 0, result
				.getLsData().length);

		return 0;
	}
	
	/*
	 * add by lisq 2011-12-12 
	 */

	/** ��ָ������Կ�������ɼ���ɢ����ѡ���͹�����Կ���㣨��ѡ����ʹ��ָ�����㷨����䷽ʽ��
	 *  �������ݽ��м��ܲ�����
	 * @param KeyIndex ��Կ����
	 * @param AlgFlag �㷨��ʶ��AES��0x88;3DES-ECB��0x81�� 3DES-CBC�� 0x82��DES-CBC��0x84
	 * @param PadFlag ����ʶ��0���ⲿ��䣻1���ڲ����
	 * @param DivNum ��ɢ����
	 * @param DivData ��ɢ����
	 * @param SessionKeyFlag ������Կ��ʶ��0���޹�����Կ��1���й�����Կ
	 * @param SkeySeed ������Կ
	 * @param DataLen �������ݳ���
	 * @param Data ��������
	 * @param CipheredDataLen �������ݳ���
	 * @param CipheredData  ����
	 * @return
	 * @throws Exception
	 */
	public synchronized int HsmDataEncrypt(int KeyVer, int KeyIndex, int AlgFlag, int PadFlag,
			int OperateFlag, int DivNum, byte[] DivData,
			int SessionKeyFlag, byte[] SkeySeed, int DataLen, byte Data[],
			int[] CipheredDataLen, byte[] CipheredData) throws Exception {
		EMResult result = new EMResult();

		// String sData1=UnionUtil.Bytes2HexString(DivData);
		// String sData2=UnionUtil.Bytes2HexString(SkeySeed);
		// String sData3=UnionUtil.Bytes2HexString(Data);

		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);

		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		result = this.HSM_W2(AlgFlag, 0, "MK-SMI", null, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, 0, 0, null, 0, null, PadFlag, DataLen, sData3);

		CipheredDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, CipheredData, 0, result
				.getLsData().length);

		return 0;
	}
	
	/** ��ָ������Կ�������ɼ���ɢ����ѡ���͹�����Կ���㣨��ѡ����ʹ��ָ�����㷨����䷽ʽ��
	 *  �������ݽ��н��ܲ�����
	 * @param KeyIndex ��Կ����
	 * @param AlgFlag �㷨��ʶ��AES��0x88;3DES-ECB��0x81�� 3DES-CBC�� 0x82��DES-CBC��0x84
	 * @param PadFlag ����ʶ��0���ⲿ��䣻1���ڲ����
	 * @param DivNum ��ɢ����
	 * @param DivData ��ɢ����
	 * @param SessionKeyFlag ������Կ��ʶ��0���޹�����Կ��1���й�����Կ
	 * @param SkeySeed ������Կ
	 * @param DataLen �������ݳ���
	 * @param Data ��������
	 * @param CipheredDataLen �������ݳ���
	 * @param CipheredData  ����
	 * @return
	 * @throws Exception
	 */
	public synchronized int HsmDataDecrypt(int KeyLen, byte[] Key, int AlgFlag, int OperateFlag, 
			int PadFlag, int DivNum, byte[] DivData,	int SessionKeyFlag, byte[] SkeySeed,
			int DataLen, byte Data[], int[] OutputDataLen, byte[] OutputData) throws Exception {
		EMResult result = new EMResult();

		// String sData1=UnionUtil.Bytes2HexString(DivData);
		// String sData2=UnionUtil.Bytes2HexString(SkeySeed);
		// String sData3=UnionUtil.Bytes2HexString(Data);
		
		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);
		String mkKey = new String(Key);

		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		result = this.HSM_W2(AlgFlag, 1, "MK-SMI", mkKey, 0, DivNum, sData1,
				SessionKeyFlag, sData2, 0, 0, null, 0, null, PadFlag, DataLen, sData3);

		OutputDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, OutputData, 0, result
				.getLsData().length);

		return 0;
	}	
	
	/** ������Key1���ܵ�����ת��ΪKey2���ܣ��ýӿ���Ҫ���ڶ���Կ��ת����
	 * @param Key1Index ����Կ1������
	 * @param AlgFlag �㷨��ʶ��AES��0x88;3DES-ECB��0x81�� 3DES-CBC�� 0x82��DES-CBC��0x84
	 * @param Pad1Flag ����ʶ��0���ⲿ��䣻1���ڲ����
	 * @param Key1DivNum ����Կ1��ɢ����
	 * @param Key1DivData ����Կ1��ɢ����
	 * @param SessionKey1Flag ���̱�ʶ��0���޹�����Կ��1���й�����Կ
	 * @param Skey1Seed ������Կ
	 * @param Key2Index	����Կ2������
	 * @param Key2DivNum ����Կ2��ɢ����
	 * @param Key2DivData ����Կ2��ɢ����
	 * @param Session2KeyFlag ���̱�ʶ��0���޹�����Կ��1���й�����Կ
	 * @param Skey2Seed ������Կ
	 * @param inDataLen ����Կ1���ܵ��������ĳ���
	 * @param bInDataByKey1 ����Կ1���ܵ���������
	 * @param OutDataLen ����Կ2���ܵ��������ĳ���
	 * @param bOutDataByKey2 ����Կ2���ܵ���������
	 * @return
	 * @throws Exception
	 */
	public synchronized int HsmTranslateKey1ToKey2(int Key1ID, int Key1Ver, int Key1Index,
			int AlgFlag, int Pad1Flag, int Div1Num, byte[] Div1Data, int SessionKey1Flag,
			byte[] Skey1Seed, int Key2ID, int Key2Ver, int Key2Index, int Key2AlgFlag, int Div2Num,
			byte[] Div2Data, int Session2KeyFlag, byte[] Skey2Seed, int inDataLen,
			byte[] bInDataByKey1, int[] OutDataLen, byte[] bOutDataByKey2) throws Exception {
		EMResult result = new EMResult();

		// String sData1=UnionUtil.Bytes2HexString(DivData);
		// String sData2=UnionUtil.Bytes2HexString(SkeySeed);
		// String sData3=UnionUtil.Bytes2HexString(Data);

		String sDivData1 = UnionUtil.byte2hex(Div1Data);
		String sSkeySeed1 = UnionUtil.byte2hex(Skey1Seed);
		String sbInDataByKey1 = UnionUtil.byte2hex(bInDataByKey1);
		String sDivData2 = UnionUtil.byte2hex(Div2Data);
		String sSkeySeed2 = UnionUtil.byte2hex(Skey2Seed);

		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		result = this.HSM_W2(AlgFlag, 2, "MK-SMI", null, Key1Index, Div1Num, sDivData1,
				SessionKey1Flag, sSkeySeed1, Key1Index, Div1Num, sDivData1,
				SessionKey1Flag, sSkeySeed1, Pad1Flag, inDataLen, sbInDataByKey1);

		OutDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, bOutDataByKey2, 0, result
				.getLsData().length);

		return 0;
	}
	/*
	 * add by lisq 2011-12-12 end
	 */

	/* ���ܻ�����У��ֵ */
	/*
	 * public synchronized int GenerateCheckValue(byte[] Keys)throws Exception {
	 * EMResult result=new EMResult(); String sData3=new String(Keys); result =
	 * this.HSM_W3(sData3); System.arraycopy(result.getLsData(), 0, Keys,0,
	 * result.getLsData().length); return 0; }
	 */
	/* ����ܼ���У��ֵ */
	/*
	 * public synchronized int GenerateCheckValue2(byte[] Keys,byte[]
	 * CheckValue)throws Exception {
	 * 
	 * EMResult result=new EMResult(); String sData3=new String(Keys); String
	 * sData4=new String(CheckValue); result = this.HSM_W4(Keys);
	 * System.arraycopy(result.getLsData(), 0, Keys,0,
	 * result.getLsData().length); return 0; }
	 */

	/* �����̨ */
	public synchronized int HSMSha_1(int datalen, byte[] data, byte[] hash)
			throws Exception {
		EMResult result = new EMResult();
		String sData1 = UnionUtil.byte2hex(data);
		result = this.HSM_GM(datalen, sData1);
		System.arraycopy(result.getLsData(), 0, hash, 0,
				result.getLsData().length);

		return 0;
	}

	public synchronized int HsmMACGen(int KeyIndex, int KeyVer, int PadFlag,
			int DivNum, byte[] DivData, int SessionKeyFlag, byte[] SkeySeed,
			byte[] IcvData, int DataLen, byte Data[], int MACDataLen,
			byte[] MACData) throws Exception {
		EMResult result = new EMResult();

		// String sData1=new String(DivData);
		// String sData2=new String(SkeySeed);
		// String sData3=new String(Data);
		// String sData4=new String(IcvData);
		//
		// String sData5=new String(MACData);
		// String sData6=new String(ICVResult);
		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);
		String sData4 = UnionUtil.byte2hex(IcvData);
		int AlgFlag;

		if (SessionKeyFlag == 1) {
			AlgFlag = 1;
		} else {
			AlgFlag = 0;
		}

		result = this.HSM_U3(1, AlgFlag, KeyIndex, DivNum, sData1,
				SessionKeyFlag, sData2, PadFlag, sData4, DataLen, sData3,
				MACDataLen, "1234");
		System.arraycopy(result.getLsData(), 0, MACData, 0,
				result.getLsData().length);
		return 0;
	}

	public synchronized int HsmTACGen(int KeyIndex, int KeyVer, int PadFlag,
			int DivNum, byte[] DivData, int SessionKeyFlag, byte[] SkeySeed,
			byte[] IcvData, int DataLen, byte Data[], int MACDataLen,
			byte[] MACData) throws Exception {
		EMResult result = new EMResult();

		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);
		String sData4 = UnionUtil.byte2hex(IcvData);

		result = this.HSM_U3(1, 2, KeyIndex, DivNum, sData1, SessionKeyFlag,
				sData2, PadFlag, sData4, DataLen, sData3, MACDataLen, "1234");
		System.arraycopy(result.getLsData(), 0, MACData, 0,
				result.getLsData().length);
		return 0;
	}

	public synchronized int HsmDataEncryptDecrypt(int KeyVer, int KeyIndex,
			int DivNum, byte[] DivData, int SessionKeyFlag, byte[] SkeySeed,
			int Alg_Flag, int Alg_ID, byte[] IvData, int DataLen, byte Data[],
			int[] CipheredDataLen, byte[] CipheredData) throws Exception {
		EMResult result = new EMResult();

		// String sData1=UnionUtil.Bytes2HexString(DivData);
		// String sData2=UnionUtil.Bytes2HexString(SkeySeed);
		// String sData3=UnionUtil.Bytes2HexString(Data);

		String sData1 = UnionUtil.byte2hex(DivData);
		String sData2 = UnionUtil.byte2hex(SkeySeed);
		String sData3 = UnionUtil.byte2hex(Data);

		result = this.HSM_U1(Alg_Flag, Alg_ID, "MK-SMI", KeyIndex, DivNum,
				sData1, SessionKeyFlag, sData2, DataLen, sData3,
				"0000000000000000");

		CipheredDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, CipheredData, 0, result
				.getLsData().length);

		return 0;
	}

	public synchronized int HsmImportKey(int KeyVer, int KeyIndex,
			int MkDvsNum, byte[] MkDvsData, int CckVer, int CckIndex,
			int CckDvsNum, byte[] CckDvsData, int SessionKeyFlag,
			byte[] SkeySeed, int KeyHeaderLen, byte[] KeyHeader, byte[] IvData,
			int MacHeaderLen, byte[] MacHeader, int[] CipheredDataLen,
			byte[] CipheredData, byte[] Mac) throws Exception {
		EMResult result = new EMResult();
		// String sData1=UnionUtil.Bytes2HexString(DivData);

		String sData1 = UnionUtil.byte2hex(MkDvsData);
		String sData2 = UnionUtil.byte2hex(CckDvsData);
		String sData3 = UnionUtil.byte2hex(SkeySeed);
		String sData4 = UnionUtil.byte2hex(KeyHeader);
		String sData5 = UnionUtil.byte2hex(IvData);
		String sData6 = UnionUtil.byte2hex(MacHeader);

		result = this.HSM_U2('T', 1, 2, "MK-SMI", KeyIndex, MkDvsNum, sData1,
				1, CckIndex, CckDvsNum, sData2, SessionKeyFlag, sData3,
				"0000000000000000", KeyHeaderLen / 2, sData4, KeyHeaderLen / 2,
				sData5, MacHeaderLen / 2, sData6, MacHeaderLen / 2);

		System.arraycopy(result.getMac(), 0, Mac, 0, result.getMac().length);
		CipheredDataLen[0] = result.getLsDataLen();
		System.arraycopy(result.getLsData(), 0, CipheredData, 0, result
				.getLsData().length);

		return 0;
	}

	// add by changzx 2010 - 8 -27
	/*
	 * ����ָ������Ҫ���ɢ�õ�һ��������Ƭ����Կ���ñ�����Կ���ܣ�û����䣩,��������ԿУ��ֵ�����
	 */
	public synchronized int HsmGenerateMulKeyAndCheck(
			int AlgFlag,
			int SessionKeyFlag,// 0����ʹ�ù�����Կ 1��ʹ�ù�����Կ
			int EncKeyID, int EncKeyVer, int EncKeyIndex, int EncKeyDvsNum,
			byte[] EncKeyDvsData,
			byte[] Seed, // ��������
			int KeyNum, byte[] MulKeyID, byte[] MulKeyVer, int[] MulKeyIndex,
			int KeyDvsNum,
			/*
			 * String[] KeyDvsData, String[] MulKeys, String[] MulCheckValue
			 */
			byte[] KeyDvsData, byte[] MulKeys, byte[] MulCheckValue)
			throws Exception {// ����U2ָ��

		int cnt = KeyNum;
		int EncryptPadLen = 0, offset = 0, mulKeyOffset = 0, mulKeyCheValOffset = 0;
		EMResult result = new EMResult();
		int AlgMode = 2; // ���ܲ�����mac
		int Scheme = 2; // M/Chip4��CBCģʽ��ǿ�����X80�������������������ݣ���������ָ����CBCģʽ��ǿ�����0x80���������������ݣ�
		String KeyType = "ZEK";
		String DivData = UnionUtil.byte2hex(EncKeyDvsData);
		int CckType = 1;// ������Կ���� TK
		int CckDivNum = KeyDvsNum;
		int KeyIndex = EncKeyIndex;
		// String CckDivData= UnionUtil.byte2hex(KeyDvsData);
		String CckDivData = new String(KeyDvsData);
		String[] MulKeysStr = new String[cnt];
		for (int i = 0; i < cnt; i++) {
			MulKeysStr[i] = CckDivData.substring(offset, offset + 16);
			offset += 16;
		}
		String IV_CBC = "0000000000000000";

		char mechism = 'T';
		/*System.out.println("KeyDvsData.length =" + KeyDvsData.length
				+ "\nKeyDvsData" + new String(KeyDvsData));*/
		for (int i = 0; i < cnt; i++) {
			KeyIndex = MulKeyIndex[i];
			/*
			 * CckDivData = CckDivData.substring(offset,offset+16); offset +=
			 * 16;
			 */
			CckDivData = MulKeysStr[i];
			result = this.HSM_U2(mechism, AlgMode, Scheme, KeyType, KeyIndex,
					CckDivNum, CckDivData, CckType, EncKeyIndex, EncKeyDvsNum,
					DivData, SessionKeyFlag, Seed.toString(), IV_CBC,
					EncryptPadLen, null, 0, null, 0, null, 0);

			/*
			 * MulKeys[i] = new String(result.getLsData()); MulCheckValue[i] =
			 * new String(result.getICV());
			 */
			// System.arraycopy(src, srcPos, dest, destPos, length)
			System.arraycopy(result.getLsData(), 0, MulKeys, mulKeyOffset,
					result.getLsData().length);
			mulKeyOffset += result.getLsData().length;

			System.arraycopy(result.getICV(), 0, MulCheckValue,
					mulKeyCheValOffset, result.getICV().length);
			mulKeyCheValOffset += result.getICV().length;

		}

		return 0;
	}

	/*
	 * ������Կ���Ļ����ģ�
	 */

	public synchronized int HsmExportPK(int PKIndex, int PKType, int Encflag,
			int EncKeyVer, int EncKeyIndex, int PadDataLen, byte[] PadData,
			int[] PKDataLen, byte[] PKData, int[] PKCheckLen,
			byte[] PKCheckValue) throws Exception {
		EMResult res = new EMResult();
		byte[] tmpPk = new byte[1024 + 1];

		int alg_Flag = 1; // ����ģʽ 3DES-CBC ģʽ
		String KeyType = "MK-AC";
		// ����C7ָ���ȡ��Կ����
		String DivData2 = null;
		String SKeySeed2 = null;
		String tmpIkstrHex = null;
		String tmpIkstr = null;

		if (Encflag == 1) {
			res = this.HSM_C7(PKIndex, PKType);
			tmpPk = res.getPk();
			String tmpPkstr = new String(tmpPk);
			tmpIkstrHex = UnionUtil.Bytes2HexString(tmpPk);

			PKDataLen[0] = 256;
			System.arraycopy(tmpPk, 0, PKData, 0, tmpPk.length);
		} else if (Encflag == 2) {
			PKDataLen[0] = 16;
			tmpIkstr = "10001";
			tmpIkstrHex = "1000180000000000";
			System.arraycopy(tmpIkstr.getBytes(), 0, PKData, 0, tmpIkstr
					.getBytes().length);

		}
		// ����U1

		res = this.HSM_U1(3, 0x90,
				KeyType,
				// д�����㹫ԿMAC��������000����Կֵȫ0
				// EncKeyIndex,
				0, 0, DivData2, 0, SKeySeed2, tmpIkstrHex.length(),
				tmpIkstrHex, null);
		PKCheckLen[0] = 16;
		System.arraycopy(res.getLsData(), 0, PKCheckValue, 0, res
				.getLsDataLen());

		return 0;
	}

	/**/

	/********************************* <�ӿ�ʵ��End> **************************************/

	/********************************* <˽�з���Start> **************************************/

	private EMResult HSM_34(String vkIndex, int lenOfVK) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "34";
		cmdLen += 2;

		if ((vkIndex == null)
				|| ((lenOfVK != 256) && (lenOfVK != 512) && (lenOfVK != 1024)
						&& (lenOfVK != 2048) && (lenOfVK != 4096))) {
			throw new Exception("HSM_34,������");
		}

		cmdBuff += UnionUtil.LeftAddZero("" + lenOfVK, 4);
		cmdLen += 4;

		this.CheckValue(vkIndex.length(), 2, 2, "vkIndex��������Ϊ2");
		if (Integer.parseInt(vkIndex) != 99)
			this.CheckValue(Integer.parseInt(vkIndex), 0, 20,
					"vkIndex����Ϊ[1-20]������");

		cmdBuff += vkIndex;
		cmdLen += 2;

		outStr = commWithHsm(cmdLen, cmdBuff, "3500", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;

		int vklen = Integer.parseInt(outStr.substring(offset, offset + 4));
		logger.info("vklen=[" + vklen + "]");
		offset += 4;
		offset += vklen;
		logger.info("offset=[" + offset + "] outStrlen=[" + outStr.length()
				+ "]");
		OutData.setPk(UnionUtil.BytesCopy(outBytes, offset));

		return OutData;
	}

	private EMResult HSM_37(int flag, int vkIndex, int dataLen, String data)
			throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "37";
		cmdLen += 2;

		// this.CheckNull(flag,"HSM_37,����ʶ����Ϊ��!");
		// this.CheckLength(flag, 1, "vkIndex��������Ϊ1");
		// this.CheckNull(vkIndex,"HSM_37,vkIndex��������Ϊ��!");
		// this.CheckLength(vkIndex, 2, "vkIndex��������Ϊ2");

		// ��䷽ʽ

		cmdBuff += Integer.toString(flag);
		cmdLen++;

		this.CheckValue(vkIndex, 0, 20, "vkIndex����Ϊ[1-20]������");
		cmdBuff += Integer.toString(vkIndex);
		cmdLen += 2;

		// ���ݳ���
		cmdBuff += UnionUtil.LeftAddZero("" + dataLen, 4);
		cmdLen += 4;

		// ����
		cmdBuff += data;
		cmdLen += data.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "3800", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		int signlen = Integer.parseInt(outStr.substring(offset, offset + 4));
		offset += 4;

		OutData.setSignature(UnionUtil.BytesCopy(outBytes, offset, signlen));

		return OutData;
	}

	private EMResult HSM_38(int flag, int signLen, String signature,
			int dataLen, String data, String pk) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "38";
		cmdLen += 2;

		// this.CheckNull(flag,"HSM_38,����ʶ����Ϊ��!");
		// this.CheckLength(flag, 1, "HSM_38,����ʶ����Ϊ1");

		// ��䷽ʽ
		cmdBuff += Integer.toString(flag);
		cmdLen++;

		// ǩ������
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(signLen), 4);
		cmdLen += 4;

		// ǩ��
		cmdBuff += signature;
		cmdLen += signature.length();
		logger.info("HSM_38 signature.length()=[" + signature.length() + "]");
		// �ָ���
		cmdBuff += ";";
		cmdLen += 1;

		// ���ݳ���
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(dataLen), 4);
		cmdLen += 4;

		// �ָ��
		cmdBuff += data;
		cmdLen += data.length();

		cmdBuff += ";";
		cmdLen += 1;

		// ��Կ
		cmdBuff += pk;
		cmdLen += pk.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "3900", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		return OutData;
	}

	private EMResult HSM_CC(int zpk1Length, String zpk1, int zpk2Length,
			String zpk2, String pinBlockByZPK1, String pinFormat1,
			String pinFormat2, String accNo1, int lenOfAccNo1, String accNo2,
			int lenOfAccNo2) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "CC";
		cmdLen += 2;

		this.CheckNull(zpk1, "HSM_CC,zpk1����Ϊ��!");
		this.CheckNull(zpk2, "HSM_CC,zpk2����Ϊ��!");
		this.CheckNull(pinFormat1, "HSM_CC,pinFormat1����Ϊ��!");
		this.CheckNull(pinBlockByZPK1, "HSM_CC,pinBlockByZPK1����Ϊ��!");
		this.CheckNull(pinFormat2, "HSM_CC,pinFormat2����Ϊ��!");
		this.CheckNull(accNo1, "HSM_CC,accNo1����Ϊ��!");
		this.CheckNull(accNo2, "HSM_CC,accNo2����Ϊ��!");

		// ԴZPK1
		String keyStr = getRacalKeyString(zpk1Length, zpk1);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		// Ŀ��ZPK2
		keyStr = getRacalKeyString(zpk2Length, zpk2);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		// ������볤��
		cmdBuff += "12";
		cmdLen += 2;

		cmdBuff += pinBlockByZPK1;
		cmdLen += pinBlockByZPK1.length();

		// ԴPIN block��ʽ
		cmdBuff += pinFormat1;
		cmdLen += pinFormat1.length();

		// Ŀ��PIN block��ʽ
		cmdBuff += pinFormat2;
		cmdLen += pinFormat2.length();

		// �ʺŻ򿨺�
		cmdBuff += get12LenAccountNumber(lenOfAccNo1, accNo1);
		cmdLen += 12;

		// Ŀ���ʺŻ򿨺�
		if (pinFormat2.equals("10")) {
			cmdBuff += get12LenAccountNumber(lenOfAccNo2, accNo2);
			cmdLen += 12;
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "CD00", OutData);
		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		offset += 2; // ���볤��

		String destPinBlockFrm = outStr.substring(outStr.length() - 2);
		int pinBlk2len = 16;
		if (destPinBlockFrm.equals("09"))
			pinBlk2len = 40;
		// Ŀ��PIN block

		OutData.setPinBlockByZPK2(UnionUtil.BytesCopy(outBytes, offset,
				pinBlk2len));
		return OutData;
	}

	private EMResult HSM_JG(int zpkLength, String zpk, String pinFormat,
			String accNo, int lenOfAccNo, String pinByLmk) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "JG";
		cmdLen += 2;

		this.CheckNull(zpk, "HSM_JG,zpk����Ϊ��!");
		this.CheckNull(pinFormat, "HSM_JG,pinFormat����Ϊ��!");
		this.CheckNull(accNo, "HSM_JG,accNo����Ϊ��!");
		this.CheckNull(pinByLmk, "HSM_JG,pinByLmk����Ϊ��!");

		// ZPK
		String keyStr = getRacalKeyString(zpkLength, zpk);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		// PIN���ʽ
		cmdBuff += pinFormat;
		cmdLen += pinFormat.length();

		// �ʺŻ򿨺�
		cmdBuff += get12LenAccountNumber(lenOfAccNo, accNo);
		cmdLen += 12;

		// pin
		cmdBuff += pinByLmk;
		cmdLen += pinByLmk.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "JH00", OutData);
		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		// Ŀ��PIN block

		OutData.setPinBlockByZPK(UnionUtil.BytesCopy(outBytes, offset, 16));
		return OutData;
	}

	private EMResult HSM_GI(String encyFlag, String padMode, String lmkType,
			int keyLength, int lenOfDesKeyByPK, String desKeyByPK,
			String vkIndex) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "GI";
		cmdLen += 2;

		cmdBuff += encyFlag;
		cmdLen += 2;

		cmdBuff += padMode;
		cmdLen += 2;

		cmdBuff += lmkType;
		cmdLen += 4;

		this.CheckValue(lenOfDesKeyByPK, 1, 9999,
				"lenOfDesKeyByPK���ݳ���Ϊ[1-9999]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(lenOfDesKeyByPK)
				.toUpperCase(), 4);
		cmdLen += 4;

		cmdBuff += desKeyByPK;
		cmdLen += lenOfDesKeyByPK;
		// �ָ���
		cmdBuff += ";";
		cmdLen++;
		// ˽Կ����

		this
				.CheckValue(Integer.parseInt(vkIndex), 0, 20,
						"vkIndex����Ϊ[1-20]������");
		cmdBuff += vkIndex;
		cmdLen += 2;

		/*
		 * // �ָ��� cmdBuff += ";"; cmdLen++;
		 * 
		 * // ZMK���ܵ���Կ���ĳ��ȱ�־ cmdBuff += getKeyXYZScheme(keyLength); cmdLen++;
		 * 
		 * cmdBuff += getKeyXYZScheme(keyLength); cmdLen++;
		 */

		outStr = commWithHsm(cmdLen, cmdBuff, "GJ00", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		byte[] initValue = UnionUtil.BytesCopy(outBytes, offset, 16);
		logger.info("HSM_GI::initValueStr=["
				+ (new String(initValue, "ISO-8859-1")) + "]");
		offset += 16;

		byte[] desKeyByLMK = getKeyBytesFromBValWithXYZ(outBytes, offset);
		if (desKeyByLMK.length != keyLength) {
			// DES��Կ��LMK�� 16H��32H��1A+32H��1A+48H ��DES��Կ����ָ����LMK���¼��ܵ�DES��Կ��
			desKeyByLMK = UnionUtil.BytesCopy(outBytes, offset, keyLength);
		}
		OutData.setData(desKeyByLMK);
		if (outBytes[offset] == 'X' || outBytes[offset] == 'Y'
				|| outBytes[offset] == 'Z') {
			offset += 1;
		}
		logger.info("HSM_GI::desKeyByLMKStr=["
				+ (new String(desKeyByLMK, "ISO-8859-1")) + "]");

		offset += desKeyByLMK.length;
		byte[] checkValue = UnionUtil.BytesCopy(outBytes, offset, 16);
		OutData.setMac(checkValue);
		logger.info("HSM_GI::checkValueStr=["
				+ (new String(checkValue, "ISO-8859-1")) + "]");
		return OutData;

	}

	private EMResult HSM_H3(String vkIndex, String fillType, String zpk,
			String pan, String desKeyByPK) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "H3";
		cmdLen += 2;

		this.CheckLength(vkIndex, 2, "�������ݳ���Ϊ2");
		this.CheckValue(Integer.parseInt(vkIndex), 0, 20, "������Ϊ[1-20]������");
		cmdBuff += vkIndex;
		cmdLen += 2;

		cmdBuff += fillType;
		cmdLen += 1;

		String keyStr = getRacalKeyString(zpk.length(), zpk);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		this.CheckValue(pan.length(), 4, 20, "pan���ݳ���Ϊ[1-20]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(pan.length())
				.toUpperCase(), 2);
		cmdLen += 2;

		cmdBuff += pan;
		cmdLen += pan.length();

		cmdBuff += desKeyByPK;
		cmdLen += desKeyByPK.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "H400", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		offset += 2; // pin���ĵĳ���
		byte[] pinByZpk = UnionUtil.BytesCopy(outBytes, offset, 32);
		logger.info("HSM_H3::pinByZpkStr=["
				+ (new String(pinByZpk, "ISO-8859-1")) + "]");
		OutData.setPinBlockByZPK2(pinByZpk);

		return OutData;

	}

	private EMResult HSM_H5(String srctype, String zpk1, String pan,
			String destype, String destKey, String pin) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "H5";
		cmdLen += 2;

		// ԴKEY����
		cmdBuff += srctype;
		cmdLen += 1;

		// ԴZPK1/PVK1
		String keyStr = getRacalKeyString(zpk1.length(), zpk1);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		// �ʺų���
		this.CheckValue(pan.length(), 4, 20, "pan���ݳ���Ϊ[1-20]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(pan.length())
				.toUpperCase(), 2);
		cmdLen += 2;

		// �ʺţ�PAN��
		cmdBuff += pan;
		cmdLen += pan.length();

		// Ŀ��KEY����
		cmdBuff += destype;
		cmdLen += 1;

		// Ŀ��ZPK��/PVK
		keyStr = getRacalKeyString(destKey.length(), destKey);
		cmdBuff += keyStr;
		cmdLen += keyStr.length();

		// PIN����
		cmdBuff += pin;
		cmdLen += pin.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "H600", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;
		byte[] pinByZpk = UnionUtil.BytesCopy(outBytes, offset, 32);
		logger.info("HSM_H3::pinByZpkStr=["
				+ (new String(pinByZpk, "ISO-8859-1")) + "]");
		OutData.setPinBlockByZPK2(pinByZpk);

		return OutData;

	}

	private EMResult HSM_U5(int Alg_flag, String keyType) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		int cmdLen = 0;
		int offset = 0;

		cmdBuff = "U5";
		cmdLen += 2;

		keyLenType = getXYZFlagFromMobRule(Alg_flag);

		cmdBuff += keyLenType;
		cmdLen += 1;

		keyScheme = getKeyScheme(keyType);
		cmdBuff += keyScheme;
		cmdLen += 3;

		outStr = commWithHsm(cmdLen, cmdBuff, "U600", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;
		OutData.setMac(UnionUtil.BytesCopy(outBytes, outBytes.length - 16));

		switch (Alg_flag) {
		case 0x01:
			OutData.setKEK(UnionUtil.BytesCopy(outBytes, offset, 16));
			offset += 16;
			break;
		case 0x02:
			OutData.setKEK(UnionUtil.BytesCopy(outBytes, offset, 32));
			offset += 32;
			break;
		case 0x03:
			OutData.setKEK(UnionUtil.BytesCopy(outBytes, offset, 48));
			offset += 48;
			break;
		default:
			throw new Exception("KEY���Ȳ��ڹ涨��Χ��");
		}
		OutData.setLmkKek(getKeyBytesFromBValWithXYZ(outBytes, offset));

		return OutData;
	}

	private EMResult HSM_U9(int KeyVer, int KeyIndex, int AlgFlag, int KeyNum,
			int DivNum, String DivData, String keyType) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "U9";
		cmdLen += 2;

		// Alg flag
		keyLenType = getLSFlagFromMobRule(AlgFlag);
		cmdBuff += keyLenType;
		cmdLen += 1;

		// key type
		keyScheme = getKeyScheme(keyType);
		cmdBuff += keyScheme;
		cmdLen += 3;

		// root-key
		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(KeyIndex), 3);
		cmdLen += 3;

		// �̶�������Կ������
		cmdBuff += KeyNum;
		cmdLen += 1;
		// cmdBuff+=Integer.toString(DivData.length()/32);
		// cmdLen+=1;

		cmdBuff += Integer.toString(DivNum);
		cmdLen += 1;
		// Div data
		int i;
		for (i = 1; i <= KeyNum; i++) {
			cmdBuff += DivData;
		}
		// cmdLen +=32*DivNum;
		cmdLen += 32 * DivNum * KeyNum;

		outStr = commWithHsm(cmdLen, cmdBuff, "UA00", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;

		switch (KeyNum) {
		case 1:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 32));
			offset += 32;
			break;
		case 2:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 64));
			offset += 64;
			break;
		case 3:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 96));
			offset += 96;
			break;
		// ʡ�Ե�4-9
		default:
			throw new Exception("������Կ�������ڹ涨��Χ��");
		}

		return OutData;
	}

	private EMResult HSM_W4(int AlgFlag, String Keys) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "W4";
		cmdLen += 2;

		// key type
		keyLenType = getXYZFlagFromMobRule(AlgFlag);
		cmdBuff += keyLenType;
		cmdLen += 1;

		// root-key
		cmdBuff += Keys;
		cmdLen += 32;

		outStr = commWithHsm(cmdLen, cmdBuff, "W508", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;

		switch (AlgFlag) {
		case 1:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 16));
			offset += 16;
			break;
		case 2:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 16));
			offset += 16;
			break;
		case 3:
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 16));
			offset += 16;
			break;
		// ʡ�Ե�4-9
		default:
			throw new Exception("������Կ�������ڹ涨��Χ��");
		}

		return OutData;
	}

	// �����̨

	private EMResult HSM_GM(int datalen, String data) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "GM01";
		cmdLen += 4;

		// rand number's bytes
		// String tempcmd=new String(Integer.toHexString(RandBytes));
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(datalen), 5);
		// cmdBuff+=tempcmd;
		cmdLen += 5;
		cmdBuff += data;
		cmdLen += datalen;

		outStr = commWithHsm(cmdLen, cmdBuff, "GN00", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;

		OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, datalen));
		offset += datalen;

		return OutData;
	}

	private EMResult HSM_U3(int AlgFlag, int Scheme_2, int KeyIndex,
			int DivNum, String DivData, int processFlag, String processData,
			int macFillType, String IV, int macDataLen, String macData,
			int macSelfLenFlag, String srcMAC) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "U3";
		cmdLen += 2;

		cmdBuff += Integer.toHexString(AlgFlag);
		cmdLen += 1;

		// Scheme_2
		// 0:��׼����3DES-MAC;1:ʹ�ù�����Կ����DESMAC;2:����TAC
		keyLenType = getMACFlagFromMobRule_Center(Scheme_2);
		cmdBuff += keyLenType;
		cmdLen += 1;

		// root-key
		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(KeyIndex), 3);
		cmdLen += 3;

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(DivNum);
		cmdLen += 1;
		if (DivNum >= 1) {
			cmdBuff += DivData;
			cmdLen += 16 * DivNum;

		}

		// process flag
		// cmdBuff +=Integer.toString(processFlag);
		// cmdLen+=1;
		if (processFlag == 1) {
			// process data
			cmdBuff += processData;
			cmdLen += processData.length();
		}

		// macFillType
		cmdBuff += Integer.toString(macFillType);
		cmdLen += 1;

		// IV
		cmdBuff += IV;
		cmdLen += IV.length();

		this.CheckValue(macDataLen, 1, 999, "MAC���ݳ���Ϊ[1-999]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(macDataLen / 2), 3);
		cmdLen += 3;

		// macData
		cmdBuff += macData;
		cmdLen += macData.length();

		// macSelfLenFlag
		cmdBuff += Integer.toString(macSelfLenFlag);
		cmdLen += 1;

		// source MAC
		if (AlgFlag == 2) {
			// process srcMAC
			cmdBuff += srcMAC;
			cmdLen += srcMAC.length();
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "U400", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;
		if (AlgFlag != 2) {
			switch (macSelfLenFlag) {
			case 1:
				OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 8));
				offset += 8;
				break;
			case 2:
				OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 16));
				offset += 16;
				break;

			}
		}

		return OutData;
	}

	private EMResult HSM_U1(int AlgFlag, int Alg_ID,
			// int Scheme,
			String KeyType, int KeyIndex, int DivNum, String DivData,
			int processFlag, String processData,
			// int dataFillType,
			int dataLen, String data, String IV) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "U1";
		cmdLen += 2;

		// Scheme
		// keyLenType = getLSFlagFromMobRule(AlgFlag);
		// cmdBuff+=keyLenType;
		// cmdLen+=1;

		if (AlgFlag == 1) {
			/* ���� */
			// 3DES-ECB
			if (Alg_ID == 0x81) {
				cmdBuff += Integer.toString(0);
				cmdLen += 1;
				cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
				cmdLen += 2;

			}
			// 3DES-CBC
			if (Alg_ID == 0x82) {
				cmdBuff += Integer.toString(0);
				cmdLen += 1;
				cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
				cmdLen += 2;

			}
			// DES-ECB
			if (Alg_ID == 0x83) {
				if (processFlag == 1) {
					cmdBuff += Integer.toString(6);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
					cmdLen += 2;
				} else {
					cmdBuff += Integer.toString(4);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
					cmdLen += 2;
				}
			}
			// DES-CBC
			if (Alg_ID == 0x84) {
				if (processFlag == 1) {
					cmdBuff += Integer.toString(6);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
					cmdLen += 2;
				} else {
					cmdBuff += Integer.toString(4);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
					cmdLen += 2;
				}

			}

		} else {
			// 3DES-ECB
			if (Alg_ID == 0x81) {
				cmdBuff += Integer.toString(1);
				cmdLen += 1;
				cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
				cmdLen += 2;

			}
			// 3DES-CBC
			if (Alg_ID == 0x82) {
				cmdBuff += Integer.toString(1);
				cmdLen += 1;
				cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
				cmdLen += 2;

			}
			// DES-ECB
			if (Alg_ID == 0x83) {
				if (processFlag == 1) {
					cmdBuff += Integer.toString(6);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
					cmdLen += 2;
				} else {
					cmdBuff += Integer.toString(5);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(1), 2);
					cmdLen += 2;
				}

			}
			// DES-CBC
			if (Alg_ID == 0x84) {
				if (processFlag == 1) {
					cmdBuff += Integer.toString(6);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
					cmdLen += 2;
				} else {
					cmdBuff += Integer.toString(5);
					cmdLen += 1;
					cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
					cmdLen += 2;
				}

			} else {
				cmdBuff += Integer.toString(3);
				cmdLen += 1;
				cmdBuff += UnionUtil.LeftAddZero(Integer.toString(2), 2);
				cmdLen += 2;
			}

		}
		// cmdBuff+=Integer.toString(AlgFlag);
		// cmdLen+=1;

		// cmdBuff+=Integer.toString(Scheme);
		// cmdLen+=1;

		// key type
		// keyScheme=getKeyScheme(KeyType);
		cmdBuff += "109";
		cmdLen += 3;
		// cmdBuff+=KeyType;
		// cmdLen+=3;

		// root-key
		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(KeyIndex), 3);
		cmdLen += 3;

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(DivNum);
		cmdLen += 1;
		if (DivNum >= 1) {
			cmdBuff += DivData;
			cmdLen += 16 * DivNum;

		}

		/*
		 * cmdBuff +=Integer.toString(DivNum); cmdLen+=1;
		 * 
		 * //Div data cmdBuff+=DivData; cmdLen +=32*DivNum;
		 */
		// process flag
		// cmdBuff +=Integer.toString(processFlag);
		// cmdLen+=1;

		if (processFlag == 1) {
			// process data
			cmdBuff += processData;
			cmdLen += processData.length();
		}

		//
		// dataFillType
		// cmdBuff +=Integer.toString(dataFillType);
		// cmdLen+=1;
		if (AlgFlag == 1) {
			cmdBuff += "01";
			cmdLen += 2;
		} else if (AlgFlag == 3) {
			cmdBuff += "02";
			cmdLen += 2;
		}
		// DataLen
		// cmdBuff +=UnionUtil.LeftAddZero(Integer.toString(dataLen),3);
		// cmdLen+=3;
		if (keyLenType == "1" || keyLenType == "2") {
			cmdBuff += UnionUtil.LeftAddZero(Integer
					.toString((dataLen / 2) + 16), 3);
			cmdLen += 3;
		} else {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toString(dataLen / 2), 3);
			cmdLen += 3;
		}

		if (keyLenType == "1" || keyLenType == "2") {
			// IV
			cmdBuff += IV;
			cmdLen += IV.length();
		}

		// Data
		cmdBuff += data;
		cmdLen += data.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "U200", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");

		offset = 4;
		byte[] bytesLen = UnionUtil.BytesCopy(outBytes, offset, 3);
		int iLen = Integer.parseInt(new String(bytesLen)) * 2;
		offset += 3;

		if (AlgFlag == 1) {
			OutData.setLen(iLen);
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, iLen));
			offset += iLen;
		} else if (AlgFlag == 3) {
			OutData.setLen(iLen * 2);
			OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, iLen * 2));
			offset += iLen * 2;
		}

		return OutData;
	}

	private EMResult HSM_U2(char mechism, // ��ȫ����
			int AlgMode, // ģʽ��־ 0-������ 1-���ܲ�����MAC 2-���ܲ�������ԿУ��ֵ
			int Scheme, // ����ID 0
			String KeyType, // ����Կ����
			int KeyIndex, // ����Կ������
			int DivNum, // ��ɢ����
			String DivData, // ��ɢ����
			int CckType, // ������Կ����
			int CckIndex, // ������Կ����
			int CckDivNum, // ������Կ��ɢ����
			String CckDivData, // ��ɢ����
			int processFlag, // ������Կ��ʶ
			String processData, // ��������
			String IV_CBC, // IV ���� scheme 0 ,3ʱ��
			int EncryptPadLen, // scheme Ϊ 2��3ʱ�У��������
			String EncryptPad, int EncryptPadOffset, String IV_MAC, // algMode
			// Ϊ1ʱ��
			int MacPadLen, // MAC ������ݳ���
			String MacPad, int MacPadOffset) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "U2";
		cmdLen += 2;
		/*
		 * if(processFlag==1) { cmdBuff +="S"; cmdLen+=1; } else { cmdBuff
		 * +="T"; cmdLen+=1; }
		 */
		cmdBuff += mechism;
		cmdLen += 1;

		cmdBuff += Integer.toString(AlgMode);
		cmdLen += 1;

		cmdBuff += Integer.toString(Scheme);
		cmdLen += 1;
		// cmdBuff +=Scheme;
		// cmdLen +=1;

		// keyScheme=getKeyScheme(KeyType);
		cmdBuff += "109";
		cmdLen += 3;

		// root-key
		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(KeyIndex), 3);
		cmdLen += 3;

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(DivNum);
		// cmdBuff +=DivNum;
		cmdLen += 1;
		if (DivNum >= 1) {
			cmdBuff += DivData;
			cmdLen += 16 * DivNum;
		}

		// cck-key

		cmdBuff += Integer.toString(CckType);
		cmdLen += 1;

		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(CckIndex), 3);
		cmdLen += 3;

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(CckDivNum);
		// cmdBuff +=CckDivNum;
		cmdLen += 1;
		if (CckDivNum >= 1) {
			cmdBuff += CckDivData;
			cmdLen += 16 * CckDivNum;
		}

		// process flag
		if (processFlag == 1 && mechism != 'T') {
			// process data
			cmdBuff += "Y";
			cmdLen += 1;
			cmdBuff += processData;
			cmdLen += processData.length();
		}

		// ģʽ��ʶint AlgMode,
		// ����IDint Scheme,
		if (Scheme == 0 || Scheme == 3) {
			cmdBuff += IV_CBC;
			cmdLen += IV_CBC.length();
		}

		if (Scheme == 2 || Scheme == 3) {
			cmdBuff += UnionUtil.LeftAddZero(
					Integer.toHexString(EncryptPadLen), 4);
			cmdLen += 4;

			// cmdBuff +=EncryptPad;
			// cmdLen +=EncryptPadLen*2;

			cmdBuff += UnionUtil.LeftAddZero(Integer
					.toHexString(EncryptPadOffset), 4);
			cmdLen += 4;
		}

		if (AlgMode == 1) {
			cmdBuff += IV_MAC;
			cmdLen += IV_MAC.length();

			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MacPadLen), 4);
			cmdLen += 4;

			cmdBuff += MacPad;
			cmdLen += MacPadLen * 2;

			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MacPadOffset),
					4);
			cmdLen += 4;
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "U300", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;

		if (AlgMode == 1) {
			OutData.setMac(UnionUtil.BytesCopy(outBytes, offset, 16));
			offset += 16;
		}

		byte[] bytesLen = UnionUtil.BytesCopy(outBytes, offset, 4);
		int iLen = Integer.parseInt(new String(bytesLen), 16) * 2;
		// int iLen=48;
		offset += 4;

		OutData.setLen(iLen);
		OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, iLen));
		offset += iLen;

		if (AlgMode == 2)// У��ֵ
		{

			OutData.setICV(UnionUtil.BytesCopy(outBytes, offset, 16));

		}
		/*
		 * OutData.setKEK(UnionUtil.BytesCopy(outBytes,offset+16,4)); offset +=
		 * 4;
		 * 
		 * OutData.setLsData(UnionUtil.BytesCopy(outBytes,offset+16+4,48));
		 * offset += 48;
		 */

		return OutData;
	}

	private EMResult HSM_R1(int RandBytes) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "R1";
		cmdLen += 2;

		// rand number's bytes
		// String tempcmd=new String(Integer.toHexString(RandBytes));
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(RandBytes), 5);
		// cmdBuff+=tempcmd;
		cmdLen += 5;

		outStr = commWithHsm(cmdLen, cmdBuff, "R200", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;

		OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, RandBytes));
		offset += RandBytes;

		return OutData;
	}

	private EMResult HSM_W0(int AlgFlag, int Scheme,
			int KeyIndex,
			// int KeyNum,
			int DivNum, String DivData, int processFlag, String processData,
			int macFillType, String IV, int macDataLen, String macData,
			int macSelfLenFlag, String srcMAC) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "W0";
		cmdLen += 2;

		cmdBuff += Integer.toHexString(AlgFlag);
		cmdLen += 1;

		// Scheme
		keyLenType = getMACFlagFromMobRule(Scheme);
		cmdBuff += keyLenType;
		cmdLen += 1;

		// root-key
		cmdBuff += "K";
		cmdLen += 1;

		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(KeyIndex), 3);
		cmdLen += 3;

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(DivNum);
		cmdLen += 1;
		if (DivNum >= 1) {
			cmdBuff += DivData;
			cmdLen += 32 * DivNum;

		}

		/*
		 * cmdBuff +=Integer.toString(DivNum); cmdLen+=1;
		 * 
		 * //Div data cmdBuff+=DivData; cmdLen +=32*DivNum;
		 */
		// process flag
		cmdBuff += Integer.toString(processFlag);
		cmdLen += 1;
		if (processFlag == 1) {
			// process data
			cmdBuff += processData;
			cmdLen += processData.length();
		}
		//
		// macFillType
		cmdBuff += Integer.toString(macFillType);
		cmdLen += 1;

		// IV
		cmdBuff += IV;
		cmdLen += IV.length();

		// macDataLen
		// cmdBuff
		// +=UnionUtil.LeftAddZero(Integer.toHexString(macDataLen*2),4).toUpperCase();
		cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(macData.length()),
				4).toUpperCase();
		cmdLen += 4;
		System.out.println("aaaa111111=" + macDataLen);
		System.out.println("aaaa=" + Integer.toHexString(macDataLen));
		System.out.println("aaaacccc="
				+ UnionUtil.LeftAddZero(Integer.toHexString(macDataLen), 4)
						.toUpperCase());

		// macData
		cmdBuff += macData;
		cmdLen += macData.length();

		// macSelfLenFlag
		cmdBuff += Integer.toString(macSelfLenFlag);
		cmdLen += 1;

		// source MAC
		if (AlgFlag == 2) {
			// process srcMAC
			cmdBuff += srcMAC;
			cmdLen += srcMAC.length();
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "W100", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;
		if (AlgFlag != 2) {
			switch (macSelfLenFlag) {
			case 1:
				OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 8));
				offset += 8;
				break;
			case 2:
				OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, 16));
				offset += 16;
				break;

			}
		}

		if (AlgFlag == 3) {
			switch (macSelfLenFlag) {
			case 1:
				OutData.setICV(UnionUtil.BytesCopy(outBytes, offset, 8));
				offset += 8;
				break;
			case 2:
				OutData.setICV(UnionUtil.BytesCopy(outBytes, offset, 16));
				offset += 16;
				break;

			}
		}

		return OutData;
	}

	// result = this.HSM_W2(AlgFlag,OperateFlag,"ZMK",
	// KeyIndex,DivNum,sData1,SessionKeyFlag,sData2,PadFlag,DataLen,sData3);
	private EMResult HSM_W2(int AlgFlag, int Scheme, String KeyType, String mkKey, int Key1Index, 
			int Div1Num, String Div1Data, int process1Flag, String process1Data, int Key2Index,
			int Div2Num, String Div2Data, int process2Flag, String process2Data, int dataFillType,
			int dataLen, String data)throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;
		String keyScheme = null;
		String Lscheme2 = null;
		int cmdLen = 0;
		int offset = 0;

		// command
		cmdBuff = "W2";
		cmdLen += 2;

		// Scheme
		keyLenType = getLSFlagFromMobRule(AlgFlag);
		cmdBuff += keyLenType;
		cmdLen += 1;

		cmdBuff += Integer.toString(Scheme);
		cmdLen += 1;

		// key type
		keyScheme = getKeyScheme(KeyType);
		cmdBuff += keyScheme;
		cmdLen += 3;
		// cmdBuff+=Integer.toString(KeyType);
		// cmdLen+=3;

		// root-key
		if(mkKey != null && mkKey.length() != 0){
			cmdBuff += getKeyXYZScheme(mkKey.length()) + mkKey;
			cmdLen += mkKey.length() + 1;
		}
		else{
			cmdBuff += "K";
			cmdLen += 1;

			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(Key1Index), 3);
			cmdLen += 3;
		}
		

		// ������ɢ����Ϊ0
		cmdBuff += Integer.toString(Div1Num);
		cmdLen += 1;
		if (Div1Num >= 1) {
			cmdBuff += Div1Data;
			cmdLen += 32 * Div1Num;

		}

		/*
		 * cmdBuff +=Integer.toString(DivNum); cmdLen+=1;
		 * 
		 * //Div data cmdBuff+=DivData; cmdLen +=32*DivNum;
		 */
		// process flag
		cmdBuff += Integer.toString(process1Flag);
		cmdLen += 1;

		if (process1Flag == 1) {
			// process data
			cmdBuff += process1Data;
			cmdLen += process1Data.length();
		}
		// add by lisq 2011-12-12
		if(Scheme == 2){
			// root-key
			cmdBuff += "K";
			cmdLen += 1;

			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(Key2Index), 3);
			cmdLen += 3;

			// ������ɢ����Ϊ0
			cmdBuff += Integer.toString(Div2Num);
			cmdLen += 1;
			if (Div2Num >= 1) {
				cmdBuff += Div2Data;
				cmdLen += 32 * Div2Num;

			}
			
			// process flag
			cmdBuff += Integer.toString(process2Flag);
			cmdLen += 1;

			if (process2Flag == 1) {
				// process data
				cmdBuff += process2Data;
				cmdLen += process2Data.length();
			}
		}
		//add by lisq 2011-12-12 end
		// dataFillType
		cmdBuff += Integer.toString(dataFillType);
		cmdLen += 1;

		// DataLen
		//cmdBuff +=UnionUtil.LeftAddZero(Integer.toString(dataLen),3);
		cmdBuff += String.format("%03d", dataLen);
		cmdLen+=3;
		/*
		if (keyLenType == "1" || keyLenType == "2") {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toString(dataLen + 16), 3);
			cmdLen += 3;
		} else {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toString(dataLen), 3);
			cmdLen += 3;
		}
		*/
	
		// Data
		cmdBuff += data;
		cmdLen += data.length();

		outStr = commWithHsm(cmdLen, cmdBuff, "W300", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");

		offset = 4;
		byte[] bytesLen = UnionUtil.BytesCopy(outBytes, offset, 3);
		int iLen = Integer.parseInt(new String(bytesLen));
		offset += 3;

		OutData.setLen(iLen);
		OutData.setLsData(UnionUtil.BytesCopy(outBytes, offset, iLen));
		offset += iLen;

		return OutData;
	}

	private EMResult HSM_W3(String Kyes) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String keyLenType = null;

		int cmdLen = 0;
		int offset = 0;

		cmdBuff = "W3X";
		cmdLen += 3;

		cmdBuff += Kyes;
		cmdLen += 32;

		/*
		 * keyLenType = getXYZFlagFromMobRule(Alg_flag);
		 * 
		 * cmdBuff+=keyLenType; cmdLen+=1;
		 * 
		 * 
		 * switch(Alg_flag) { case 0x01: cmdBuff+=Kyes; cmdLen +=16; break; case
		 * 0x02: cmdBuff+=Kyes; cmdLen +=32; break; case 0x03: cmdBuff+=Kyes;
		 * cmdLen +=48; break; default: throw new Exception("KEY���Ȳ��ڹ涨��Χ��"); }
		 */

		outStr = commWithHsm(cmdLen, cmdBuff, "W400", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		offset = 4;
		OutData.setMac(UnionUtil.BytesCopy(outBytes, outBytes.length - 16));

		return OutData;
	}

	private EMResult HSM_A0(int WK_len, String keyType, int Mode, int KEK_len,
			String KEK_mk) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;
		int WK_len2;

		cmdBuff = "A0";
		cmdLen += 2;

		/* ģʽ��ʶ 0=������Կ,1=������Կ����ZMK�¼���,2��������Կ���ڹ�Կ�¼��� */
		if (Mode != 0 && Mode != 1) {
			throw new Exception("ģʽ��ʶ���ڹ涨��Χ��");
		}

		cmdBuff += Mode;
		cmdLen += 1;

		/* �����Կ���� */
		String keyScheme = getKeyScheme(keyType);
		cmdBuff += keyScheme;
		cmdLen += 3;

		/* ��Կ(���ȣ����� */
		String keyLenType = getXYZFlagFromMobRule(WK_len);

		cmdBuff += keyLenType;
		cmdLen += 1;

		if (Mode == 1) {
			/* 32λZMK/X+32λZMK/A+3H */
			/*
			 * ����ģʽ��ʶΪ1ʱ����ʾ��ZMK��
			 * �������������Կ��LMK���¼��ܵ���Կ��1A+3H��ʾʹ��K+3λ������ʽ��ȡ���ܻ��ڱ�����Կ��
			 */
			if (KEK_mk.length() != 32 && KEK_mk.length() != 33) {
				throw new Exception("KEK_mk���Ȳ���!");
			}

			cmdBuff += KEK_mk;
			cmdLen += KEK_mk.length();

			cmdBuff += keyLenType;
			cmdLen += 1;

		}

		outStr = commWithHsm(cmdLen, cmdBuff, "A100", OutData);
		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;

		OutData.setMac(UnionUtil.BytesCopy(outBytes, outBytes.length - 16));

		OutData.setLmkWk(getKeyBytesFromBValWithXYZ(outBytes, offset));
		if (outBytes[offset] == 'X' || outBytes[offset] == 'Y'
				|| outBytes[offset] == 'Z') {
			offset += 1;
		}
		offset += OutData.getLmkWk().length;

		if (Mode == 1) {
			OutData.setKekWk(getKeyBytesFromBValWithXYZ(outBytes, offset));
		}
		return OutData;
	}

	private EMResult HSM_MS(int MAK_len, int Mode, int mackeyType,
			int messType, String MAK_mk, String IV_Mac, int MAC_len,
			String MAC_data

	) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String aaa = null;
		String bbb = null;
		int cmdLen = 0;

		cmdBuff = "MS";
		cmdLen += 2;

		/* ģʽ��ʶ */
		if (Mode != 0 && Mode != 1 && Mode != 2 && Mode != 3)
			throw new Exception("HSM_MS,ģʽ��ʶ���ڹ涨��Χ��");

		cmdBuff += Mode;
		cmdLen += 1;

		/*
		 * ��Կ����ģʽ�� 0=�ն���֤��Կ 1=������֤��Կ
		 */
		if (mackeyType != 0 && mackeyType != 1)
			throw new Exception("HSM_MS,��Կ���Ͳ��ڹ涨��Χ��");

		cmdBuff += mackeyType;
		cmdLen += 1;

		/*
		 * ��Կ���ȣ� 0=��������DES��Կ 1=˫������DES��Կ
		 */
		if (MAK_len != 0 && MAK_len != 1)
			throw new Exception("HSM_MS,��Կ���Ȳ��ڹ涨��Χ��");

		cmdBuff += MAK_len;
		cmdLen += 1;

		/* 0=��Ϣ����Ϊ������ 1=��Ϣ����Ϊ��չʮ������Կ */
		if (messType != 0 && messType != 1)

			throw new Exception("HSM_MS,��Կ���Ȳ��ڹ涨��Χ��");

		cmdBuff += messType;
		cmdLen += 1;

		/* ��Կ */
		/*
		 * ��ӦLMK���¼��ܵ���Կ�� TAK��LMK�ԣ�16-17���¼��� ZAK��LMK�ԣ�26-27���¼���
		 */
		cmdBuff += MAK_mk;
		cmdLen += MAK_mk.length();

		/* IV_MAC ��ʼֵ��������Ϣ���Ϊ2��3ʱ��ʾ */
		if (Mode == 2 || Mode == 3) {
			if (IV_Mac.length() != 8)
				throw new Exception("HSM_MS,IV_Mac��");
			cmdBuff += IV_Mac;
			cmdLen += IV_Mac.length();
		}

		/* MAC���ݳ��� */
		this.CheckValue(MAC_len, 1, 9999, "MAC���ݳ���Ϊ[1-9999]������");
		if (messType == 0) {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MAC_len)
					.toUpperCase(), 4);
		} else {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MAC_len / 2)
					.toUpperCase(), 4);
		}
		cmdLen += 4;

		/* MAC ���� */
		if (MAC_len > 0) {
			this.CheckNull(MAC_data, "MAC��������Ϊ��");
			cmdBuff += MAC_data;
			cmdLen += MAC_data.length();
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "MT00", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;

		OutData.setData(UnionUtil.BytesCopy(outBytes, offset));

		return OutData;
	}

	private EMResult HSM_MU(int MAK_len, int Mode, int mackeyType,
			int messType, String MAK_mk, String IV_Mac, int MAC_len,
			String MAC_data

	) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;

		int cmdLen = 0;

		cmdBuff = "MU";
		cmdLen += 2;

		/* ģʽ��ʶ */
		if (Mode != 0 && Mode != 1 && Mode != 2 && Mode != 3)
			throw new Exception("HSM_MU,ģʽ��ʶ���ڹ涨��Χ��");

		cmdBuff += Mode;
		cmdLen += 1;

		/*
		 * ��Կ����ģʽ�� 0=�ն���֤��Կ 1=������֤��Կ
		 */
		if (mackeyType != 0 && mackeyType != 1)
			throw new Exception("HSM_MU,��Կ���Ͳ��ڹ涨��Χ��");

		cmdBuff += mackeyType;
		cmdLen += 1;

		/*
		 * ��Կ���ȣ� 0=��������DES��Կ 1=˫������DES��Կ
		 */
		if (MAK_len != 0 && MAK_len != 1)
			throw new Exception("HSM_MU,��Կ���Ȳ��ڹ涨��Χ��");

		cmdBuff += MAK_len;
		cmdLen += 1;

		/* 0=��Ϣ����Ϊ������ 1=��Ϣ����Ϊ��չʮ������Կ */
		if (messType != 0 && messType != 1)

			throw new Exception("HSM_MU,��Կ���Ȳ��ڹ涨��Χ��");

		cmdBuff += messType;
		cmdLen += 1;

		/* ��Կ */
		/*
		 * ��ӦLMK���¼��ܵ���Կ�� TAK��LMK�ԣ�16-17���¼��� ZAK��LMK�ԣ�26-27���¼���
		 */
		cmdBuff += MAK_mk;
		cmdLen += MAK_mk.length();

		/* IV_MAC ��ʼֵ��������Ϣ���Ϊ2��3ʱ��ʾ */
		if (Mode == 2 || Mode == 3) {
			if (IV_Mac.length() != 8)
				throw new Exception("HSM_MU,IV_Mac��");
			cmdBuff += IV_Mac;
			cmdLen += IV_Mac.length();
		}

		/* MAC���ݳ��� */
		this.CheckValue(MAC_len, 1, 9999, "MAC���ݳ���Ϊ[1-9999]������");
		if (messType == 0) {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MAC_len)
					.toUpperCase(), 4);
		} else {
			cmdBuff += UnionUtil.LeftAddZero(Integer.toHexString(MAC_len / 2)
					.toUpperCase(), 4);
		}
		cmdLen += 4;

		/* MAC ���� */
		if (MAC_len > 0) {
			this.CheckNull(MAC_data, "MAC��������Ϊ��");
			cmdBuff += MAC_data;
			cmdLen += MAC_data.length();
		}

		outStr = commWithHsm(cmdLen, cmdBuff, "MV00", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");
		int offset = 4;

		OutData.setData(UnionUtil.BytesCopy(outBytes, offset));

		return OutData;
	}

	/**
	 * POSP-test
	 */
	private EMResult HSM_19(int Alg_flag) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String Alg_flag2 = null;
		int cmdLen = 0;

		cmdBuff = "19";
		cmdLen += 2;

		// this.CheckNull(Alg_flag, "�������[Alg_flag]Ϊ��");
		// cmdBuff+=Alg_flag;
		// cmdLen+=1;

		/* �����ƶ�����������ܻ���Ӧ�ļ����㷨��־�� */
		switch (Alg_flag) {
		case 0x01:
			Alg_flag2 = "X";
			break;
		case 0x02:
			Alg_flag2 = "Y";
			break;
		case 0x03:
			Alg_flag2 = "Z";
			break;
		default:
			throw new Exception("�㷨ѡ��ڹ涨��Χ��");
		}
		cmdBuff += Alg_flag2;
		cmdLen += 1;

		/* ���㳤�� */
		cmdStr = getPackageLen(cmdLen) + cmdBuff;

		/* ���ݽ��� */
		outStr = ExchangeData(cmdStr);

		/* ���ݼ��� */
		String retcode = this.CheckResult(outStr, "1A00");
		if (retcode == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (retcode.endsWith("00")) {
			OutData.setMac(outStr.substring(outStr.length() - 16,
					outStr.length()).getBytes());

			switch (Alg_flag) {
			case 0x01:
				OutData.setLmkKek(outStr.substring(4, 16 + 4).getBytes());
				OutData.setKEK(outStr.substring(20, 16 + 20).getBytes());

				break;
			case 0x02:
				OutData.setLmkKek(outStr.substring(4, 32 + 4).getBytes());
				OutData.setKEK(outStr.substring(36, 32 + 36).getBytes());
				break;
			case 0x03:
				OutData.setLmkKek(outStr.substring(4, 48 + 4).getBytes());
				OutData.setKEK(outStr.substring(52, 48 + 52).getBytes());
				break;
			default:
				throw new Exception("KEY���Ȳ��ڹ涨��Χ��");
			}

			OutData.setRecode(0);
		} else {
			OutData.setRecode(Integer.parseInt(retcode));
		}
		return OutData;
	}

	private EMResult HSM_1D(int WK_len, int WK_type, int KEK_len, String KEK_mk

	) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;
		int WK_len2;

		cmdBuff = "1D";
		cmdLen += 2;

		/* �����ƶ�����������ܻ���Ӧ�ļ����㷨��־�� */
		switch (WK_len) {
		case 0x01:
			WK_len2 = 1;
			break;
		case 0x02:
			WK_len2 = 2;
			break;
		case 0x03:
			WK_len2 = 3;
			break;
		default:
			throw new Exception("�㷨ѡ��ڹ涨��Χ��");
		}
		cmdBuff += WK_len2;
		cmdLen += 1;

		/* KEK_MK */
		cmdBuff += KEK_mk;
		cmdLen += KEK_mk.length();

		/* ���㳤�� */
		cmdStr = getPackageLen(cmdLen) + cmdBuff;

		/* ���ݽ��� */
		outStr = ExchangeData(cmdStr);

		/* ���ݼ��� */
		String retcode = this.CheckResult(outStr, "1A00");
		if (retcode == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (retcode.endsWith("00")) {
			switch (WK_len) {
			case 0x01:
				OutData.setLmkWk(outStr.substring(4, 16 + 4).getBytes());
				OutData.setKekWk(outStr.substring(20, 16 + 20).getBytes());

				break;
			case 0x02:
				OutData.setLmkWk(outStr.substring(4, 32 + 4).getBytes());
				OutData.setKekWk(outStr.substring(36, 32 + 36).getBytes());
				break;
			case 0x03:
				OutData.setLmkWk(outStr.substring(4, 48 + 4).getBytes());
				OutData.setKekWk(outStr.substring(52, 48 + 52).getBytes());
				break;
			default:
				throw new Exception("KEY���Ȳ��ڹ涨��Χ��");
			}
			// OutData.setData(outStr.substring(4, outStr.length()).getBytes());
			OutData.setRecode(0);
		} else {
			OutData.setRecode(Integer.parseInt(retcode));
		}
		return OutData;
	}

	private EMResult HSM_82(int MAK_len, int MAK_type, String MAK_mk,
			int MAC_len, String MAC_data

	) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String aaa = null;
		String bbb = null;
		int cmdLen = 0;

		cmdBuff = "82";
		cmdLen += 2;

		/* �����ƶ�����������ܻ���Ӧ�ļ����㷨��־�� */
		switch (MAK_type) {
		case 0x01:
			aaa = "L";
			break;
		case 0x02:
			aaa = "M";
			break;
		case 0x03:
			aaa = "N";
			break;
		default:
			throw new Exception("�㷨ѡ��ڹ涨��Χ��");
		}
		cmdBuff += aaa;
		cmdLen += 1;

		cmdBuff += "L";
		cmdLen += 1;

		switch (MAK_len) {
		case 0x02:
			bbb = "U";
			break;
		case 0x03:
			bbb = "T";
			break;
		default:
			throw new Exception("MAK����ѡ��ڹ涨��Χ��");
		}
		cmdBuff += bbb;
		cmdLen += 1;
		/* MAK_MK */
		cmdBuff += MAK_mk;
		cmdLen += MAK_mk.length();

		/* MAC���ݳ��� */
		this.CheckValue(MAC_len, 1, 999, "MAC���ݳ���Ϊ[1-999]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(MAC_len), 3);
		cmdLen += 3;

		/* MAC ���� */
		if (MAC_len > 0) {
			this.CheckNull(MAC_data, "MAC��������Ϊ��");
			cmdBuff += MAC_data;
			cmdLen += MAC_data.length();
		}

		/* ���㳤�� */
		cmdStr = getPackageLen(cmdLen) + cmdBuff;

		/* ���ݽ��� */
		outStr = ExchangeData(cmdStr);

		/* ���ݼ��� */
		String retcode = this.CheckResult(outStr, "8300");
		if (retcode == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (retcode.endsWith("00")) {
			OutData.setData(outStr.substring(4, outStr.length()).getBytes());
			OutData.setRecode(0);
		} else {
			OutData.setRecode(Integer.parseInt(retcode));
		}
		return OutData;
	}

	private EMResult HSM_83(int MAK_len, int MAK_type, String MAK_mk,
			String Mac, int MAC_len, String MAC_data

	) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		String aaa = null;
		String bbb = null;
		int cmdLen = 0;

		cmdBuff = "83";
		cmdLen += 2;

		/* �����ƶ�����������ܻ���Ӧ�ļ����㷨��־�� */
		switch (MAK_type) {
		case 0x01:
			aaa = "L";
			break;
		case 0x02:
			aaa = "M";
			break;
		case 0x03:
			aaa = "N";
			break;
		default:
			throw new Exception("�㷨ѡ��ڹ涨��Χ��");
		}
		cmdBuff += aaa;
		cmdLen += 1;

		cmdBuff += "L";
		cmdLen += 1;

		switch (MAK_len) {
		case 0x02:
			bbb = "U";
			break;
		case 0x03:
			bbb = "T";
			break;
		default:
			throw new Exception("MAK����ѡ��ڹ涨��Χ��");
		}
		cmdBuff += bbb;
		cmdLen += 1;
		/* MAK_MK */
		cmdBuff += MAK_mk;
		cmdLen += MAK_mk.length();

		/* MAC */
		cmdBuff += Mac;
		cmdLen += 8;

		/* MAC���ݳ��� */
		this.CheckValue(MAC_len, 1, 999, "MAC���ݳ���Ϊ[1-999]������");
		cmdBuff += UnionUtil.LeftAddZero(Integer.toString(MAC_len), 3);
		cmdLen += 3;

		/* MAC ���� */
		if (MAC_len > 0) {
			this.CheckNull(MAC_data, "MAC��������Ϊ��");
			cmdBuff += MAC_data;
			cmdLen += MAC_data.length();
		}

		/* ���㳤�� */
		cmdStr = getPackageLen(cmdLen) + cmdBuff;

		/* ���ݽ��� */
		outStr = ExchangeData(cmdStr);

		/* ���ݼ��� */
		String retcode = this.CheckResult(outStr, "8400");
		if (retcode == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (retcode.endsWith("00")) {
			OutData.setData(outStr.substring(4, outStr.length()).getBytes());
			OutData.setRecode(0);
		} else {
			OutData.setRecode(Integer.parseInt(retcode));
		}
		return OutData;
	}

	/*
	 * �������Ĺ�Կ
	 */
	private EMResult HSM_C7(int pkindex, int pktype) throws Exception {
		EMResult OutData = new EMResult();
		String cmdBuff = "";
		String cmdStr = null;
		String outStr = null;
		int cmdLen = 0;

		cmdBuff = "C7";
		cmdLen += 2;

		if (pkindex < 0 || pkindex > 20) {
			throw new Exception("HSM_C7,������");
		}

		cmdBuff += Integer.toString(pkindex);
		cmdLen += 2;

		outStr = commWithHsm(cmdLen, cmdBuff, "C800", OutData);

		if (outStr == null) {
			OutData.setRecode(-1);
			return OutData;
		}
		if (OutData.getRecode() != 0) {
			return OutData;
		}

		byte[] outBytes = outStr.getBytes("ISO-8859-1");

		if (pktype == 1) {
			// ȡ�㹫Կ
			int offset = 10;
			int offsetEnd = 128;
			OutData.setPk(UnionUtil.BytesCopy(outBytes, offset, offsetEnd));
		} else if (pktype == 2) {
			// ȡDER����Ĺ�Կ
			int offset = 4;
			OutData.setPk(UnionUtil.BytesCopy(outBytes, offset));
		}
		return OutData;
	}

	/**
	 * 
	 * @param ver
	 * @param index
	 * @param hsmVer
	 * @param hsmIndex
	 * @param hsmGroupNo
	 */
	private void CaclByVerAndIndex(int ver, int index, int[] hsmVer,
			int[] hsmIndex, int[] hsmGroupNo) {
		int TotalIndex = 0;
		TotalIndex = ver * index;

		hsmVer[0] = TotalIndex / 320;
		hsmIndex[0] = (TotalIndex % 320) / 10;
		hsmGroupNo[0] = (TotalIndex % 320) % 10 + 1;

	}

	/**
	 * 
	 * @param ver
	 * @param index
	 * @param hsmVer
	 * @param hsmIndex
	 * @param hsmGroupNo
	 */
	/*
	 * private void CaclByVerAndIndexEx(int ver,int index,int[] hsmVer,int[]
	 * hsmIndex,int[] hsmGroupNo) { int TotalIndex=0; TotalIndex=index;
	 * 
	 * hsmVer[0]=0; hsmGroupNo[0]=TotalIndex/16; hsmIndex[0]=TotalIndex%16+1;
	 * 
	 * }
	 */

	private void CaclByVerAndIndexEx(int ver, int index, int[] hsmVer,
			int[] hsmIndex, int[] hsmGroupNo) {
		int TotalIndex = 0;
		String sIndex = Integer.toString(index);
		int iIndex = Integer.parseInt(sIndex);
		TotalIndex = iIndex;

		hsmVer[0] = TotalIndex / 320;
		hsmGroupNo[0] = (TotalIndex % 320) / 10;
		hsmIndex[0] = (TotalIndex % 320) % 10 + 1;
	}

	/* ���㱨���� */
	private String getPackageLen(int cmdLen) throws Exception {
		Integer h1, h2;
		byte head[] = { 0, 0 };
		String cmdLenStr = null;

		h1 = new Integer(cmdLen / 256);
		h2 = new Integer(cmdLen % 256);
		head[0] = h1.byteValue();
		head[1] = h2.byteValue();
		cmdLenStr = new String(head, "ISO-8859-1");
		return cmdLenStr;
	}

	/* �ͼ��ܻ��������ݽ��� */
	private String ExchangeData(String cmdStr) {
		UnionSocket sock = null;
		String outStr = null;

		try {
			sock = new UnionSocket();
			if (!sock.connectHSM(HsmHost, HsmPort)) {
				logger.error("���ܻ�����ʧ��");
				return null;
			}
			outStr = sock.ExchangeData(cmdStr);
			if (sock != null) {
				sock.Close();
				sock = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		return outStr;
	}

	/* ��ȡ�������ݵ���Ϣͷ������ֵ��ǰ4���ַ��������Ƿ񷵻ط����� */
	private String CheckMessHeadAndResult(String recv, String MessHead,
			String cmdHeaderSrc) {
		String MessHeadDest = "";
		String cmdHeaderDest = "";
		String retcode = "";

		if (recv == null) {
			logger.error("��������Ϊ��");
			return null;
		}

		if (this.HsmMessaLen <= 0)
			return CheckResult(recv, cmdHeaderSrc);

		if (recv.trim() == "" || recv.trim().length() < (this.HsmMessaLen + 4)) {
			logger.error("�������ݳ���С��" + (this.HsmMessaLen + 4));
			return null;
		}

		MessHeadDest = recv.substring(0, this.HsmMessaLen);
		if (MessHead.equals(MessHeadDest) == false) {
			logger.error("�������ݵ���Ϣͷ��һ��.�յ���MessHead[" + MessHead
					+ " ������MessHeadDest:[" + MessHeadDest + "]");
			return null;
		} else
			return CheckResult(recv.substring(this.HsmMessaLen), cmdHeaderSrc);
	}

	/* ��ȡ�������ݵķ���ֵ��ǰ4���ַ��������Ƿ񷵻ط����� */
	private String CheckResult(String recv, String cmdHeaderSrc) {
		String cmdHeaderDest = "";
		String retcode = "";

		if (recv == null) {
			logger.error("��������Ϊ��");
			return null;
		}
		if (recv.trim() == "" || recv.trim().length() < 4) {
			logger.error("�������ݳ���С��4");
			return null;
		}
		cmdHeaderDest = recv.substring(0, 4);
		if (cmdHeaderDest.equals(cmdHeaderSrc)) {
			retcode = "00";
		} else {
			retcode = recv.substring(2, 4);
		}
		return retcode;
	}

	/* ������������Ƿ�Ϊ�գ����׳��쳣 */
	private void CheckNull(String sParam, String sThrowMsg) throws Exception {
		if (sParam == null) {
			throw new Exception(sThrowMsg);
		}
	}

	/* ����������������Ƿ�Ϸ������׳��쳣 */
	private void CheckLength(String sParam, int len, String sThrowMsg)
			throws Exception {
		if (sParam.length() != len) {
			throw new Exception(sThrowMsg);
		}
	}

	/* ������������Ƿ��ڸ����������ڣ����׳��쳣 */
	private void CheckValue(int iParam, int iStart, int iEnd, String sThrowMsg)
			throws Exception {
		if (iParam < iStart || iParam > iEnd) {
			throw new Exception(sThrowMsg);
		}
	}

	/* ת��CBCD�뵽ʮ�������ַ������������ֽ��� */
	private String ConvertCBCD2String(String sSrc, int[] len) {
		if (sSrc == null)
			return null;

		String Str = sSrc.trim();
		int len1 = Str.length();

		len[0] = len1 / 2;
		byte bytStr[] = UnionUtil.HexString2Bytes(Str);
		byte bytDest[] = new byte[len1];
		UnionUtil.CBCD2HexStr(bytStr, bytDest, len1 / 2);
		return new String(bytDest);

	}

	/* ת��CBCD�뵽ʮ�������ַ������������ֽ��� */
	private String ConvertCBCD2String(byte[] bytSrc, int[] len) {
		if (bytSrc.length <= 0)
			return null;

		int len1 = bytSrc.length;
		len[0] = len1;
		byte bytDest[] = new byte[len1 * 2];
		UnionUtil.CBCD2HexStr(bytSrc, bytDest, len1);

		return new String(bytDest);
	}

	/* �����ƶ�����������ܻ���Ӧ����Կ���ȱ�־ */
	private String getXYZFlagFromMobRule(int Alg_flag) throws Exception {
		String Flag = "";
		switch (Alg_flag) {
		case 1:
			Flag = "Z";
			break;
		case 2:
			Flag = "X";
			break;
		case 3:
			Flag = "Y";
			break;
		default:
			throw new Exception("��Կ���ȱ�ʶ���ڹ涨��Χ��");
		}
		return Flag;
	}

	/* �����ƶ�����������ܻ���Ӧ����ɢ�㷨��־ */
	private String getLSFlagFromMobRule(int Alg_flag) throws Exception {
		String Flag = "";
		switch (Alg_flag) {
		case 0x88:
			Flag = "0";
			break;
		case 0x82:
			Flag = "1";
			break;
		case 0x84:
			Flag = "2";
			break;
		case 0x83:
			Flag = "3";
			break;
		case 0x81:
			Flag = "4";
			break;
		default:
			throw new Exception("��Կ���ȱ�ʶ���ڹ涨��Χ��");
		}
		return Flag;
	}

	/* �����ƶ�����������ܻ���Ӧ��MAC�㷨��־ */
	private String getMACFlagFromMobRule(int Alg_flag) throws Exception {
		String Flag = "";
		switch (Alg_flag) {
		case 0x88:
			Flag = "0";
			break;
		case 0x82:
			Flag = "4";
			break;
		case 0x84:
			Flag = "1";
			break;
		case 0x83:
			Flag = "3";
			break;
		case 0x80:
			Flag = "2";
			break;
		case 0x81:
			Flag = "5";
			break;
		default:
			throw new Exception("��Կ���ȱ�ʶ���ڹ涨��Χ��");
		}
		return Flag;
	}

	/* �����ƶ�����������ܻ���Ӧ����Կ���� */
	private String getKeyTypeFromMobType(int iKeyType) throws Exception {
		String keyType = "";
		switch (iKeyType) {
		case 0x01: // ����Կ������Կ
			keyType = "ZMK";
			break;
		case 0x02: // ��PIN������Կ
			keyType = "ZPK";
			break;
		case 0x03: // ��MAC������Կ
			keyType = "ZAK";
			break;
		case 0x04: // �����ݼ�����Կ
			keyType = "ZEK";
			break;
		default:
			throw new Exception("��Կ���Ͳ��ڹ涨��Χ��");
		}
		return keyType;
	}

	private String getKeyScheme(String keyType) throws Exception {
		String keyScheme = "";
		if (keyType.equalsIgnoreCase("ZMK"))
			keyScheme = "000";
		else if (keyType.equalsIgnoreCase("ZPK"))
			keyScheme = "001";
		else if (keyType.equalsIgnoreCase("TAK"))
			keyScheme = "003";
		else if (keyType.equalsIgnoreCase("ZAK"))
			keyScheme = "008";
		else if (keyType.equalsIgnoreCase("ZEK"))
			keyScheme = "00A";
		else if (keyType.equalsIgnoreCase("MK-SMI"))
			keyScheme = "209";
		else if (keyType.equalsIgnoreCase(" MK-AC"))
			keyScheme = "109";
		else
			throw new Exception("��Կ���Ͳ��ڹ涨��Χ��");
		return keyScheme;
	}

	private String getKeyFromKeyValueWithXYZ(String keyValue) throws Exception {
		int keylen = 16;
		int offset = 0;
		String xyzStr = "";
		String key = "";
		if (keyValue == null || keyValue.length() == 0)
			return "";
		xyzStr = keyValue.substring(0, 1);
		if (xyzStr.equalsIgnoreCase("Z")) {
			keylen = 16;
			offset = 1;
		} else if (xyzStr.equalsIgnoreCase("X")) {
			keylen = 32;
			offset = 1;
		} else if (xyzStr.equalsIgnoreCase("Y")) {
			keylen = 48;
			offset = 1;
		} else {
			keylen = 16;
			offset = 0;
		}
		key = keyValue.substring(offset, offset + keylen);
		return key;
	}

	private byte[] getKeyBytesFromBValWithXYZ(byte[] bytes, int offset)
			throws Exception {
		if (bytes == null || bytes.length == 0)
			return null;
		byte[] keyBytes = null;
		switch (bytes[offset]) {
		case 'Z':
			keyBytes = UnionUtil.BytesCopy(bytes, offset + 1, 16);
			break;
		case 'X':
			keyBytes = UnionUtil.BytesCopy(bytes, offset + 1, 32);
			break;
		case 'Y':
			keyBytes = UnionUtil.BytesCopy(bytes, offset + 1, 48);
			break;
		default:
			keyBytes = UnionUtil.BytesCopy(bytes, offset, 16);
			break;
		}
		return keyBytes;
	}

	private String getRacalKeyString(int keyLength, String keyValue)
			throws Exception {
		String keyString = "";
		if (keyLength < 0 || keyLength != keyValue.length()) {
			throw new Exception("��Կ���ȴ�");
		}
		switch (keyLength) {
		case 16:
			keyString = keyValue;
			break;
		case 32:
			keyString += "X";
			keyString += keyValue;
			break;
		case 48:
			keyString += "Y";
			keyString += keyValue;
			break;
		default:
			throw new Exception("��Կ���Ȳ��ڹ涨��Χ��");
		}
		return keyString;
	}

	private String get12LenAccountNumber(int lenOfAccNo, String accNo)
			throws Exception {
		String accNoOf12Len = "";
		if (lenOfAccNo < 0 || lenOfAccNo != accNo.length()) {
			throw new Exception("�ʺų��ȴ�");
		}
		if (lenOfAccNo >= 13)
			accNoOf12Len = accNo.substring(lenOfAccNo - 13, lenOfAccNo - 1);
		else {
			accNoOf12Len = UnionUtil.LeftAddZero(accNoOf12Len, 12);
		}
		return accNoOf12Len;
	}

	private String getKeyXYZScheme(int length) throws Exception {
		String keyScheme = "";
		switch (length) {
		case 16:
			keyScheme = "Z";
			break;
		case 32:
			keyScheme = "X";
			break;
		case 48:
			keyScheme = "Y";
			break;
		default:
			throw new Exception("��Կ���Ȳ��ڹ涨��Χ��");
		}
		return keyScheme;
	}

	/* �����ƶ�����������ܻ���Ӧ��MAC�㷨��־--�����̨���� */
	private String getMACFlagFromMobRule_Center(int Alg_flag) throws Exception {
		String Flag = "";
		switch (Alg_flag) {
		case 0:
			Flag = "0";
			break;
		case 1:
			Flag = "1";
			break;
		case 2:
			Flag = "2";
			break;
		default:
			throw new Exception("��Կ���ȱ�ʶ���ڹ涨��Χ��");
		}
		return Flag;
	}

	private String getDivNumScheme(int Div_Num) throws Exception {
		String LScheme = "";
		switch (Div_Num) {
		case 1:
			LScheme = "1";
			break;
		case 2:
			LScheme = "2";
			break;
		case 3:
			LScheme = "3";
			break;
		default:
			throw new Exception("��ɢ�������ڹ涨��Χ��");
		}
		return LScheme;
	}

	/********************************* <˽�з���End> **************************************/
}
