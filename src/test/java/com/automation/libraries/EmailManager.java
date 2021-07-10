package com.automation.libraries;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailManager {
	final static Logger logger = Logger.getLogger(EmailManager.class);

	public String toAddress = "";
	public String ccAddress = "";
	public String bccAddress = "";

	// private List<String> emailAttachments = new ArrayList();

	/*
	 * public static void main(String[] args) { String emailBody =
	 * "Congradulations!" +"<br><br>"+
	 * "You passed our final interview and we are exited to make you a job offer! + "
	 * + "<br><br> Regards, <br>HR team";
	 * 
	 * List<String> attachments = new ArrayList<>(); EmailManager myEmail = new
	 * EmailManager(); myEmail.toAddress =
	 * "musabaytechcorp@gmail.com;frank@musabaytechnologies.com"; myEmail.ccAddress
	 * = "gintonic0729@gmail.com;jo0ooe89@gmail.com";
	 * myEmail.sendEmail(emailBody,attachments ); }
	 */

	public void sendEmail(String emailBody, List<String> attachments) {
		sendEmail("smtp.gmail.com", "587", "qa.testing.811@gmail.com", "Muralim811!!", "Automated Testing Email Report",
				emailBody, attachments);
	}

	public void sendEmail(String host, String port, final String emailUserID, final String emailUserPassword,
			String subject, String emailBody, List<String> attachments) {
		try {
			// set SMTP server properties
			Properties prop = new Properties();
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.user", emailUserID);
			prop.put("mail.password", emailUserPassword);
			logger.info("Step1> preparing email configuration...");

			// create a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUserID, emailUserPassword);
				}
			};

			Session session = Session.getInstance(prop, auth);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailUserID));

			msg.addRecipients(Message.RecipientType.TO, setMultipleEmails(toAddress));
			if ((!ccAddress.isEmpty()) && (!ccAddress.equals(null))) {
				msg.addRecipients(Message.RecipientType.CC, setMultipleEmails(ccAddress));
			}
			if ((!bccAddress.isEmpty()) && (!bccAddress.equals(null))) {
				msg.addRecipients(Message.RecipientType.BCC, setMultipleEmails(bccAddress));
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// create message parts
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailBody, "text/html");
			// create multi-parts
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			// add attachments
			if (attachments.size() > 0) {
				for (String singleAttachment : attachments) {
					MimeBodyPart attachPart = new MimeBodyPart();
					try {
						attachPart.attachFile(singleAttachment);
					} catch (Exception e) {
						logger.error("Attaching files to email failed...");
						logger.error(e.getMessage());
					}
					multipart.addBodyPart(attachPart);
				}
			}
			logger.info("Step2> Attaching report files & error screenshots...");
			msg.setContent(multipart);

			logger.info("Step3> Seding email in progress...");
			Transport.send(msg);

			logger.info("Step4> Sending email completed.");
		} catch (Exception e) {
			logger.error("Sending email failed ...");
			logger.error("Error: ", e);
		}
	}

	private Address[] setMultipleEmails(String emailAddresses) {
		String multipleEmails[] = emailAddresses.split(";");
		InternetAddress[] addresses = new InternetAddress[multipleEmails.length];
		try {
			for (int i = 0; i < multipleEmails.length; i++) {
				addresses[i] = new InternetAddress(multipleEmails[i]);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return addresses;
	}

	public void sendEmail(List<String> errorScreenshots) {
		String emailBody = "Test email by JavaMail API!" + "<br><br>" + "Automated Testing Reports included."
				+ "<br><br> Regards, <br>Test Automation Team";
		sendEmail(emailBody, errorScreenshots);

	}
}