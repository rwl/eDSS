package com.ncond.dss.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.ncond.dss.editor.scanner.DSSPartitionTokenScanner;

public class DSSDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new DebugPartitioner(
				new DSSPartitionTokenScanner(),
				new String[] {
					DSSPartitionTokenScanner.DSS_COMMENT,
					DSSPartitionTokenScanner.DSS_INLINE_COMMENT
				}
			);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

}
