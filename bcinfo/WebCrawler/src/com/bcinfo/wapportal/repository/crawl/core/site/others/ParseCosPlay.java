package com.bcinfo.wapportal.repository.crawl.core.site.others;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.bcinfo.wapportal.repository.crawl.core.AbstractHtmlParseTemplete;
import com.bcinfo.wapportal.repository.crawl.core.Parse;
import com.bcinfo.wapportal.repository.crawl.util.RegexUtil;

/**
 * @author lvhs E-mail:lvhs@flying-it.com
 * @version 创建时间：2010-3-5 下午03:53:12 类说明
 *          :抓取cosplay网站动漫图片(http://wangyou.pcgames.com.cn/cosplay/dm/)
 */
public class ParseCosPlay extends AbstractHtmlParseTemplete implements Parse {

	private static Logger log = Logger.getLogger(ParseCosPlay.class);

	public ParseCosPlay() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkPageOfLinks(String link) {
		List<String> links = null;
		try {
			links = new ArrayList<String>();
			links.add(link);

			String page = this.getPageContent(link, "div", "class",
					"art_area mt10");
			if (!"".equals(page)) {
				Parser parser = new Parser(link);
				parser.setEncoding("gb2312");

				NodeFilter filter = new AndFilter(new TagNameFilter("div"),
						new HasAttributeFilter("class", "art_page"));
				NodeList nodeList = parser.extractAllNodesThatMatch(filter);
				if (nodeList != null && nodeList.size() > 0) {
					for (NodeIterator iter = nodeList.elements(); iter
							.hasMoreNodes();) {
						Div node = (Div) iter.nextNode();
						// log.debug("------"+node.toHtml());
						if (node != null) {
							filter = new NodeClassFilter(LinkTag.class);
							NodeList node_list = node.getChildren()
									.extractAllNodesThatMatch(filter);
							if (node_list != null && node_list.size() > 0) {
								for (NodeIterator it = node_list.elements(); it
										.hasMoreNodes();) {
									LinkTag link_tag = (LinkTag) it.nextNode();
									String link_ = link_tag.extractLink();
									if (link_ != null
											&& !"".trim().equals(link_)) {
										links.add(link_);
									}
								}
							}
						}
					}
				}
			}

			if (links.size() > 1) {
				Iterator<String> iterator = new UniqueFilterIterator(links
						.iterator());
				List<String> list = new ArrayList<String>();
				while (iterator.hasNext()) {
					list.add(iterator.next());
				}

				if (!list.isEmpty()) {
					links.clear();
					links.addAll(list);
				}
			}

			if (log.isDebugEnabled()) {
				for (String temp : links) {
					log.debug("当前分页:" + temp);
				}
			}

		} catch (Exception e) {
			links = new ArrayList<String>();
			links.add(link);
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return links;
	}

	@Override
	public String getTargetContent(String link) {
		String content = "";
		try {
			Parser parser = new Parser(link);
			parser.setEncoding("gb2312");

			NodeList nodeList = parser.extractAllNodesThatMatch(new AndFilter(
					new TagNameFilter("div"), new HasAttributeFilter("class",
							"art_area mt10")));
			if (nodeList != null && nodeList.size() > 0) {
				
				Div node = (Div) nodeList.elementAt(0);
				content += node.toHtml();

			}
			parser.reset();
			parser.setInputHTML(content);
			Lexer lexer = parser.getLexer();
			Node node = null;
			content = "";
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof ScriptTag) {
					continue;
				} else if (node instanceof TextNode
						&& node.toHtml().trim().startsWith("$")) {
					continue;
				} else if (node instanceof TagNode
						&& node.toHtml().trim().contains("</script>")) {
					continue;
				}
				content += node.toHtml().trim();
			}
			content=this.commonParseContent(content);
			// content=content.replaceFirst(RegexUtil.REGEX_SCRIPT_START,
			// replacement);
			// content =
			// RegexUtil.replaceAll("<[sS][cC][rR][iI][pP][tT]\\s+[^>]+>.*?</[sS][cC][rR][iI][pP][tT]>",
			// content);
			// <script>$("#art_topnav").appendTo("#art_topnav_to"); </script>
			// content =
			// content.replaceAll("<script>$(\\\"#art_topnav\\\").appendTo(\\\"#art_topnav_to\\\"); </script>",
			// replacement);
			// content = RegexUtil.eliminateString(RegexUtil.REGEX_SCRIPT_START,
			// RegexUtil.REGEX_SCRIPT_END, content);
			// content=content.replaceAll("$(\"#art_topnav\").appendTo(\"#art_topnav_to\");",
			// replacement);

		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return content;
	}

	@Override
	public Boolean parse(String folderId, String title, String link) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(String link) {
		// TODO Auto-generated method stub
		return this.simpleParse(link);
	}

	public static void main(String[] args) {
		String link = "http://wangyou.pcgames.com.cn/cosplay/dm/0808/1166551.html";
		ParseCosPlay pp = new ParseCosPlay();
		System.out.println(pp.parse(link));
	}

}
