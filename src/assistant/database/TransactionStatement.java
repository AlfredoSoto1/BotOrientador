/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import assistant.database.SubTransactionResult.Replacement;

/**
 * @author Alfredo
 */
public class TransactionStatement {
	
	private String SQL;
	private List<Object> parameters;
	private Function<?, List<?>> batchedParameters;
	
	/**
	 * This is only for normal SQL transactions
	 * @param SQL
	 * @param parameters
	 */
	public TransactionStatement(String SQL, List<?> parameters) {
		this.SQL = SQL;
		this.parameters = new ArrayList<>();
		
		List<String> tempReplacements = new LinkedList<>();
        for (Object param : parameters) {
            if (param instanceof Replacement replacement) {
            	tempReplacements.add(replacement.getReplacement());
                for (var value : replacement.getValues())
                	this.parameters.add(value);
            } else {
            	this.parameters.add(param);
            }
        }
        
        this.SQL = String.format(SQL, tempReplacements.toArray());
	}
	
	/**
	 * This is only for batched SQL transactions
	 * @param SQL
	 * @param batchedParameters
	 */
	public <T> TransactionStatement(String SQL, List<T> batch, Function<T, List<?>> batchedParameters) {
		if(batch.isEmpty())
			throw new IllegalArgumentException("Empty batch is not allowed, need one element at least.");
		
		List<String> tempReplacements = new LinkedList<>();
        for (Object param : batchedParameters.apply(batch.getFirst())) {
            if (param instanceof Replacement replacement)
            	tempReplacements.add(replacement.getReplacement());
        }
        
        this.batchedParameters = batchedParameters;
        this.SQL = String.format(SQL, tempReplacements.toArray());
	}
	
	public String SQL() {
		return SQL;
	}
	
	public List<?> parameters() {
		if (parameters == null)
			throw new IllegalAccessError("Cannot read parameters for a batch statement");
		return parameters;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Function<T, List<?>> batchedParameters() {
		if (batchedParameters == null)
			throw new IllegalAccessError("Cannot read batched parameters for a regular statement");
		return (Function<T, List<?>>) batchedParameters;
	}
	
	public <T> List<?> batchedParameters(T batchElement) {
		if (batchedParameters == null)
			throw new IllegalAccessError("Cannot read batched parameters for a regular statement");
		
		@SuppressWarnings("unchecked")
		Function<T, List<?>> function = (Function<T, List<?>>) batchedParameters;
		
		List<Object> parameters = new LinkedList<>();
        for (Object param : function.apply(batchElement)) {
            if (param instanceof Replacement replacement) {
                for (var value : replacement.getValues())
                	parameters.add(value);
            } else {
            	parameters.add(param);
            }
        }
		return parameters;
	}
}
