package com.zyh.pro.graphxxmlbuilder.test;

import com.zyh.pro.graphxxmlbuilder.main.XMLTreeNodeAdapter;
import com.zyh.pro.xmlparser.main.XMLNode;
import org.junit.Test;

import java.util.Enumeration;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XMLTreeNodeTest {
	@Test
	public void adapting() {
		XMLNode rootNode = new XMLNode("root");
		XMLNode child1 = new XMLNode("child1");
		rootNode.addChild(child1);
		XMLNode child2 = new XMLNode("child2");
		rootNode.addChild(child2);

		XMLTreeNodeAdapter o = null;
		XMLTreeNodeAdapter adapter = new XMLTreeNodeAdapter(o, rootNode);
		Enumeration children = adapter.children();
		assertThat(children.nextElement().toString(), is(child1.toString()));
		assertThat(children.nextElement().toString(), is(child2.toString()));

		assertThat(adapter.getAllowsChildren(), is(true));

		assertThat(adapter.getChildAt(0).toString(), is(child1.toString()));
		assertThat(adapter.getChildAt(1).toString(), is(child2.toString()));
		assertThat(adapter.getChildCount(), is(2));
		assertThat(adapter.getIndex(new XMLTreeNodeAdapter(adapter, child2)), is(1));
		assertThat(adapter.getChildAt(0).getParent().toString(), is(rootNode.toString()));
	}
}
