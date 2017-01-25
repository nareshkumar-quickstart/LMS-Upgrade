package com.softech.vu360.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

public class SendMailService {
   private static Logger log = Logger.getLogger(SendMailService.class.getName());
		
   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {


      return sendSMTPMessage(toAddr,
                             ccAddr,
                             bccAddr,
                             fromAddr,
                             fromAddrPersonalName,
                             subject,
                             body,
                             null,
                             null,
                             null,
                             null);
   }


   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         InputStream inputStream,
                                         String filename,
                                         String attachment,
                                         Map<Object,Object> headers) {


		Session session = Session.getDefaultInstance(
				VU360Properties.getVU360Properties(), null);
		session.setDebug(log.isDebugEnabled());

		try {

			// create a message
			MimeMessage msg = new MimeMessage(session);

			InternetAddress address = new InternetAddress(fromAddr);
			if (fromAddrPersonalName != null) {
				try {
					address.setPersonal(fromAddrPersonalName);
				} catch (java.io.UnsupportedEncodingException uee) {
					log.error("Could not set Personal Name:"
							+ fromAddrPersonalName, uee);
				}
			}
			msg.setFrom(address);

			InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
			for (int i = 0; i < toAddr.length; ++i) {
				try {
					toAddresses[i] = new InternetAddress(toAddr[i]);
				} catch (AddressException ae) {
					log.error("Illegal toAddress found >>"+ toAddr[i]);
					return false;
				}
			}
			msg.setRecipients(Message.RecipientType.TO, toAddresses);

			if (ccAddr != null && ccAddr.length > 0) {
				InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
				for (int i = 0; i < ccAddr.length; ++i) {
					try {
						ccAddresses[i] = new InternetAddress(ccAddr[i]);
					} catch (AddressException ae) {
						log.error("Illegal ccAddress found >>"+ ccAddr[i]);
						return false;
					}
				}
				msg.setRecipients(Message.RecipientType.CC, ccAddresses);
			}

			if (bccAddr != null && bccAddr.length > 0) {
				InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
				for (int i = 0; i < bccAddr.length; ++i) {
					try {
						bccAddresses[i] = new InternetAddress(bccAddr[i]);
					} catch (AddressException ae) {
						log.error("Illegal bccAddress found >>"+ bccAddr[i]);
						return false;
					}
				}
				msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(body, "text/html");

			// create the Multipart and its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);

			// Set the body part headers... if any were specified
			if (headers != null && !headers.isEmpty()) {
				for (Iterator<Object> iter = headers.keySet().iterator(); iter
						.hasNext();) {
					String key = (String) iter.next();
					String value = (String) headers.get(key);
					mbp1.setHeader(key, value);
				}
			}

			if (inputStream != null) {
				// create the second message part
				/*
				 * MimeBodyPart mbp2 = new MimeBodyPart();
				 * 
				 * // attach the file to the message - TODO: change to actual
				 * mime type of attachment. mbp2.setDataHandler(new
				 * DataHandler(attachment, "text/plain"));
				 * mbp2.setFileName(filename);
				 */

				// Set the email attachment file
				MimeBodyPart attachmentPart = new MimeBodyPart();
				String mimneType = "application/octet-stream";
				ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(
						inputStream, mimneType);

				/*
				 * FileDataSource fileDataSource = new FileDataSource(filePath)
				 * {
				 * 
				 * @Override public String getContentType() { return
				 * "application/octet-stream"; } };
				 */
				attachmentPart.setDataHandler(new DataHandler(
						byteArrayDataSource));
				attachmentPart.setFileName(filename);

				mp.addBodyPart(attachmentPart);

			}

			// add the Multipart to the message
			msg.setContent(mp);

			// send the message
			Transport.send(msg);
		} catch (MessagingException mex) {
			log.error("Exception occurred during send of email message", mex);
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				log.error("NestedException:", ex);
				ex.printStackTrace();
			}
			return false;
		} catch (IOException iox) {
			log.error("Exception occurred during sending of email message", iox);
			iox.printStackTrace();
			return false;
		}
		return true;
   }

   public static boolean sendSMTPMessageMIME(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         String filename,
                                         String attachment) {

   //THIS METHOD ADDED TO SUPPORT .JAR ATTACHMENTS
      Session session = Session.getDefaultInstance(VU360Properties.getVU360Properties(), null);
      session.setDebug(log.isDebugEnabled());

      try {

          // create a message
          MimeMessage msg = new MimeMessage(session);

          InternetAddress address = new InternetAddress(fromAddr);
          if ( fromAddrPersonalName != null ) {
             try {
                address.setPersonal(fromAddrPersonalName);
             }
             catch(java.io.UnsupportedEncodingException uee) {
               log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
             }
          }
          msg.setFrom(address);


          InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
          for ( int i = 0; i < toAddr.length; ++i) {
             toAddresses[i] = new InternetAddress(toAddr[i]);

          }
          msg.setRecipients(Message.RecipientType.TO, toAddresses);


          if ( ccAddr != null && ccAddr.length > 0 ) {
             InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
             for ( int i = 0; i < ccAddr.length; ++i) {
                ccAddresses[i] = new InternetAddress(ccAddr[i]);
           }
             msg.setRecipients(Message.RecipientType.CC, ccAddresses);
          }


          if ( bccAddr != null && bccAddr.length > 0 ) {
             InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
             for ( int i = 0; i < bccAddr.length; ++i) {
                bccAddresses[i] = new InternetAddress(bccAddr[i]);
             }
             msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
          }


          msg.setSubject(subject);
          msg.setSentDate(new Date());

          // create and fill the first message part
          MimeBodyPart mbp1 = new MimeBodyPart();
          mbp1.setContent(body, "text/html");

          // create the Multipart and its parts to it
          Multipart mp = new MimeMultipart();
          mp.addBodyPart(mbp1);


          if (filename != null ) {
             // create the second message part
             MimeBodyPart mbp2 = new MimeBodyPart();

             // attach the file to the message
             DataSource ds = new  FileDataSource(attachment);
             mbp2.setDataHandler(new DataHandler(ds));
             mbp2.setFileName(filename);
             mp.addBodyPart(mbp2);

          }

          // add the Multipart to the message
          msg.setContent(mp);

          // send the message
          Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }
      return true;

   }



   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      return sendSMTPMessage(toAddr, ccAddr, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String[] toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      return sendSMTPMessage(toAddr, null, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      String[] toAddrs = new String[1];
      toAddrs[0] = toAddr;
      return sendSMTPMessage(toAddrs, null, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         InputStream inputStream,
                                         String filename) {
      String[] toAddrs = new String[1];
      toAddrs[0] = toAddr;
      return sendSMTPMessage(toAddrs, null, null, fromAddr, fromAddrPersonalName, subject, body, inputStream,filename, null,null);
   }

   public static boolean sendSMTPMessageMIME(String toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         String filename,
                                         String attachment) {
      String[] toAddrs = new String[1];
      toAddrs[0] = toAddr;
      return sendSMTPMessageMIME(toAddrs, null, null, fromAddr, fromAddrPersonalName, subject, body, filename, attachment);
   }

    public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {


        Session session = Session.getDefaultInstance(VU360Properties.getVU360Properties(), null);
        session.setDebug(log.isDebugEnabled());

        try {

            // Create a message
            MimeMessage msg = new MimeMessage(session);

            // Set the from address
            InternetAddress address = new InternetAddress(fromAddr);
            if ( fromAddrPersonalName != null ) {
                try {
                    address.setPersonal(fromAddrPersonalName);
                }
                catch(java.io.UnsupportedEncodingException uee) {
                    log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
                }
            }
            msg.setFrom(address);

            // Set the to addresses
            InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
            for ( int i = 0; i < toAddr.length; ++i) {
                toAddresses[i] = new InternetAddress(toAddr[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set the CC addresses
            if ( ccAddr != null && ccAddr.length > 0 ) {
                InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
                for ( int i = 0; i < ccAddr.length; ++i) {
                    ccAddresses[i] = new InternetAddress(ccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // Set the BCC addresses
            if ( bccAddr != null && bccAddr.length > 0 ) {
                InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
                for ( int i = 0; i < bccAddr.length; ++i) {
                    bccAddresses[i] = new InternetAddress(bccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Create the Multipart
            Multipart mp = new MimeMultipart();

            // Create the body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html");
            mp.addBodyPart(bodyPart);

            // Set the body part headers... if any were specified
            if (headers!=null && !headers.isEmpty()) {
                for (Iterator<Object> iter=headers.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    String value = (String) headers.get(key);
                    bodyPart.setHeader(key, value);
                }
            }

            // Add the Multipart to the message
            msg.setContent(mp);

            // Send the message
            Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }
      return true;

   }


    public static boolean sendSMTPMessage(String toAddr,
                                         String ccAddr,
                                         String bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {

        String[] toAddrs = null;
        if (toAddr!=null) {
            toAddrs = new String[1];
            toAddrs[0] = toAddr;
        }

        String[] ccAddrs = null;
        if (ccAddr!=null) {
            ccAddrs = new String[1];
            ccAddrs[0] = ccAddr;
        }

        String[] bccAddrs = null;
        if (bccAddr!=null) {
            bccAddrs = new String[1];
            bccAddrs[0] = bccAddr;
        }

      return sendSMTPMessage(toAddrs, ccAddrs, bccAddrs, fromAddr, fromAddrPersonalName, subject, body, headers);
    }


    public static boolean sendSMTPMessage(String smtpHost,
    									 String smtpPort,
                                         String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {


        Properties tempProps = new Properties();
        tempProps.put("mail.smtp.host", smtpHost);
        tempProps.put("mail.smtp.port", smtpPort);
        Session session = Session.getDefaultInstance(tempProps, null);
        session.setDebug(log.isDebugEnabled());

        try {

            // Create a message
            MimeMessage msg = new MimeMessage(session);

            // Set the from address
            InternetAddress address = new InternetAddress(fromAddr);
            if ( fromAddrPersonalName != null ) {
                try {
                    address.setPersonal(fromAddrPersonalName);
                }
                catch(java.io.UnsupportedEncodingException uee) {
                    log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
                }
            }
            msg.setFrom(address);

            // Set the to addresses
            InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
            for ( int i = 0; i < toAddr.length; ++i) {
                toAddresses[i] = new InternetAddress(toAddr[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set the CC addresses
            if ( ccAddr != null && ccAddr.length > 0 ) {
                InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
                for ( int i = 0; i < ccAddr.length; ++i) {
                    ccAddresses[i] = new InternetAddress(ccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // Set the BCC addresses
            if ( bccAddr != null && bccAddr.length > 0 ) {
                InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
                for ( int i = 0; i < bccAddr.length; ++i) {
                    bccAddresses[i] = new InternetAddress(bccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Create the Multipart
            Multipart mp = new MimeMultipart();

            // Create the body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html");
            mp.addBodyPart(bodyPart);

            // Set the body part headers... if any were specified
            if (headers!=null && !headers.isEmpty()) {
                for (Iterator<Object> iter=headers.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    String value = (String) headers.get(key);
                    bodyPart.setHeader(key, value);
                }
            }

            // Add the Multipart to the message
            msg.setContent(mp);

            // Send the message
            Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }
      return true;

   }


    public static boolean sendSMTPMessage(String smtpHost,
    									 String smtpPort,
                                         String toAddr,
                                         String ccAddr,
                                         String bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {

        String[] toAddrs = null;
        if (toAddr!=null) {
            toAddrs = new String[1];
            toAddrs[0] = toAddr;
        }

        String[] ccAddrs = null;
        if (ccAddr!=null) {
            ccAddrs = new String[1];
            ccAddrs[0] = ccAddr;
        }

        String[] bccAddrs = null;
        if (bccAddr!=null) {
            bccAddrs = new String[1];
            bccAddrs[0] = bccAddr;
        }

      return sendSMTPMessage(smtpHost, smtpPort, toAddrs, ccAddrs, bccAddrs, fromAddr, fromAddrPersonalName, subject, body, headers);
    }
}
