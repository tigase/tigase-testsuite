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

import java.util.Map;
import tigase.test.TestAbstract;
import tigase.test.util.Params;
import javax.management.Attribute;
import tigase.test.ResultsDontMatchException;
import tigase.xml.Element;
import tigase.test.util.ElementUtil;
import tigase.test.util.EqualError;

import static tigase.util.JIDUtils.*;


/**
 * Describe class TestPrivacyList here.
 *
 *
 * Created: Tue Dec 12 18:07:02 2006
 *
 * @author <a href="mailto:artur.hefczyc@gmail.com">Artur Hefczyc</a>
 * @version $Rev$
 */
public class TestPrivacyListBlockMsg extends TestAbstract {

  private String user_name = "test_user@localhost";
  private String user_resr = "xmpp-test";
  private String user_emil = "test_user@localhost";
  private String hostname = "localhost";
  private String jid = null;
  private String id = null;
	private String xmlns = "jabber:iq:privacy";

  private String[] elems = {"iq", "iq", "iq"};
  private int counter = 0;

  private Element expected_result, optional_result = null;
  private Attribute[] result = null;
  private Attribute[] result_2 = null;
  private String[] resp_name = null;
  private int res_cnt = 0;

	/**
	 * Creates a new <code>TestPrivacyList</code> instance.
	 *
	 */
	public TestPrivacyListBlockMsg() {
    super(
      new String[] {"jabber:client"},
      new String[] {"privacy-block-msg"},
      new String[] {"stream-open", "auth", "xmpp-bind"},
      new String[] {"user-register", "tls-init"}
      );
	}

  // Implementation of tigase.test.TestAbstract

  /**
   * Describe <code>nextElementName</code> method here.
   *
   * @param element an <code>Element</code> value
   * @return a <code>String</code> value
   * @exception Exception if an error occurs
   */
  public String nextElementName(final Element element) throws Exception {
		boolean error = false;
		String message = null;
		if (element != null) {
			if (expected_result != null) {
				EqualError res1 = ElementUtil.equalElemsDeep(expected_result, element);
				if (!res1.equals && optional_result != null) {
					EqualError res2 = ElementUtil.equalElemsDeep(optional_result, element);
					if (!res2.equals) {
						error = true;
						message = res1.message + ", " + res2.message;
					}
				} else {
					if (!res1.equals) {
						error = true;
						message = res1.message;
					}
				}
			} // end of if (ElementUtil.equalElemsDeep(expected_result, query))
		} else {
			if (expected_result != null) {
				error = true;
				message = "Missing expected element: " + expected_result.getName();
			} // end of if (expected_result == null)
		} // end of else
		if (error) {
			throw new ResultsDontMatchException(getClass().getName() +
				", expected: '" + expected_result + "', Received: '" + element + "'"
				+ ", equals error: " + message);
		} // end of if (error)
    if (counter < elems.length) {
      return elems[counter++];
    } // end of if (counter < elems.length)
    return null;
  }

  /**
   * Describe <code>getElementData</code> method here.
   *
   * @param string a <code>String</code> value
   * @return a <code>String</code> value
   * @exception Exception if an error occurs
   */
  public String getElementData(final String string) throws Exception {
    result = new Attribute[] {
      new Attribute("type", "result"),
      new Attribute("id", "privacy_" + counter),
      new Attribute("to", jid),
    };
    res_cnt = 0;
    switch (counter) {
    case 1:
			expected_result = new Element("iq",
				new String[] {"type", "id"},
				new String[] {"result", "privacy_" + counter});
      resp_name = new String[] {"iq"};
      return
        "<iq type=\"set\" id=\"privacy_" + counter + "\" from=\"" + jid + "\">"
        + "<query xmlns=\"" + xmlns + "\">"
				+ "<list name='no-msg'>"
        + "<item action='deny' order='1'>"
				+ "<message/>"
        + "</item>"
				+ "</list>"
        + "</query>"
        + "</iq>";
    case 2:
			expected_result = new Element("iq",
				new String[] {"type", "id"},
				new String[] {"result", "privacy_" + counter});
      resp_name = new String[] {"iq"};
      return
        "<iq type=\"set\" id=\"privacy_" + counter + "\" from=\"" + jid + "\">"
        + "<query xmlns=\"" + xmlns + "\">"
				+ "<active name=\"no-msg\"/>"
				+ "</query>"
        + "</iq>";
    case 3:
			expected_result = new Element("iq",
				new String[] {"type", "id"},
				new String[] {"result", "privacy_" + counter});
      resp_name = new String[] {"iq"};
      return
        "<iq type=\"set\" id=\"privacy_" + counter + "\" from=\"" + jid + "\">"
        + "<query xmlns=\"" + xmlns + "\">"
				+ "<default name=\"no-msg\"/>"
				+ "</query>"
        + "</iq>";
    default:
      return null;
    } // end of switch (counter)
  }

  /**
   * Describe <code>getRespElementNames</code> method here.
   *
   * @param string a <code>String</code> value
   * @return a <code>String[]</code> value
   * @exception Exception if an error occurs
   */
  public String[] getRespElementNames(final String string) throws Exception {
    return resp_name;
  }

  /**
   * Describe <code>getRespOptionalNames</code> method here.
   *
   * @param string a <code>String</code> value
   * @return a <code>String[]</code> value
   * @exception Exception if an error occurs
   */
  public String[] getRespOptionalNames(final String string) throws Exception {
    return null;
  }

  /**
   * Describe <code>getRespElementAttributes</code> method here.
   *
   * @param string a <code>String</code> value
   * @return an <code>Attribute[]</code> value
   * @exception Exception if an error occurs
   */
  public Attribute[] getRespElementAttributes(final String string) throws Exception {
    ++res_cnt;
		return result;
  }

  // Implementation of tigase.test.TestIfc

  /**
   * Describe <code>init</code> method here.
   *
   * @param params a <code>Params</code> value
   */
  public void init(final Params params, Map<String, String> vars) {
    super.init(params, vars);
    user_name = params.get("-user-name", user_name);
    user_resr = params.get("-user-resr", user_resr);
    user_emil = params.get("-user-emil", user_emil);
    hostname = params.get("-host", hostname);
    String name = getNodeNick(user_name);
    if (name == null || name.equals("")) {
      jid = user_name + "@" + hostname + "/" + user_resr;
    } else {
      jid = user_name + "/" + user_resr;
    } // end of else
    if (name == null || name.equals("")) {
      id = user_name + "@" + hostname;
    } else {
      id = user_name;
    } // end of else
  }

} // TestPrivacyList
