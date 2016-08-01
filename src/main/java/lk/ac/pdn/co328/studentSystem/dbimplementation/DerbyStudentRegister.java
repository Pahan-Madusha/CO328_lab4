/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.pdn.co328.studentSystem.dbimplementation;

import java.sql.*;
import java.util.ArrayList;
import lk.ac.pdn.co328.studentSystem.Student;
import lk.ac.pdn.co328.studentSystem.StudentRegister;

/**
 *
 * @author pahan
 */
public class DerbyStudentRegister extends StudentRegister {

    Connection connection = null;
    public DerbyStudentRegister() throws SQLException
    {
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        String dbURL1 = "jdbc:derby:codejava/studentDB";
        connection = DriverManager.getConnection(dbURL1);

        if (connection != null)
            {
                //String SQL_CreateTable = "DROP TABLE Students";
                String SQL_CreateTable = "CREATE TABLE Students(id INT , fname VARCHAR(24), lname VARCHAR(24), PRIMARY KEY (id))";
                System.out.println ( "Creating table addresses..." );
                try 
                {
                    Statement stmnt = connection.createStatement();
                    stmnt.execute( SQL_CreateTable );
                    stmnt.close();
                    System.out.println("Table created");
                } catch ( SQLException e )
                {
                    System.out.println(e);
                }
               System.out.println("Connected to database");
            }
            else
            {
                throw new SQLException("Connection Failed");
            }
    }
    
    @Override
    public void addStudent(Student st) throws Exception
    {
        if (connection != null)
        {
            String SQL_AddStudent = "INSERT INTO Students VALUES (" + st.getId() + ",'" + st.getFirstName() + "','" + st.getLastName() + "')";
            System.out.println ( "Adding the student..." + SQL_AddStudent);

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_AddStudent );
            stmnt.close();
            System.out.println("Student Added");

        }
        else
        {
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public void removeStudent(int regNo)
    {
        try
        {
            String SQL_RemoveStudent = "DELETE FROM Students WHERE Students.id = " + regNo;
            System.out.println("Removing the student..." + SQL_RemoveStudent);

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_RemoveStudent);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public Student findStudent(int regNo)
    {
        Student st = null;
        try
        {
            String SQL_FindStudent = "SELECT * FROM Students WHERE Students.id = " + regNo ;
            System.out.println("Finding student..." + SQL_FindStudent);

            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(SQL_FindStudent);

            int id;
            String fname, lname;
            if(rs != null && rs.next())
            {
                id = rs.getInt("id");
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                st = new Student(id, fname, lname);
            }
            return st;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void reset()
    {
        try
        {
            String SQL_Reset = "DROP TABLE Students";
            System.out.println("Resetting register..." + SQL_Reset);

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_Reset); // drop current table

            String new_table = "CREATE TABLE Students(id INT , fname VARCHAR(24), lname VARCHAR(24), PRIMARY KEY (id))";
            stmnt = connection.createStatement();
            stmnt.execute(new_table); // create new table
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<Student> findStudentsByName(String name)
    {
        Student st = null;
        ArrayList<Student> list = new ArrayList<Student>();
        try
        {
            String SQL_FindStudents = "SELECT * FROM Students WHERE Students.lname = '" + name +"' OR Students.fname = '" + name + "'";
            System.out.println("Finding students..." + SQL_FindStudents);

            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(SQL_FindStudents);

            int id;
            String fname, lname;

            if(rs != null)
                while(rs.next())
                {
                    id = rs.getInt("id");
                    fname = rs.getString("fname");
                    lname = rs.getString("lname");
                    st = new Student(id, fname, lname);
                    list.add(st);
                }

            return list;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getAllRegistrationNumbers()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        try
        {
            String SQL_FindRegNos = "SELECT id FROM Students";
            System.out.println("Finding Registration numbers..." + SQL_FindRegNos);

            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery(SQL_FindRegNos);

            int id;

            if(rs != null)
                while(rs.next())
                {
                    id = rs.getInt("id");
                    list.add(id);
                }

            return list;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    
}
