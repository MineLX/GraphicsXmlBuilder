package com.zyh.pro.graphxxmlbuilder.main;

import com.zyh.pro.xmlparser.main.XMLNode;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.io.*;

import static java.lang.System.getProperty;

public class XMLTreeEditor {

	private static final File DESKTOP = new File(getProperty("user.home") + "\\desktop");

	static {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel background;
	private JTree xmlNodeTree;
	private JTextField nodeNameField;
	private JTextField attributeNameField;
	private JTextField attributeValueField;
	private JButton renameNode;
	private JButton commitAttribute;
	private JButton insertNewNode;
	private JButton removeThisNode;
	private JList<String> propertiesList;
	private JButton removeSelectedAttribute;
	private XMLNode selectedNode;
	private XMLNode rootNode;
	private String editedPropertyName;
	private DefaultListModel<String> propertiesModel;

	private XMLTreeEditor() {
		setupList();
		setupTree();
		setupButtons();
	}

	private void setupMenus(JFrame frame) {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exportMenuItem = new JMenuItem("export");
		exportMenuItem.addActionListener(this::dumpNode);
		menu.add(exportMenuItem);
		bar.add(menu);
		frame.setJMenuBar(bar);
	}

	private void setupList() {
		propertiesModel = new DefaultListModel<>();
		propertiesList.setModel(propertiesModel);
		propertiesList.addListSelectionListener(this::onClickAnPropertyItem);
	}

	private void setupTree() {
		rootNode = new XMLNode("root");
		DefaultTreeModel treeModel = new DefaultTreeModel(new XMLTreeNodeAdapter(null, rootNode));
		xmlNodeTree.setModel(treeModel);
		xmlNodeTree.addTreeSelectionListener(this::onXMLTreeItemClicked);
	}

	private void setupButtons() {
		commitAttribute.addActionListener(this::commitAttribute);
		renameNode.addActionListener(this::renameSelectedNode);
		insertNewNode.addActionListener(this::insertNode);
		removeThisNode.addActionListener(this::removeSelectedNode);
		removeSelectedAttribute.addActionListener(this::removeSelectedAttribute);
	}

	private void onClickAnPropertyItem(ListSelectionEvent listSelectionEvent) {
		editedPropertyName = propertiesList.getSelectedValue();
		attributeNameField.setText(editedPropertyName);
		attributeValueField.setText(selectedNode.getProperty(editedPropertyName));
	}

	private void onXMLTreeItemClicked(TreeSelectionEvent e) {
		XMLTreeNodeAdapter selectedTreeNode = (XMLTreeNodeAdapter) e.getPath().getLastPathComponent();
		selectedNode = selectedTreeNode.getAdapted();
		refreshPropertiesList();
	}

	private void refreshPropertiesList() {
		propertiesModel.clear();
		System.out.println(selectedNode + "s' properties size = " + selectedNode.getProperties().size());
		selectedNode.getProperties().keySet().forEach(propertiesModel::addElement);
		propertiesList.updateUI();
	}

	private void dumpNode(ActionEvent actionEvent) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(DESKTOP);
		chooser.showSaveDialog(background);
		File selectedFile = chooser.getSelectedFile();
		try (PrintStream printStream = new PrintStream(new FileOutputStream(selectedFile))) {
			rootNode.dump(printStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void removeSelectedNode(ActionEvent actionEvent) {
		if (rootNode == selectedNode) {
			System.out.println("rootNode can't be deleted");
			return;
		}
		selectedNode.removeFromParent();
		xmlNodeTree.updateUI();
	}

	private void removeSelectedAttribute(ActionEvent actionEvent) {
		selectedNode.removeProperty(editedPropertyName);
		refreshPropertiesList();
	}

	private void insertNode(ActionEvent actionEvent) {
		XMLNode addend = new XMLNode(nodeNameField.getText());
		selectedNode.addChild(addend);
		xmlNodeTree.updateUI();
	}

	private void renameSelectedNode(ActionEvent actionEvent) {
		selectedNode.rename(nodeNameField.getText());
		xmlNodeTree.updateUI();
	}

	private void commitAttribute(ActionEvent actionEvent) {
		selectedNode.addProperty(attributeNameField.getText(), attributeValueField.getText());
		refreshPropertiesList();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("XMLTreeEditor");
		XMLTreeEditor xmlTreeEditor = new XMLTreeEditor();
		frame.setContentPane(xmlTreeEditor.background);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		xmlTreeEditor.setupMenus(frame);
		frame.pack();
		frame.setVisible(true);
	}
}
