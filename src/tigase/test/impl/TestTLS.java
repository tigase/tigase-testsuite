/*  Package Jabber Server
 *  Copyright (C) 2001, 2002, 2003, 2004, 2005
 *  "Artur Hefczyc" <kobit@users.sourceforge.net>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * $Rev$
 * Last modified by $Author$
 * $Date$
 */
package tigase.test.impl;

import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.management.Attribute;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import tigase.test.TestAbstract;
import tigase.test.util.Params;
import tigase.test.util.SocketXMLIO;
import tigase.xml.Element;

/**
 * Describe class TestTLS here.
 *
 *
 * Created: Tue May 17 20:26:22 2005
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public class TestTLS extends TestAbstract {

  private String hostname = "localhost";
  private String keys_password = "keystore";
  private String trusts_password = "truststore";
  private String keys_file = "certs/keystore";
  private String trusts_file = "certs/truststore";
  private int socket_wait = 5000;

  private String[] elems = {"starttls", "stream:stream"};
  private int counter = 0;

  private SSLSocketFactory getSSLSocketFactory() throws Exception {
    SecureRandom sr = new SecureRandom();
    sr.nextInt();
    KeyStore keys = KeyStore.getInstance("JKS");
    keys.load(new FileInputStream(keys_file), keys_password.toCharArray());
    KeyStore trusts = KeyStore.getInstance("JKS");
    trusts.load(new FileInputStream(trusts_file), trusts_password.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keys, keys_password.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trusts);
    SSLContext sslContext = SSLContext.getInstance("TLS");
//     sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);
    sslContext.init(kmf.getKeyManagers(),
      new X509TrustManager[] {new FakeTrustManager()}, sr);
    return sslContext.getSocketFactory();
  }

  private Socket initSSLSocket(Socket socket) throws Exception {
    SSLSocketFactory factory = getSSLSocketFactory();
    socket.setSoTimeout(socket_wait);
    SSLSocket tlsClient =
      (SSLSocket)factory.createSocket(socket,
        socket.getInetAddress().getHostAddress(),
        socket.getPort(), true);
    tlsClient.setUseClientMode(true);
    tlsClient.startHandshake();
    return tlsClient;
  }

  /**
   * Creates a new <code>TestTLS</code> instance.
   *
   */
  public TestTLS() {
    super(
      new String[]
      {"jabber:client", "jabber:server", "jabber:component:accept"},
      new String[] {"tls-init"},
      new String[] {"socket", "stream-open"},
      null
      );
    try {
      // Just init SSL Engine to avoid delay during test
      getSSLSocketFactory();
    } catch (Exception e) { } // end of try-catch
  }

  // Implementation of TestIfc

  /**
   * Describe <code>init</code> method here.
   *
   * @param params a <code>Params</code> value
   */
  public void init(final Params params) {
    super.init(params);
    hostname = params.get("-host", hostname);
    keys_password = params.get("-keys-file-password", keys_password);
    trusts_password = params.get("-trusts-file-password", trusts_password);
    keys_file = params.get("-keys-file", keys_file);
    trusts_file = params.get("-trusts-file", trusts_file);
    socket_wait = params.get("-socket-wait", socket_wait);
  }

  public String nextElementName(final Element reply) throws Exception {
    if (counter < elems.length) {
      String elem = elems[counter++];
      if (elem.equals("stream:stream")) {
        Socket sock = initSSLSocket((Socket)params.get("socket"));
        params.put("socket", sock);
        params.put("socketxmlio", new SocketXMLIO(sock));
      }
      return elem;
    } // end of if (counter < elems.length)
    return null;
  }

  public String getElementData(final String element) {
    if (element.equals("starttls")) {
      return "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>";
    } // end of if (element.equals("starttls")
    if (element.equals("stream:stream")) {
      return "<stream:stream "
        + "xmlns='jabber:client' "
        + "xmlns:stream='http://etherx.jabber.org/streams' "
        + "to='" + hostname + "' "
        + "version='1.0'>";
    }
    return null;
  }

  public String[] getRespElementNames(final String element) {
    if (element.equals("starttls")) {
      return new String[] {"proceed"};
    }
    if (element.equals("stream:stream")) {
      return new String[] {"stream:stream", "stream:features"};
    }
    return null;
  }

  public Attribute[] getRespElementAttributes(final String element) {
    if (element.equals("proceed")) {
      return new Attribute[]
      {
        new Attribute("xmlns", "urn:ietf:params:xml:ns:xmpp-tls")
      };
    }
    if (element.equals("stream:stream")) {
      return new Attribute[]
      {
        new Attribute("xmlns", "jabber:client"),
        new Attribute("xmlns:stream", "http://etherx.jabber.org/streams"),
        new Attribute("from", hostname),
        new Attribute("version", "1.0")
      };
    }
    if (element.equals("stream:features")) {
      return new Attribute[] { };
    }
    return null;
  }

  public String[] getRespOptionalNames(final String element) {
    return null;
  }

  class FakeTrustManager implements X509TrustManager {

    private X509Certificate[] acceptedIssuers = null;

    public FakeTrustManager(X509Certificate[] ai) {
      acceptedIssuers = ai;
    }

    public FakeTrustManager() { }

    // Implementation of javax.net.ssl.X509TrustManager

    /**
     * Describe <code>checkClientTrusted</code> method here.
     *
     * @param x509CertificateArray a <code>X509Certificate[]</code> value
     * @param string a <code>String</code> value
     * @exception CertificateException if an error occurs
     */
    public void checkClientTrusted(final X509Certificate[] x509CertificateArray,
      final String string) throws CertificateException {
    }

    public void checkServerTrusted(final X509Certificate[] x509CertificateArray,
      final String string) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return acceptedIssuers;
    }

  }

} // TestTLS
