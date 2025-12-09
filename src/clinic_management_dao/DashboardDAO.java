package clinic_management_dao;

import clinic_management_ui.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {
    private Connection conn;

    public DashboardDAO() {
        this.conn = Connect.ConnectDB();
    }

    public Dashboard getDashboardStatistics() {
        Dashboard dashboard = new Dashboard();
        dashboard.setTotalAppointments(getTotalAppointments());
        dashboard.setCompletedAppointments(getCompletedAppointments());
        dashboard.setCancelledAppointments(getCancelledAppointments());
        dashboard.setTotalRevenue(getTotalRevenue());
        return dashboard;
    }

    public int getTotalAppointments() {
        String sql = "SELECT COUNT(*) FROM appointments";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getTotalAppointments: " + e.getMessage());
        }
        return 0;
    }

    public int getCompletedAppointments() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE LOWER(status) IN ('completed', 'complete', 'hoàn thành', 'đã hoàn thành')";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Completed appointments count: " + count);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Error getCompletedAppointments: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int getCancelledAppointments() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE LOWER(status) IN ('cancelled', 'canceled', 'hủy', 'đã hủy', 'bị hủy')";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Cancelled appointments count: " + count);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Error getCancelledAppointments: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM bills WHERE payment_status = 'Paid'";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Error getTotalRevenue: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Debug method to get all distinct status values in the database
     */
    public void printDistinctStatusValues() {
        String sql = "SELECT DISTINCT status, COUNT(*) as count FROM appointments GROUP BY status";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("DEBUG: Distinct status values in database:");
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                System.out.println("  - Status: '" + status + "' (Count: " + count + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error printDistinctStatusValues: " + e.getMessage());
            e.printStackTrace();
        }
    }
}