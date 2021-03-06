/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org2.eclipse.php.internal.core.ast.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org2.eclipse.php.internal.core.PHPVersion;
import org2.eclipse.php.internal.core.ast.match.ASTMatcher;
import org2.eclipse.php.internal.core.ast.visitor.Visitor;

/**
 * Represent a echo statement.
 * <pre>e.g.<pre> echo "hello",
 * echo "hello", "world"
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EchoStatement extends Statement {

	private ASTNode.NodeList<Expression> expressions = new ASTNode.NodeList<Expression>(EXPRESSIONS_PROPERTY);

	/**
	 * The "expressions" structural property of this node type.
	 */
	public static final ChildListPropertyDescriptor EXPRESSIONS_PROPERTY = 
		new ChildListPropertyDescriptor(EchoStatement.class, "expressions", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	static {
		List<StructuralPropertyDescriptor> properyList = new ArrayList<StructuralPropertyDescriptor>(2);
		properyList.add(EXPRESSIONS_PROPERTY);
		PROPERTY_DESCRIPTORS = Collections.unmodifiableList(properyList);
	}
	
	private EchoStatement(int start, int end, AST ast, Expression[] expressions) {
		super(start, end, ast);
		if (expressions == null) {
			throw new IllegalArgumentException();
		}

		for (Expression expression : expressions) {
			this.expressions.add(expression);
		}
	}

	public EchoStatement(int start, int end, AST ast, List expressions) {
		this(start, end, ast, (Expression[]) expressions.toArray(new Expression[expressions.size()]));
	}
	
	public EchoStatement(AST ast) {
		super(ast);
	}

	public void accept0(Visitor visitor) {
		final boolean visit = visitor.visit(this);
		if (visit) {
			childrenAccept(visitor);
		}
		visitor.endVisit(this);
	}	

	public void childrenAccept(Visitor visitor) {
		for (ASTNode node : this.expressions) {
			node.accept(visitor);
		}
	}

	public void traverseTopDown(Visitor visitor) {
		accept(visitor);
		for (ASTNode node : this.expressions) {
			node.traverseTopDown(visitor);
		}
	}

	public void traverseBottomUp(Visitor visitor) {
		for (ASTNode node : this.expressions) {
			node.traverseBottomUp(visitor);
		}
		accept(visitor);
	}

	public void toString(StringBuffer buffer, String tab) {
		buffer.append(tab).append("<EchoStatement"); //$NON-NLS-1$
		appendInterval(buffer);
		buffer.append(">\n"); //$NON-NLS-1$
		for (ASTNode node : this.expressions) {
			node.toString(buffer, TAB + tab);
			buffer.append("\n"); //$NON-NLS-1$
		}
		buffer.append(tab).append("</EchoStatement>"); //$NON-NLS-1$
	}

	public int getType() {
		return ASTNode.ECHO_STATEMENT;
	}

	/**
	 * @deprecated use #expressions()
	 */
	public Expression[] getExpressions() {
		return this.expressions.toArray(new Expression[this.expressions.size()]);
	}
	
	/**
	 * @return expression list of the echo statement
	 */
	public List<Expression> expressions() {
		return this.expressions;
	}
	
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == EXPRESSIONS_PROPERTY) {
			return expressions();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	/* 
	 * Method declared on ASTNode.
	 */
	public boolean subtreeMatch(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	@Override
	ASTNode clone0(AST target) {
		final List expressions = ASTNode.copySubtrees(target, this.expressions());
		final EchoStatement echoSt = new EchoStatement(this.getStart(), this.getEnd(), target, expressions);
		return echoSt;
	}

	@Override
	List<StructuralPropertyDescriptor> internalStructuralPropertiesForType(PHPVersion apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}

}
