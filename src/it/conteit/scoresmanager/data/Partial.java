package it.conteit.scoresmanager.data;

public class Partial extends Score {

	public Partial(String desc, int teamCount) throws InconsistencyException {
		super(desc, teamCount);
	}

	public Partial(String desc, Integer[] values) throws InconsistencyException {
		super(desc, values);
		
		for(Integer v: values){
			if(v.longValue() < 0){
				throw new InconsistencyException("Invalid argument (values): all items must be >= 0");
			}
		}
	}
	
	public void setValue(int index, Integer newValue) throws InconsistencyException{
		if(newValue >= 0){
			super.setValue(index, newValue);
		} else throw new InconsistencyException("Invalid argument (newValue): must be >= 0");
	}
	
	public void subtract(IScore s) throws InconsistencyException {
		for(int i=0; i<teamCount(); i++){
			if(getValue(i) < s.getValue(i)){
				throw new InconsistencyException("Invalid argument (s.getValue(" + i + "): too big");
			}
		}
	}
	
	/**
	 * Factory method
	 * 
	 * @param desc		Description of the new score
	 * @param values	Values related to this score
	 */
	public static IScore create(String desc, Integer[] values) throws InconsistencyException{
		return new Partial(desc, values);
	}

	/**
	 * Factory method
	 * 
	 * @param desc		Description of the new score
	 * @param teamCount	Number of teams involved
	 */
	public static IScore create(String desc, int teamCount) throws InconsistencyException{
		return new Partial(desc, teamCount);
	}
}
