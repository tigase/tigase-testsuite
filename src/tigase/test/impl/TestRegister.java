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

import java.util.List;
import tigase.test.util.Params;
import tigase.test.TestAbstract;
import javax.management.Attribute;
import tigase.xml.Element;

/**
 * Describe class TestRegister here.
 *
 *
 * Created: Wed May 18 12:14:52 2005
 *
 * @author <a href="mailto:artur.hefczyc@gmail.com">Artur Hefczyc</a>
 * @version $Rev$
 */
public class TestRegister extends TestAbstract {

  private String user_name = "test_user@localhost";
  private String user_pass = "test_pass";
  private String user_emil = "test_user@localhost";

  private String[] elems = {"iq", "iq"};
  private int counter = 0;

  /**
   * Creates a new <code>TestRegister</code> instance.
   *
   */
  public TestRegister() {
    super(
      new String[] {"jabber:client"},
      new String[] {"user-register"},
      new String[] {"socket", "stream-open"},
      new String[] {"ssl-init", "tls-init"}
      );
  }

  public String nextElementName(final Element reply) {
    if (counter < elems.length) {
      return elems[counter++];
    } // end of if (counter < elems.length)
    return null;
  }

  public String getElementData(final String element) throws Exception {
    switch (counter) {
    case 1:
      return "<iq type='get' id='reg1'><query xmlns='jabber:iq:register'/></iq>";
    case 2:
      return
        "<iq type='set' id='reg2'>"
        + "<query xmlns='jabber:iq:register'>"
        + "<username>" + user_name + "</username>"
        + "<password>" + user_pass + "</password>"
        + "<email>" + user_emil + "</email>"
        + "</query>" +
        "</iq>";
    default:
      return null;
    } // end of switch (counter)
  }

  public String[] getRespElementNames(final String element) {
    return new String[] {"iq"};
  }

  public Attribute[] getRespElementAttributes(final String element) {
    switch (counter) {
    case 1:
      return new Attribute[]
      {
        new Attribute("type", "result"),
        new Attribute("id", "reg1")
      };
    case 2:
      return new Attribute[]
      {
        new Attribute("type", "result"),
        new Attribute("id", "reg2")
      };
    default:
      return null;
    } // end of switch (counter)
  }

  public String[] getRespOptionalNames(final String element) {
    return null;
  }

  // Implementation of tigase.test.TestIfc

  /**
   * Describe <code>init</code> method here.
   *
   * @param params a <code>Params</code> value
   */
  public void init(final Params params) {
    super.init(params);
    user_name = params.get("-user-name", user_name);
    user_pass = params.get("-user-pass", user_pass);
    user_emil = params.get("-user-emil", user_emil);
  }

} // TestRegister