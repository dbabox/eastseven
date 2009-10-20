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
 *         create time : 2009-10-19 下午02:45:11
 */
public class TestMain {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*
		 * <img src="http://photocdn.sohu.com/20091019/Img267501688.jpg" Width="500" border="1" height="306">
从17日开始，在空中力量的掩护下，巴基斯坦地面部队开始对南瓦齐里斯坦的塔利班大本营展开清剿行动。
与此同时，超过十万的民众已经逃离南瓦济里斯坦的部落地区。救援机构预计，随着军事攻势的升级，
将会有更多人流离失所。图为10月18日，巴基斯坦民众正在逃离南瓦济里斯坦地区。
<img src="http://photocdn.sohu.com/20091019/Img267501691.jpg" Width="500" border="1" height="340">图为10月18日，巴基斯坦民众正在逃离南瓦济里斯坦地区。
<img src="http://photocdn.sohu.com/20091019/Img267501692.jpg" Width="500" border="1" height="298">图为10月17日巴基斯坦军队的坦克正在前往南瓦济里斯坦地区。  (来源：中新网)
		 * */
		String cnt = "<img src=\"http://photocdn.sohu.com/20091019/Img267501688.jpg\" Width=\"500\" border=\"1\" height=\"306\">";
cnt+="从17日开始，在空中力量的掩护下，巴基斯坦地面部队开始对南瓦齐里斯坦的塔利班大本营展开清剿行动。";
cnt+="与此同时，超过十万的民众已经逃离南瓦济里斯坦的部落地区。救援机构预计，随着军事攻势的升级，";
cnt+="将会有更多人流离失所。图为10月18日，巴基斯坦民众正在逃离南瓦济里斯坦地区。";
cnt+="<img src=\"http://photocdn.sohu.com/20091019/Img267501691.jpg\" Width=\"500\" border=\"1\" height=\"340\">图为10月18日，巴基斯坦民众正在逃离南瓦济里斯坦地区。";
cnt+="<img src=\"http://photocdn.sohu.com/20091019/Img267501692.jpg\" Width=\"500\" border=\"1\" height=\"298\">图为10月17日巴基斯坦军队的坦克正在前往南瓦济里斯坦地区。  (来源：中新网)";
		
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
