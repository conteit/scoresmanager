package it.conteit.scoresmanager.data;
import java.util.Date;

import junit.framework.TestCase;

public abstract class AbstractTestGrest extends TestCase{
	private final String GREST_NAME = "Grest";
	private final String DAY_NAME = "Day";
	private ITeam[] teams;
	
	public void setUp(){
		try {
			teams = new ITeam[] {createTeam("Team1"), createTeam("Team2")};
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testNewGrest(){
		try {
			IGrest g = createGrest(GREST_NAME);
			assertEquals(g.getName(), GREST_NAME);
			assertEquals(g.days().length, 0);
			
			IGrest g2 = createGrest(GREST_NAME, new Date(0));
			assertEquals(g2.getName(), GREST_NAME);
			assertEquals(g2.getCreationDate(), new Date(0));
			assertEquals(g.days().length, 0);
			
			IGrest g3 = createGrest(GREST_NAME, teams);
			assertEquals(g3.getName(), GREST_NAME);
			assertEquals(g3.teamCount(), 2);
			assertEquals(g.days().length, 0);
		} catch (Exception e) {
			fail(e.getMessage());
		}	
		
		try{
			IGrest g4 = createGrest(GREST_NAME, new ITeam[]{teams[0]});
			assertEquals(g4.teamCount(), 2);
		} catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	public void testGrestFailures(){
		try {
			IGrest g = createGrest(GREST_NAME, teams);
			g.addDay(createDay(DAY_NAME, 3));
			fail("Cannot be added");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		IGrest g = null;
		IDay d = null;
		
		try {
			g = createGrest(GREST_NAME, teams);
			d = createDay(DAY_NAME, 2);
			g.addDay(d);
			assertEquals(g.days().length, 1);
			
			g.removeTeam(g.teams()[1]);
			fail("Cannot be removed");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		g.removeDay(d);
		assertEquals(g.days().length, 0);
		
		try {
			g.removeTeam(g.teams()[1]);
			assertEquals(g.teamCount(), 1);
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
		
		try {
			g.addDay(d);
			assertEquals(g.days().length, 1);
			
			g.addTeam(teams[1]);
			fail("Cannot be added");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		g.removeDay(d);
		assertEquals(g.days().length, 0);
		
		try {
			g.addTeam(teams[1]);
			assertEquals(g.teamCount(), 2);
		} catch (InconsistencyException e) {
			fail(e.getMessage());
		}
	}
	
	public void testEquals(){
		try{
			assertEquals(createGrest(GREST_NAME),createGrest(GREST_NAME));
			assertFalse(createGrest(GREST_NAME).equals(createGrest(GREST_NAME + "2")));
		} catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	public void testDesc(){
		try {
			assertEquals(createGrest(GREST_NAME).toString(), GREST_NAME);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/*Creates a IGrest*/
	public IGrest createGrest(String name) throws Exception{
		return createGrest(name, new Date());
	}
	
	public abstract IGrest createGrest(String name, Date creationDate) throws Exception;
	public abstract IGrest createGrest(String name, ITeam[] teams) throws Exception;
	
	/*Creates a ITeam*/
	public abstract ITeam createTeam(String name) throws Exception;
	
	/*Creates a Iday*/
	public abstract IDay createDay(String name, int teamCount) throws Exception;
}
