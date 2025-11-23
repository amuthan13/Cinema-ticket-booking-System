import java.sql.*;
import java.util.Scanner;

public class CinemaBookingDB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- CINEMA SEAT BOOKING ---");
            System.out.println("1. View Seats");
            System.out.println("2. Book Seat");
            System.out.println("3. Cancel Seat");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;
            int ch;
            try {
                ch = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try {
                switch (ch) {
                    case 1 -> displaySeats();
                    case 2 -> {
                        System.out.print("Seat to book (e.g. A3): ");
                        String seat = sc.nextLine().trim().toUpperCase();
                        bookSeat(seat);
                    }
                    case 3 -> {
                        System.out.print("Seat to cancel (e.g. A3): ");
                        String seat = sc.nextLine().trim().toUpperCase();
                        cancelSeat(seat);
                    }
                    case 4 -> {
                        System.out.println("Goodbye!");
                        sc.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static void displaySeats() throws Exception {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT seat_id, status FROM seats ORDER BY seat_id")) {

            char lastRow = 0;
            boolean any = false;
            while (rs.next()) {
                any = true;
                String seat = rs.getString("seat_id");
                String status = rs.getString("status");
                char r = seat.charAt(0);

                if (lastRow == 0) lastRow = r;
                if (r != lastRow) {
                    System.out.println();
                    lastRow = r;
                }
                System.out.print("[" + ("booked".equals(status) ? "X" : seat) + "] ");
            }
            if (!any) System.out.println("No seats found in database. Run the SQL script to initialize.");
            else System.out.println();
        }
    }

    static void bookSeat(String seatId) throws Exception {
        if (seatId == null || seatId.isBlank()) {
            System.out.println("Invalid seat id.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            String checkSql = "SELECT status FROM seats WHERE seat_id = ? FOR UPDATE";
            try (PreparedStatement check = con.prepareStatement(checkSql)) {
                check.setString(1, seatId);
                ResultSet rs = check.executeQuery();
                if (!rs.next()) {
                    System.out.println("Seat does not exist.");
                    con.rollback();
                    return;
                }
                if ("booked".equals(rs.getString("status"))) {
                    System.out.println("Seat already booked.");
                    con.rollback();
                    return;
                }
            }

            String updateSql = "UPDATE seats SET status='booked' WHERE seat_id = ?";
            try (PreparedStatement up = con.prepareStatement(updateSql)) {
                up.setString(1, seatId);
                int r = up.executeUpdate();
                if (r > 0) {
                    con.commit();
                    System.out.println("Seat " + seatId + " booked.");
                } else {
                    con.rollback();
                    System.out.println("Failed to book seat.");
                }
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    static void cancelSeat(String seatId) throws Exception {
        if (seatId == null || seatId.isBlank()) {
            System.out.println("Invalid seat id.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE seats SET status='available' WHERE seat_id = ? AND status='booked'")) {
            ps.setString(1, seatId);
            int r = ps.executeUpdate();
            if (r > 0) System.out.println("Booking cancelled for " + seatId);
            else System.out.println("Seat not booked or invalid.");
        }
    }
}
