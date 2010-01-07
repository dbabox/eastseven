/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.ui.spcp.domain;

/**
 * @author dongq
 * 
 *         create time : 2009-9-9 ����11:58:20<br>
 *         ��Դ���͹�����<br>
 *         1 MP3<br>
 *         2 ����<br>
 *         3 ͼƬ(��Դ��)<br>
 *         4 Ц��<br>
 *         5 ����<br>
 *         6 ��Ƶ(��Դ��)<br>
 *         7 �����ļ�����<br>
 *         8 ����(��Դ��)<br>
 *         9 С˵<br>
 *         10 �ֻ����<br>
 *         11 ��ʳ<br>
 *         12 ������(��Դ��)<br>
 *         13 web��ȡ�ı�<br>
 *         14 ��Ϸ<br>
 *         15 ����<br>
 *         16 ������<br>
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
			name = "������";
			break;
		case ResourceType.E_BOOK:
			name = "������";
			break;
		case ResourceType.FICTION:
			name = "С˵";
			break;
		case ResourceType.FOOD:
			name = "��ʳ";
			break;
		case ResourceType.GAME:
			name = "��Ϸ";
			break;
		case ResourceType.JOKE:
			name = "Ц��";
			break;
		case ResourceType.MP3:
			name = "MP3";
			break;
		case ResourceType.OTHERS:
			name = "����";
			break;
		case ResourceType.PIC:
			name = "ͼƬ";
			break;
		case ResourceType.SMS:
			name = "����";
			break;
		case ResourceType.SOFTWARE:
			name = "���";
			break;
		case ResourceType.SOUND:
			name = "����";
			break;
		case ResourceType.SUBJECT:
			name = "����";
			break;
		case ResourceType.VIDEO:
			name = "��Ƶ";
			break;
		case ResourceType.WEB_TEXT:
			name = "WEB�ı�";
			break;
		case ResourceType.WORDS:
			name = "����";
			break;
		default:
			break;
		}
		return name;
	}
}
