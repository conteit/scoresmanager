package it.conteit.scoresmanager.data;

public class TestColor extends AbstractTestColor {

	@Override
	public IColor<?> createColor() throws Exception {
		return new Color((java.awt.Color) getContainedColor());
	}

	@Override
	public IColor<?> createColorWithNull() throws Exception {
		return new Color(null);
	}

	@Override
	public Object getContainedColor() {
		return new java.awt.Color(0,0,0);
	}

}
