/**
 * 
 */
package com.bcinfo.crawl.utils;

/**
 * @author dongq
 * 
 *         create time : 2010-6-11 上午09:26:12
 */
public final class RegexUtil {

	public static final String A = "<[aA]\\s+[^>]+>|</[aA]>";
	
	public static final String B = "<[bB]\\s+[^>]+>|<[bB]>|</[bB]>";
	public static final String BR = "<br />";
	
	public final static String COMMENT = "<!-{2,}.*?-{2,}>";
	public final static String CENTER = "<[cC][eE][nN][tT][eE][rR]>|</[cC][eE][nN][tT][eE][rR]>";

	public final static String DIV = "<[dD][iI][vV]\\s+[^>]+>|<[dD][iI][vV]>|</[dD][iI][vV]>";
	
	public final static String FONT = "<[fF][oO][nN][tT]\\s+[^>]+>|</[fF][oO][nN][tT]>|<[fF][oO][nN][tT]>";

	public final static String LINK = "<[lL][iI][nN][kK]\\s+[^>]+>";
	
	public static final String P = "<[pP]>|<[pP]\\s+[^>]+>";
	public static final String P_END = "</[pP]>";
	public static final String P_START = "<[pP]\\s+[^>]+>|<[pP]>";

	public static final String U = "<[uU]\\s+[^>]+>|</[uU]>|<[uU]>";
	
	public final static String IMG = "<[iI][mM][gG]\\s+[^>]+>";
	public final static String IFRAME = "<[iI][fF][rR][aA][mM][eE]\\s+[^>]+>|</[iI][fF][rR][aA][mM][eE]>";

	public final static String SPAN = "<[sS][pP][aA][nN]>|<[sS][pP][aA][nN]\\s+[^>]+>|</[sS][pP][aA][nN]>";
	public final static String STYLE = "(<[sS][tT][yY][lL][eE]\\s+[^>]+>.*?</[sS][tT][yY][lL][eE]>)|(<[sS][tT][yY][lL][eE]>.*?</[sS][tT][yY][lL][eE]>)";
	public final static String STRONG = "<[sS][tT][rR][oO][nN][gG]>|</[sS][tT][rR][oO][nN][gG]>";
	public final static String SCRIPT = "(<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>.*?</[sS][cC][rR][iI][pP][tT]>)|(<[sS][cC][rR][iI][pP][tT]>.*?</[sS][cC][rR][iI][pP][tT]>)";
	
	public final static String TD = "<[tT][dD]>|<[tT][dD]\\s+[^>]+>";
	public final static String TABLE = "<[tT][aA][bB][lL][eE]\\s+[^>]+>|<[tT][aA][bB][lL][eE]>|</[tT][aA][bB][lL][eE]>|<[tT][bB][oO][dD][yY]\\s+[^>]+>|<[tT][bB][oO][dD][yY]>|</[tT][bB][oO][dD][yY]>|<[tT][rR]\\s+[^>]+>|<[tT][rR]>|</[tT][rR]>|<[tT][dD]\\s+[^>]+>|<[tT][dD]>|</[tT][dD]>";
	
	/**
	 * 回车换行
	 */
	public final static String ENTER_TAB = "\\r\\n";
	
	/**
	 * 换行符
	 */
	public final static String TAB = "\\n";
	
	/**
	 * 回车符
	 */
	public final static String ENTER = "\\r";
}
