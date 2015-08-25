package it.conteit.scoresmanager.data;

public class TestScore extends AbstractTestScore {

	@Override
	public IScore createScore(String name, int teamCount) throws Exception {
		return Score.create(name, teamCount);
	}

	@Override
	public IScore createScore(String name, Integer[] values) throws Exception {
		return Score.create(name, values);
	}

	@Override
	public int scoreType() {
		return 0;
	}

}
