package it.conteit.scoresmanager.data;

import java.util.Date;

public class TestGrest extends AbstractTestGrest {

	@Override
	public IDay createDay(String name, int teamCount) throws Exception {
		return Day.create(name, teamCount);
	}

	@Override
	public IGrest createGrest(String name) throws Exception {
		return Grest.create(name);
	}

	@Override
	public IGrest createGrest(String name, ITeam[] teams) throws Exception {
		return Grest.create(name, teams);
	}

	@Override
	public IGrest createGrest(String name, Date creationDate) throws Exception {
		return Grest.create(name, creationDate);
	}

	@Override
	public ITeam createTeam(String name) throws Exception{
		return Team.create(name);
	}

}
