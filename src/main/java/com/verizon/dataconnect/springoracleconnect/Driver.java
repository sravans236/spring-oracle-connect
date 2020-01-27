package com.verizon.dataconnect.springoracleconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ohadr.common.utils.JsonUtils.convertResultSetToJson;

@Component
public class Driver {

    private Connection oracleConnection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private CallableStatement callableStatement;

    public Driver() {
        this.oracleConnection = oracleConnection;
        this.statement = statement;
        this.resultSet = resultSet;
        this.preparedStatement = preparedStatement;
        this.callableStatement = callableStatement;
    }

    private static Connection getConnection() throws SQLException {
        IConnector oracleConnector = new OracleConnectorImpl();
        Connection oracleConnection = oracleConnector.
                createConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
        return oracleConnection;
    }

    public String executeStatement(String sql) throws SQLException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        String result = "";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

            oracleConnection = Driver.getConnection();

            System.out.println("SQL Connection to database established!");

            // this allows to stream the records on heap issues
            statement = oracleConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            if (sql.length() > 0) {
                String sqlUpperCase = sql.toUpperCase();
                if (sqlUpperCase.contains("SELECT")) {
                    resultSet = statement.executeQuery(sql);
                    result = convertResultSetToJson(resultSet);
                    statement.close();
                } else if (sqlUpperCase.contains("INSERT") || sql.contains("UPDATE")) {
                    preparedStatement = oracleConnection.prepareStatement(sql);
                    int effectedRows = preparedStatement.executeUpdate();
                    result = "Total number of rows inserted :" + String.valueOf(effectedRows);
                    preparedStatement.clearParameters();
                    preparedStatement.close();
                    System.out.println("Insert/Update executed succesfully. Rows effected :" + effectedRows);
                } else if (sqlUpperCase.contains("DELETE")) {
                    preparedStatement = oracleConnection.prepareStatement(sql);
                    boolean isDeleted = preparedStatement.execute();
                    result = "Delete operation :" + String.valueOf(isDeleted);
                    preparedStatement.clearParameters();
                    preparedStatement.close();
                    System.out.println("Delete status.. :" + isDeleted);
                } else if (sqlUpperCase.contains("EXEC")) {
                    //String sample = "{ call GET_SINGLE_EMP(IN-1-VARCHAR,OUT-2-VARCHAR) }" ;
                    String noParamSP = "{ call procPrintHelloWorld(?) }";
                    callableStatement = oracleConnection.prepareCall(noParamSP);
                    //callableStatement.setInt(1, 7934);
                    callableStatement.registerOutParameter(1, Types.VARCHAR);
                    callableStatement.execute();
                    result = "STORED PROCEDURE NEED TO RUN";
                    System.out.println(callableStatement.getString(1));
                } else {
                    result = "SORRY.... only supported operations are SELECT/INSERT/UPDATE..";
                    System.out.println("only supported operations are SELECT/INSERT/UPDATE.");
                }
            } else {
                result = "You have entered Incorrect query... please check input.";
                System.out.println("Please enter valid query...");
            }
        } catch (Exception ex) {
            result = "Something wrong happened... please check again.";
            System.out.println("SQL Error" + ex.getMessage());
        } finally {
            oracleConnection.close();
        }

        return result;
    }
}
