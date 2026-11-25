package org.store.pro3assignment.traceability;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.store.pro3assignment.station3.model.ProductRecord;

import java.util.List;

@Repository
public class TraceabilityRepository {

  private final JdbcTemplate jdbc;

  public TraceabilityRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<String> findAnimalIdsByProduct(String productId) {
    String sql = """
                SELECT DISTINCT a.registration_number
                FROM product_tray pt
                JOIN part p   ON p.tray_id = pt.tray_id
                JOIN animal a ON a.animal_id = p.animal_id
                WHERE pt.product_id = CAST(? AS INTEGER)
                ORDER BY a.registration_number
                """;
    return jdbc.queryForList(sql, String.class, productId);
  }

  public List<ProductRecord> findProductsByAnimal(String registrationNumber) {
    String sql = """
                SELECT DISTINCT pr.product_id, pr.name AS product_type
                FROM animal a
                JOIN part p          ON p.animal_id = a.animal_id
                JOIN product_tray pt ON pt.tray_id = p.tray_id
                JOIN product pr      ON pr.product_id = pt.product_id
                WHERE a.registration_number = ?
                ORDER BY pr.product_id
                """;
    return jdbc.query(sql,
        (rs, rowNum) -> new ProductRecord(rs.getString("product_id"), rs.getString("product_type")),
        registrationNumber);
  }

  public boolean animalExists(String registrationNumber) {
    Integer count = jdbc.queryForObject(
        "SELECT COUNT(*) FROM animal WHERE registration_number = ?", Integer.class, registrationNumber);
    return count != null && count > 0;
  }

  public boolean productExists(String productId) {
    Integer count = jdbc.queryForObject(
        "SELECT COUNT(*) FROM product WHERE product_id = CAST(? AS INTEGER)", Integer.class, productId);
    return count != null && count > 0;
  }
}