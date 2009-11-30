/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dongq
 * 
 *         create time : 2009-9-11 ����09:55:33<br>
 *         ������ʽ������<br>
 *         �ṩ���õı�ǩ��ȡ���ʽ<br>
 *         ������˵�������ʽ�������˱�ǩ�ڵ��������Լ���ǩ��Сд����<br>
 */
public class RegexUtil {

	/**
	 * IMG��ǩ
	 */
	public final static String REGEX_IMG = "<[iI][mM][gG]\\s+[^>]+>";

	/**
	 * EM��ǩ
	 */
	public final static String REGEX_EM = "<[eE][mM]\\s+[^>]+>|</[eE][mM]>";
	
	/**
	 * embed
	 */
	public final static String REGEX_EMBED = "<[eE][mM][bB][eE][dD]\\s+[^>]+>|</[eE][mM][bB][eE][dD]>";
	
	/**
	 * TABLEȫ�ױ�ǩ
	 */
	public final static String REGEX_TABLE = "<[tT][aA][bB][lL][eE]\\s+[^>]+>|<[tT][aA][bB][lL][eE]>|</[tT][aA][bB][lL][eE]>";
	public final static String REGEX_TABLE_ALL = "<[tT][aA][bB][lL][eE]\\s+[^>]+>|<[tT][aA][bB][lL][eE]>|</[tT][aA][bB][lL][eE]>|<[tT][bB][oO][dD][yY]\\s+[^>]+>|<[tT][bB][oO][dD][yY]>|</[tT][bB][oO][dD][yY]>|<[tT][rR]\\s+[^>]+>|<[tT][rR]>|</[tT][rR]>|<[tT][dD]\\s+[^>]+>|<[tT][dD]>|</[tT][dD]>";
	public final static String REGEX_TABLE_TBODY = "<[tT][bB][oO][dD][yY]\\s+[^>]+>|<[tT][bB][oO][dD][yY]>|</[tT][bB][oO][dD][yY]>";
	public final static String REGEX_TABLE_TR = "<[tT][rR]\\s+[^>]+>|<[tT][rR]>|</[tT][rR]>";
	public final static String REGEX_TABLE_TD = "<[tT][dD]\\s+[^>]+>|<[tT][dD]>|</[tT][dD]>";
	/**
	 * TABLE��ʼ��ǩ
	 */
	public final static String REGEX_TABLE_START = "<[tT][aA][bB][lL][eE]\\s+[^>]+>";
	
	/**
	 * �����Ե�TABLE��ʼ��ǩ
	 */
	public final static String REGEX_TABLE_START_NO_ATTR = "<[tT][aA][bB][lL][eE]>";
	
	/**
	 * TABLE������ǩ
	 */
	public final static String REGEX_TABLE_END = "</[tT][aA][bB][lL][eE]>";
	
	/**
	 * ע��
	 */
	public final static String REGEX_COMMENT = "<!-{2,}.*?-{2,}>";

	/**
	 * �����Ե�P��ʼ��ǩ
	 */
	public final static String REGEX_P_START = "<[pP]\\s+[^>]+>";
	
	/**
	 * �����Ե�P��ʼ��ǩ
	 */
	public final static String REGEX_P_START_NO_ATTR = "<[pP]>";
	
	/**
	 * P������ǩ
	 */
	public final static String REGEX_P_END = "</[pP]>";
	
	/**
	 * �����Ե�STYLE��ʼ��ǩ
	 */
	public final static String REGEX_STYLE_START = "<[sS][tT][yY][lL][eE]\\s+[^>]+>";
	
	/**
	 * �����Ե�STYLE��ʼ��ǩ
	 */
	public final static String REGEX_STYLE_START_NO_ATTR = "<[sS][tT][yY][lL][eE]>";

	/**
	 * STYLE������ǩ
	 */
	public final static String REGEX_STYLE_END = "</[sS][tT][yY][lL][eE]>";
	
	/**
	 * CENTER��ʼ�������ǩ
	 */
	public final static String REGEX_CENTER = "<[cC][eE][nN][tT][eE][rR]>|</[cC][eE][nN][tT][eE][rR]>";
	
	public final static String REGEX_CENTER_START = "<[cC][eE][nN][tT][eE][rR]>";
	
	public final static String REGEX_CENTER_END = "</[cC][eE][nN][tT][eE][rR]>";
	
	/**
	 * SPAN��ʼ�������ǩ
	 */
	public final static String REGEX_SPAN = "<[sS][pP][aA][nN]\\s+[^>]+>|<[sS][pP][aA][nN]>|</[sS][pP][aA][nN]>";

	/**
	 * U
	 */
	public final static String REGEX_U_ALL = "<[uU]\\s+[^>]+>|<[uU]>|</[uU]>";
	
	/**
	 * STRONG��ʼ�������ǩ
	 */
	public final static String REGEX_STRONG = "<[sS][tT][rR][oO][nN][gG]>|</[sS][tT][rR][oO][nN][gG]>";
	
	/**
	 * �����ӱ�ǩ
	 */
	public final static String REGEX_A = "<[aA]\\s+[^>]+>|</[aA]>";
	
	public final static String REGEX_A_CONTAIN = "<[aA]\\s+[^>]+>.*?</[aA]>";
	
