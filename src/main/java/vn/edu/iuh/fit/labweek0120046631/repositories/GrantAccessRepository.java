package vn.edu.iuh.fit.labweek0120046631.repositories;

import vn.edu.iuh.fit.labweek0120046631.connectDB.ConnectDB;
import vn.edu.iuh.fit.labweek0120046631.models.Account;
import vn.edu.iuh.fit.labweek0120046631.models.GrantAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrantAccessRepository {

    public GrantAccess getGrantAccess(String accountId) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        GrantAccess grantAccess = null;

        try{
            statement = con.prepareStatement("SELECT * FROM grant_access " +
                    "WHERE account_id = ?");
            statement.setString(1, accountId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String roleId = rs.getString("role_id");
                Account acc = new Account(rs.getString("account_id"));
                boolean isGrant = rs.getBoolean("is_grant");
                String note = rs.getString("note");

                grantAccess = new GrantAccess(roleId, acc, isGrant, note);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return grantAccess;
    }


    //Lấy list account ID đã cấp quyền
    public List<String> getListAccountID() throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        PreparedStatement statement = null;
        List<String> listAccID = new ArrayList<>();

        try{
            statement = con.prepareStatement("select acc.account_id from account acc JOIN grant_access ga ON acc.account_id = ga.account_id");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String accId = rs.getString("account_id");

                listAccID.add(accId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listAccID;
    }

    public boolean insertGrantAccess(GrantAccess grantAccess) throws SQLException, ClassNotFoundException {
        Connection con;
        con = ConnectDB.getInstance().getConnection();
        try {
            PreparedStatement statement = null;

            statement = con.prepareStatement("INSERT INTO grant_access\n" +
                    "VALUES (?,?,?,?)");
            statement.setString(1, grantAccess.getId());
            statement.setString(2, grantAccess.getAccount().getId());
            statement.setBoolean(3, grantAccess.isGrant());
            statement.setString(4, grantAccess.getNote());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
