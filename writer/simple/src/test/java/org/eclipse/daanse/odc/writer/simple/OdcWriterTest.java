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
package org.eclipse.daanse.odc.writer.simple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.eclipse.daanse.odc.simple.model.CommandType;
import org.eclipse.daanse.odc.simple.model.Connection;
import org.eclipse.daanse.odc.simple.model.ConnectionType;
import org.eclipse.daanse.odc.simple.model.CredentialsMethod;
import org.eclipse.daanse.odc.simple.model.DocumentProperties;
import org.eclipse.daanse.odc.simple.model.OdcFile;
import org.eclipse.daanse.odc.simple.model.OfficeDataConnection;
import org.eclipse.daanse.odc.simple.model.PowerQueryConnection;
import org.junit.jupiter.api.Test;

class OdcWriterTest {

    private final OdcWriter writer = new OdcWriter();

    @Test
    void testSqlSourceExample() {
        Connection connection = new Connection(ConnectionType.ODBC,
                "DRIVER=SQL Server;SERVER=mysqlserver;APP=2007 Microsoft Office system;Trusted_Connection=Yes",
                Optional.empty(), List.of(), Optional.of("SELECT * FROM Northwind.dbo.Invoices Invoices"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(connection);
        OdcFile odcFile = OdcFile.of("Northwind", odc);

        String result = writer.write(odcFile);

        assertThat(result)
            .contains("Northwind")
            .contains("ODBC")
            .contains("SELECT * FROM Northwind.dbo.Invoices Invoices")
            .contains("<odc:Connection odc:Type=\"ODBC\">")
            .contains("ODC.Database");
    }

    @Test
    void testOlapExample() {
        Connection connection = new Connection(ConnectionType.OLEDB,
                "Provider=MSOLAP.3;Integrated Security=SSPI;Persist Security Info=True;Data Source=myolapserver;Initial Catalog=Adventure Works DW",
                Optional.of(CommandType.Cube), List.of(), Optional.of("Adventure Works"), Optional.of("Application1"),
                Optional.of(CredentialsMethod.Stored), Optional.of(true), Optional.empty());

        DocumentProperties props = new DocumentProperties(Optional.empty(), Optional.of("Adventure Works"),
                Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(connection);
        OdcFile odcFile = new OdcFile(Optional.of("Adventure Works"), props, odc, Optional.of("Adventure Works DW"),
                Optional.empty(), Optional.of("Adventure Works"));

        String result = writer.write(odcFile);

        assertThat(result)
            .contains("Adventure Works")
            .contains("OLEDB")
            .contains("ODC.Cube")
            .contains("<odc:CommandType>Cube</odc:CommandType>")
            .contains("<odc:SSOApplicationID>Application1</odc:SSOApplicationID>")
            .contains("<odc:CredentialsMethod>Stored</odc:CredentialsMethod>")
            .contains("<odc:AlwaysUseConnectionFile/>")
            .contains("Adventure Works DW");
    }

    @Test
    void testPowerQueryExample() {
        PowerQueryConnection pqConnection = new PowerQueryConnection(ConnectionType.OLEDB,
                "Provider=Microsoft.Mashup.OleDb.1;Data Source=$Workbook$;Location=DimCustomer",
                Optional.of(CommandType.SQL), Optional.of("SELECT * FROM [DimCustomer]"), Optional.empty(),
                Optional.empty(), Optional.empty());

        DocumentProperties props = new DocumentProperties(
                Optional.of("Connection to the 'DimCustomer' query in the workbook."),
                Optional.of("Query - DimCustomer"), Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(pqConnection);
        OdcFile odcFile = new OdcFile(Optional.of("Query - DimCustomer"), props, odc, Optional.empty(),
                Optional.empty(), Optional.empty());

        String result = writer.write(odcFile);

        assertThat(result)
            .contains("Query - DimCustomer")
            .contains("Microsoft.Mashup.OleDb.1")
            .contains("<odc:PowerQueryConnection odc:Type=\"OLEDB\">")
            .contains("<odc:CommandType>SQL</odc:CommandType>")
            .contains("SELECT * FROM [DimCustomer]")
            .contains("Connection to the &apos;DimCustomer&apos; query in the workbook.");
    }

    @Test
    void testXmlEscaping() {
        Connection connection = new Connection(ConnectionType.ODBC, "DRIVER=SQL Server;SERVER=test<>&\"'server",
                Optional.empty(), List.of(), Optional.of("SELECT * FROM table WHERE col = '<test>'"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(connection);
        OdcFile odcFile = OdcFile.of("Test & <Escaping>", odc);

        String result = writer.write(odcFile);

        assertThat(result)
            .contains("test&lt;&gt;&amp;&quot;&apos;server")
            .contains("Test &amp; &lt;Escaping&gt;")
            .contains("SELECT * FROM table WHERE col = &apos;&lt;test&gt;&apos;");
    }
}
