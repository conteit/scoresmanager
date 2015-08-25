package it.conteit.scoresmanager.data;

public class AlreadyAddedException extends InconsistencyException {
	private static final long serialVersionUID = 7355384969089497792L;

	public AlreadyAddedException(Object item) {
		super(item.toString() + " already insert in collection");
	}
	
}
