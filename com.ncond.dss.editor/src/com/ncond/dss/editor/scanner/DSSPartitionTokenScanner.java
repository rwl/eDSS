package com.ncond.dss.editor.scanner;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.ncond.dss.executive.ExecCommands;
import com.ncond.dss.parser.Parser;

public class DSSPartitionTokenScanner implements IPartitionTokenScanner {

	public final static String DSS_COMMENT = "__dss_comment";
	public final static String DSS_INLINE_COMMENT = "__dss_inline_comment";
	public final static String DSS_KEY = "__dss_key";
	public final static String DSS_VALUE = "__dss_value";

	private static Parser parser = Parser.getAuxInstance();

	protected IDocument document;

	protected int offset;
	protected int end;
	protected int tokenOffset;

	protected String contentType;
	protected int partitionOffset;

	public void setRange(IDocument document, int offset, int length) {
		this.document = document;
		this.offset = offset;
		this.end = offset + length;

		System.out.println("setRange: " + offset + " " + length);
	}

	public IToken nextToken() {
		int lineNum, length, idx;
		String line, key, value;
		Token token;

		try {
			lineNum = document.getLineOfOffset(offset);
			length = document.getLineLength(lineNum);
			line = document.get(offset, length);
		} catch (BadLocationException e) {
			System.err.println("nextToken, bad location: " + offset);
			return Token.EOF;
		}

		parser.setCmdBuffer(line);

		key = parser.getNextParam();
		value = parser.makeString();

		idx = ExecCommands.commandList.getCommand(value);

		switch (idx) {
		case -1:
			token = new Token(IDocument.DEFAULT_CONTENT_TYPE);
			break;
		case 18:
			token = new Token(DSS_COMMENT);
			break;
		default:
			token = new Token(IDocument.DEFAULT_CONTENT_TYPE);
			break;
		}

		tokenOffset = offset + parser.getPosition();
		return token;
	}

	public int getTokenOffset() {
		System.out.println("getTokenOffset: " + tokenOffset);
		return tokenOffset;
	}

	public int getTokenLength() {
		int length;
		if (offset < end) {
			length = offset - tokenOffset;
		} else {
			length = end - tokenOffset;
		}
		System.out.println("getTokenLength: " + length);
		return length;
	}

	public void setPartialRange(IDocument document, int offset, int length,
			String contentType, int partitionOffset) {
		this.contentType = contentType;
		this.partitionOffset = partitionOffset;

		System.out.println("setPartialRange: " +
				offset + " " +
				length + " " +
				contentType + " " +
				partitionOffset);

		if (partitionOffset > -1) {
			int delta= offset - partitionOffset;
			if (delta > 0) {
//				super.setRange(document, partitionOffset, length + delta);
				this.offset = offset;
				return;
			}
		}
		setRange(document, offset, length);
	}

}
