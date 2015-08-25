package it.conteit.scoresmanager.data;
import junit.framework.TestCase;

public abstract class AbstractTestTeam extends TestCase{
	public void testNewTeam(){
		try{
			ITeam t = createTeam(getTeamName(), getTeamColor());
			assertEquals(t.getName(), getTeamName());
			assertEquals(t.getColor(), getTeamColor());
		}catch(Exception e){
			fail(e.getMessage());
		}

		try{
			ITeam t2 = createTeamWithNoColor(getTeamName());
			fail(t2.toString() + " must be invalid");
		} catch(Exception e){
			assertTrue(e instanceof InconsistencyException);
		}

	}
	
	public void testEquals(){
		try{
			assertEquals(createTeam(getTeamName(), getTeamColor()), createTeam(getTeamName(), getTeamColor()));
			assertFalse(createTeam("team1", getTeamColor()).equals(createTeam("team2", getTeamColor())));
		} catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	public void testDesc(){
		try{
			ITeam t = createTeam(getTeamName(), getTeamColor());
			assertEquals(t.toString(), getTeamName());
		}catch(Exception e){
			fail(e.getMessage());
		}
	}

	public ITeam createTeamWithNoColor(String name) throws Exception{
		return createTeam(name, null);
	}

	/*Create a Team*/
	public abstract ITeam createTeam(String name, IColor<?> color) throws Exception;

	public abstract String getTeamName();
	public abstract IColor<?> getTeamColor();
}
