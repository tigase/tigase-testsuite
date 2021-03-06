/*
 * Tigase Test Suite - Tigase Test Suite - automated testing framework for Tigase Jabber/XMPP Server.
 * Copyright (C) 2005 Tigase, Inc. (office@tigase.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 */
package tigase.test.impl;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Map;
import tigase.test.TestAbstract;
import tigase.test.ResultCode;
import javax.management.Attribute;
import tigase.xml.Element;
import tigase.test.util.Params;
import tigase.test.util.SocketBosh;

/**
 * Describe class TestScoket here.
 *
 *
 * Created: Tue May 17 20:19:38 2005
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public class TestSocketBosh extends TestAbstract {

	/**
	 * <code>RECEIVE_BUFFER_SIZE</code> defines a size for TCP/IP packets.
	 * XMPP data packets are quite small usually, below 1kB so we don't need
	 * big TCP/IP data buffers.
	 */
	private static final int RECEIVE_BUFFER_SIZE = 2*1024;
  private String hostname = null;
	private String serverip = null;
  private int port = 5280;
  private int socket_wait = 5000;
	private boolean keep_alive = false;
	private boolean ignore_presence = false;


  /**
   * Creates a new <code>TestScoket</code> instance.
   *
   */
  public TestSocketBosh() {
    super(
      new String[]
      {"jabber:client", "jabber:server", "jabber:component:accept"},
      new String[] {"socket-bosh"},
      null,
      null
      );
  }

	@Override
  public String nextElementName(final Element reply) {
    return null;
  }

	@Override
  public String getElementData(final String element) {
    return null;
  }

	@Override
  public String[] getRespElementNames(final String element) {
    return null;
  }

	@Override
  public Attribute[] getRespElementAttributes(final String element) {
    return null;
  }

	@Override
  public String[] getRespOptionalNames(final String element) {
    return null;
  }

  // Implementation of TestIfc

  static long connectCounter = 0;
  static long connectAverTime = 0;

  /**
   * Describe <code>run</code> method here.
   *
   * @return a <code>boolean</code> value
   */
	@Override
  public boolean run() {
    try {
      //      debug("socket create...\n");
      Socket client = new Socket();
      //      debug("socket setting reuse address to true...\n");
      client.setReuseAddress(true);
      //      debug("socket setting so timeout...\n");
      client.setSoTimeout(socket_wait);
			client.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
      //      debug("socket connecting...\n");
      //      System.out.println("socket-wait = " + socket_wait);
      long timeStart = System.currentTimeMillis();
      client.connect(new InetSocketAddress(serverip, port), socket_wait);
      long timeEnd = System.currentTimeMillis();
      long connectTime = timeEnd - timeStart;
      connectAverTime = (connectAverTime + connectTime) / 2;
      if (++connectCounter % 10 == 0) {
        debug("Socket connect aver time: " + connectAverTime + " ms\n");
      } // end of if (timeEnd - timeStart > 1000)
      //      debug("socket done.");
      params.put("socket", client);
// 			System.out.println("Creating SocketBosh");
			SocketBosh socket = new SocketBosh(client);
			socket.setIgnorePresence(ignore_presence);
			socket.setKeepAlive(keep_alive);
      params.put("socketxmlio", socket);
			params.put("bosh-mode", "true");
      return true;
    } catch (SocketTimeoutException e) {
      resultCode = ResultCode.PROCESSING_EXCEPTION;
      exception = e;
      return false;
    } catch (Exception e) {
      resultCode = ResultCode.PROCESSING_EXCEPTION;
      exception = e;
      e.printStackTrace();
			System.out.println("IP: " + serverip + ", PORT:" + port);
      return false;
    } // end of try-catch
  }

  /**
   * Describe <code>init</code> method here.
   *
	 * @param map a <code>Map</code> value
	 * @param vars
   */
	@Override
  public void init(final Params map, Map<String, String> vars) {
    super.init(map, vars);
		serverip = map.get("-serverip", "127.0.0.1");
		hostname = map.get("-host", "localhost");
    port = map.get("-port", port);
    socket_wait = params.get("-socket-wait", socket_wait);
		ignore_presence = params.get("-ignore-presence", ignore_presence);
		keep_alive = params.get("-keep-alive", keep_alive);
  }

} // TestScoket
