/*
 * Tigase XMPP/Jabber Test Suite
 * Copyright (C) 2004-2017 "Tigase, Inc." <office@tigase.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation,  either version 3 of the
 * License, or (at your option) any later version.
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
package tigase.test;

import java.util.Map;
import tigase.test.util.Params;
import tigase.xml.Element;

/**
 * Describe interface TestIfc here.
 *
 *
 * Created: Tue May 17 19:15:00 2005
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public interface TestIfc {

  String[] baseXMLNS();

  String[] implemented();

  String[] depends();

  String[] optional();

  void init(Params params, Map<String, String> vars);

	void release();

  boolean run();

  ResultCode getResultCode();

  String getResultMessage();

  Element getLastResult();

  //List<HistoryEntry> getHistory();

	void setHistoryCollector(HistoryCollectorIfc histColl);


	String getName();

	void setName(String name);

} // TestIfc