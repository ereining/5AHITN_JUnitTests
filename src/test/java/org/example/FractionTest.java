package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

class FractionTest {

    private static Connection connect(String connectionString){
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(connectionString);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return  conn;
    }

    @org.junit.jupiter.api.Test
    void connectToDatabase() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection postgres = connect("jdbc:postgresql://localhost:5432/postgres?" +
                "user=postgres&password=postgres");
        Assertions.assertNotNull(postgres);
        Connection mysql = connect("jdbc:mysql://localhost:3306?user=root&password=root");
        Assertions.assertNotNull(mysql);

        System.out.println(postgres.getClientInfo());
        PreparedStatement db = mysql.prepareStatement("CREATE DATABASE `db`");
        Assertions.assertFalse(db.execute());
        // disconnect from server
        mysql.close();
        mysql = connect("jdbc:mysql://localhost:3306/db?user=root&password=root");
        Assertions.assertNotNull(mysql);
        // reconnect to server + database

        PreparedStatement table = mysql.prepareStatement("CREATE TABLE `db.Persons` ( `Name` varchar(255) )");
        Assertions.assertFalse(table.execute());

        PreparedStatement row = mysql.prepareStatement("INSERT INTO `db.Persons` ( `Name` )  values (?) ");
        row.setString(1,"Marco");
        Assertions.assertFalse(row.execute());

        PreparedStatement select = mysql.prepareStatement("SELECT * from `db.Persons`");
        Assertions.assertFalse(row.execute());

    }


    @org.junit.jupiter.api.Test
    void constructorTest(){
        // check normal use
        Fraction f1 = new Fraction(1, 1);
        Assertions.assertEquals("1 / 1", f1.toString());

        // check negative numbers
        Fraction f2 = new Fraction(1, -1);
        Assertions.assertEquals("1 / -1", f2.toString());

        // check two negative numbers
        Fraction f3 = new Fraction(-1, -1);
        Assertions.assertEquals("1 / 1", f3.toString());

        // check if the divisor is 0
        Fraction f4 = new Fraction(1, 0);
        Assertions.assertEquals("1 / 1", f4.toString());
    }


    @org.junit.jupiter.api.Test
    void getDividend() {
        // normal value
        Fraction f = new Fraction(1, 10);
        Assertions.assertEquals(1, f.getDividend());

        // negative value
        Fraction f2 = new Fraction(-1, 10);
        Assertions.assertEquals(-1, f2.getDividend());

        // 0 value
        Fraction f3 = new Fraction(0, 10);
        Assertions.assertEquals(0, f3.getDividend());
    }

    @org.junit.jupiter.api.Test
    void setDividend() {
        // normal value
        Fraction f = new Fraction(1, 10);
        f.setDividend(2);

        Assertions.assertEquals(2, f.getDividend());

        // negative value
        Fraction f2 = new Fraction(1, 10);
        f2.setDividend(-1);

        Assertions.assertEquals(-1, f2.getDividend());

        // 0 value
        Fraction f3 = new Fraction(0, 10);
        f3.setDividend(0);

        Assertions.assertEquals(0, f3.getDividend());
    }

    @org.junit.jupiter.api.Test
    void getDivisor() {
        // normal
        Fraction f = new Fraction(1, 10);
        Assertions.assertEquals(10, f.getDivisor());

        // negative value
        Fraction f2 = new Fraction(1, -10);
        Assertions.assertEquals(-10, f2.getDivisor());

        // 0 value
        Fraction f3 = new Fraction(1, 0);
        Assertions.assertEquals(1, f3.getDivisor());
    }

    @org.junit.jupiter.api.Test
    void setDivisor() {
        Fraction f = new Fraction(1, 10);
        try {
            f.setDivisor(20);
        }catch (Exception ignored){}

        Assertions.assertEquals(20, f.getDivisor());

        // negative value
        Fraction f2 = new Fraction(1, 10);
        try {
            f2.setDivisor(-1);
        } catch (Exception ignored) {}

        Assertions.assertEquals(-1, f2.getDivisor());

        // 0 value
        Fraction f3 = new Fraction(1, 10);
        try {
            f3.setDivisor(0);
        }catch (Exception e){
            Assertions.assertEquals("can't divide through 0",e.getMessage());
        }

        Assertions.assertEquals(10, f3.getDivisor());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        // normal
        Fraction f = new Fraction(17, 3);
        Assertions.assertEquals("17 / 3", f.toString());

        // negative 1
        Fraction f2 = new Fraction(-17, 3);
        Assertions.assertEquals("-17 / 3", f2.toString());

        // negative 2
        Fraction f3 = new Fraction(17, -3);
        Assertions.assertEquals("17 / -3", f3.toString());

        // negative 3
        Fraction f4 = new Fraction(-17, -3);
        Assertions.assertEquals("17 / 3", f4.toString());

        // 0 value 1
        Fraction f5 = new Fraction(0, 3);
        Assertions.assertEquals("0 / 3", f5.toString());

        // 0 value 2
        Fraction f6 = new Fraction(1, 0);
        Assertions.assertEquals("1 / 1", f6.toString());
    }

    @Test
    void add() {
        Fraction f1 = new Fraction(12,78);
        Fraction f2 = new Fraction(4,6);

        Fraction result = f1.add(f2).shorten();

        Assertions.assertEquals(32, result.getDividend());
        Assertions.assertEquals(39, result.getDivisor());
    }

    @Test
    void sub() {
        Fraction f1 = new Fraction(1,70);
        Fraction f2 = new Fraction(2,90);

        Fraction result = f1.sub(f2).shorten();
        Assertions.assertEquals(-1, result.getDividend());
        Assertions.assertEquals(126, result.getDivisor());
    }

    @Test
    void mul() {
        Fraction f1 = new Fraction(1,27);
        Fraction f2 = new Fraction(9,44);

        Fraction result = f1.mul(f2).shorten();
        Assertions.assertEquals(1, result.getDividend());
        Assertions.assertEquals(132, result.getDivisor());
    }

    @Test
    void div() {
        Fraction f1 = new Fraction(1,27);
        Fraction f2 = new Fraction(9,44);

        Fraction result = f1.div(f2).shorten();

        Assertions.assertEquals(44, result.getDividend());
        Assertions.assertEquals(243, result.getDivisor());
    }

    @Test
    void shorten() {
        // normal
        Fraction f = new Fraction(56, 528);
        f.shorten();
        Assertions.assertEquals(7, f.getDividend());
        Assertions.assertEquals(66, f.getDivisor());

        // negative 1
        Fraction f1 = new Fraction(-56, 528);
        f1.shorten();
        Assertions.assertEquals(-7, f1.getDividend());
        Assertions.assertEquals(66, f1.getDivisor());

        // negative 2
        Fraction f2 = new Fraction(56, -528);
        f2.shorten();
        Assertions.assertEquals(7, f2.getDividend());
        Assertions.assertEquals(-66, f2.getDivisor());
    }


}