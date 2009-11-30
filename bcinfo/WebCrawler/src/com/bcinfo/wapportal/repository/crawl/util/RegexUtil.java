/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dongq
 * 
 *         create time : 2009-9-11 上午09:55:33<br>
 *         正则表达式工具类<br>
 *         提供常用的标签提取表达式<br>
 *         无特殊说明，表达式均考虑了标签内的所有属性及标签大小写问题<br>
 */
public class RegexUtil {

	/**
	 * IMG标签
	 */
	public final static String REGEX_IMG = "<[iI][mM][gG]\\s+[^>]+>";

	/**
	 * EM标签
	 */
	public final static String REGEX_EM = "<[eE][mM]\\s+[^>]+>|</[eE][mM]>";
	
	/**
	 * embed
	 */
	public final static String REGEX_EMBED = "<[eE][mM][bB][eE][dD]\\s+[^>]+>|</[eE][mM][bB][eE][dD]>";
	
	/**
	 * TABLE全套标签
	 */
	public final static String REGEX_TABLE = "<[tT][aA][bB][lL][eE]\\s+[^>]+>|<[tT][aA][bB][lL][eE]>|</[tT][aA][bB][lL][eE]>";
	public final static String REGEX_TABLE_ALL = "<[tT][aA][bB][lL][eE]\\s+[^>]+>|<[tT][aA][bB][lL][eE]>|</[tT][aA][bB][lL][eE]>|<[tT][bB][oO][dD][yY]\\s+[^>]+>|<[tT][bB][oO][dD][yY]>|</[tT][bB][oO][dD][yY]>|<[tT][rR]\\s+[^>]+>|<[tT][rR]>|</[tT][rR]>|<[tT][dD]\\s+[^>]+>|<[tT][dD]>|</[tT][dD]>";
	public final static String REGEX_TABLE_TBODY = "<[tT][bB][oO][dD][yY]\\s+[^>]+>|<[tT][bB][oO][dD][yY]>|</[tT][bB][oO][dD][yY]>";
	public final static String REGEX_TABLE_TR = "<[tT][rR]\\s+[^>]+>|<[tT][rR]>|</[tT][rR]>";
	public final static String REGEX_TABLE_TD = "<[tT][dD]\\s+[^>]+>|<[tT][dD]>|</[tT][dD]>";
	/**
	 * TABLE开始标签
	 */
	public final static String REGEX_TABLE_START = "<[tT][aA][bB][lL][eE]\\s+[^>]+>";
	
	/**
	 * 无属性的TABLE开始标签
	 */
	public final static String REGEX_TABLE_START_NO_ATTR = "<[tT][aA][bB][lL][eE]>";
	
	/**
	 * TABLE结束标签
	 */
	public final static String REGEX_TABLE_END = "</[tT][aA][bB][lL][eE]>";
	
	/**
	 * 注释
	 */
	public final static String REGEX_COMMENT = "<!-{2,}.*?-{2,}>";

	/**
	 * 带属性的P开始标签
	 */
	public final static String REGEX_P_START = "<[pP]\\s+[^>]+>";
	
	/**
	 * 无属性的P开始标签
	 */
	public final static String REGEX_P_START_NO_ATTR = "<[pP]>";
	
	/**
	 * P结束标签
	 */
	public final static String REGEX_P_END = "</[pP]>";
	
	/**
	 * 带属性的STYLE开始标签
	 */
	public final static String REGEX_STYLE_START = "<[sS][tT][yY][lL][eE]\\s+[^>]+>";
	
	/**
	 * 无属性的STYLE开始标签
	 */
	public final static String REGEX_STYLE_START_NO_ATTR = "<[sS][tT][yY][lL][eE]>";

	/**
	 * STYLE结束标签
	 */
	public final static String REGEX_STYLE_END = "</[sS][tT][yY][lL][eE]>";
	
	/**
	 * CENTER开始或结束标签
	 */
	public final static String REGEX_CENTER = "<[cC][eE][nN][tT][eE][rR]>|</[cC][eE][nN][tT][eE][rR]>";
	
	public final static String REGEX_CENTER_START = "<[cC][eE][nN][tT][eE][rR]>";
	
	public final static String REGEX_CENTER_END = "</[cC][eE][nN][tT][eE][rR]>";
	
