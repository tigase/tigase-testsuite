package tigase.test.junit;

import java.io.FileNotFoundException;

import tigase.test.impl.TestCommon;
import tigase.test.parser.ParseException;
import tigase.test.util.Params;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, ParseException {

		tigase.test.util.Params par = new Params();
		par.put("-source-file", "tests/data/vcard-temp.cot");

		TestCommon t = new TestCommon();
		t.init(par);

	}

}