package it.conteit.scoresmanager.control.management.visitors;

import it.conteit.scoresmanager.data.*;

public interface IDataVisitor {
	public void visit(Score s);
	/*public void visit(Partial p);
	public void visit(Penality p);*/
	public void visit(Day d);
	public void visit(Grest g);
	public void visit(Color color);
	public void visit(Team team);
}
