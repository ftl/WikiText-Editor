package ft.vex.wikitext.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class WikiTextEditor extends EditorPart {

	public static final String ID = "ft.vex.wikitext.editor"; //$NON-NLS-1$
	
	private Text sourceText;

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
		setInput(input);
	}

	@Override
	public void createPartControl(final Composite parent) {
		sourceText = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
	}

	@Override
	public void setFocus() {
		sourceText.setFocus();
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
	}

}
