/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.domain;

/**
 * @author dongq
 * 
 *         create time : 2009-9-9 上午11:58:20<br>
 *         资源类型工具类<br>
 *         1 MP3<br>
 *         2 铃音<br>
 *         3 图片(资源类)<br>
 *         4 笑话<br>
 *         5 短信<br>
 *         6 视频(资源类)<br>
 *         7 其他文件类型<br>
 *         8 文字(资源类)<br>
 *         9 小说<br>
 *         10 手机软件<br>
 *         11 美食<br>
 *         12 大文字(资源类)<br>
 *         13 web获取文本<br>
 *         14 游戏<br>
 *         15 主题<br>
 *         16 电子书<br>
 */
public final class ResourceType {

	public final static int MP3 = 1;

	public final static int SOUND = 2;

	public final static int PIC = 3;

	public final static int JOKE = 4;

	public final static int SMS = 5;

	public final static int VIDEO = 6;

	public final static int OTHERS = 7;

	public final static int WORDS = 8;

	public final static int FICTION = 9;

	public final static int SOFTWARE = 10;

	public final static int FOOD = 11;

	public final static int CLOB = 12;

	public final static int WEB_TEXT = 13;

	public final static int GAME = 14;

	public final static int SUBJECT = 15;

	public final static int E_BOOK = 16;

	public static String getTypeName(int type) {
		String name = "";
		switch (type) {
		case ResourceType.CLOB:
			name = "大文字";
			break;
		case ResourceType.E_BOOK:
			name = "电子书";
			break;
		case ResourceType.FICTION:
			name = "小说";
			break;
		case ResourceType.FOOD:
			name = "美食";
			break;
		case ResourceType.GAME:
			name = "游戏";
			break;
		case ResourceType.JOKE:
			name = "笑话";
			break;
		case ResourceType.MP3:
			name = "MP3";
			break;
		case ResourceType.OTHERS:
			name = "其他";
			break;
		case ResourceType.PIC:
			name = "图片";
			break;
		case ResourceType.SMS:
			name = "短信";
			break;
		case ResourceType.SOFTWARE:
			name = "软件";
			break;
		case ResourceType.SOUND:
			name = "声音";
			break;
		case ResourceType.SUBJECT:
			name = "主题";
			break;
		case ResourceType.VIDEO:
			name = "视频";
			break;
		case ResourceType.WEB_TEXT:
			name = "WEB文本";
			break;
		case ResourceType.WORDS:
			name = "文字";
			break;
		default:
			break;
		}
		return name;
	}
}
