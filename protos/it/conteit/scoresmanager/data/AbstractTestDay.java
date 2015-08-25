package it.conteit.scoresmanager.data;
import junit.framework.TestCase;

public abstract class AbstractTestDay extends TestCase{
	private final String DAY_DESC = "08/08/08 - Day";
	public void testNewDay(){
		try {
			IDay d = createDay(DAY_DESC, 4);
			assertEquals(d.getDescription(), DAY_DESC);
			assertEquals(d.teamCount(), 4);
		} catch (Exception e) {
			e.printStackTrace();fail(e.getMessage());
		}
		
		try {
			IDay d = createDay(DAY_DESC, 7);
			assertEquals(d.getDescription(), DAY_DESC);
			assertEquals(d.teamCount(), 7);
		} catch (Exception e) {
			e.printStackTrace();fail(e.getMessage());
		}
		
		try {
			IDay d = createDay(DAY_DESC, 2);
			assertEquals(d.getDescription(), DAY_DESC);
			assertEquals(d.teamCount(), 2);
		} catch (Exception e) {
			e.printStackTrace();fail(e.getMessage());
		}
		
		try {
			createDay(DAY_DESC, 1);
			fail("1 Must fail");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try {
			createDay(DAY_DESC, -1);
			fail("-1 Must fail");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testEquals(){
		try {
			assertEquals(createDay(DAY_DESC, 5), createDay(DAY_DESC, 5));
			assertFalse(createDay(DAY_DESC, 5).equals(createDay(DAY_DESC, 3)));
			assertFalse(createDay(DAY_DESC, 5).equals(createDay(DAY_DESC + "3", 5)));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testDesc(){
		IDay d = null;
		
		try {
			d = createDay(DAY_DESC, 5);
			assertEquals(d.toString(), DAY_DESC);
			
			d.setDescription(DAY_DESC + "2");
			assertEquals(d.toString(), DAY_DESC + "2");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			d.setDescription("");
			fail("must be invalid");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try {
			d.setDescription(null);
			fail("must be invalid");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testScores(){
		IDay d = null;
		try {
			d = createDay(DAY_DESC, 2);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		IScore s = null;
		try {
			d.addScore(createPartial("1", new Integer[]{1,2}));
			s = createPartial("2", new Integer[]{2,1});
			d.addScore(s);
			assertEquals(d.scores().length, 2);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			d.addScore(createPartial("2", new Integer[]{2,1}));
			fail("already insert");
		} catch (Exception e) {
			assertEquals(d.scores().length, 2);
		}
		
		try {
			d.addScore(createPartial("2", new Integer[]{2,1,0}));
			fail("teamCount mismatch");
		} catch (Exception e) {
			assertEquals(d.scores().length, 2);
		}
		
		try {
			IScore ps = d.partialsSum();
			IScore ts = d.totalScore();
			
			assertEquals(ps, ts);
			assertEquals(ps.getValue(0), new Integer(3));
			assertEquals(ps.getValue(1), new Integer(3));
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
		
		try {
			d.addScore(createPenality("3", new Integer[]{-2,-1}));
			d.addScore(createPenality("4", new Integer[]{-1,-3}));
			
			assertEquals(d.scores().length, 4);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			IScore ps = d.penalitiesSum();
			IScore ts = d.totalScore();
			
			assertEquals(ps.getValue(0), new Integer(-3));
			assertEquals(ps.getValue(1), new Integer(-4));
			
			assertEquals(ts.getValue(0), new Integer(0));
			assertEquals(ts.getValue(1), new Integer(-1));
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
		
		IScore s2 = d.scores()[1];
		assertEquals(s, s2);
		
		d.removeScore(s);
		assertEquals(d.scores().length, 3);
		
		
		try {
			IScore ts = d.totalScore();
			
			assertEquals(ts.getValue(0), new Integer(-2));
			assertEquals(ts.getValue(1), new Integer(-2));
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
	
		try {
			d.addScore(s);
			assertEquals(d.scores().length, 4);
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
	}
	
	/*Creates a IDay*/
	public abstract IDay createDay(String desc, int teamCount) throws Exception;
	
	/*Creates a Partial*/
	public abstract IScore createPartial(String desc, Integer[] values) throws Exception;
	
	/*Creates a Penality*/
	public abstract IScore createPenality(String desc, Integer[] values) throws Exception;
}
