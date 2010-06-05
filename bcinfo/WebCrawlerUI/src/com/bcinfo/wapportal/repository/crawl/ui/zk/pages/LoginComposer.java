package com.bcinfo.wapportal.repository.crawl.ui.zk.pages;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bcinfo.wapportal.repository.crawl.ui.zk.dao.UserDao;
import com.bcinfo.wapportal.repository.crawl.ui.zk.domain.UserBean;

/**
 * @author dongq
 * 
 *         create time : 2009-11-24 ����10:01:50
 */
public class LoginComposer extends GenericForwardComposer {

	private static final Logger log = Logger.getLogger(LoginComposer.class);
	
	private static final long serialVersionUID = 1L;

	private UserDao dao;
	private String location;
	
	private Textbox nametb;
	private Textbox pwdtb;
	private Window loginWin;
	
	public LoginComposer() {
		dao = new UserDao();
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		location = (String)arg.get("location");
		System.out.println(" location : "+location);
		if(!this.requestScope.isEmpty()) {
			for(Object key : this.requestScope.keySet()) {
				System.out.println(" servlet username : " + key + " - " + this.requestScope.get(key));
			}
		}
		System.out.println(this.application.getNativeContext());
	}
	
	public void onOK$pwdtb(){
		onClick$login();
	}
	
	public void onClick$login(){
		UserBean user = dao.getUser(nametb.getValue());
		if(user!=null){
			if("1".equals(user.getUserStatus())){
				if(user.getPassword().equals(pwdtb.getValue())){
					session.setAttribute("user", user);
					if(location!=null){
						loginWin.onClose();
						log.info("��Ҫ�û���¼:"+user);
					}else{
						log.info("�û���ҳ��¼:"+user);
						execution.sendRedirect("index.zul");
					}
					
				}else{
					alert("�������");
				}
			}else{
				alert("���ʺ��ѱ�ͣ�ã�����ϵϵͳ����Ա���ø��ʺ�");
			}
			
		}else{
			alert("�û�������");
		}
	}
}
