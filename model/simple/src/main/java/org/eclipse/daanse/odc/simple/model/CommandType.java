/*
* Copyright (c) 2025 Contributors to the Eclipse Foundation.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*   SmartCity Jena - initial
*   Stefan Bischof (bipolis.org) - initial
*/
package org.eclipse.daanse.odc.simple.model;

/**
 * Enumeration of supported database command types.
 */
public enum CommandType {
    /** Table-based command type */
    Table,
    /** SQL query command type */
    SQL,
    /** OLAP cube command type */
    Cube,
    /** List-based command type */
    List,
    /** Default command type */
    Default,
    /** Table collection command type */
    TableCollection
}
