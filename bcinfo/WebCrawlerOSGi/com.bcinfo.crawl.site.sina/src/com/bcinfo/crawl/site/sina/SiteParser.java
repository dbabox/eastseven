/**
 * 
 */
package com.bcinfo.crawl.site.sina;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bcinfo.crawl.dao.log.service.CrawlerLogService;
import com.bcinfo.crawl.dao.service.WebCrawlerDao;
import com.bcinfo.crawl.domain.model.Site;
import com.bcinfo.crawl.site.service.AbstractSiteParser;
import com.bcinfo.crawl.utils.RegexUtil;

/**
 * @author dongq
 * 
 * create time : 2010-6-25 上午09:47:41
 */
public class SiteParser extends AbstractSiteParser implements Job {

	private static final Log log = LogFactory.getLog(SiteParser.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		setSite((Site)jobDataMap.get("site"));
		setWebCrawlerDao((WebCrawlerDao)jobDataMap.get("webCrawlerDao"));
		setCrawlerLogService((CrawlerLogService)jobDataMap.get("crawlerLogService"));
		run();
		log.info("["+getSite().getChannelId()+"."+getSite().getChannelName()+"-"+getSite().getName()+"]["+getSite().getUrl()+"]"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getNextFireTime()));
	}

	@Override
	public String hook(String content) {
		String cnt = "";
		try {
			cnt = new String(content);
			cnt = cnt.replaceAll(RegexUtil.STYLE, replacement)
					 .replaceAll(RegexUtil.SCRIPT, replacement)
					 .replaceAll("采用本调查数据，来源请注明新浪四川", replacement)
					 .replaceAll("&nbsp", " ").replaceAll("<!-- -->", replacement)
			         .replaceAll("上一页\\d+下一页", replacement)
			         .replaceAll("<img  src=\"http://i2.sinaimg.cn/home/c.gif\" height=5 width=1 style=\"border:none;\">", replacement)
			         .replaceAll("转发此文至微博", replacement)
			         .replaceAll("关注最新互联网动态，看新浪科技微博", replacement)
			         .replaceAll("新浪声明：本文为《财经网》授权新浪财经中心(财经、科技频道)转载，并未授权新浪网合作伙伴转载，如新浪网合作伙伴擅自转载构成侵权，责任自负。", replacement)
			         .replaceAll("此消息系转载自新浪合作媒体，新浪网登载此文出于传递更多信息之目的，并不意味着赞同其观点或证实其描述。文章内容仅供参考，不构成投资建议。投资者据此操作，风险自担。", replacement)
			         .replaceAll("特别说明：由于各方面情况的不断调整与变化，新浪网所提供的所有考试信息仅供参考，敬请考生以权威部门公布的正式信息为准。", replacement)
			         .replaceAll("更多信息请访问：新浪高考频道 高考论坛 高考博客圈订阅高考免费短信服务", replacement)
			         .replaceAll("<br />.*?\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", replacement)
			         .replaceAll(RegexUtil.COMMENT, replacement)
			         ;
			
		} catch (Exception e) {
			return content;
		}
		return cnt;
	}

}
