package it.conteit.scoresmanager.data;

public class TestTeam extends AbstractTestTeam {

	@Override
	public ITeam createTeam(String name, IColor<?> color) throws Exception {
		return Team.create(name, (Color) color);
	}

	@Override
	public IColor<?> getTeamColor() {
		try {
			return new Color(new java.awt.Color(3,3,3));
		} catch (InconsistencyException e) {
			fail(e.getMessage());
			return null;
		}
	}

	@Override
	public String getTeamName() {
		return "NewTeam";
	}

}
