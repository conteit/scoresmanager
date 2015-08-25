package it.conteit.scoresmanager.data;

public class TestDay extends AbstractTestDay {

	@Override
	public IDay createDay(String desc, int teamCount) throws Exception {
		return Day.create(desc, teamCount);
	}

	@Override
	public IScore createPartial(String desc, Integer[] values) throws Exception {
		return Partial.create(desc, values);
	}

	@Override
	public IScore createPenality(String desc, Integer[] values)
			throws Exception {
		return Penality.create(desc, values);
	}

}
