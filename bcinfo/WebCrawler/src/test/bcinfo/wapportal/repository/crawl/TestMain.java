/**
 * 
 */
package test.bcinfo.wapportal.repository.crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;

import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author dongq
 * 
 *         create time : 2009-10-19 ����02:45:11
 */
public class TestMain {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*
		 * <img src="http://photocdn.sohu.com/20091019/Img267501688.jpg" Width="500" border="1" height="306">
��17�տ�ʼ���ڿ����������ڻ��£��ͻ�˹̹���沿�ӿ�ʼ����������˹̹���������Ӫչ������ж���
���ͬʱ������ʮ��������Ѿ��������߼���˹̹�Ĳ����������Ԯ����Ԥ�ƣ����ž��¹��Ƶ�������
�����и���������ʧ����ͼΪ10��18�գ��ͻ�˹̹���������������߼���˹̹������
<img src="http://photocdn.sohu.com/20091019/Img267501691.jpg" Width="500" border="1" height="340">ͼΪ10��18�գ��ͻ�˹̹���������������߼���˹̹������
<img src="http://photocdn.sohu.com/20091019/Img267501692.jpg" Width="500" border="1" height="298">ͼΪ10��17�հͻ�˹̹���ӵ�̹������ǰ�����߼���˹̹������  (��Դ��������)
		 * */
		String cnt = "<img src=\"http://photocdn.sohu.com/20091019/Img267501688.jpg\" Width=\"500\" border=\"1\" height=\"306\">";
cnt+="��17�տ�ʼ���ڿ����������ڻ��£��ͻ�˹̹���沿�ӿ�ʼ����������˹̹���������Ӫչ������ж���";
cnt+="���ͬʱ������ʮ��������Ѿ��������߼���˹̹�Ĳ����������Ԯ����Ԥ�ƣ����ž��¹��Ƶ�������";
cnt+="�����и���������ʧ����ͼΪ10��18�գ��ͻ�˹̹���������������߼���˹̹������";
cnt+="<img src=\"http://photocdn.sohu.com/20091019/Img267501691.jpg\" Width=\"500\" border=\"1\" height=\"340\">ͼΪ10��18�գ��ͻ�˹̹���������������߼���˹̹������";
cnt+="<img src=\"http://photocdn.sohu.com/20091019/Img267501692.jpg\" Width=\"500\" border=\"1\" height=\"298\">ͼΪ10��17�հͻ�˹̹���ӵ�̹������ǰ�����߼���˹̹������  (��Դ��������)";
		
		System.out.println(cnt);
		System.out.println("");

		String[] tmp = cnt.split(RegexUtil.REGEX_IMG);
		for(int i=0;i<tmp.length;i++){
			String var = tmp[i];
			//System.out.println(var);
		}
		
		List<String> imgList = new ArrayList<String>();
		//http://.*.([Gg][Ii][Ff]|[Jj][Pp][Gg]|[Bb][Mm][Pp]|[Jj][Pp][Ee][Gg])
		Pattern pattern = Pattern.compile(RegexUtil.REGEX_IMG);
		Matcher matcher = pattern.matcher(cnt);
		Parser parser = new Parser();
		while(matcher.find()){
			int start = matcher.start();
			int end = matcher.end();
			String inputHTML =cnt.substring(start, end); 
			imgList.add(inputHTML);
			parser.setInputHTML(inputHTML);
			NodeIterator iter = parser.elements();
			ImageTag imgTag = (ImageTag)iter.nextNode();
			String str = imgTag.getImageURL();
			System.out.println(imgTag.getImageURL()+" | "+str.substring(str.lastIndexOf("/")+1));
		}
		
		for(String img : imgList){
			System.out.println(img);
			
		}
	}

}
