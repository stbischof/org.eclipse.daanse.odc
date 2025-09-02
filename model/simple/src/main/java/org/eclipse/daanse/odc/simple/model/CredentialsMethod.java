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
 * Enumeration of supported authentication credential methods.
 */
public enum CredentialsMethod {
    /** Windows integrated authentication */
    Integrated,
    /** No authentication required */
    None,
    /** Stored username and password authentication */
    Stored
}
