///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.manager;

/**
 * @author Changpeng Pan
 * @version $Id$
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
