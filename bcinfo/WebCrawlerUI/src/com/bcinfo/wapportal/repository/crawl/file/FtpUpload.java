/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

import com.bcinfo.wapportal.repository.crawl.dao.FtpConfigDao;

/**
 * @author dongq
 * 
 *         create time : 2009-10-20 下午07:10:14
 */
public class FtpUpload {

	public static void main(String[] args) {
		FtpUpload ftp = new FtpUpload();
		System.out.println("start....");
		boolean bln = ftp.uploadFile(
				"C:/Download/FBI设套钓出间谍 五角大楼顶级科学家落网(图).xml",
				"/usr/local/dongq/remotedir/test.xml");
		System.out.println("end... ftp is " + bln);
	}

	String hostName = "218.205.231.65";
	// TODO 暂时的
	String userName = "dongq";

	String password = "20090714";

	String remoteDir = "/usr/local/dongq/remotedir";

	public String getRemoteDir() {
		return remoteDir;
	}

	String port = "21";
	private FtpConfigDao dao;

	public FtpUpload() {
		if (remoteDir == null || remoteDir.equalsIgnoreCase("")) {
			remoteDir = null;
		}
	}

	public FtpUpload(String code) {
		dao = new FtpConfigDao();
		Map<String, String> map = dao.getFtpConfig(code);
		this.hostName = map.get("ftp_host");
		this.password = map.get("ftp_password");
		this.port = map.get("ftp_port");
		this.remoteDir = map.get("ftp_dir");
		this.userName = map.get("ftp_user");
		toString();
	}

	public boolean uploadFile(String localfilename, String remotefilename) {
		FTPClient ftp = new FTPClient();
		int reply;
		try {
			ftp.connect(hostName, Integer.parseInt(port));
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return false;
			}
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
		}

		try {
			if (!ftp.login(userName, password)) {
				ftp.logout();
				return false;
			}

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// ftp.setFileType(FTP.ASCII_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			// ftp.changeWorkingDirectory(remoteDir);

			ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			InputStream input = new FileInputStream(localfilename);
			if (input == null) {
				System.out.println("本地文件不存在");

			}
			ftp.storeFile(remotefilename, input);
			input.close();
			ftp.logout();
		} catch (FTPConnectionClosedException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
			return false;
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
			return false;
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "FtpUpload [hostName=" + hostName + ", password=" + password
				+ ", port=" + port + ", remoteDir=" + remoteDir + ", userName="
				+ userName + "]";
	}

}
