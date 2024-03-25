package com.sas.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.sql.*;
import java.util.*;

import org.apache.log4j.*;

/**
 * <code>DOSQLFormat</code> provides a means to produce concatenated messages in
 * language-neutral way. Use this to construct messages displayed for end users.
 *
 * <p>
 * <code>DOSQLFormat</code> takes a set of objects, formats them, then inserts
 * the formatted strings into the pattern at the appropriate places.
 *
 * <p>
 * <strong>Note:</strong> <code>DOSQLFormat</code> differs from the other
 * <code>Format</code> classes in that you create a <code>DOSQLFormat</code>
 * object with one of its constructors (not with a <code>getInstance</code>
 * style factory method). The factory methods aren't necessary because
 * <code>DOSQLFormat</code> doesn't require any complex setup for a given
 * locale. In fact, <code>DOSQLFormat</code> doesn't implement any locale
 * specific behavior at all. It just needs to be set up on a sentence by
 * sentence basis.
 *
 * <p>
 * Here are some examples of usage: <blockquote>
 * 
 * <pre>
 * Object[] arguments = {
 *     new Integer(7),
 *     new Date(System.currentTimeMillis()),
 *     "a disturbance in the Force"
 * };
 *
 * String result = DOSQLFormat.format(
 *     "At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.",
 *     arguments);
 *
 * <em>output</em>: At 12:30 PM on Jul 3, 2053, there was a disturbance
 *           in the Force on planet 7.
 *
 * </pre>
 * 
 * </blockquote> Typically, the message format will come from resources, and the
 * arguments will be dynamically set at runtime.
 *
 * <p>
 * Example 2: <blockquote>
 * 
 * <pre>
 * Object[] testArgs = {new Long(3), "MyDisk"};
 *
 * DOSQLFormat form = new DOSQLFormat(
 *     "The disk \"{1}\" contains {0} file(s).");
 *
 * System.out.println(form.format(testArgs));
 *
 * // output, with different testArgs
 * <em>output</em>: The disk "MyDisk" contains 0 file(s).
 * <em>output</em>: The disk "MyDisk" contains 1 file(s).
 * <em>output</em>: The disk "MyDisk" contains 1,273 file(s).
 * </pre>
 * 
 * </blockquote>
 *
 * <p>
 * The pattern is of the form: <blockquote>
 * 
 * <pre>
 * DOSQLFormatPattern := string ( "{" DOSQLFormatElement "}" string )*
 *
 * DOSQLFormatElement := argument { "," elementFormat }
 *
 * elementFormat := "time" { "," datetimeStyle }
 *                | "date" { "," datetimeStyle }
 *                | "number" { "," numberStyle }
 *                | "choice" { "," choiceStyle }
 *
 * datetimeStyle := "short"
 *                  | "medium"
 *                  | "long"
 *                  | "full"
 *                  | dateFormatPattern
 *
 * numberStyle := "currency"
 *               | "percent"
 *               | "integer"
 *               | numberFormatPattern
 *
 * choiceStyle := choiceFormatPattern
 * </pre>
 * 
 * </blockquote> If there is no <code>elementFormat</code>, then the argument
 * must be a string, which is substituted. If there is no
 * <code>dateTimeStyle</code> or <code>numberStyle</code>, then the default
 * format is used (for example, <code>NumberFormat.getInstance</code>,
 * <code>DateFormat.getTimeInstance</code>, or
 * <code>DateFormat.getInstance</code>).
 *
 * <p>
 * In strings, single quotes can be used to quote the "{" (curly brace) if
 * necessary. A real single quote is represented by ''. Inside a
 * <code>DOSQLFormatElement</code>, quotes are <strong>not</strong> removed. For
 * example, {1,number,$'#',##} will produce a number format with the pound-sign
 * quoted, with a result such as: "$#31,45".
 *
 * <p>
 * If a pattern is used, then unquoted braces in the pattern, if any, must
 * match: that is, "ab {0} de" and "ab '}' de" are ok, but "ab {0'}' de" and
 * "ab } de" are not.
 *
 * <p>
 * The argument is a number from 0 to 9, which corresponds to the arguments
 * presented in an array to be formatted.
 *
 * <p>
 * It is ok to have unused arguments in the array. With missing arguments or
 * arguments that are not of the right class for the specified format, a
 * <code>ParseException</code> is thrown. First, <code>format</code> checks to
 * see if a <code>Format</code> object has been specified for the argument with
 * the <code>setFormats</code> method. If so, then <code>format</code> uses that
 * <code>Format</code> object to format the argument. Otherwise, the argument is
 * formatted based on the object's type. If the argument is a
 * <code>Number</code>, then <code>format</code> uses
 * <code>NumberFormat.getInstance</code> to format the argument; if the argument
 * is a <code>Date</code>, then <code>format</code> uses
 * <code>DateFormat.getDateTimeInstance</code> to format the argument.
 * Otherwise, it uses the <code>toString</code> method.
 *
 * <p>
 * For more sophisticated patterns, you can use a <code>ChoiceFormat</code> to
 * get output such as: <blockquote>
 * 
 * <pre>
 * DOSQLFormat form = new DOSQLFormat("The disk \"{1}\" contains {0}.");
 * double[] filelimits = {0,1,2};
 * String[] filepart = {"no files","one file","{0,number} files"};
 * ChoiceFormat fileform = new ChoiceFormat(filelimits, filepart);
 * form.setFormat(1,fileform); // NOT zero, see below
 *
 * Object[] testArgs = {new Long(12373), "MyDisk"};
 *
 * System.out.println(form.format(testArgs));
 *
 * // output, with different testArgs
 * output: The disk "MyDisk" contains no files.
 * output: The disk "MyDisk" contains one file.
 * output: The disk "MyDisk" contains 1,273 files.
 * </pre>
 * 
 * </blockquote> You can either do this programmatically, as in the above
 * example, or by using a pattern (see {@link ChoiceFormat} for more
 * information) as in: <blockquote>
 * 
 * <pre>
 * form.applyPattern("There {0,choice,0#are no files|1#is one file|1#are {0,number,integer} files}.");
 * </pre>
 * 
 * </blockquote>
 * <p>
 * <strong>Note:</strong> As we see above, the string produced by a
 * <code>ChoiceFormat</code> in <code>DOSQLFormat</code> is treated specially;
 * occurances of '{' are used to indicated subformats, and cause recursion. If
 * you create both a <code>DOSQLFormat</code> and <code>ChoiceFormat</code>
 * programmatically (instead of using the string patterns), then be careful not
 * to produce a format that recurses on itself, which will cause an infinite
 * loop.
 * <p>
 * <strong>Note:</strong> formats are numbered by order of variable in the
 * string. This is <strong>not</strong> the same as the argument numbering! For
 * example: with "abc{2}def{3}ghi{0}...",
 * <ul>
 * <li>format0 affects the first variable {2}
 * <li>format1 affects the second variable {3}
 * <li>format2 affects the second variable {0}
 * <li>and so on.
 * </ul>
 * <p>
 * When a single argument is parsed more than once in the string, the last match
 * will be the final result of the parsing. For example,
 * 
 * <pre>
 * DOSQLFormat mf = new DOSQLFormat("{0,number,#.##}, {0,number,#.#}");
 * Object[] objs = { new Double(3.1415) };
 * String result = mf.format(objs);
 * // result now equals "3.14, 3.1"
 * objs = null;
 * objs = mf.parse(result, new ParsePosition(0));
 * // objs now equals {new Double(3.1)}
 * </pre>
 * <p>
 * Likewise, parsing with a DOSQLFormat object using patterns containing
 * multiple occurances of the same argument would return the last match. For
 * example,
 * 
 * <pre>
 * DOSQLFormat mf = new DOSQLFormat("{0}, {0}, {0}");
 * String forParsing = "x, y, z";
 * Object[] objs = mf.parse(forParsing, new ParsePosition(0));
 * // result now equals {new String("z")}
 * </pre>
 * <p>
 * You can use <code>setLocale</code> followed by <code>applyPattern</code> (and
 * then possibly <code>setFormat</code>) to re-initialize a
 * <code>DOSQLFormat</code> with a different locale.
 *
 * @see DataSet
 * @version 1.0 , 10/22/02
 * @author Tom Lo
 */
