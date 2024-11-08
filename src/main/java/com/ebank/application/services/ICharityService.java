package com.ebank.application.services;

import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICharityService implements InterfaceCRUD<CharityCampaignModel> {

    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public String add(CharityCampaignModel charityCampaignModel) {
        return "";
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM publication WHERE ID = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting publication with ID: " + id, e);
        }
    }

    @Override
    public void update(CharityCampaignModel charityCampaignModel, int id) {

    }

    public Map<String, Object> getPublicationStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        String sql = "SELECT COUNT(*) AS totalPublications, AVG(publicationDate) AS avgPublicationDate FROM publication";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                statistics.put("totalPublications", rs.getInt("totalPublications"));
                statistics.put("avgPublicationDate", rs.getDate("avgPublicationDate"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving publication statistics", e);
        }

        return statistics;
    }

    public void update(Publication publication, int a) {
        String sql = "UPDATE publication SET title = ?, CampaignName = ?, Description = ?, picture = ?, publicationDate = ? WHERE ID = ?";
        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setString(1, publication.getTitle());
            pst.setString(2, publication.getCampaignName());
            pst.setString(3, publication.getDescription());
            pst.setString(4, publication.getPicture());

            pst.setDate(5, publication.getPublicationDate()); // Assuming publicationDate is a java.util.Date
            pst.setInt(6, a);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating publication with ID: " + publication.getId(), e);
        }
    }

    @Override
    public List<CharityCampaignModel> getAll() {
        return List.of();
    }

    public List<Publication> getByCharityId(int id) {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM publication WHERE CompagnieDeDon_Patente = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Publication publication = new Publication();
                publication.setId(rs.getInt("ID"));
                publication.setCompagnieDeDon_Patente(rs.getInt("CompagnieDeDon_Patente"));
                publication.setTitle(rs.getString("title"));
                publication.setCampaignName(rs.getString("CampaignName"));
                publication.setDescription(rs.getString("Description"));
                publication.setPicture(rs.getString("picture"));
                publication.setPublicationDate(rs.getDate("publicationDate"));

                publications.add(publication);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publications;
    }

    public CharityCampaignModel getCharityBy(int id) {
        CharityCampaignModel charityCampaign = null;
        String sql = "SELECT * FROM charitycampaignmodel WHERE compagnieDeDon_Patente = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                charityCampaign = new CharityCampaignModel();
                charityCampaign.setAcc_num(rs.getInt("acc_num"));
                charityCampaign.setBalance(rs.getDouble("balance"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return charityCampaign;
    }
}
