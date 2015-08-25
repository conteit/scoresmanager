package it.conteit.scoresmanager.data;
import junit.framework.TestCase;

public abstract class AbstractTestColor extends TestCase{
	public void testNewColor(){
		IColor<?> c = null;
		try {
			c = createColor();
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
		assertEquals(c.getValue(), getContainedColor());
		
		try{
			IColor<?> c2 = createColorWithNull();
			fail(c2.toString() + " must be invalid");
		} catch(Exception e){
			assertTrue(e instanceof InconsistencyException);
		}
	}
	
	/*Creates a IColor<?>*/
	public abstract IColor<?> createColor() throws Exception;
	
	/*Creates a IColor<?> that contains null*/
	public abstract IColor<?> createColorWithNull() throws Exception;
	
	/*Refers to the color implementation contained in IColor<?>*/
	public abstract Object getContainedColor();
}
