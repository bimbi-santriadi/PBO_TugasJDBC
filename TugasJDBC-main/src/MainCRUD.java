import java.sql.*;
import java.util.Scanner;

public class MainCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/tugasjdbc";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu CRUD Faktur ---");
            System.out.println("1. Tambah Faktur");
            System.out.println("2. Lihat Semua Faktur");
            System.out.println("3. Update Faktur");
            System.out.println("4. Hapus Faktur");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            int menu = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (menu) {
                case 1:
                    tambahFaktur(scanner);
                    break;
                case 2:
                    lihatSemuaFaktur();
                    break;
                case 3:
                    updateFaktur(scanner);
                    break;
                case 4:
                    hapusFaktur(scanner);
                    break;
                case 5:
                    System.out.println("Terima kasih!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // CREATE
    private static void tambahFaktur(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Masukkan No Faktur: ");
            String noFaktur = scanner.nextLine();
            System.out.print("Masukkan Kode Barang: ");
            String kodeBarang = scanner.nextLine();
            System.out.print("Masukkan Nama Barang: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang: ");
            double hargaBarang = scanner.nextDouble();
            System.out.print("Masukkan Jumlah Beli: ");
            int jumlahBeli = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            double total = hargaBarang * jumlahBeli;

            String sql = "INSERT INTO faktur VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, noFaktur);
            statement.setString(2, kodeBarang);
            statement.setString(3, namaBarang);
            statement.setDouble(4, hargaBarang);
            statement.setInt(5, jumlahBeli);
            statement.setDouble(6, total);
            statement.executeUpdate();

            System.out.println("Faktur berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan faktur: " + e.getMessage());
        }
    }

    // READ
    private static void lihatSemuaFaktur() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM faktur";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("\n--- Data Faktur ---");
            while (resultSet.next()) {
                System.out.println("No Faktur    : " + resultSet.getString("no_faktur"));
                System.out.println("Kode Barang  : " + resultSet.getString("kode_barang"));
                System.out.println("Nama Barang  : " + resultSet.getString("nama_barang"));
                System.out.printf("Harga Barang : %.2f%n", resultSet.getDouble("harga_barang"));
                System.out.println("Jumlah Beli  : " + resultSet.getInt("jumlah_beli"));
                System.out.printf("Total        : %.2f%n", resultSet.getDouble("total"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Gagal membaca data faktur: " + e.getMessage());
        }
    }

    // UPDATE
    private static void updateFaktur(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Masukkan No Faktur yang akan diupdate: ");
            String noFaktur = scanner.nextLine();
            System.out.print("Masukkan Nama Barang Baru: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang Baru: ");
            double hargaBarang = scanner.nextDouble();
            System.out.print("Masukkan Jumlah Beli Baru: ");
            int jumlahBeli = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            double total = hargaBarang * jumlahBeli;

            String sql = "UPDATE faktur SET nama_barang = ?, harga_barang = ?, jumlah_beli = ?, total = ? WHERE no_faktur = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, namaBarang);
            statement.setDouble(2, hargaBarang);
            statement.setInt(3, jumlahBeli);
            statement.setDouble(4, total);
            statement.setString(5, noFaktur);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Faktur berhasil diperbarui.");
            } else {
                System.out.println("No Faktur tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal memperbarui faktur: " + e.getMessage());
        }
    }

    // DELETE
    private static void hapusFaktur(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Masukkan No Faktur yang akan dihapus: ");
            String noFaktur = scanner.nextLine();

            String sql = "DELETE FROM faktur WHERE no_faktur = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, noFaktur);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Faktur berhasil dihapus.");
            } else {
                System.out.println("No Faktur tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus faktur: " + e.getMessage());
        }
    }
}
