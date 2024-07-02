package com.abcbank.application.TransfertServices;

import com.abcbank.application.Interface.transfertInterface;
import com.abcbank.application.Models.User;
import com.abcbank.application.Utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransfertService implements transfertInterface {

    Connection conn = MaConnexion.getInstance().getCnx();

    @Override
    public void deposit(double amount, User currentUser) throws SQLException {
        if(amount <= 0) {
            throw new IllegalArgumentException("Please enter a positive value");
        }

        double total = amount + currentUser.getBalance();
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    @Override
    public void withdraw(double amount, User currentUser) throws SQLException {
        if((currentUser.getBalance() - amount) < 0 || amount <= 0) {
            throw new IllegalArgumentException("Insufficient balance or invalid amount");
        }

        double total = currentUser.getBalance() - amount;
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    @Override
    public void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        if(receiverAccNumber.isBlank() || receiverAccNumber.equals(String.valueOf(currentUser.getAcc_num()))) {
            throw new IllegalArgumentException("Invalid receiver account number");
        }

        int receiverAccNum = Integer.parseInt(receiverAccNumber);
        String sql = "SELECT * FROM users WHERE acc_num = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, receiverAccNum);
        ResultSet rs = pst.executeQuery();

        if(rs.next()) {
            User receiver = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getInt("acc_num"),
                    rs.getDouble("balance")
            );

            if(amount <= 0 || amount > currentUser.getBalance()) {
                throw new IllegalArgumentException("Invalid amount or insufficient balance");
            }

            // Withdraw amount from the sender's account
            withdraw(amount, currentUser);

            // Deposit amount to the receiver's account
            receiver.setBalance(receiver.getBalance() + amount);
            String updateReceiverSql = "UPDATE users SET balance = ? WHERE acc_num = ?";
            PreparedStatement updateReceiverPst = conn.prepareStatement(updateReceiverSql);
            updateReceiverPst.setDouble(1, receiver.getBalance());
            updateReceiverPst.setInt(2, receiver.getAcc_num());
            updateReceiverPst.executeUpdate();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}