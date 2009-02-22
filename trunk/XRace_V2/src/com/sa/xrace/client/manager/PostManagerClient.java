package com.sa.xrace.client.manager;


/**
 * @author Changpeng Pan
 * @version $Id: PostManagerClient.java,v 1.1 2008-11-17 07:32:26 cpan Exp $
 */
public interface PostManagerClient {
	public void sendLoginPostToServer();
	public void sendLogoutPostToServer();
	public void sendStartPostToServer();
	public void sendNormalPostToServer();
	public void sendIdlePostToServer();
	public void sendAccidentPostToServer();
	public void sendCarTypePostToServer();
}
