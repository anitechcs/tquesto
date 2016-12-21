package com.anitech.tquesto.service;

import com.anitech.tquesto.domain.User;

/**
 * Email service interface
 * 
 * @author Tapas
 *
 */
public interface MailService {

	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

	public void sendActivationEmail(User user, String baseUrl);

	public void sendCreationEmail(User user, String baseUrl);

	public void sendPasswordResetMail(User user, String baseUrl);

}
