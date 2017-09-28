package db;

import java.util.LinkedList;

import org.junit.Test;

import java.util.List;

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
	

	@Test
	public void testJoinColInfoMatchRow() {
		Database db = new Database();
		db.loadTable("t1");
		db.loadTable("t2");
		db.loadTable("t3");
		db.loadTable("t4");
		Table t1 = db.getTable("t1");
		Table t2 = db.getTable("t2");
		Table t3 = db.getTable("t3");
		Table t4 = db.getTable("t4");
		List<String> list2 = t1.joinColInfo( t2);
		System.out.println(list2);
		System.out.println(Join.join(t1, t2));
		System.out.println(Join.join(new Table[]{t1,t2,t3,t4}));
		System.out.println(Join.join(t3, t4));
	}

	@Test
	public void testJoin(){
		
	}

	public static void main(String[] args) {
		System.out.println("hehe");
		// jh61b.junit.TestRunner.runTests("all", TestCommands.class);
	}
}