public class VARSQLFormat {
	private static Logger log = Logger.getLogger(VARSQLFormat.class);
	private static final int ch = (int) '\'';
	private static final int ch2 = (int) ':';
	private String sql = null;
	private String[] keys = null;
	private String pattern = null;

	public VARSQLFormat(String sqlPattern) {
		this.applyPattern(sqlPattern);
	}

	/**
	 * Convenience routine. Avoids explicit creation of DOSQLFormat, but doesn't
	 * allow future optimizations.
	 */
	final public static PreparedStatement prepareDBStatement(String sqlPattern, Connection conn, Map valus)
			throws SQLException {
		return new VARSQLFormat(sqlPattern).prepareStatement(conn, valus);
	}

	final public static PreparedStatement prepareDBStatementNotlog(String sqlPattern, Connection conn, Map valus)
			throws SQLException {
		return new VARSQLFormat(sqlPattern).prepareStatementNotlog(conn, valus);
	}

	private static final String START = "':";
	private static final String END = "'";

	final private static Object[] convertToSQLAndKeys(String s) {
		StringBuilder v = new StringBuilder(s);
		StringBuilder sb = new StringBuilder();
		int last = 0;
		int length = v.length();
		List ls = new ArrayList<String>();
		while (true) {
			int is = v.indexOf(START, last);
			if (is == -1) {
				sb.append(v.substring(last, length));
				break;
			}
			sb.append(v.substring(last, is));
			int ie = v.indexOf(END, is + 2);
			if (ie == -1) {
				sb.append(v.substring(is + 2, length));
				break;
			}
			sb.append('?');
			String key = v.substring(is + 2, ie);
			ls.add(key);
			last = ie + 1;
		}
		String[] sa = new String[ls.size()];
		ls.toArray(sa);
		Object[] oa = { sb.toString(), sa };
		return oa;
	}

