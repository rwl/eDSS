package com.ncond.dss.editor.scanner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.*;

import com.ncond.dss.executive.ExecCommands;

public class DSSPartitionScanner extends RuleBasedPartitionScanner {

	public final static String DSS_COMMENT = "__dss_comment";
	public final static String DSS_INLINE_COMMENT = "__dss_inline_comment";
	public final static String DSS_ARRAY = "__dss_array";
	public final static String DSS_CMD_KEY = "__dss_cmd_key";
	public final static String DSS_CMD_VAL = "__dss_cmd_val";

	public DSSPartitionScanner() {

		IToken comment = new Token(DSS_COMMENT);
//		IToken array = new Token(DSS_ARRAY);
		IToken inlineComment = new Token(DSS_INLINE_COMMENT);
		IToken cmdKey = new Token(DSS_CMD_KEY);
		IToken cmdVal = new Token(DSS_CMD_VAL);

		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add( new EndOfLineRule("//", comment) );
		rules.add( new EndOfLineRule("!", inlineComment) );

//		rules.add( new SingleLineRule("[", "]", array) );
//		rules.add( new SingleLineRule("{", "}", array) );
//		rules.add( new SingleLineRule("(", ")", array) );
//		rules.add( new SingleLineRule("\"", "\"", array) );
//		rules.add( new SingleLineRule("'", "'", array) );

		for (String cmd : ExecCommands.execCommand) {
			rules.add( new SingleLineRule(cmd, "=", cmdVal) );
		}

		rules.add( new SingleLineRule("command", "=", cmdKey) );
		rules.add( new SingleLineRule("command", "=", cmdKey) );

	        IPredicateRule[] result = new IPredicateRule[rules.size()];
	        rules.toArray(result);
		setPredicateRules(result);
	}

	public class DSSWordDetector implements IWordDetector {
		public boolean isWordPart(char character) {
			return Character.isJavaIdentifierPart(character);
		}
		public boolean isWordStart(char character) {
			return Character.isJavaIdentifierStart(character);
		}
	}

}
