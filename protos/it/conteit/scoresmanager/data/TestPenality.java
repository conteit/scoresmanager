package it.conteit.scoresmanager.data;

public class TestPenality extends AbstractTestScore {

	@Override
	public IScore createScore(String name, int teamCount) throws Exception {
		return Penality.create(name, teamCount);
	}

	@Override
	public IScore createScore(String name, Integer[] values) throws Exception {
		return Penality.create(name, values);
	}

	@Override
	public int scoreType() {
		return 2;
	}

}
