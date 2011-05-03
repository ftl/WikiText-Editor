package ft.vex.wikitext.ui.editor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.wikitext.core.WikiText;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wst.xml.vex.core.internal.dom.Document;
import org.eclipse.wst.xml.vex.core.internal.dom.DocumentWriter;

import ft.vex.wikitext.Activator;
import ft.vex.wikitext.VexDocumentBuilder;

@SuppressWarnings("restriction")
public class WikiTextEditor extends EditorPart {

	public static final String ID = "ft.vex.wikitext.editor"; //$NON-NLS-1$
	
	private Document document;
	
	private Text sourceText;

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		if (!(input instanceof IStorageEditorInput))
			throw new PartInitException(input + " is not supported.");
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		try {
			loadDocument((IStorageEditorInput) input);
		} catch (CoreException e) {
			throw new PartInitException(e.getStatus());
		}
	}

	private void loadDocument(IStorageEditorInput input) throws CoreException {
		final VexDocumentBuilder documentBuilder = new VexDocumentBuilder();
		final MarkupParser markupParser = new MarkupParser(getMarkupLanguage(input), documentBuilder);
		final InputStreamReader reader = new InputStreamReader(input.getStorage().getContents()); // TODO infer encoding
		try {
			try {
				markupParser.parse(reader);
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, Activator.ID, e.getMessage(), e));
			}
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.ID, e.getMessage(), e));
			}
		}
		document = documentBuilder.getDocument();
	}
	
	private static MarkupLanguage getMarkupLanguage(final IStorageEditorInput input) {
		final String name;
		if (input instanceof IFileEditorInput)
			name = ((IFileEditorInput) input).getFile().getName();
		else
			name = input.getName();
		final MarkupLanguage result = WikiText.getMarkupLanguageForFilename(name);
		if (result == null) 
			return WikiText.getMarkupLanguage("Textile"); //$NON-NLS-1$
		return result;
	}

	@Override
	public void createPartControl(final Composite parent) {
		sourceText = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
		if (document != null) {
			final DocumentWriter documentWriter = new DocumentWriter();
			final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try {
				documentWriter.write(document, buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sourceText.setText(new String(buffer.toByteArray()));
		}
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
