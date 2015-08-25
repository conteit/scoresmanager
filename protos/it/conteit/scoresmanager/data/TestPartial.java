package it.conteit.scoresmanager.data;

public class TestPartial extends AbstractTestScore {

	@Override
	public IScore createScore(String name, int teamCount) throws Exception {
		return Partial.create(name, teamCount);
	}

	@Override
	public IScore createScore(String name, Integer[] values) throws Exception {
		return Partial.create(name, values);
	}

	@Override
	public int scoreType() {
		return 1;
	}

}