	/**
	 * Sets the pattern. See the class description.
	 * 
	 * @param source
	 * @return
	 */
	final public void applyPattern(String sqlPattern) {
		this.pattern = sqlPattern;
		Object[] oa = convertToSQLAndKeys(sqlPattern);
		this.sql = (String) oa[0];
		this.keys = (String[]) oa[1];

		/*
		 * ArrayList al = new ArrayList(); StringReader sr = new
		 * StringReader(sqlPattern); StringBuffer sb = new StringBuffer();
		 * StringBuffer sbvalue = new StringBuffer(); boolean isappend = false;
		 * boolean isreally = false; try { while (true) { int i = 0; try { i =
		 * sr.read(); } catch (IOException ex) { throw new NullPointerException(
		 * "Can not read SQL pattern. [" + ex.getMessage() + "]"); } if (i ==
		 * -1) { break; }
		 * 
		 * switch (i) { case ch: { if (isreally) { isreally = false; isappend =
		 * false; sb.append('?'); al.add(sbvalue.toString()); sbvalue.delete(0,
		 * sbvalue.length()); break; } else if (isappend) { sb.append('\'');
		 * sb.append(sbvalue.toString()); sb.append('\''); sbvalue.delete(0,
		 * sbvalue.length()); isappend = false; break; } else { isappend = true;
		 * break; }
		 * 
		 * } case ch2: { if (isappend) { isreally = true; break; } } default: {
		 * if (isappend) { sbvalue.append((char) i); } else { sb.append((char)
		 * i); } } } } } finally { sr.close(); } this.pattern = sqlPattern;
		 * this.sql = sb.toString().trim(); this.keys = new String[al.size()];
		 * al.toArray(keys);
		 */
	}

	final public void addBatch(PreparedStatement psm, Map values) throws SQLException {
		bindPrepareStatemant(psm, values);
		psm.addBatch();
	}

	private void bindPrepareStatemant(PreparedStatement sm, Map values) throws SQLException {
		for (int i = 0, j = keys.length; i < j; i++) {
			String key = keys[i];
			Object value = null;
			// System.out.println(b+"::"+key);
			value = values.get(key);
			if (values == null || (!values.containsKey(key))) {
				throw new IllegalArgumentException("Can not find column[" + key + "] in VARSQL.\nSQL:" + pattern);
			}

			// if (value == null) {
			// sm.setNull(i + 1, Types.VARCHAR);
			// } else {
			sm.setObject(i + 1, value);
			// }
		}
	}

	/**
	 *
	 *
	 * @param conn
	 * @param values
	 * @return
	 */
	final public PreparedStatement prepareStatement(Connection conn, Map values) throws SQLException {
		log.debug(sql);
		log.debug(values);
		PreparedStatement sm = conn.prepareStatement(sql);
		bindPrepareStatemant(sm, values);
		return sm;
	}

	final public PreparedStatement prepareStatementNotlog(Connection conn, Map values) throws SQLException {
		PreparedStatement sm = conn.prepareStatement(sql);
		bindPrepareStatemant(sm, values);
		return sm;
	}

	public static void main(String[] args) throws Exception {
		String s = "d')";
		// VARSQLFormat v = new VARSQLFormat(s);
		// System.out.println("----->" + v.sql);
		Object oa[] = VARSQLFormat.convertToSQLAndKeys(s);
		String sql = (String) oa[0];
		System.out.println("----->" + sql);
		String[] sa = (String[]) oa[1];
		for (int i = 0, j = sa.length; i < j; i++) {
			System.out.println("----->" + sa[i]);
		}

	}

	public String getSql() {
		return sql;
	}

	public String getPattern() {
		return pattern;
	}

	public String[] getKeys() {
		return keys;
	}

}