	/**
	 * SELECT��ʼ��ǩ
	 */
	public final static String REGEX_SELECT_START = "<[sS][eE][lL][eE][cC][tT]\\s+[^>]+>";
	
	/**
	 * SELECT������ǩ
	 */
	public final static String REGEX_SELECT_END = "</[sS][eE][lL][eE][cC][tT]>";
	
	/**
	 * SCRIPT��ʼ��ǩ
	 */
	public final static String REGEX_SCRIPT_START = "<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>";
	
	/**
	 * SCRIPT������ǩ
	 */
	public final static String REGEX_SCRIPT_END = "</[sS][cC][rR][iI][pP][tT]>";
	
	/**
	 * IFRAME��ʼ�������ǩ
	 */
	public final static String REGEX_IFRAME = "<[iI][fF][rR][aA][mM][eE]\\s+[^>]+>|</[iI][fF][rR][aA][mM][eE]>";
	
	/**
	 * IFRAME��ʼ��ǩ
	 */
	public final static String REGEX_IFRAME_START = "<[iI][fF][rR][aA][mM][eE]\\s+[^>]+>";
	
	/**
	 * IFRAME������ǩ
	 */
	public final static String REGEX_IFRAME_END = "</[iI][fF][rR][aA][mM][eE]>";
	
	/**
	 * DIV��ǩ
	 */
	public final static String REGEX_DIV = "<[dD][iI][vV]\\s+[^>]+>|</[dD][iI][vV]>";
	
	/**
	 * DIV��ʼ��ǩ
	 */
	public final static String REGEX_DIV_START = "<[dD][iI][vV]\\s+[^>]+>";
	
	public final static String REGEX_DIV_START_NO_ATTR = "<[dD][iI][vV]>";
	
	/**
	 * DIV������ǩ
	 */
	public final static String REGEX_DIV_END = "</[dD][iI][vV]>";
	
	/**
	 * FONT��ǩ
	 */
	public final static String REGEX_FONT = "<[fF][oO][nN][tT]\\s+[^>]+>|</[fF][oO][nN][tT]>";

	/**
	 * �س�����
	 */
	public final static String REGEX_ENTER_TAB = "\\r\\n";
	
	/**
	 * ���з�
	 */
	public final static String REGEX_TAB = "\\n";
	
	/**
	 * �س���
	 */
	public final static String REGEX_ENTER = "\\r";
	
	/**
	 * BR��ǩ
	 */
	public final static String REGEX_BR = "<br/>";
	
	/**
	 * ����BR��ǩ
	 */
	public final static String REGEX_BR_DOUBLE = "<br/><br/>";
	
	public final static String REGEX_BR_TRI = "<br/><br/><br/>";
	
	/**
	 * �ո�ת���ַ�
	 */
	public final static String REGEX_ESC_SPACE = "&nbsp;";
	
	/**
	 * ��ʽ����ǩ֮��Ŀո�
	 */
	public final static String REGEX_FORMAT_SPACE = ">\\s+<";
	
	/**
	 * ����ָ����������ʽ��Χ�������ı����ݲ������޳� �÷���Ϊ̰����������<br>
	 * beginRegex~endRegex֮�䲻�ܰ����κ���Ҫ���ı�����<br>
	 * ��Ҫ�����޳�������Ƕ��Ĺ�桢�����ȷ���������ı�ǩ��������<br>
	 * @param beginRegex
	 * @param endRegex
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public final static String eliminateString(String beginRegex, String endRegex, String content) {
		String newContent = "";
		try{
			int beginIndex = 0;
			int endIndex = 0;
			Matcher matcher = Pattern.compile(beginRegex).matcher(content);
			if (matcher.find()) beginIndex = matcher.start();
			matcher = Pattern.compile(endRegex).matcher(content);
			while (matcher.find()) endIndex = matcher.end();
			newContent = content.substring(0, beginIndex) + content.substring(endIndex, content.length());
		}catch(Exception e){
			//�׳��쳣�򷵻�ԭ����������ݣ������ݲ���
			newContent = content;
		}
		return newContent;
	}
	
	/**
	 * ��ȡƥ��ָ�����ʽ������
	 * @param regex ������ʽ
	 * @param content �ı�����
	 * @return
	 */
	public final static String extractContentWithRegex(String regex, String content){
		String newContent = "";
		try{
			int beginIndex = 0;
			int endIndex = 0;
			Matcher matcher = Pattern.compile(regex).matcher(content);
			while(matcher.find()){
				beginIndex = matcher.start();
				endIndex = matcher.end();
				newContent += content.substring(beginIndex, endIndex);
			}
		}catch(Exception e){
			//�׳��쳣�򷵻�ԭ����������ݣ������ݲ���
			newContent = content;
		}
		return newContent;
	}
	/**/
	public final static String replaceAll(String regex, String content) {
		String newContent = "";
		try{
			int beginIndex = 0;
			int endIndex = 0;
			Matcher matcher = Pattern.compile(regex).matcher(content);
			newContent = content;
			while(matcher.find()){
				beginIndex = matcher.start();
				endIndex = matcher.end();
				newContent = newContent.replace(content.substring(beginIndex, endIndex), "");
			}
		}catch(Exception e){
			//�׳��쳣�򷵻�ԭ����������ݣ������ݲ���
			newContent = content;
		}
		return newContent;
	}
	
}
