package com.c1.donguri.reservation;

public class Reservation_DAO {

    public class ReservationDAO {

            String sql = "INSERT INTO reservation " +
                    "(reservation_id, from_id, email_content_id, recipient_email, is_done, scheduled_date) " +
                    "VALUES (SYS_GUID(), ?, ?, ?, 'N', ?)";

            try (Connection con = DBManager.connect();
                 PreparedStatement ps = con.prepareStatement(sql)){

                ps.setString(1, r.getFromId());
                ps.setString(2, r.getEmailContentId());
                ps.setString(3, r.getRecipientEmail());
                ps.setString(4, r.getScheduledDate());

                return ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        // 전체 조회
        public List<Reservation> getAll() {
            List<Reservation> list = new ArrayList<>();
            String sql = "SELECT * FROM reservation ORDER BY scheduled_date";

            try (Connection con = DBManager.connect();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setReservationId(rs.getString("reservation_id"));
                    r.setRecipientEmail(rs.getString("recipient_email"));
                    r.setIsDone(rs.getString("is_done"));
                    r.setScheduledDate(rs.getString("scheduled_date"));
                    list.add(r);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        // 삭제
        public int delete(String id) {
            String sql = "DELETE FROM reservation WHERE reservation_id=?";

            try (Connection con = DBManager.connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, id);
                return ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

