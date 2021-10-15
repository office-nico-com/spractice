package com.office_nico.spractice.web.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

public class CustomDialect extends AbstractProcessorDialect {

	private static final String NAME = "custom dialect";
	private static final String PREFIX = "ex";

	public CustomDialect() {
		super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		Set<IProcessor> proccessors = new HashSet<>();
		
		proccessors.add(new HtmlEscapeBreakLineProccessor(dialectPrefix, getDialectProcessorPrecedence()));
		proccessors.add(new BreakLineProccessor(dialectPrefix, getDialectProcessorPrecedence()));
		
		return proccessors;
	}
}
