package menus;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.Selectable;

public class MyTreeModel extends AbstractTreeModel<Object> implements Selectable<Object>{
	
	private static final long serialVersionUID = 1L;
	private MenuItem _root;

	public MyTreeModel(Object root) {
		// set the root
		super(root);
		_root = (MenuItem) root;

	}

	@Override
	public boolean isLeaf(Object node) {
		return ((MenuItem) node).getChildren().size() == 0; // at most 4 levels
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((MenuItem) parent).getChildren().get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return ((MenuItem) parent).getChildren().size();
	}

	public boolean isMutiple() {
		return true;
	}

}
