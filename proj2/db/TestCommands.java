package db;

import java.util.LinkedList;

import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class TestCommands {
	// private static final ByteArrayOutputStream outContent = new
	// ByteArrayOutputStream();
	// private static final ByteArrayOutputStream errContent = new
	// ByteArrayOutputStream();

	// @BeforeClass
	// public static void setUpStreams() {
	// System.setOut(new PrintStream(outContent));
	// System.setErr(new PrintStream(errContent));
	// }

	// @AfterClass
	// public static void cleanUpStreams() {
	// System.setOut(null);
	// System.setErr(null);
	// }

	// @Test
	// public void testCreate() {
	// Database db = new Database();
	// String[] colInfo = { "iq", "int", "what", "String" };
	// outContent.reset();
	// db.createTable("sbility", colInfo);
	// assertEquals("", outContent.toString());
	// errContent.reset();
	// db.createTable("sbility", new String[]{"hehe","hh"});
	// assertEquals("ERROR: TABLE ALREADY EXISTS.\r\n", errContent.toString());
	// assertEquals("what", db.tables.get("sbility").getColName(1));
	// assertEquals("String",
	// db.tables.get("sbility").getColumn("what").getType());
	// }

	// @Test
	// public void testInsert() {
	// Database db = new Database();
	// String[] colInfo = { "iq", "int", "what", "string" };
	// String name = "sbility";
	// db.createTable(name, colInfo);
	// assertEquals(2, db.getTable(name).colNum());
	// db.insertRowInto(name, new String[]{"80","'fool you'"});
	// db.insertRowInto(name, new String[]{"90","'fool you twice'"});
	// assertEquals("'fool you twice'",
	// db.getTable(name).getColumn("what").get(1));
	// db.insertRowInto(name, new String[]{"gg","800"});
	// db.printTable(name);
	// }

	// @Test
	// public void testLoad() {
	// Database db = new Database();
	// String name = "teams";
	// db.loadTable(name);
	// db.printTable(name);
	// }

//	 @Test
//	 public void testStore() {
//	 Database db = new Database();
//	 String[] colInfo = { "iq", "int", "what", "string" };
//	 String name = "sbility";
//	 db.createTable(name, colInfo);
//	 db.insertRowInto(name, new String[] { "80", "'fool you'" });
//	 db.insertRowInto(name, new String[] { "90", "'fool you twice'" });
//	 db.storeTable(name);
//	 db.printTable(name);
//	 }

	// @Test
	// public void testCommonColsAndIndexesOf() {
	// Database db = new Database();
	// db.loadTable("t1");
	// db.loadTable("t2");
	// Table t1 = db.getTable("t1");
	// Table t2 = db.getTable("t2");
	// LinkedList<String> common = new LinkedList<>();
	// common.add("x");
	// assertEquals(common, t1.commonCols(t2));
	// Table.Column col = t1.getColumn(0);
	// ArrayList<Integer> ids = new ArrayList<Integer>();
	// ids.add(2);
	// ids.add(4);
	// assertEquals(ids, col.indexesOf("13"));
	// ArrayList<Integer> ids2 = new ArrayList<Integer>();
	// assertEquals(ids2, col.indexesOf("15"));
	//
	// }
	

//	@Test
//	public void testJoinColInfoMatchRow() {
//		Database db = new Database();
//		db.loadTable("t1");
//		db.loadTable("t2");
//		db.loadTable("t3");
//		db.loadTable("t4");
//		Table t1 = db.getTable("t1");
//		Table t2 = db.getTable("t2");
//		Table t3 = db.getTable("t3");
//		Table t4 = db.getTable("t4");
//		List<String> list2 = t1.joinColInfo( t2);
//		System.out.println(list2);
//		System.out.println(Join.join(t1, t2));
//		System.out.println(Join.join(new Table[]{t1,t2,t3,t4}));
//		System.out.println(Join.join(t3, t4));
//	}
public static final String INT = "int";
public static final String FLOAT = "float";
public static final String STRING = "string";

	// @Test
	// public void testAdd(){
	// 	assertEquals("11",Operation.add("5",INT, "6",INT));
	// 	assertEquals("11.9",Operation.add("5",INT, "6.9",FLOAT));
	// 	assertEquals("12.3",Operation.add("5.5",FLOAT, "6.8",FLOAT));
	// 	assertEquals("'hehexixi'",Operation.add("'hehe'",STRING, "'xixi'",STRING));
	// 	assertEquals("NaN",Operation.add("5", INT,"NaN",INT));
	// 	assertEquals("6.0",Operation.add("NOVALUE", FLOAT,"6",INT));
	// 	assertEquals("5",Operation.add("5", INT, "NOVALUE",INT));
	// 	assertEquals("5.0",Operation.add("5", INT, "NOVALUE",FLOAT));
	// 	assertEquals("5.8",Operation.add("5.8", FLOAT, "NOVALUE",INT));
	// 	assertEquals("'HEHE'",Operation.add("'HEHE'", STRING, "NOVALUE",STRING));
		
	// }
	// @Test
	// public void testMinus(){
	// 	assertEquals("11",Operation.minus("17",INT, "6",INT));
	// 	assertEquals("11.0",Operation.minus("17",INT, "6.0",FLOAT));
	// 	assertEquals("1.1",Operation.minus("8", INT,"6.9",FLOAT));
	// 	assertEquals("-1.3",Operation.minus("5.5", FLOAT,"6.8",FLOAT));
	// 	assertEquals("NaN",Operation.minus("5",INT, "NaN",INT));
	// 	assertEquals("NOVALUE",Operation.minus("NOVALUE",INT, "NOVALUE",INT));
	// 	assertEquals("5",Operation.minus("5", INT,"NOVALUE",INT));
	// 	assertEquals("5.0",Operation.minus("5", INT,"NOVALUE",FLOAT));
	// 	assertEquals("5.8",Operation.minus("5.8", FLOAT,"NOVALUE",INT));
	// 	assertEquals("-5.8",Operation.minus("NOVALUE",INT,"5.8",FLOAT));
	// }
	// @Test
	// public void testMultiply(){
	// 	assertEquals("60",Operation.multiply("10", INT,"6",INT));
	// 	assertEquals("60.0",Operation.multiply("10",INT, "6.0",FLOAT));
	// 	assertEquals("-1.21",Operation.multiply("1.1", FLOAT,"-1.1",FLOAT));
	// 	assertEquals("37.4",Operation.multiply("5.5", FLOAT,"6.8",FLOAT));
	// 	assertEquals("NaN",Operation.multiply("5",INT, "NaN",INT));
	// 	assertEquals("NOVALUE",Operation.multiply("NOVALUE", INT,"NOVALUE",INT));
	// 	assertEquals("0",Operation.multiply("5", INT,"NOVALUE",INT));
	// 	assertEquals("0.0",Operation.multiply("5", INT,"NOVALUE",FLOAT));
	// 	assertEquals("0.0",Operation.multiply("5.8",FLOAT, "NOVALUE",INT));
	// 	assertEquals("0.0",Operation.multiply("NOVALUE",INT,"5.8",FLOAT));
	// }
	// @Test
	// public void testDivide(){
	// 	assertEquals("2",Operation.divide("17",INT, "6",INT));
	// 	assertEquals("2.0",Operation.divide("12.0",FLOAT, "6.0",FLOAT));
	// 	assertEquals("4.0",Operation.divide("8",INT, "2.0",FLOAT));
	// 	assertEquals("2.576",Operation.divide("8.5",FLOAT, "3.3",FLOAT));
	// 	assertEquals("NaN",Operation.divide("5", INT,"NaN",INT));
	// 	assertEquals("NOVALUE",Operation.divide("NOVALUE",INT, "NOVALUE",INT));
	// 	assertEquals("NaN",Operation.divide("5", INT,"NOVALUE",INT));
	// 	assertEquals("NaN",Operation.divide("5.8", INT,"NOVALUE",FLOAT));
	// 	assertEquals("0.0",Operation.divide("NOVALUE",INT,"5.8",FLOAT));
	// 	assertEquals("0.0",Operation.divide("NOVALUE",INT,"5.0",FLOAT));
	// 	assertEquals("0.0",Operation.divide("NOVALUE",FLOAT,"8",INT));
	// 	assertEquals("0",Operation.divide("NOVALUE",INT,"8",INT));
	// }

	public static void testColFlt(){
		String[] exprs = {""};
		String[] conds;
		Table[] tbs;
	}
	public static void main(String[] args) {
		// System.out.println("hehe");
		jh61b.junit.TestRunner.runTests("all", TestCommands.class);
	}
}