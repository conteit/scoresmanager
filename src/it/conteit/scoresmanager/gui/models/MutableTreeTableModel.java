/*
  Copyright (c) 2006, Ulrich Hilger, Light Development, http://www.lightdev.com
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:

    - Redistributions of source code must retain the above copyright notice, this 
       list of conditions and the following disclaimer.

    - Redistributions in binary form must reproduce the above copyright notice, 
       this list of conditions and the following disclaimer in the documentation 
       and/or other materials provided with the distribution.

    - Neither the name of Light Development nor the names of its contributors may be 
       used to endorse or promote products derived from this software without specific 
       prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
  SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
  OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package it.conteit.scoresmanager.gui.models;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * Tree table model that provides some data manipulation methods.
 * 
 * <p>
 * While class DefaultTreeModel has some useful methods for manipulation of tree
 * data, class DefaultTreeTableModel does not extend DefaultTreeModel and thus
 * does not inherit those methods. This is one of the rare occasions where the
 * single inheritance rule of Java combined with the particular implementation
 * of JXTreeTable combining JTable and JTree leads to redundant code. The JTree
 * inside JXTreeTable (implemented as a TreeTableCellRenderer) is not
 * constructed with a DefaultTreeModel but with a TreeTableModel. Thus although
 * the required code exists in DefaultTreeModel there is no way to pull it into
 * our implementation other than to copy some of the code.
 * </p>
 * 
 * @author Ulrich Hilger
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the BSD License, for
 *         details see file license.txt in the distribution package of this
 *         software
 * 
 * @version 1, 31.03.2006
 */
public class MutableTreeTableModel extends DefaultTreeTableModel {

	/**
	 * create a new instance of a MutableTreeTableModel object
	 */
	public MutableTreeTableModel() {
		super();
	}

	/**
	 * create a new instance of a MutableTreeTableModel object
	 * 
	 * @param root
	 *            root node for newly created model
	 */
	public MutableTreeTableModel(TreeNode root) {
		super(root);
	}

	/**
	 * Invoke this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate event.
	 * This is the preferred way to add children as it will create the
	 * appropriate event.
	 */
	public void insertNodeInto(MutableTreeNode newChild,
			MutableTreeNode parent, int index) {
		parent.insert(newChild, index);

		int[] newIndexs = new int[1];

		newIndexs[0] = index;
		nodesWereInserted(parent, newIndexs);
	}

	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the preferred
	 * way to remove a node as it handles the event creation for you.
	 */
	public void removeNodeFromParent(MutableTreeNode node) {
		MutableTreeNode parent = (MutableTreeNode) node.getParent();

		if (parent == null)
			throw new IllegalArgumentException("node does not have a parent.");

		int[] childIndex = new int[1];
		Object[] removedArray = new Object[1];

		childIndex[0] = parent.getIndex(node);
		parent.remove(childIndex[0]);
		removedArray[0] = node;
		nodesWereRemoved(parent, childIndex, removedArray);
	}

	public void setRoot(TreeNode root){
		super.setRoot(root);
	}
}
