package com.zyh.pro.graphxxmlbuilder.main;

import com.zyh.pro.xmlparser.main.XMLNode;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Iterator;

public class XMLTreeNodeAdapter implements TreeNode {

	private final XMLNode adapted;

	private final XMLTreeNodeAdapter parent;

	public XMLTreeNodeAdapter(XMLTreeNodeAdapter parent, XMLNode adapted) {
		this.parent = parent;
		this.adapted = adapted;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return new XMLTreeNodeAdapter(this, adapted.getChildAt(childIndex));
	}

	@Override
	public int getChildCount() {
		return adapted.getChildren().size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return adapted.getChildren().indexOf(((XMLTreeNodeAdapter) node).adapted);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<XMLTreeNodeAdapter> children() {
		return new Enumeration<>() {
			Iterator<XMLNode> iterator = adapted.getChildren().iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public XMLTreeNodeAdapter nextElement() {
				return new XMLTreeNodeAdapter(XMLTreeNodeAdapter.this, iterator.next());
			}
		};
	}

	@Override
	public String toString() {
		return adapted.toString();
	}

	public XMLNode getAdapted() {
		return adapted;
	}
}
