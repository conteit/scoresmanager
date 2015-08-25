package it.conteit.scoresmanager.data;
import junit.framework.TestCase;

public abstract class AbstractTestScore extends TestCase{
	private final String SCORE_DESC = "ScoreName";

	public void testNewScoreFromCount(){
		try {
			IScore s = createScore(SCORE_DESC, 3);
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, 2);
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			createScore(SCORE_DESC, 1);
			fail("Must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			createScore(SCORE_DESC, 0);
			fail("Must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			createScore(SCORE_DESC, -1);
			fail("Must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void testNewScoreFormValues(){
		switch(scoreType()){
		case 0:
			ScoreFormValues();
			break;
		case 1:
			PartialFromValues();
			break;
		case 2:
			PenalityFromValues();
			break;
		default:
			fail("Not implemented - " + scoreType());
		}
	}

	public void PartialFromValues(){
		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {3, 0, 1});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {3, 1});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {1});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void PenalityFromValues(){
		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {-1, -2, -4});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {-1, -1});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {-1});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void ScoreFormValues(){
		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {-1, 0, 1});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {-1, 1});
			assertEquals(s.getDescription(), SCORE_DESC);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {1});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			IScore s = createScore(SCORE_DESC, new Integer[] {});
			fail(s.toString() + " must have 2 teams or more");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void testEquals(){
		try {
			assertEquals(createScore(SCORE_DESC, 3), createScore(SCORE_DESC, 3));
			assertEquals(createScore(SCORE_DESC, 5), createScore(SCORE_DESC, 5));
			assertFalse(createScore(SCORE_DESC, 3).equals(createScore(SCORE_DESC + "2", 3)));
			assertFalse(createScore(SCORE_DESC, 3).equals(createScore(SCORE_DESC, 5)));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testDesc(){
		try{
			assertEquals(createScore(SCORE_DESC, 3).toString(), SCORE_DESC);
		} catch(Exception e){
			fail(e.getMessage());
		}
	}

	public void testDescChange(){
		IScore s = null;
		try {
			s = createScore(SCORE_DESC, 5);

			assertEquals(SCORE_DESC, s.getDescription());

			s.setDescription(SCORE_DESC + "2");
			assertEquals(SCORE_DESC + "2", s.getDescription());		
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			s.setDescription("");
			fail("\"\" must be an invalid desc");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			s.setDescription(null);
			fail("NULL must be an invalid desc");
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	public void testValueChange(){
		switch(scoreType()){
		case 0:
			ScoreChange();
			break;
		case 1:
			PartialChange();
			break;
		case 2:
			PenalityChange();
			break;
		default:
			fail("Not implemented");
		}
	}

	public void ScoreChange(){
		try {
			IScore s = createScore(SCORE_DESC, 5);
			
			for(int i=0; i<s.teamCount(); i++){
				assertEquals(s.getValue(i), new Integer(0));
				
				s.setValue(i, i-2);
				assertEquals(s.getValue(i), new Integer(i-2));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void PartialChange(){
		try {
			IScore s = createScore(SCORE_DESC, 5);
			
			for(int i=0; i<s.teamCount(); i++){
				assertEquals(s.getValue(i), new Integer(0));
				
				s.setValue(i, i);
				assertEquals(s.getValue(i), new Integer(i));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void PenalityChange(){
		try {
			IScore s = createScore(SCORE_DESC, 5);
			
			for(int i=0; i<s.teamCount(); i++){
				assertEquals(s.getValue(i), new Integer(0));
				
				s.setValue(i, -i);
				assertEquals(s.getValue(i), new Integer(-i));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/*Creates a Score with teamCount*/
	public abstract IScore createScore(String name, int teamCount) throws Exception;

	/*Creates a Score with values*/
	public abstract IScore createScore(String name, Integer[] values) throws Exception;

	/*Returns 0 = Score, 1 = Partial, 2 = Penality*/
	public abstract int scoreType();
}
