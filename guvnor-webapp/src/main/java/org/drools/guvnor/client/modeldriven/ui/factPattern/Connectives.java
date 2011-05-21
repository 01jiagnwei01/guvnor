/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.client.modeldriven.ui.factPattern;

import org.drools.guvnor.client.common.DirtyableHorizontalPane;
import org.drools.guvnor.client.modeldriven.ui.CEPOperatorsDropdown;
import org.drools.guvnor.client.modeldriven.ui.CEPOperatorsDropdown.OperatorSelection;
import org.drools.guvnor.client.modeldriven.ui.ConstraintValueEditor;
import org.drools.guvnor.client.modeldriven.ui.RuleModeller;
import org.drools.ide.common.client.modeldriven.SuggestionCompletionEngine;
import org.drools.ide.common.client.modeldriven.brl.BaseSingleFieldConstraint;
import org.drools.ide.common.client.modeldriven.brl.ConnectiveConstraint;
import org.drools.ide.common.client.modeldriven.brl.FactPattern;
import org.drools.ide.common.client.modeldriven.brl.SingleFieldConstraint;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public class Connectives {
    private FactPattern  pattern;
    private RuleModeller modeller;

    /**
     * Returns the pattern.
     */
    public FactPattern getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern.
     */
    public void setPattern(FactPattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the completions.
     */
    public SuggestionCompletionEngine getCompletions() {
        return this.modeller.getSuggestionCompletions();
    }

    /**
     * Returns the modeller.
     */
    public RuleModeller getModeller() {
        return modeller;
    }

    /**
     * Sets the modeller.
     */
    public void setModeller(RuleModeller modeller) {
        this.modeller = modeller;
    }

    public Widget connectives(SingleFieldConstraint c,
                              String factClass) {
        if ( c.connectives != null && c.connectives.length > 0 ) {
            DirtyableHorizontalPane horiz = new DirtyableHorizontalPane();
            for ( int i = 0; i < c.connectives.length; i++ ) {
                ConnectiveConstraint con = c.connectives[i];
                horiz.add( connectiveOperatorDropDown( con,
                                                       c.getFieldName() ) );
                horiz.add( connectiveValueEditor( con,
                                                  factClass,
                                                  c.getFieldName() ) );
            }
            return horiz;
        } else {
            //nothing to do
            return null;
        }

    }

    private Widget connectiveValueEditor(final BaseSingleFieldConstraint con,
                                         String factClass,
                                         String fieldName) {
        String typeNumeric = this.modeller.getSuggestionCompletions().getFieldType( factClass,
                                                                                    fieldName );
        return new ConstraintValueEditor( pattern,
                                          fieldName,
                                          con,
                                          this.modeller,
                                          typeNumeric,
                                          false );
    }

    private Widget connectiveOperatorDropDown(final ConnectiveConstraint con,
                                              String fieldName) {

        String factType = this.pattern.getFactType();
        String[] operators = this.getCompletions().getConnectiveOperatorCompletions( factType,
                                                                                     fieldName );
        CEPOperatorsDropdown w = new CEPOperatorsDropdown( operators,
                                                           con );

        w.addValueChangeHandler( new ValueChangeHandler<OperatorSelection>() {

            public void onValueChange(ValueChangeEvent<OperatorSelection> event) {
                OperatorSelection selection = event.getValue();
                String selected = selection.getValue();
                con.setOperator( selected );
            }
        } );

        return w;
    }
}