	/**
	 * SPAN开始或结束标签
	 */
	public final static String REGEX_SPAN = "<[sS][pP][aA][nN]\\s+[^>]+>|<[sS][pP][aA][nN]>|</[sS][pP][aA][nN]>";

	/**
	 * U
	 */
	public final static String REGEX_U_ALL = "<[uU]\\s+[^>]+>|<[uU]>|</[uU]>";
	
	/**
	 * STRONG开始或结束标签
	 */
	public final static String REGEX_STRONG = "<[sS][tT][rR][oO][nN][gG]>|</[sS][tT][rR][oO][nN][gG]>";
	
	/**
	 * 超链接标签
	 */
	public final static String REGEX_A = "<[aA]\\s+[^>]+>|</[aA]>";
	
	public final static String REGEX_A_CONTAIN = "<[aA]\\s+[^>]+>.*?</[aA]>";
	
	/**
	 * SELECT开始标签
	 */
	public final static String REGEX_SELECT_START = "<[sS][eE][lL][eE][cC][tT]\\s+[^>]+>";
	
	/**
	 * SELECT结束标签
	 */
	public final static String REGEX_SELECT_END = "</[sS][eE][lL][eE][cC][tT]>";
	
	/**
	 * SCRIPT开始标签
	 */
	public final static String REGEX_SCRIPT_START = "<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>";
	
	/**
	 * SCRIPT结束标签
	 */
	public final static String REGEX_SCRIPT_END = "</[sS][cC][rR][iI][pP][tT]>";
	
	/**
	 * IFRAME开始或结束标签
	 */
	public final static String REGEX_IFRAME = "<[iI][fF][rR][aA][mM][eE]\\s+[^>]+>|</[iI][fF][rR][aA][mM][eE]>";
	
	/**
	 * IFRAME开始标签
	 */
	public final static String REGEX_IFRAME_START = "<[iI][fF][rR][aA][mM][eE]\\s+[^>]+>";
	
	/**
	 * IFRAME结束标签
	 */
	public final static String REGEX_IFRAME_END = "</[iI][fF][rR][aA][mM][eE]>";
	
	/**
	 * DIV标签
	 */
	public final static String REGEX_DIV = "<[dD][iI][vV]\\s+[^>]+>|</[dD][iI][vV]>";
	
	/**
	 * DIV开始标签
	 */
	public final static String REGEX_DIV_START = "<[dD][iI][vV]\\s+[^>]+>";
	
	public final static String REGEX_DIV_START_NO_ATTR = "<[dD][iI][vV]>";
	
	/**
	 * DIV结束标签
	 */
	public final static String REGEX_DIV_END = "</[dD][iI][vV]>";
	
	/**
	 * FONT标签
	 */
	public final static String REGEX_FONT = "<[fF][oO][nN][tT]\\s+[^>]+>|</[fF][oO][nN][tT]>";

	/**
	 * 回车换行
	 */
	public final static String REGEX_ENTER_TAB = "\\r\\n";
	
	/**
	 * 换行符
	 */
	public final static String REGEX_TAB = "\\n";
	
	/**
	 * 回车符
	 */
	public final static String REGEX_ENTER = "\\r";
	
	/**
	 * BR标签
	 */
	public final static String REGEX_BR = "<br/>";
	
	/**
	 * 两个BR标签
	 */
	public final static String REGEX_BR_DOUBLE = "<br/><br/>";
	
	public final static String REGEX_BR_TRI = "<br/><br/><br/>";
	
	/**
	 * 空格转义字符
	 */
	public final static String REGEX_ESC_SPACE = "&nbsp;";
	
	/**
	 * 格式化标签之间的空格
	 */
	public final static String REGEX_FORMAT_SPACE = ">\\s+<";
	
	/**
	 * 根据指定的正则表达式范围，搜索文本内容并将其剔除 该方法为贪婪性搜索，<br>
	 * beginRegex~endRegex之间不能包含任何需要的文本内容<br>
	 * 主要用于剔除正文中嵌入的广告、导航等非正文所需的标签及其内容<br>
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
			//抛出异常则返回原来输入的内容，即内容不变
			newContent = content;
		}
		return newContent;
	}
	
	/**
	 * 抽取匹配指定表达式的内容
	 * @param regex 正则表达式
	 * @param content 文本内容
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
			//抛出异常则返回原来输入的内容，即内容不变
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
			//抛出异常则返回原来输入的内容，即内容不变
			newContent = content;
		}
		return newContent;
	}
	
}
