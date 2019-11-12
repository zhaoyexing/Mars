package com.st.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

/**
 * <p>
 * 邮件发送类
 * </p>
 * <p>
 * 支持普通模式和HTML模式，可发送多个附件，支持SMTP服务器认证。<br>
 * 基于javamail开发，使用时请将javamail包含在classpath系统变量中。
 * </p>
 * <p>
 * <br>
 * 使用说明：
 * </p>
 * <p>
 * Mail mail=new Mail();
 * </p>
 * <p>
 * mail.setXXX ....
 * </p>
 * <p>
 * mail.send();<br>
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class Mail {
	private static Logger log=LoggerFactory.getLogger(Mail.class);
	//收件人地址
	private Address[] to = null;
	//抄送地址
	private Address[] cc = null;
	//设置暗送地址
	private Address[] bcc = null;
	private String from = "";
	private String title = "";
	private String content = "";
	private String smtpHost = "";
	private int smtpPort = 25;
	private String content_type = MODE_TEXT;
	private String htmlMailDesc = "";

	private String smtpUser = "";
	private String smtpPassword = "";
	private boolean isAuthenticationSMTP = false;

	private Vector vFiles = new Vector();
	private Vector vURLs = new Vector();
	private Vector imageFiles = new Vector();
	private Vector imageURLs = new Vector();


	public Mail() {
	}

	/**
	 * 设置SMTP服务器，使用默认端口
	 * 
	 * @param server
	 *            SMTP服务器IP
	 */
	public void setSmtpHost(String server) {
		this.smtpHost = server;
	}

	/**
	 * 设置SMTP服务器
	 * 
	 * @param server
	 *            SMTP服务器IP
	 * @param port
	 *            端口
	 */
	public void setSmtpHost(String server, int port) {
		this.smtpHost = server;
		this.smtpPort = port;
	}

	/**
	 * 设置收件人地址
	 * 
	 * @param aEmail
	 *            收件人Email地址
	 */
	public void setTo(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.to = getAddress(s);
	}

	/**
	 * 设置多个收件人地址
	 * 
	 * @param Emails
	 *            收件人Email地址
	 */
	public void setTo(String[] Emails) {
		this.to = getAddress(Emails);
	}

	/**
	 * 设置抄送地址
	 * 
	 * @param aEmail
	 *            抄送地址
	 */
	public void setCC(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.cc = getAddress(s);
	}

	/**
	 * 设置多个抄送地址
	 * 
	 * @param Emails
	 *            抄送地址
	 */
	public void setCC(String[] Emails) {
		this.cc = getAddress(Emails);
	}

	/**
	 * 设置暗送地址
	 * 
	 * @param
	 *
	 */

	public void setBCC(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.bcc = getAddress(s);
	}

	/**
	 * 设置多个暗送地址
	 * 
	 * @param Emails
	 *            暗送地址
	 */
	public void setBCC(String[] Emails) {
		this.bcc = getAddress(Emails);
	}

	/**
	 * 设置发件人地址
	 * 
	 * @param aEmail
	 *            发件人地址
	 */
	public void setFrom(String aEmail) {
		// if(!isValidEmailAddress(aEmail)){
		// throw new MyException("Invalid Email Address");
		// }
		this.from = aEmail;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param mailTitle
	 *            邮件主题
	 */
	public void setSubject(String mailTitle) {
		//this.title = mailTitle;
		/*try {
			  // MimeUtility.encodeText(String text, String charset, String
            // encoding) throws java.io.UnsupportedEncodingException
            // text 头值 . charset 字符集。如果此参数为 null，则使用平台的默认字符集。
            // encoding 要使用的编码。当前支持的值为 "B" 和 "Q"。如果此参数为 null，则在大部分字符使用 ASCII
            // 字符集编码时使用 "Q" 编码；其他情况使用 "B" 编码。
			this.title = MimeUtility.encodeText(new String(	mailTitle.getBytes("UTF-8"), "UTF-8"), "UTF-8", "B");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.title = mailTitle;
	}

	/**
	 * 设置邮件文字内容
	 * 
	 * @param mailContent
	 *            邮件文字内容
	 */
	public void setBody(String mailContent) {
		this.content = mailContent;
	}

	/**
	 * 设置邮件字符类型
	 * 
	 * @param contentType
	 *            请从静态变量TEXT和HTML中选择
	 */
	public void setContentType(String contentType) {
		this.content_type = contentType;
	}

	/**
	 * 设置HTML格式邮件在一般模式下显示的说明
	 * 
	 * @param desc
	 *            说明文字
	 */
	public void setHtmlMailDesc(String desc) {
		this.htmlMailDesc = desc;
	}

	/**
	 * 设置SMTP服务器用户认证
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void setSmtpAuthentication(String username, String password) {
		this.smtpUser = username;
		this.smtpPassword = password;
		this.isAuthenticationSMTP = true;
	}

	/**
	 * 添加附件
	 * 
	 * @param afile
	 *            本地文件
	 */
	public void addAttachment(File afile) {
		vFiles.add(afile);
	}

	/**
	 * 添加附件
	 * 
	 * @param fileURL
	 *            文件URL
	 */
	public void addAttachment(URL fileURL) {
		vURLs.add(fileURL);
	}
	public void addAttachmentImage(File afile) {
		imageFiles.add(afile);
	}

	/**
	 * 添加附件
	 * 
	 * @param fileURL
	 *            文件URL
	 */
	public void addAttachmentImage(URL fileURL) {
		imageURLs.add(fileURL);
	}
	/**
	 * 标示邮件是否附带附件
	 * 
	 * @return
	 */
	public boolean hasAttachment() {
		return vFiles.size() + vURLs.size() > 0;
	}
	/**
	 * 标示邮件是否附带图片
	 * 
	 * @return
	 */
	public boolean hasAttachmentImage() {
		return imageFiles.size() + imageURLs.size() > 0;
	}
	/**
	 * 发送邮件
	 */
	public void send() {
		try {
			Properties server = new Properties();
			if (StringUtils.isNull(this.smtpHost)) {
				throw new NullPointerException("Please set SMTP host");
			} else {
				server.put("mail.smtp.host", this.smtpHost);
			}
			server.put("mail.smtp.port", String.valueOf(this.smtpPort));
			if (this.isAuthenticationSMTP) {
				server.put("mail.smtp.auth", "true");
			}
			Session conn = Session.getInstance(server, null);

			MimeMessage msg = new MimeMessage(conn);
			if (StringUtils.isNull(this.from)) {
				throw new NullPointerException("Please set FROM address");
			} else {
				msg.setFrom(new InternetAddress(this.from));
			}
			if (this.to != null) {
				msg.setRecipients(Message.RecipientType.TO, this.to);
			}
			// else {
			// throw new NullPointerException("Please set TO address");
			// }
			if (this.cc != null) {
				msg.setRecipients(Message.RecipientType.CC, this.cc);
			}
			if (this.bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, this.bcc);
			}

			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			msg.setSubject("=?UTF-8?B?" + enc.encode(this.title.getBytes())
					+ "?=");

			if (!hasAttachment()) {
				Multipart mp = new MimeMultipart();
				// 如果没有带附件
				if (this.isHtmlModeMail()) {
					// 是HTML格式的邮件
					if (!this.hasHtmlDesc()) {
						msg.setContent(this.content, this.content_type);
					} else {	
						MimeBodyPart mbp = null;

						mbp = new MimeBodyPart();
						mbp.setContent(this.content, this.content_type);
						mp.addBodyPart(mbp);

						mbp = new MimeBodyPart();
						mbp.setContent(this.htmlMailDesc, this.MODE_TEXT);
						mp.addBodyPart(mbp);
					
						msg.setContent(mp);
					}
				} else {
					// 是文本格式的邮件
					msg.setText(this.content);
				}
			} else {
				// 有附件
				Multipart mp = new MimeMultipart();
				MimeBodyPart mbp = null;
				// 邮件正文
				for (int i = 0; i < vFiles.size(); i++) {
					mbp = new MimeBodyPart();
					File file = (File) vFiles.get(i);
					FileDataSource fds = new FileDataSource(file);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(file.getName()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}
				for (int i = 0; i < vURLs.size(); i++) {
					mbp = new MimeBodyPart();
					URL url = (URL) vURLs.get(i);
					// URLDataSource uds=new URLDataSource(url);
					mbp.setDataHandler(new DataHandler(url));
					try {
						mbp.setFileName(MimeUtility.encodeText(url.getFile()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}

				mbp = new MimeBodyPart();
				mbp.setContent(this.content, this.content_type);
				mp.addBodyPart(mbp);

				if (this.isHtmlModeMail() && this.hasHtmlDesc()) {
					mbp = new MimeBodyPart();
					mbp.setContent(this.htmlMailDesc, this.MODE_TEXT);
					mp.addBodyPart(mbp);
				}

				msg.setContent(mp);
			}
			msg.saveChanges();
			if (this.isAuthenticationSMTP) {
				Transport transport = conn.getTransport("smtp");
				transport.connect(this.smtpHost, this.smtpUser,
						this.smtpPassword);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} else {
				Transport.send(msg, msg.getAllRecipients());
			}

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public boolean isValidEmailAddress(String email) {
		if (StringUtils.isNull(email))
			return false;
		if (email.indexOf("@") > 0)
			return !email.endsWith("@");
		return false;
	}

	private Address[] getAddress(String[] add) {
		Address[] a = new Address[add.length];
		for (int i = 0; i < add.length; i++) {
			try {
				a[i] = new InternetAddress(add[i]);
			} catch (AddressException ex) {
				ex.printStackTrace();
			}
		}
		return a;
	}

	public boolean isHtmlModeMail() {
		return this.content_type.equals(this.MODE_HTML);
	}

	public boolean hasHtmlDesc() {
		if (!this.isHtmlModeMail())
			return false;
		return !StringUtils.isNull(this.htmlMailDesc);
	}

	/**
	 * 普通模式
	 */
	public static final String MODE_TEXT = "text/plain;charset=UTF-8";

	/**
	 * HTML模式
	 */
	public static final String MODE_HTML = "text/html;charset=UTF-8";
	/**
	 * 对外提供的发送邮件接口
	 * @param mailAddress
	 * @param subject
	 * @param bodyText
	 */
	public static void  sendMail(String mailAddress,String subject,String bodyText){
		Mail mail=new Mail();
		mail.setSmtpHost("smtp.qiye.163.com");/** 设置SMTP **/
		String mailFrom="GaoHaoHao@social-touch.com";
		String password="Haozi_2016";
		mail.setFrom(mailFrom);
		mail.setSmtpAuthentication(mailFrom, password);/** 账号及密码 **/
		mail.setTo(mailAddress);/** 发送给谁 **/
		mail.setContentType(Mail.MODE_HTML);
		mail.setSubject(subject);/** 邮件主题 **/
		mail.setBody(bodyText);/** 邮件内容 **/
		mail.send();
	}
	
	/**
	 * 发送邮件
	 */
	public void sendMail(String htmlStr){
		
			//获取邮件服务器的配置
			Properties server=new Properties();
			if (StringUtils.isNull(this.smtpHost)) {
				throw new NullPointerException("Please set SMTP host");
			} else {
				server.put("mail.smtp.host", this.smtpHost);
			}
			server.put("mail.smtp.port", String.valueOf(this.smtpPort));
			if (this.isAuthenticationSMTP) {
				server.put("mail.smtp.auth", "true");
			}
			//获取发送邮件的session
			Session conn = Session.getInstance(server, null);
			try {
				MimeMessage createMixedMail = createMixedMail(conn,htmlStr);
				if (this.isAuthenticationSMTP) {
					Transport transport = conn.getTransport("smtp");
					transport.connect(this.smtpHost, this.smtpUser,this.smtpPassword);
					transport.sendMessage(createMixedMail, createMixedMail.getAllRecipients());
					transport.close();
				} else {
					Transport.send(createMixedMail, createMixedMail.getAllRecipients());
				}
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			
			
		
		
	}
	
	 public  MimeMessage createMixedMail(Session session,String htmlStr) throws Exception {
		          //创建邮件
		         MimeMessage msg = new MimeMessage(session);
		         if (StringUtils.isNull(this.from)) {
						throw new NullPointerException("Please set FROM address");
					} else {
						msg.setFrom(new InternetAddress(this.from));
					}
					if (this.to != null) {
						msg.setRecipients(Message.RecipientType.TO, this.to);
					}
					if (this.cc != null) {
						msg.setRecipients(Message.RecipientType.CC, this.cc);
					}
					if (this.bcc != null) {
						msg.setRecipients(Message.RecipientType.BCC, this.bcc);
					}

					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					msg.setSubject("=?UTF-8?B?" + enc.encode(this.title.getBytes())
							+ "?=");   
				
					  //正文
	                   MimeBodyPart text = new MimeBodyPart();     					
	                   text.setContent(htmlStr,"text/html;charset=UTF-8");
	                  

	                   //描述关系:正文和附件
	                   MimeMultipart mp2 = new MimeMultipart();
	                   mp2.setSubType("related");
	                   mp2.addBodyPart(text);
	                   msg.setContent(mp2);
	                   msg.saveChanges();
	                 //返回创建好的的邮件
	                 return msg;
		     }
}
